package it.polimi.tiw.controllers.auth;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import it.polimi.tiw.DAOs.UserDAO;
import it.polimi.tiw.beans.User;
import it.polimi.tiw.exceptions.DuplicateUsernameException;
import it.polimi.tiw.exceptions.MismatchingPasswordsException;
import it.polimi.tiw.exceptions.MissingParametersException;
import it.polimi.tiw.utils.EnvUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/*
 * This Servet handles the Sign Up page and process.
 * GET requests are translated into redirects to the SignUpPage.html, rendered through Thymeleaf.
 * POST requests are elaborated as sign up tentatives, meaning they are supposed to carry a new User's username
 * and password (+ confirm password), which is checked and added into the Database if ok.
 */
@WebServlet("/SignUpServlet")
public class SignUpServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	// The Database Connection
	private Connection connection = null;
	
	/*
	 *  Initializes the Servlet, creating the DB Connection as well
	 */
    public void init() throws ServletException {
    	try {
    		connection = EnvUtil.getConnection();
    	} catch (SQLException e) {
			e.printStackTrace();
			throw new ServletException("Database error", e);
		}
    }
	
	/*
	 * Handles the SignUp logic, checking the data inputted by the User and ensuring that everything is ok.
	 * The SignUp process is split into 2 phases, with 2 screens in total.
	 * This method handles both of them, determining which screen to show to the User based on the values.
	 * If Username and both Passwords are missing, it shows the Screen 1.
	 * If they are there, it shows the Screen 2.
	 * When all the data is there, it prompts the DAO to add the User into the Database, then logs the User in.
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {		
		// Current phase
		String phase = request.getParameter("phase");
		
		if (phase == null) {
		    request.setAttribute("error", "Invalid submission: missing phase.");
		    request.getRequestDispatcher("/signup").forward(request, response);
		    return;
		}
		
		switch (phase) {
		    case "1":		    	
		        // Phase 1: Credentials check
		        handlePhase1(request, response);
		        break;

		    case "2":		    	
		        // Phase 2: User's info
		        handlePhase2(request, response);
		        break;

		    default:
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("Unknown signup phase.");
		        break;
		}

	}
	
	/*
	 * Handles credentials check.
	 * In Phase 1 the User chose a Username and typed his password twice.
	 * In order to continue, Username must not be in the DB, and the passwords must be the same.
	 */
	private void handlePhase1(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = request.getParameter("username");
		String password1 = request.getParameter("password1");
		String password2 = request.getParameter("password2");
		
		try {
			// Creates and intializes the DAO for Users
			UserDAO userDAO = new UserDAO(connection);
			
			// Checks if the credentials are ok
			userDAO.validateCredentials(username, password1, password2);
			
			// Success → move to phase 2
			// Send 200 OK
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write("Phase 1 OK");


			
		} catch (SQLException e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Database error.");
		} catch (MissingParametersException e) {
			// Some parameters were either null or empty Strings
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Missing some parameters.");
		} catch (MismatchingPasswordsException e) {
			// The 2 passwords were different
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Passwords are not the same.");
		} catch (DuplicateUsernameException e) {
			// The Username already exists in the DB
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Username already taken.");
		}
	}

	/*
	 * Handles user's info check.
	 * In Phase 2 the User entered his real Name, Surname and Address..
	 * In order to continue, all data must be non-null and non empty Strings.
	 * Credentials are supposed to have been already checked
	 */
	private void handlePhase2(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    // Credentials
		String username = request.getParameter("username");
		String password1 = request.getParameter("password1");
		String password2 = request.getParameter("password2");
		// User's info
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String address = request.getParameter("address");
		
		// Checks the new data
		if (firstName == null || firstName.isBlank() ||
			lastName == null || lastName.isBlank() ||
			address == null || address.isBlank()
		) {
			// Missing some data
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Missing some parameters.");
            return;
		}
		
		// Re-checks the credentials (something might have happened during the back-and-forth
		
		// Connects to the DB
		try {
			// Creates and intializes the DAO for Users
			UserDAO userDAO = new UserDAO(connection);
			
			// Checks if the credentials are ok
			userDAO.validateCredentials(username, password1, password2);
			
			// Everything is ok. Adding the User to the DB.
			// Creates the User Bean
			User user = new User();
			// Adds the data
			user.setUsername(username);
			user.setFirstName(firstName);
			user.setSurname(lastName);
			user.setAddress(address);
			
			// Inserts the User in the DB.
			userDAO.insert(user, password1);
			
			// SUCCESS: send response to let frontend know to proceed
			response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write("Signup successful.");
			
		} catch (SQLException e) {
			e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Database error.");
		} catch (MissingParametersException | MismatchingPasswordsException | DuplicateUsernameException e) {
			/*
			 * Something has happened to the credentials.
			 * It could be the User trying to do some shenanigans to us, or
			 * some genuine error; either way these credentials can't be used,
			 * so the User must re-start the whole SignUp process again
			 */
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Invalid credentials: " + e.getMessage());
		}
	}

	/*
	 * Destroys the Servlet object, closing the Connection while doing so.
	 */
	public void destroy() {
		try {
			if (connection != null) {
				connection.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}

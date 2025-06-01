package it.polimi.tiw.controllers.auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

import java.sql.Connection;
import java.sql.SQLException;

import it.polimi.tiw.DAOs.UserDAO;
import it.polimi.tiw.beans.User;
import it.polimi.tiw.exceptions.*;
import it.polimi.tiw.utils.EnvUtil;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	// Database connection
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

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().println("Login Servlet");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    // Reads the credentials from the Request
	    String username = request.getParameter("username");
	    String password = request.getParameter("password");

	    try {
	        // Creates and initializes the DAO for Users
	        UserDAO userDAO = new UserDAO(connection);
	        // Tries to authenticate the User
	        User user = userDAO.checkCredentials(username, password);

	        // Authentification successful: set session info
	        HttpSession session = request.getSession();
	        session.setAttribute("user", user);
	        session.setAttribute("loginTime", System.currentTimeMillis());

	        // Respond with success (HTTP 200)
	        response.setStatus(HttpServletResponse.SC_OK);
	        response.getWriter().write("Login successful");

	    } catch (MissingParametersException e) {
	        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
	        response.getWriter().write("Missing parameters");
	    } catch (UserNotFoundException e) {
	        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	        response.getWriter().write("User not found");
	    } catch (IncorrectPasswordException e) {
	        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	        response.getWriter().write("Incorrect password");
	    } catch (SQLException e) {
	        e.printStackTrace();
	        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	        response.getWriter().write("Database error");
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

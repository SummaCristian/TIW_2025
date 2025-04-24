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
import it.polimi.tiw.utils.DBUtil;

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
    		connection = DBUtil.getConnection();
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
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Reads the credentials from the Request
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		
		// Connects to the DB
		try {
			// Creates and intializes the DAO for Users
			UserDAO userDAO = new UserDAO(connection);
			// Tries to authenticate the User
			User user = userDAO.checkCredentials(username, password);
			
			// Authentification successful
			// Adds the User's info to the session
			HttpSession session = request.getSession();
			session.setAttribute("user", user);
			
			// Redirect to HomePage
			request.getRequestDispatcher("/home").forward(request, response);
		} catch (MissingParametersException e) {
			request.setAttribute("error", "Missing some parameters");
			request.getRequestDispatcher("/login").forward(request, response);
		} catch (UserNotFoundException e) {
			request.setAttribute("error", "User not found");
			request.getRequestDispatcher("/login").forward(request, response);
		} catch (IncorrectPasswordException e) {
			request.setAttribute("error", "Incorrect password");
			request.getRequestDispatcher("/login").forward(request, response);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ServletException("Database error", e);
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

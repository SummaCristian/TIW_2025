package it.polimi.tiw.controllers;

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
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        super();
        // TODO Auto-generated constructor stub
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
		try (Connection connection = DBUtil.getConnection()) {
			// Creates and intializes the DAO for Users
			UserDAO userDAO = new UserDAO(connection);
			// Tries to authenticate the User
			User user = userDAO.checkCredentials(username, password);
			
			// Authentification successful
			// Adds the User's info to the session
			HttpSession session = request.getSession();
			session.setAttribute("user", session);
			
			// TODO: Redirect the user
			// response.sendRedirect("Home");
			System.out.println("Logged in");
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

}

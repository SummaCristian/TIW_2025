package it.polimi.tiw.controllers.auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Servlet implementation class SignOutServlet
 */
@WebServlet("/signout")
public class SignOutServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	/*
	 * Signs the User out by invalidating his Session
	 */
	private void signOut(HttpServletRequest request) {
		// Tries retrieving the Session, but doesn't create one if there isn't
	    HttpSession session = request.getSession(false);
	    
	    if (session != null) {
	    	// Invalidates the Session
	        session.invalidate();
	    }
	}


	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		signOut(request);
		
		// Forward back to the Login Screen
		request.getRequestDispatcher("/").forward(request, response);
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		signOut(request);
	}
}
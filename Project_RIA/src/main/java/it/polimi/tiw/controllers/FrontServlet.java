package it.polimi.tiw.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/*
 * This Servet handles the entering logic in the website.
 * In both GET and POST, if the User is not logged in he is redirected to "/login",
 * otherwise he is redirected to the main page.
 */
@WebServlet("/app")
public class FrontServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Shared logic for both GET and POST
    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getRequestURI().substring(request.getContextPath().length());
        
        if (path.startsWith("/css/") || path.startsWith("/js/") || path.startsWith("/images/")) {
            // Static resource: do nothing, let Tomcat serve it
            return;
        }

        // Otherwise, normal app flow
        Object user = request.getSession(false) != null
                ? request.getSession(false).getAttribute("user")
                : null;

        if (user != null) {
            request.getRequestDispatcher("/home").forward(request, response);
        } else {
            request.getRequestDispatcher("/login").forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }
}
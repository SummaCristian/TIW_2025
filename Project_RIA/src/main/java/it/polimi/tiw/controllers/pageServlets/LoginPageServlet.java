package it.polimi.tiw.controllers.pageServlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/login")
public class LoginPageServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        // Retrieve the HTML file
        String filePath = getServletContext().getRealPath("/WEB-INF/pages/LoginPage.html");

        try (var reader = new java.io.BufferedReader(new java.io.FileReader(filePath));
             var writer = response.getWriter()) {

            String line;
            while ((line = reader.readLine()) != null) {
                writer.println(line);
            }
        }
    }
}

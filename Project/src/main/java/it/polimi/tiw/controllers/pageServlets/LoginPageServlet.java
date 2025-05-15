package it.polimi.tiw.controllers.pageServlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templateresolver.FileTemplateResolver;
import org.thymeleaf.web.IWebExchange;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

import java.io.IOException;
import java.util.Locale;

/*
 * This Servet handles the Login page and process.
 * Both GET and POST requests are handled by rendering the LoginPage.html in Thymeleaf
 */
@WebServlet("/login")
public class LoginPageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	 private TemplateEngine templateEngine;
	 private JakartaServletWebApplication application;

	 // Initializes the Thymeleaf Template Engine, used to render the HTML page
    @Override
    public void init() {
        FileTemplateResolver resolver = new FileTemplateResolver();
        resolver.setPrefix(getServletContext().getRealPath("/WEB-INF/pages/") + "/");
        resolver.setSuffix(".html");
        resolver.setTemplateMode("HTML");
        resolver.setCharacterEncoding("UTF-8");

        templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(resolver);

        // Required by Thymeleaf 3.1 to support Jakarta Servlet
        application = JakartaServletWebApplication.buildApplication(getServletContext());
    }
    
    private void loadPage(HttpServletRequest request, HttpServletResponse response) throws IOException {
    	IWebExchange webExchange = application.buildExchange(request, response);
        WebContext context = new WebContext(webExchange, Locale.getDefault());
        
        // Check if there's a session-based login error (flash message)
        HttpSession session = request.getSession(false);
        if (session != null) {
            String loginError = (String) session.getAttribute("loginError");
            if (loginError != null) {
                request.setAttribute("error", loginError);
                session.removeAttribute("loginError"); // flash: use once
            }
        }
        
        // Checks if there is any error in the request
        if (request.getAttribute("error") != null) {
            context.setVariable("error", request.getAttribute("error"));
        }

        response.setContentType("text/html;charset=UTF-8");
        templateEngine.process("LoginPage", context, response.getWriter());
    }
    
	// Renders the LoginPage.html file through the Thymeleaf Template Engine, and sends the output to the User's browser
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        loadPage(request, response);
    }


	// Renders the LoginPage.html file through the Thymeleaf Template Engine, and sends the output to the User's browser
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		loadPage(request, response);
	}

}
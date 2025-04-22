package it.polimi.tiw.controllers.auth;

import java.io.IOException;
import java.util.Locale;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templateresolver.FileTemplateResolver;
import org.thymeleaf.web.IWebExchange;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

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

	// Renders the SignUpPage.html file through the Thymeleaf Template Engine, and sends the output to the User's browser
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {		
		IWebExchange webExchange = application.buildExchange(request, response);
        WebContext context = new WebContext(webExchange, Locale.getDefault());

        response.setContentType("text/html;charset=UTF-8");
        templateEngine.process("SignUpPage", context, response.getWriter());
	}
}

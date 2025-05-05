package it.polimi.tiw.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templateresolver.FileTemplateResolver;
import org.thymeleaf.web.IWebExchange;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

import java.io.IOException;
import java.util.Locale;

/*
 * This Servet handles the error page.
 * Both GET and POST requests are handled by rendering the ErrorPage.html in Thymeleaf
 */
@WebServlet("/error")
public class ErrorPageServlet extends HttpServlet {
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
        
        // Read error attributes
        Object statusCode = request.getAttribute("jakarta.servlet.error.status_code");
        Object message = request.getAttribute("jakarta.servlet.error.message");

        context.setVariable("statusCode", statusCode != null ? statusCode : 500);
        context.setVariable("message", message != null ? message : "An unexpected error occurred.");

        
        response.setContentType("text/html;charset=UTF-8");
        templateEngine.process("ErrorPage", context, response.getWriter());
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
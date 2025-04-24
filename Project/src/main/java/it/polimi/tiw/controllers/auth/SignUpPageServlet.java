package it.polimi.tiw.controllers.auth;

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
 * This Servet handles the Sign Up page and process.
 * Both GET and POST requests are handled by rendering the SignUpPage.html in Thymeleaf
 */
@WebServlet("/signup")
public class SignUpPageServlet extends HttpServlet {
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
    
	// Renders the LoginPage.html file through the Thymeleaf Template Engine, and sends the output to the User's browser
    private void loadPage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        IWebExchange webExchange = application.buildExchange(request, response);
        WebContext context = new WebContext(webExchange, Locale.getDefault());

        // Set error message if present
        if (request.getAttribute("error") != null) {
            context.setVariable("error", request.getAttribute("error"));
        }

        // Copy any pre-filled inputs (e.g., from failed validation)
        String[] fieldNames = { "username", "password1", "password2", "firstName", "lastName", "address" };
        for (String name : fieldNames) {
            if (request.getAttribute(name) != null) {
                context.setVariable(name, request.getAttribute(name));
            }
        }

        // Decide which phase to render
        String phase = (String) request.getAttribute("phase");
        String templateToRender = "SignUpPage"; // default
        
        if ("2".equals(phase)) {
        	// Phase2: renders the page with the User's personal info
            templateToRender = "SignUpPage2";
        }

        // Renders the selected page and sends it to the User
        response.setContentType("text/html;charset=UTF-8");
        templateEngine.process(templateToRender, context, response.getWriter());
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
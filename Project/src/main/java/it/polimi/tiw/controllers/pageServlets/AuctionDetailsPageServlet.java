package it.polimi.tiw.controllers.pageServlets;

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
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Locale;

import it.polimi.tiw.DAOs.*;
import it.polimi.tiw.beans.*;
import it.polimi.tiw.exceptions.NoSuchAuctionException;
import it.polimi.tiw.utils.*;

/*
 * This Servet handles the Sell Page.
 * Both GET and POST requests are handled by rendering the LoginPage.html in Thymeleaf
 */
@WebServlet("/auction-details")
public class AuctionDetailsPageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	 private TemplateEngine templateEngine;
	 private JakartaServletWebApplication application;
	 
	// Database connection
	private Connection connection = null;

	 // Initializes the Thymeleaf Template Engine, used to render the HTML page, and the DB connection
    @Override
    public void init() {
    	// Initializes the Database connection
    	try {
			connection = EnvUtil.getConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	// Initializes the Thymeleaf template engine
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
    
    private void loadPage(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    	// The User's Login Time
    	long loginTime = (long) request.getSession().getAttribute("loginTime");
    	
    	// The Auction's ID
    	String idParam = request.getParameter("id");
    	
    	if (idParam != null) {
            try {
                int auctionId = Integer.parseInt(idParam);

                // Creates the DAO
                AuctionDAO auctionDao = new AuctionDAO(connection);
                
                // Retrieves the Auction Bean
                Auction auction = auctionDao.getAuctionById(auctionId, loginTime);

                // Adds the Auction to the request
            	request.setAttribute("auction", auction);
            	
            	// Infer the origin page from the Referer header
            	String referer = request.getHeader("Referer");
            	String goBackTo = "HOME"; // Default fallback

            	if (referer != null) {
            	    if (referer.contains("/buy")) {
            	        goBackTo = "BUY";
            	    } else if (referer.contains("/sell")) {
            	        goBackTo = "SELL";
            	    } else if (referer.contains("/home")) {
            	        goBackTo = "HOME";
            	    }
            	}

            	// Inject it as a request attribute
            	request.setAttribute("goBackTo", goBackTo);
            	
            	// Render the page
            	IWebExchange webExchange = application.buildExchange(request, response);
                WebContext context = new WebContext(webExchange, Locale.getDefault());
                
                response.setContentType("text/html;charset=UTF-8");
                templateEngine.process("AuctionDetailsPage", context, response.getWriter());
                
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid auction ID");
            } catch (NoSuchAuctionException e) {
				// The Auction with the ID in the request doesn't exist
            	String message = "Auction with ID #" + Integer.parseInt(idParam) + " doesn't exist";
            	response.sendError(HttpServletResponse.SC_NOT_FOUND, message);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing auction ID");
        }
    }
    
	// Renders the HomePage.html file through the Thymeleaf Template Engine, and sends the output to the User's browser
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        loadPage(request, response);
    }


	// Renders the HomePage.html file through the Thymeleaf Template Engine, and sends the output to the User's browser
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		loadPage(request, response);
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
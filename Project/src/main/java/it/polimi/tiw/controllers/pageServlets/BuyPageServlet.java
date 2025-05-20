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
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

import it.polimi.tiw.DAOs.*;
import it.polimi.tiw.beans.*;
import it.polimi.tiw.utils.*;

/*
 * This Servet handles the Buy Page.
 * Both GET and POST requests are handled by rendering the LoginPage.html in Thymeleaf
 */
@WebServlet("/buy")
public class BuyPageServlet extends HttpServlet {
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
    
    private void loadPage(HttpServletRequest request, HttpServletResponse response) throws IOException {
    	// Retrieve all the necessary data
    	AuctionDAO auctionDao = new AuctionDAO(connection);
    	ItemDAO itemDao = new ItemDAO(connection);
    	User user = (User) request.getSession().getAttribute("user");
    	
    	// Writes the User in the Request too
    	request.setAttribute("user", user);
    	
    	// The User's Login Time
    	long loginTime = (long) request.getSession().getAttribute("loginTime");
    	
    	// The User's query text
    	String query = request.getParameter("query");
    	request.setAttribute("query", query);
    	
    	// The Results for the User's Query
    	List<Auction> searchResults = null;
    	
    	if (query != null && !query.trim().isBlank()) {
    		try {
    			// Splits the Query string into an array of Strings, one for each word inside the query.
    			// Words are individual character groups separated by spaces.
    			String[] keywords = query.trim().split("\\s+");
    			
    			// Performs the Search
    			searchResults = auctionDao.getAuctionsForKeywords(keywords, loginTime); // always returns a List, empty if there are NO results
    	    	
    		} catch (SQLException e) {
    	        e.printStackTrace();
    	    }
    	}
    	
    	// If empty, Thymeleaf will adapt the layout with a message.
    	// If null, Thymeleaf will not show the section entirely.
    	request.setAttribute("searchResults", searchResults);
    	
    	// Won Auctions
    	List<Auction> wonAuctions = null;
    	
    	try {
			wonAuctions = auctionDao.getAuctionsWonBy(user, loginTime);
			
		} catch (SQLException e) {
			// Leave openAuctions to null, letting Thymeleaf handle the error
			
			e.printStackTrace();
		}
    	
    	request.setAttribute("wonAuctions", wonAuctions);
    	
    	// Render the page
    	IWebExchange webExchange = application.buildExchange(request, response);
        WebContext context = new WebContext(webExchange, Locale.getDefault());
        
        response.setContentType("text/html;charset=UTF-8");
        templateEngine.process("BuyPage", context, response.getWriter());
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
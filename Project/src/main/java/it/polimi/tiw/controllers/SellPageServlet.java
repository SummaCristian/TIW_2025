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
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;

import it.polimi.tiw.DAOs.*;
import it.polimi.tiw.beans.*;
import it.polimi.tiw.utils.*;

/*
 * This Servet handles the Sell Page.
 * Both GET and POST requests are handled by rendering the LoginPage.html in Thymeleaf
 */
@WebServlet("/sell")
public class SellPageServlet extends HttpServlet {
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
			connection = DBUtil.getConnection();
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
    	User user = (User) request.getSession().getAttribute("user");
    	
    	// Open Auctions
    	List<Auction> openAuctions = null;
    	
    	// The User's Login Time
    	long loginTime = (long) request.getSession().getAttribute("loginTime");
    	
    	try {
			openAuctions = auctionDao.getAuctionsCreatedBy(user, false, loginTime);
			
		} catch (SQLException e) {
			// Leave openAuctions to null, letting Thymeleaf handle the error
		}
    	
    	request.setAttribute("openAuctions", openAuctions);
    	
    	// Closed Auctions
    	List<Auction> closedAuctions = null;
    	
    	try {
    		closedAuctions = auctionDao.getAuctionsCreatedBy(user, true, loginTime);
    	} catch (SQLException e) {
    		// Leave closedAuctions to null, letting Thymeleaf handle the error
    	}
    	
    	request.setAttribute("closedAuctions", closedAuctions);
    	
    	// Render the page
    	IWebExchange webExchange = application.buildExchange(request, response);
        WebContext context = new WebContext(webExchange, Locale.getDefault());
        
        response.setContentType("text/html;charset=UTF-8");
        templateEngine.process("SellPage", context, response.getWriter());
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
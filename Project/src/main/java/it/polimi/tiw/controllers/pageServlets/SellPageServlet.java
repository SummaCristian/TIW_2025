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
    	
    	// Open Auctions
    	List<Auction> openAuctions = null;
    	
    	try {
			openAuctions = auctionDao.getAuctionsCreatedBy(user, false, loginTime);
			
		} catch (SQLException e) {
			// Leave openAuctions to null, letting Thymeleaf handle the error
			
			e.printStackTrace();
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
    	
    	// Items available for new Auctions
    	List<Item> availableItems = null;
    	
    	try {
    		availableItems = itemDao.getAvailableItemsForUserId(user.getId());
    	} catch (SQLException e) {
    		// Leaves availableItems to null, letting Thymeleaf handle the error
    	}
    	
    	request.setAttribute("availableItems", availableItems);
    	
    	// A Default Date and Time for the Create Auction form's ClosingDate.
    	// Set at tomorrow at 3:00 PM (15:00)
    	LocalDateTime tomorrowAt3PM = LocalDateTime.now()
    		    .plusDays(1)
    		    .withHour(15)
    		    .withMinute(0)
    		    .withSecond(0)
    		    .withNano(0);

    	request.setAttribute("defaultClosingDate", tomorrowAt3PM);
    	
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
package it.polimi.tiw.controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.google.gson.*;
import java.lang.reflect.Type;
import it.polimi.tiw.DAOs.AuctionDAO;
import it.polimi.tiw.beans.Auction;
import it.polimi.tiw.beans.User;
import it.polimi.tiw.utils.EnvUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/*
 * This Servlet responds to requests for Auctions data, 
 * more specifically Auctions created by the User and still Open.
 * The results are sent in a JSON format.
 */
@WebServlet("/api/auctions/closed")
public class GetClosedAuctionsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	// Database connection
	private Connection connection = null;

	@Override
	public void init() {
		// Initializes the Database connection
		try {
			connection = EnvUtil.getConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Retrieve all the necessary data
    	AuctionDAO auctionDao = new AuctionDAO(connection);
    	User user = (User) request.getSession().getAttribute("user");
    	
    	// Check session validity
        if (user == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
            return;
        }
    	
    	// The User's Login Time
    	long loginTime = (long) request.getSession().getAttribute("loginTime");
    	
    	// Open Auctions
    	List<Auction> closedAuctions = null;
    	
    	try {
			closedAuctions = auctionDao.getAuctionsCreatedBy(user, true, loginTime);
			
		} catch (SQLException e) {
			// Send Error 500 and stop, Client will handle the error on his side
	        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 500
	        return;
		}
    	
    	// Convert to JSON
    	Gson gson = new GsonBuilder()
    		    .registerTypeAdapter(LocalDateTime.class, new JsonSerializer<LocalDateTime>() {
    		        @Override
    		        public JsonElement serialize(LocalDateTime src, Type typeOfSrc, JsonSerializationContext context) {
    		            return new JsonPrimitive(src.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    		        }
    		    })
    		    .create();
    	
        String json = gson.toJson(closedAuctions);
    	
    	// Send the JSON as the response to the client
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json);
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

package it.polimi.tiw.controllers;

import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import it.polimi.tiw.DAOs.AuctionDAO;
import it.polimi.tiw.beans.Auction;
import it.polimi.tiw.beans.User;
import it.polimi.tiw.utils.EnvUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * This Servlet responds to GET requests for auctions matching
 * a keyword-based search query. It excludes auctions created
 * by the current user and those that ended before login time.
 */
@WebServlet("/api/auctions/search")
public class GetSearchResultsServlet extends HttpServlet {
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
    	
    	// The User's query text
    	String query = request.getParameter("query");
    	
    	// The Results for the User's Query
    	List<Auction> searchResults = null;
    	
    	// Checks if the Query is not empty
    	if (query == null || query.trim().isEmpty()) {
    	    response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400
    	    return;
    	}
    	
    	try {
			// Splits the Query string into an array of Strings, one for each word inside the query.
			// Words are individual character groups separated by spaces.
			String[] keywords = query.trim().split("\\s+");
			
			// Performs the Search
			searchResults = auctionDao.getAuctionsForKeywords(keywords, user.getId(), loginTime); // always returns a List, empty if there are NO results
		} catch (SQLException e) {
	        e.printStackTrace();
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
    	
        String json = gson.toJson(searchResults);
    	
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

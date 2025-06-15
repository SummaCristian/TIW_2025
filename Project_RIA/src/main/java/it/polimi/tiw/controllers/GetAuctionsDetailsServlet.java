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
import it.polimi.tiw.exceptions.NoSuchAuctionException;
import it.polimi.tiw.utils.EnvUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * This Servlet responds to GET requests for a single auction's details.
 * Requires a valid auction ID via the `id` parameter.
 * Returns the auction and its dynamic content (e.g., offers) as JSON.
 */
@WebServlet("/api/auctions/details")
public class GetAuctionsDetailsServlet extends HttpServlet {
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
    	
    	// The Auction's ID passed in the request
    	String auctionIdParam = request.getParameter("id");
    	
    	// Check if the ID was passed in the request
    	if (auctionIdParam == null) {
    		// No ID, return error
    	    response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400
    	    return;
    	}
    	
    	int auctionId = 0;
    	
    	// Check the validity of the ID
    	try {
    		auctionId = Integer.valueOf(auctionIdParam);
    	} catch (NumberFormatException e) {
    		// Invalid ID format passed in the request, return error
    		response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400
    		return;
    	}
    	
    	// Number format is valid for ID
    	
    	// The Auction's Bean the User requested
    	Auction auction = null;
    	
    	try {
			// Retrieves the Auction
			auction = auctionDao.getAuctionById(auctionId, loginTime);
		} catch (SQLException e) {
			// Internal error
	        e.printStackTrace();
	        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 500
	        return;
	    } catch (NoSuchAuctionException e) {
	    	// ID is not associated to any Auction, return error
	    	response.setStatus(HttpServletResponse.SC_NOT_FOUND); // 404
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
    	
        String json = gson.toJson(auction);
    	
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
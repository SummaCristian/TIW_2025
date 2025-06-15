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

import it.polimi.tiw.DAOs.ItemDAO;
import it.polimi.tiw.beans.Item;
import it.polimi.tiw.beans.User;
import it.polimi.tiw.utils.EnvUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/*
 * This Servlet responds to requests for Items data, 
 * more specifically Items created by the User and NOT associated to ANY Auction.
 * The results are sent in a JSON format.
 */
@WebServlet("/api/items/available")
public class GetAvailableItemsServlet extends HttpServlet {
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
    	ItemDAO itemDao = new ItemDAO(connection);
    	User user = (User) request.getSession().getAttribute("user");
    	
    	// Check session validity
        if (user == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
            return;
        }
    	
    	// The User's Login Time
    	long loginTime = (long) request.getSession().getAttribute("loginTime");
    	
    	// Items available for new Auctions
    	List<Item> availableItems = null;
    	
    	try {
    		availableItems = itemDao.getAvailableItemsForUserId(user.getId());			
		} catch (SQLException e) {
			// Send Error 500 and stop, Client will handle the error on his side
	        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 500
	        return;
		}
    	
    	// Convert to JSON
    	// Convert to JSON
    	Gson gson = new GsonBuilder()
    		    .registerTypeAdapter(LocalDateTime.class, new JsonSerializer<LocalDateTime>() {
    		        @Override
    		        public JsonElement serialize(LocalDateTime src, Type typeOfSrc, JsonSerializationContext context) {
    		            return new JsonPrimitive(src.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    		        }
    		    })
    		    .create();
    	
        String json = gson.toJson(availableItems);
    	
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

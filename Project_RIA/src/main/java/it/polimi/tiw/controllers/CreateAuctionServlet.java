package it.polimi.tiw.controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import it.polimi.tiw.DAOs.AuctionDAO;
import it.polimi.tiw.DAOs.ItemDAO;
import it.polimi.tiw.beans.Auction;
import it.polimi.tiw.beans.Item;
import it.polimi.tiw.exceptions.InvalidDateException;
import it.polimi.tiw.exceptions.ItemAlreadyInAuctionException;
import it.polimi.tiw.exceptions.MissingParametersException;
import it.polimi.tiw.exceptions.NegativeIncrementException;
import it.polimi.tiw.exceptions.NoItemsSelectedException;
import it.polimi.tiw.exceptions.NotOwningItemException;
import it.polimi.tiw.utils.EnvUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/*
 * This Servlet handles the action of creating a new Auction and adding it to the Database.
 * The Auction is created based on the data coming from the User's request, which is 
 * appropriately validated before committing any change.
 * The Servlet handles hazards such as invalid IDs, invalid User, ownership mismatching between 
 * the Items and the User, and the absence of Items (every Auction requires at least 1 Item inside).
 * Once these checks are passed, it computes the missing data like the Auction's Base Price (sum of all the
 * Item's prices).
 * Before committing the new Auction creation, it also updates all the Item's references to now point to
 * the Auction: if this fails, it rolls back the change and throws an error.
 */
@WebServlet("/CreateAuction")
public class CreateAuctionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	// Database connection
	private Connection connection = null;
	
	// Initializes the DB connection
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
    
    // Forwards back without doing anything
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	request.getRequestDispatcher("/sell").forward(request, response);
    }


	/*
	 * Checks if the required Data is ok, then creates the new Auction with it.
	 * Forwards back to the Sell page with errors if there is any.
	 * Forwards back to the Sell page with no error if everything is ok. 
	 */
    @Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Reading data from Request
		String minIncrementParam = request.getParameter("minIncrement");
		String closingDateParam = request.getParameter("closingDate");
		String[] itemIdStrings = request.getParameterValues("itemIds");
		String sellerIdParam = request.getParameter("sellerId");
		
		// Proper variables (parsing needed)
		int basePrice = 0;
		int minIncrement = 0;
		LocalDateTime closingDate = null;
		List<Integer> itemIds = new ArrayList<>();
		int sellerId = 0;
		List<Item> items = null;
		
		// Checking the data
		try {
			// Checks if all the data was received
			checkData(
				minIncrementParam,
				closingDateParam,
				itemIdStrings,
				sellerIdParam
			);
			
			// Parse Integer Data from Strings
			minIncrement = Integer.parseInt(minIncrementParam);
			sellerId = Integer.parseInt(sellerIdParam);
			
			// Checks if the minIncrement is > 0
			if (minIncrement <= 0) {
				throw new NegativeIncrementException();
			}
			
			// Parse Date from String
			closingDate = parseDate(closingDateParam);
			
			// Parse Integer IDs from Array of Strings
			for (String id: itemIdStrings) {
				itemIds.add(Integer.parseInt(id));
			}
			
			// Retrieve the Items based on the IDs passed in the Request
        	ItemDAO itemDao = new ItemDAO(connection);
        	
        	items = itemDao.getItemsByIDs(itemIds);
        	
        	// Checks if at least 1 Item was selected
        	if (items.isEmpty()) {
        		throw new NoItemsSelectedException();
        	}
        	
        	// Checks if all the Items are NOT part of other Auctions and if they all belong to the User
        	for (Item item: items) {
        		if (item.getAuctionId() != null) {
        			throw new ItemAlreadyInAuctionException();
        		}
        		
        		if (item.getCreatorId() != sellerId) {
        			throw new NotOwningItemException();
        		}
        	}
			
		} catch (MissingParametersException e) {
			// Missing some data
			
			// Return an error message
			sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Missing parameters. Please try again..."); // 400 
			
			return;
		} catch (NumberFormatException e) {
			// One or more Integers were not numbers
			
			// Return an error message
			sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Something went wrong. Please try again..."); // 400
			
			return;
			
		} catch (InvalidDateException e) {
			// The inputted Date was in the past
			
			// Return an error message
			sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Closing Date must be in the future. Please try again..."); // 400
			
			return;
			
		} catch (NoItemsSelectedException e) {
			// No Items were selected to be put into this Auction

			// Return an error message
			sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Auctions must contain at least 1 Item. Please try again..."); // 400
			
			return;
			
		} catch (ItemAlreadyInAuctionException e) {
			// One or more of the selected Item(s) is already part of another Auction

			// Return an error message
			sendError(response, HttpServletResponse.SC_CONFLICT, "An Item cannot be assigned to more than 1 Auction. Please try again..."); // 409

			return;
			
		} catch (NotOwningItemException e) {
			// One or more of the selected Item(s) does NOT belong to the User

			// Return an error message
			sendError(response, HttpServletResponse.SC_FORBIDDEN, "Stealing is not allowed. Please try again..."); // 403

			return;
			
		} catch (NegativeIncrementException e) {
			// The Minimum Increment passed was <= 0
			
			// Return an error message
			sendError(response, HttpServletResponse.SC_BAD_REQUEST, "An Auction's minimum increment can't be 0 or negative. Please try again..."); // 400
			
			return;
			
		} catch (SQLException e) {
			e.printStackTrace();
		    sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "A database error occurred. Please try again later.");
		}
		
		// Everything is ok, continue
		
		// Calculates Base Price as the SUM of all the Item's prices
		basePrice = items.stream()
							.mapToInt(Item::getPrice)
							.sum();
		
        try {
        	// Create the Auction Bean
        	Auction auction = new Auction(
        		0,
        		basePrice,
        		minIncrement,
        		closingDate,
        		sellerId,
        		false,
        		items
        	);
        	
        	// Inserts the Auction in the Database
        	AuctionDAO auctionDao = new AuctionDAO(connection);
        	
        	int auctionId = auctionDao.insert(auction);
        	
        	// Returns the success statis code
			response.setStatus(HttpServletResponse.SC_OK); // 200
			
		} catch (SQLException e) {
			e.printStackTrace();
		    sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "A database error occurred. Please try again later.");
		}

	}
	
	/*
	 * Checks if all the data is here or if something is missing
	 * Throws MissingParametersException if something is missing
	 * Returns nothing if everything is ok
	 */
	private void checkData(
			String minIncrement,
			String closingDate,
			String[] itemIds,
			String sellerId
	) throws MissingParametersException {
		if (minIncrement == null || minIncrement.isBlank() 
				|| closingDate == null || closingDate.isBlank() 
				|| sellerId == null || sellerId.isBlank()
		) {
			throw new MissingParametersException();
		}
		
		if (itemIds == null || itemIds.length == 0) {
			throw new MissingParametersException();
		}
		
		for (String id : itemIds) {
			if (id == null || id.isBlank()) {
				throw new MissingParametersException();
			}
		}
	}
	
	/*
	 * Reads the String param and parses it into a Date object.
	 * Throws InvalidDateException if the Date is in the past.
	 */
	private LocalDateTime parseDate(String date) throws InvalidDateException {
		// Parse the Date
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
		LocalDateTime closingDate = LocalDateTime.parse(date, formatter);
		
		// Check if the Date is not in the past
		if (closingDate.isBefore(LocalDateTime.now())) {
			throw new InvalidDateException();
		}
		
		return closingDate;
	}
	
	
	/*
	 * Sends a basic error with the specified statusCode and String message
	 */
	private void sendError(HttpServletResponse response, int statusCode, String message) throws IOException {
        response.setStatus(statusCode);
        response.setContentType("text/plain;charset=UTF-8");
        response.getWriter().write(message);
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
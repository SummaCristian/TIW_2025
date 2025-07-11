package it.polimi.tiw.controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;

import it.polimi.tiw.DAOs.AuctionDAO;
import it.polimi.tiw.DAOs.OfferDAO;
import it.polimi.tiw.DAOs.UserDAO;
import it.polimi.tiw.beans.Auction;
import it.polimi.tiw.beans.Offer;
import it.polimi.tiw.beans.User;
import it.polimi.tiw.exceptions.ClosedAuctionException;
import it.polimi.tiw.exceptions.LessThanBasePriceException;
import it.polimi.tiw.exceptions.LessThanMinIncrementException;
import it.polimi.tiw.exceptions.MissingParametersException;
import it.polimi.tiw.exceptions.NegativeOfferException;
import it.polimi.tiw.exceptions.NoSuchAuctionException;
import it.polimi.tiw.exceptions.UserNotFoundException;
import it.polimi.tiw.exceptions.UserOwnAuctionException;
import it.polimi.tiw.utils.EnvUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/*
 * This Servlet handles the action of making an Offer to an Auction.
 * It validates the User's inputs from the request, ensuring hazards such as Users
 * making Offers to their own Auctions or Auctions not allowed by the rules don't happen.
 * It checks if the constraints in value are all met before committing any change.
 * Since it checks if the Offer is correctly allowed before inserting, it safely updates
 * the Auction's Current Highest Bid too to now point to the new Offer.
 */
@WebServlet("/MakeOffer")
public class MakeOfferServlet extends HttpServlet {
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


	/*
	 * Checks if the required Data is ok, then creates the new Auction with it.
	 * Forwards back to the Sell page with errors if there is any.
	 * Forwards back to the Sell page with no error if everything is ok. 
	 */
    @Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Reading data from Request
		String userIdParam = request.getParameter("userId");
		String auctionIdParam = request.getParameter("auctionId");
		String offerPriceParam = request.getParameter("offer");
		
		// Proper variables (parsing needed)
		int userId = 0;
		int auctionId = 0;
		int offerPrice = 0;
		
		User user = null;
		
		// Calculate the current Date and Time, the closest to the user's submit action.
		// Generated server-side to avoid tampering or misuse from client input.
		LocalDateTime offerDate = LocalDateTime.now();
		
		// Checking the data
		try {
			// Checks if all the data was received
			checkData(userIdParam, auctionIdParam, offerPriceParam);
			
			// Parse Integer Data from Strings
			userId = Integer.parseInt(userIdParam);
			auctionId = Integer.parseInt(auctionIdParam);
			offerPrice = Integer.parseInt(offerPriceParam);
			
			// The User's Login Time
	    	long loginTime = (long) request.getSession().getAttribute("loginTime");
			
			// Retrieve the Auction based on the IDs passed in the Request
	    	// Check if the Auction exists in the DB.
        	AuctionDAO auctionDao = new AuctionDAO(connection);
        	Auction auction = auctionDao.getAuctionById(auctionId, loginTime);
        	
        	// Check if the User exists in the DB.
        	UserDAO userDao = new UserDAO(connection);
        	user = userDao.getUserById(userId);
        	
        	// Check if the User is not attempting to make an Offer to his OWN Auction (NOT ALLOWED)
        	if (user.getId() == auction.getSellerId()) {
        		throw new UserOwnAuctionException();
        	}
        	
        	// Check if the Auction is stil open
        	if (auction.isSold() || auction.getClosingDate().isBefore(offerDate)) {
        		throw new ClosedAuctionException();
        	}
        	
        	// Check if the Offered Price can be accepted for this Auction
        	if (offerPrice <= 0) {
        		// Negative or Zero
        		throw new NegativeOfferException();
        	}
        	
        	if (offerPrice < auction.getBasePrice()) {
        		// Less than the Auction's Base Price
        		throw new LessThanBasePriceException();
        	}
        	
        	if (auction.getHighestBid() != null && offerPrice < auction.getHighestBid().getOfferedPrice() + auction.getMinIncrement()) {
        		// There already is at least 1 Offer, but
    			// the Offer is less than the minimum allowed increment for this Auction
        		throw new LessThanMinIncrementException();
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
			
		} catch (UserNotFoundException e) {
			// No User exists with the ID passed in the request
			
			// Return an error message
			sendError(response, HttpServletResponse.SC_NOT_FOUND, "Something is wrong with your User ID. Please try again..."); // 404 
			
			return;
			
		} catch (UserOwnAuctionException e) {
			// The User's ID mathes the Auction's Seller ID.
			// A User can't raise Offers to Auctions he created.

			// Return an error message
			sendError(response, HttpServletResponse.SC_FORBIDDEN, "You can't raise Offers to your own Auctions."); // 403
			
			return;
			
		} catch (ClosedAuctionException e) {
			// The User is trying to raise an Offer to an Auction that is already CLOSED.

			// Return an error message
			sendError(response, HttpServletResponse.SC_CONFLICT, "This Auction is closed and can't accept any more Offers."); // 409 

			return;
			
		} catch (LessThanMinIncrementException e) {
			// The User tried offering less than Current Highest Offer + Minimum Increment

			// Return an error message
			sendError(response, HttpServletResponse.SC_BAD_REQUEST, "The Offer is too low for this Auction. Please try again..."); // 400 

			return;
			
		} catch (NegativeOfferException e) {
			// The Offered price in this request was negative or 0, which is not allowed
			
			// Return an error message
			sendError(response, HttpServletResponse.SC_BAD_REQUEST, "You can't offer 0€ or less. Please try again..."); // 400 
			
			return;
			
		} catch (LessThanBasePriceException e) {
			// The Offered price is less than the Auction's Base Price
			
			// Return an error message
			sendError(response, HttpServletResponse.SC_BAD_REQUEST, "You can't offer less than this Auction's base price. Please try again..."); // 400
			
			return;
			
		} catch (NoSuchAuctionException e) {
			// The ID passed in this request does not correspond to any existing Auction inside the DB.
			
			// Return an error message
			sendError(response, HttpServletResponse.SC_NOT_FOUND, "Something went wrong. Please try again..."); // 404 
			
		} catch (SQLException e) {
			e.printStackTrace();
		    sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "A database error occurred. Please try again later."); // 500
		}
		
		// Everything is ok, continue
		
        try {
        	// Create the Offer Bean
        	Offer offer = new Offer(
        		0,
        		user.getId(),
        		user.getUsername(),
        		auctionId,
        		offerPrice,
        		offerDate
        	);
        	
        	// Inserts the Offer in the Database
        	OfferDAO offerDao = new OfferDAO(connection);
        	
        	offerDao.insert(offer);
        	
        	// Returns the success code
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
			String userId,
			String auctionId,
			String offerPrice
	) throws MissingParametersException {
		if (userId == null || userId.isBlank() 
				|| auctionId == null || auctionId.isBlank() 
				|| offerPrice == null || offerPrice.isBlank()
		) {
			throw new MissingParametersException();
		}
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
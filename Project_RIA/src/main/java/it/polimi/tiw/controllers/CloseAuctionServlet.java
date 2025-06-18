package it.polimi.tiw.controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;

import it.polimi.tiw.DAOs.AuctionDAO;
import it.polimi.tiw.DAOs.UserDAO;
import it.polimi.tiw.beans.Auction;
import it.polimi.tiw.beans.User;
import it.polimi.tiw.exceptions.AuctionNotExpiredYetException;
import it.polimi.tiw.exceptions.ClosedAuctionException;
import it.polimi.tiw.exceptions.MissingParametersException;
import it.polimi.tiw.exceptions.NoSuchAuctionException;
import it.polimi.tiw.exceptions.NotOwningAuctionException;
import it.polimi.tiw.exceptions.UserNotFoundException;
import it.polimi.tiw.utils.EnvUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/*
 * This Servlet handles the action of closing an Auction.
 * It ensures that only the User who actually owns the Auction is allowed
 * to perform such action, and handles all possible hazards,
 * such as invalid Auction ID, Auction already closed or other potential problems.
 * Once the Auctions has been closed, this Servlet also updates it to contain the
 * User who won it, shown as Buyer User. 
 * This only happens if there is at least 1 Offer, and in case of multiple, the User is chosen
 * via the Current Highest Bid, aka the highest Offer made to the Auction.
 * Buyer remains null if no Offers have been made to the Auction when at closing time.
 */
@WebServlet("/CloseAuction")
public class CloseAuctionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	// Database connection
	private Connection connection = null;
	
	// Initializes the DB connection
	@Override
	public void init() {
		try {
			connection = EnvUtil.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * Handles the GET Requests by closing the Auction and forwarding back to the AuctionDetailsPage
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		closeAuction(request, response);
	}
	
	/*
	 * Handles the POST Requests by closing the Auction and forwarding back to the AuctionDetailsPage
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		closeAuction(request, response);
	}
	
	/*
	 * Checks the data inside the request and acts accordingly.
	 * If everything is ok, the Auction is closed and the appropriate data updates are performed.
	 * The User is then forwarded back to the AuctionDetailsPage, where he will see the outcome of the action.
	 */
	private void closeAuction(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Retrieve data from request
		String userIdParam = request.getParameter("userId");
		String auctionIdParam=request.getParameter("auctionId");
		
		// The actual parameters, to fill later
		int userId = 0;
		int auctionId = 0;
		
		// The DAOs needed to check the data
		UserDAO userDao = new UserDAO(connection);
		AuctionDAO auctionDao = new AuctionDAO(connection);
		
		// The User's Login Time
    	long loginTime = (long) request.getSession().getAttribute("loginTime");
				
		try {
			// Check if data is not null
			if (userIdParam == null || userIdParam.trim().isBlank() || auctionIdParam == null || auctionIdParam.trim().isBlank()) {
				throw new MissingParametersException();
			}
			
			// Checks if the parameters are actually numbers
			userId = Integer.valueOf(userIdParam);
			auctionId = Integer.valueOf(auctionIdParam);
			
			// Checks if the User exists inside the DB
			User user = userDao.getUserById(userId);
			
			// Checks if the Auction exists inside the DB
			Auction auction = auctionDao.getAuctionById(auctionId, loginTime);
			
			// Checks if the Auction belongs to the User
			if (auction.getSellerId() != user.getId()) {
				// Not the same User
				throw new NotOwningAuctionException();
			}
			
			// Checks if the Auction is still open and can be closed
			if (auction.isSold()) {
				throw new ClosedAuctionException();
			}
			
			// Checks if the Auction is expired (expected)
			if (LocalDate.now().isBefore(auction.getClosingDate().toLocalDate())) {
				// The Auction is not expired yet, it can't be closed before its Closing Date
				throw new AuctionNotExpiredYetException();
			}
			
			// Everything OK, continue
			
			// Updates the Auction to mark it as CLOSED and BuyerID from HighestBID if present
			auctionDao.markAuctionAsClosed(auction);
			
			// Returns the success status code
			response.setStatus(HttpServletResponse.SC_OK); // 200
			
		} catch (MissingParametersException e) {
			// Either UserId, AuctionId or both are missing
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing parameters. Please try again..."); // 400
			
		} catch (NumberFormatException e) {
			// Either UserId, AuctionId or both are not numbers
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Something is wrong with the parameters. Please try again..."); // 400
			
		} catch (UserNotFoundException e) {
			// The UserID passed in the request does not exist inside the DB
			response.sendError(HttpServletResponse.SC_NOT_FOUND, "The UserID passed in the request does not exist. Please try again..."); // 404
			
		} catch (NoSuchAuctionException e) {
			// The AuctionID passed in the request does not exist inside the DB
			response.sendError(HttpServletResponse.SC_NOT_FOUND, "The AuctionID passed in the request does not exist. Please try again..."); // 404 
			
		} catch (NotOwningAuctionException e) {
			// The UserID passed in the request does not match the Auction's SellerID
			response.sendError(HttpServletResponse.SC_FORBIDDEN, "You can't close other people's Auctions!"); // 403
			
		} catch (ClosedAuctionException e) {
			// The Auction is already Sold, can't be closed again
			response.sendError(HttpServletResponse.SC_CONFLICT, "This Auction is already closed."); // 409 
			
		} catch (AuctionNotExpiredYetException e) {
			// The Auction is not expired yet, it can't be closed before its Closing Date
			response.sendError(HttpServletResponse.SC_CONFLICT, "This Auction can't be closed before its Closing Date."); // 409 
			
		} catch (SQLException e) {
			// Database Error
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Something went wrong. Please try again..."); // 500
			
		}
		
		
	}
	
	
	/*
	 * Destroys the Servlet object, closing the Connection while doing so
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

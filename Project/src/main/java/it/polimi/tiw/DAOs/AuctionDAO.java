package it.polimi.tiw.DAOs;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import it.polimi.tiw.beans.Auction;
import it.polimi.tiw.beans.Item;
import it.polimi.tiw.beans.Offer;
import it.polimi.tiw.beans.User;
import it.polimi.tiw.exceptions.NoSuchAuctionException;

/*
 * This class acts as a DAO (Data Access Object) for the Auctions Table in the DB.
 * It's responsible for creating and executing the Queries that involve this specific Table.
 */
public class AuctionDAO {
	// The connection to the Database
	private Connection conn;

    public AuctionDAO(Connection conn) {
        this.conn = conn;
    }
    
    /*
     * Builds the Bean from the data retrieved from a Query's ResultsSet
     */
    private Auction buildAuction(
		ResultSet results,
		List<Item> itemsInAuction,
		List<Offer> offersInAuction,
		String formattedRemainingTime
	) throws SQLException {
    	// Parses the ClosingDate
    	Timestamp timestamp = results.getTimestamp("ClosingDate");
		LocalDateTime closingDate = null;

		if (timestamp != null) {
		    closingDate = timestamp.toLocalDateTime();
		}
    	
		// Creates the Bean
    	Auction auction = new Auction(
            results.getInt("Id"),
            results.getInt("BasePrice"),
            results.getInt("MinIncrement"),
            results.getObject("HighestBid") != null ? results.getInt("HighestBid") : null,
            closingDate,
            results.getInt("SellerId"),
            results.getBoolean("IsSold"),
            results.getObject("BuyerId") != null ? results.getInt("BuyerId") : null,
    	    results.getObject("FinalPrice") !=  null ? results.getInt("FinalPrice") : null,
    	    results.getObject("SellerUsername") != null ? results.getString("SellerUsername") : null,
            results.getObject("BuyerUsername") != null ? results.getString("BuyerUsername") : null,
            results.getObject("BuyerAddress") != null ? results.getString("BuyerAddress") : null,
            formattedRemainingTime,
            itemsInAuction,
            offersInAuction
        );
    	
    	return auction;
    }
    
    /*
     * Creates the Formatted String representing the Remaining Time for an Auction.
     * Remaining Time is calculated as Auction.ClosingDate - Login Time (from Session).
     * Format supports DAY, HOUR, MINUTES, optionals if 0, plural if necessary.
     * Returns "Closed" if the difference is negative.
     */
    private String formatRemainingTime(long millis) {
        if (millis <= 0) {
            return "Closed";
        }

        long totalMinutes = millis / (60 * 1000);
        long days = totalMinutes / (60 * 24);
        long hours = (totalMinutes % (60 * 24)) / 60;
        long minutes = totalMinutes % 60;

        StringBuilder sb = new StringBuilder();
        if (days > 0) {
            sb.append(days).append(" day").append(days > 1 ? "s" : "").append(", ");
        }
        if (hours > 0 || days > 0) { // show hours even if 0, if days are present
            sb.append(hours).append(" hour").append(hours != 1 ? "s" : "").append(", ");
        }
        sb.append(minutes).append(" minute").append(minutes != 1 ? "s" : "");

        return sb.toString();
    }
    
    /*
     * Inserts the Auction passed as parameter into the Database.
     * Returns the Auction's ID, assigned by the DB.
     * If the Auction Bean contains Items, those Items are updated so that they reference
     * this Auction in their AuctionId.
     */
    public int insert(Auction auction) throws SQLException {
    	// The ID assigned by the DB
    	int auctionId = 0;
    	
    	// Stores the old value for the AUTO COMMIT feature
    	boolean oldAutoCommit = conn.getAutoCommit();
    	
    	try {
			// Disables AUTO COMMIT
			conn.setAutoCommit(false);
			
			// Prepares the Statement
			String query = "INSERT "
					+ "INTO Auctions (BasePrice, MinIncrement, ClosingDate, SellerId) "
					+ "VALUES (?, ?, ?, ?)";
			
			PreparedStatement statement = null;
			
			try {
				// Compiles the Statement
				statement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				// Sets the Statement variables
				statement.setInt(1, auction.getBasePrice());
				statement.setInt(2, auction.getMinIncrement());
				statement.setTimestamp(3, Timestamp.valueOf(auction.getClosingDate()));
				statement.setInt(4, auction.getSellerId());
				
				// Runs the Statement
				statement.executeUpdate();
				
				// The Statement returns the set of ID(s) assigned to the inserted Item(s)
				try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
				    if (generatedKeys.next()) {
				    	// The ID that was assigned to the Auction
				        auctionId = generatedKeys.getInt(1);
				    } else {
				    	// Error
				        throw new SQLException("Creating Auction failed, no ID obtained.");
				    }
				}
			} finally {
				// Closes the PreparedStatement
				if (statement != null) {
					statement.close();
				}
			}
			
			// If the Auction contains Items, updates them with this Auction ID just retrieved
			if (auction.getItems() != null && !auction.getItems().isEmpty()) {
			    ItemDAO itemDAO = new ItemDAO(conn);
			    itemDAO.updateItemsAuctionId(auction.getItems(), auctionId);
			}
			
			// Manual COMMIT
			conn.commit();
		} catch (SQLException e) {
			// If something goes wrong it does a ROLLBACK
			// This ROLLBACK also rolls back the Items.AuctionId update
			conn.rollback();
			throw e;
		} finally {
			// After finishing the INSERT, it RESETS the AUTO COMMIT to how it was before
			conn.setAutoCommit(oldAutoCommit);
		}
		
		// Returns the ID assigned to the Auction    	
    	return auctionId;
    }
    
    /*
     * Returns a single Auction based on the ID passed as parameter.
     * Throws NoSuchAuctionException if the ID is not associated to any Auction.
     */
    public Auction getAuctionById(int id, long loginTime) throws NoSuchAuctionException, SQLException {
    	Auction auction = null;
    	
    	// Prepares the Query statement
    	String query = "SELECT Auctions.*, "
    			+ "Seller.Username AS SellerUsername, "
    			+ "Buyer.Username AS BuyerUsername, "
    			+ "Buyer.Address AS BuyerAddress "
    			+ "FROM Auctions "
    			+ "JOIN Users AS Seller ON Auctions.SellerId = Seller.Id "
    			+ "LEFT JOIN Users AS Buyer ON Auctions.BuyerId = Buyer.Id "
    			+ "WHERE Auctions.Id = ?";
    	
    	PreparedStatement statement = null;
    	ResultSet results = null;
    	
    	try {
    		// Compiles the Query
    		statement = conn.prepareStatement(query);
    		// Sets the Query variable
    		statement.setInt(1, id);
    		
    		try {
    			// Runs the Query
    			results = statement.executeQuery();
    			
    			// Prepares the ItemDAO, used to query the Items belonging to this Auction
        		ItemDAO itemDao = new ItemDAO(conn);
        		// Prepares the OfferDAO, used to query the Offers belonging to this Auction
        		OfferDAO offerDao = new OfferDAO(conn);
        		
        		if (results.next()) {
        			// Found the Auction
        			
        			// Queries the Items inside the Auction
    				List<Item> itemsInAuction = itemDao.getItemsInAuction(id);
    				
    				// Queries the Offers inside the Auction
    				List<Offer> offersInAuction = offerDao.getOffersForAuction(id);
    				
    				// Calculates the Remaining Time
    				long closingTime = results.getDate("ClosingDate").getTime();

    				long diffMillis = closingTime - loginTime;

    				String formattedRemainingTime = formatRemainingTime(diffMillis);
    				
    				// Builds the Bean object
    	            auction = buildAuction(results, itemsInAuction, offersInAuction, formattedRemainingTime);
        		} else {
        			// No Auction for this ID
        			throw new NoSuchAuctionException();
        		}
        		
    		} finally {
    			// Closed the ResultSet
    			if(results != null) {
    				results.close();
    			}
    		}
    	} finally {
    		// Closes the PreparedStatement
    		if(statement != null) {
    			statement.close();
    		}
    	}
    	
    	// Returns the Auction Bean
    	return auction;
    }

    
    /*
     * Returns a List of all the Auctions created by the User passed as parameter.
     * The Boolean parameter allows to specify if the Auctions should be closed or not.
     * The List of Auctions is ordered in Ascending Order based on the ClosingDate.
     * It also takes as parameter a long, corresponding to the User's Login Time from Session.
     */
    public List<Auction> getAuctionsCreatedBy(User user, boolean closed, long loginTime) throws SQLException {
    	// Prepares the List to return
    	List<Auction> auctions = new ArrayList<>();
    	
    	// Prepares the Query statement
    	 String query = "SELECT Auctions.*, "
                 + "Seller.Username AS SellerUsername, "
                 + "Buyer.Username AS BuyerUsername, "
                 + "Buyer.Address AS BuyerAddress "
                 + "FROM Auctions "
                 + "JOIN Users AS Seller ON Auctions.SellerId = Seller.Id "
                 + "LEFT JOIN Users AS Buyer ON Auctions.BuyerId = Buyer.Id "
                 + "WHERE SellerId = ? AND IsSold = ? "
                 + "ORDER BY ClosingDate ASC";
    	PreparedStatement statement = null;
    	ResultSet results = null;
    	try {
    		// Compiles the Query
    		statement = conn.prepareStatement(query);
    		// Sets the Query variables
    		statement.setInt(1, user.getId());
    		statement.setBoolean(2, closed);
    		
    		try {
    			// Runs the Query
    			results = statement.executeQuery();
    			
    			// Prepares the ItemDAO, used to query the Items belonging to each Auction
    			ItemDAO itemDao = new ItemDAO(conn);
    			// Prepares the OfferDAO, used to query the Offers belonging to this Auction
        		OfferDAO offerDao = new OfferDAO(conn);
    			
    			while (results.next()) {
    				// Queries the Items inside the Auction
    				List<Item> itemsInAuction = itemDao.getItemsInAuction(results.getInt("Id"));
    				
    				// Queries the Offers inside the Auction
    				List<Offer> offersInAuction = offerDao.getOffersForAuction(results.getInt("Id"));
    				
    				// Calculates the Remaining Time
    				long closingTime = results.getDate("ClosingDate").getTime();

    				long diffMillis = closingTime - loginTime;

    				String formattedRemainingTime = formatRemainingTime(diffMillis);

    				
    				// Builds the Bean object
    	            Auction auction = buildAuction(results, itemsInAuction, offersInAuction, formattedRemainingTime);
    	            
    	            // Adds the Bean into the List
    	            auctions.add(auction);
    	        }
    		} finally {
    			// Closes the ResultSet
    			if(results != null) {
    				results.close();
    			}
    		}
    	} finally {
    		// Closes the PreparedStatement
    		if(statement != null) {
    			statement.close();
    		}
    	}
    	    	
    	// Returns the List
    	return auctions;
    }
    
    /*
	 * Returns a List of all the Auctions NOT created by the User passed as parameter AND stil open.
	 * The List is ordered by the ClosingDate in Ascending order.
	 */
    public List<Auction> getAuctionsNotCreatedBy(User user, long loginTime) throws SQLException {
    	// Prepares the List to return
    	List<Auction> auctions = new ArrayList<>();
    	
    	// Prepares the Query statement
    	String query = "SELECT Auctions.*, " +
                "Seller.Username AS SellerUsername, " +
                "Buyer.Username AS BuyerUsername, " +
                "Buyer.Address AS BuyerAddress " +
                "FROM Auctions " +
                "JOIN Users AS Seller ON Auctions.SellerId = Seller.Id " +
                "LEFT JOIN Users AS Buyer ON Auctions.BuyerId = Buyer.Id " +
                "WHERE Auctions.SellerId != ? AND Auctions.IsSold = false " +
                "ORDER BY Auctions.ClosingDate ASC";
    	PreparedStatement statement = null;
    	ResultSet results = null;
    	try {
    		// Compiles the Query
    		statement = conn.prepareStatement(query);
    		// Sets the Query variables
    		statement.setInt(1, user.getId());
    		
    		try {
    			// Runs the Query
    			results = statement.executeQuery();
    			
    			// Prepares the ItemDAO, used to query the Items belonging to each Auction
    			ItemDAO itemDao = new ItemDAO(conn);
    			// Prepares the OfferDAO, used to query the Offers belonging to this Auction
        		OfferDAO offerDao = new OfferDAO(conn);
    			
    			while (results.next()) {
    				// Queries the Items inside the Auction
    				List<Item> itemsInAuction = itemDao.getItemsInAuction(results.getInt("Id"));
    				
    				// Queries the Offers inside the Auction
    				List<Offer> offersInAuction = offerDao.getOffersForAuction(results.getInt("Id"));
    				
    				// Calculates the Remaining Time
    				long closingTime = results.getDate("ClosingDate").getTime();

    				long diffMillis = closingTime - loginTime;

    				String formattedRemainingTime = formatRemainingTime(diffMillis);

    				
    				// Builds the Bean object
    	            Auction auction = buildAuction(results, itemsInAuction, offersInAuction, formattedRemainingTime);
    				
    	            // Adds the Bean into the List
    	            auctions.add(auction);
    	        }
    		} finally {
    			// Closes the ResultSet
    			if(results != null) {
    				results.close();
    			}
    		}
    	} finally {
    		// Closes the PreparedStatement
    		if(statement != null) {
    			statement.close();
    		}
    	}
    	
    	// Returns the List
    	return auctions;
    }
    
    /*
	 * Returns a List of all the Auctions won by the User passed as parameter.
	 * The List is ordered from the NEWEST to the OLDEST.
	 */
    public List<Auction> getAuctionsWonBy(User user, long loginTime) throws SQLException {
    	// Prepares the List to return
    	List<Auction> auctions = new ArrayList<>();
    	
    	// Prepares the Query statement
    	String query = "SELECT Auctions.*, " +
                "Seller.Username AS SellerUsername, " +
                "Buyer.Username AS BuyerUsername, " +
                "Buyer.Address AS BuyerAddress " +
                "FROM Auctions " +
                "JOIN Users AS Seller ON Auctions.SellerId = Seller.Id " +
                "LEFT JOIN Users AS Buyer ON Auctions.BuyerId = Buyer.Id " +
                "WHERE Auctions.BuyerId = ? " +
                "ORDER BY Auctions.ClosingDate DESC";
    	PreparedStatement statement = null;
    	ResultSet results = null;
    	try {
    		// Compiles the Query
    		statement = conn.prepareStatement(query);
    		// Sets the Query variables
    		statement.setInt(1, user.getId());
    		
    		try {
    			// Runs the Query
    			results = statement.executeQuery();
    			
    			// Prepares the ItemDAO, used to query the Items belonging to each Auction
    			ItemDAO itemDao = new ItemDAO(conn);
    			// Prepares the OfferDAO, used to query the Offers belonging to this Auction
        		OfferDAO offerDao = new OfferDAO(conn);
    			
    			while (results.next()) {
    				// Queries the Items inside the Auction
    				List<Item> itemsInAuction = itemDao.getItemsInAuction(results.getInt("Id"));
    				
    				// Queries the Offers inside the Auction
    				List<Offer> offersInAuction = offerDao.getOffersForAuction(results.getInt("Id"));
    				
    				// Calculates the Remaining Time
    				long closingTime = results.getDate("ClosingDate").getTime();

    				long diffMillis = closingTime - loginTime;

    				String formattedRemainingTime = formatRemainingTime(diffMillis);

    				
    				// Builds the Bean object
    	            Auction auction = buildAuction(results, itemsInAuction, offersInAuction, formattedRemainingTime);
    				
    	            // Adds the Bean into the List
    	            auctions.add(auction);
    	        }
    		} finally {
    			// Closes the ResultSet
    			if(results != null) {
    				results.close();
    			}
    		}
    	} finally {
    		// Closes the PreparedStatement
    		if(statement != null) {
    			statement.close();
    		}
    	}
    	
    	// Returns the List
    	return auctions;
    }
}

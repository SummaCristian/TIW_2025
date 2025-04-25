package it.polimi.tiw.DAOs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polimi.tiw.beans.Auction;
import it.polimi.tiw.beans.User;

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
     * Returns a List of all the Auctions created by the User passed as parameter.
     * The Boolean parameter allows to specify if the Auctions should be closed or not.
     * The List of Auctions is ordered in Ascending Order based on the ClosingDate
     */
    public List<Auction> getAuctionsCreatedBy(User user, boolean closed) throws SQLException {
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
                 "WHERE SellerId = ? AND IsSold = ? " +
                 "ORDER BY ClosingDate ASC";
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
    			
    			while (results.next()) {
    				// Puts the data into a Bean object
    	            Auction auction = new Auction();
    	            
    	            auction.setId(results.getInt("Id"));
    	            auction.setBasePrice(results.getInt("BasePrice"));
    	            auction.setMinIncrement(results.getInt("MinIncrement"));
    	            auction.setHighestBid(results.getObject("HighestBid") != null ? results.getInt("HighestBid") : null);
    	            auction.setClosingDate(results.getDate("ClosingDate"));
    	            auction.setSellerId(results.getInt("SellerId"));
    	            auction.setSellerUsername(results.getObject("SellerUsername") != null ? results.getString("SellerUsername") : null);
    	            auction.setSold(results.getBoolean("IsSold"));
    	            auction.setBuyerId(results.getObject("BuyerId") != null ? results.getInt("BuyerId") : null);
    	            auction.setBuyerUsername(results.getObject("BuyerUsername") != null ? results.getString("BuyerUsername") : null);
    	            auction.setBuyerAddress(results.getObject("BuyerAddress") != null ? results.getString("BuyerAddress") : null);
    	            auction.setFinalPrice(results.getObject("FinalPrice") !=  null ? results.getInt("FinalPrice") : null);

    	            // Adds the Bean into the List
    	            auctions.add(auction);
    	        }
    		} finally {
    			// Closes the ResultSet
    			results.close();
    		}
    	} finally {
    		// Closes the PreparedStatement
    		statement.close();
    	}
    	
    	// Returns the List
    	return auctions;
    }
    
    /*
	 * Returns a List of all the Auctions NOT created by the User passed as parameter AND stil open.
	 * The List is ordered by the ClosingDate in Ascending order.
	 */
    public List<Auction> getAuctionsNotCreatedBy(User user) throws SQLException {
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
    			
    			while (results.next()) {
    				// Puts the data into a Bean object
    	            Auction auction = new Auction();
    	            
    	            auction.setId(results.getInt("Id"));
    	            auction.setBasePrice(results.getInt("BasePrice"));
    	            auction.setMinIncrement(results.getInt("MinIncrement"));
    	            auction.setHighestBid(results.getObject("HighestBid") != null ? results.getInt("HighestBid") : null);
    	            auction.setClosingDate(results.getDate("ClosingDate"));
    	            auction.setSellerId(results.getInt("SellerId"));
    	            auction.setSellerUsername(results.getObject("SellerUsername") != null ? results.getString("SellerUsername") : null);
    	            auction.setSold(results.getBoolean("IsSold"));
    	            auction.setBuyerId(results.getObject("BuyerId") != null ? results.getInt("BuyerId") : null);
    	            auction.setBuyerUsername(results.getObject("BuyerUsername") != null ? results.getString("BuyerUsername") : null);
    	            auction.setBuyerAddress(results.getObject("BuyerAddress") != null ? results.getString("BuyerAddress") : null);
    	            auction.setFinalPrice(results.getObject("FinalPrice") !=  null ? results.getInt("FinalPrice") : null);

    	            // Adds the Bean into the List
    	            auctions.add(auction);
    	        }
    		} finally {
    			// Closes the ResultSet
    			results.close();
    		}
    	} finally {
    		// Closes the PreparedStatement
    		statement.close();
    	}
    	
    	// Returns the List
    	return auctions;
    }
    
    /*
	 * Returns a List of all the Auctions won by the User passed as parameter.
	 * The List is ordered from the NEWEST to the OLDEST.
	 */
    public List<Auction> getAuctionsWonBy(User user) throws SQLException {
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
    			
    			while (results.next()) {
    				// Puts the data into a Bean object
    	            Auction auction = new Auction();
    	            
    	            auction.setId(results.getInt("Id"));
    	            auction.setBasePrice(results.getInt("BasePrice"));
    	            auction.setMinIncrement(results.getInt("MinIncrement"));
    	            auction.setHighestBid(results.getObject("HighestBid") != null ? results.getInt("HighestBid") : null);
    	            auction.setClosingDate(results.getDate("ClosingDate"));
    	            auction.setSellerId(results.getInt("SellerId"));
    	            auction.setSellerUsername(results.getObject("SellerUsername") != null ? results.getString("SellerUsername") : null);
    	            auction.setSold(results.getBoolean("IsSold"));
    	            auction.setBuyerId(results.getObject("BuyerId") != null ? results.getInt("BuyerId") : null);
    	            auction.setBuyerUsername(results.getObject("BuyerUsername") != null ? results.getString("BuyerUsername") : null);
    	            auction.setBuyerAddress(results.getObject("BuyerAddress") != null ? results.getString("BuyerAddress") : null);
    	            auction.setFinalPrice(results.getObject("FinalPrice") !=  null ? results.getInt("FinalPrice") : null);

    	            // Adds the Bean into the List
    	            auctions.add(auction);
    	        }
    		} finally {
    			// Closes the ResultSet
    			results.close();
    		}
    	} finally {
    		// Closes the PreparedStatement
    		statement.close();
    	}
    	
    	// Returns the List
    	return auctions;
    }
}

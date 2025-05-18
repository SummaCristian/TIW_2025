package it.polimi.tiw.DAOs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import it.polimi.tiw.beans.Offer;
import it.polimi.tiw.exceptions.NoSuchOfferException;

/*
 * This class acts as a DAO (Data Access Object) for the Offer Table in the DB.
 * It's responsible for creating and executing the Queries that involve this specific Table.
 */
public class OfferDAO {
	// The connection to the Database
	private Connection conn;
	
	public OfferDAO(Connection conn) {
		this.conn = conn;
	}
	
	/*
	 * Builds the Bean from the data retrieved from a Query's ResultSet
	 */
	private Offer buildOffer(ResultSet results) throws SQLException {
		// Parses the OfferDate
    	Timestamp timestamp = results.getTimestamp("OfferDate");
		LocalDateTime offerDate = null;

		if (timestamp != null) {
		    offerDate = timestamp.toLocalDateTime();
		}
		
		Offer offer = new Offer(
			results.getInt("Id"),
			results.getInt("UserId"),
			results.getString("Username"),
			results.getInt("AuctionId"),
			results.getInt("OfferedPrice"),
			offerDate
		);
		
		return offer;
	}
	
	/*
	 * Inserts the Offer passsed as parametere into the Database.
	 * Returns the Offer's ID, assigned by the DB.
	 * Once the Offer is created, it also UPDATES the Auction's Current Highest Bid to this one.
	 */
	public int insert(Offer offer) throws SQLException {
		// The ID assigned by the DB
		int offerId = 0;
		
		// Stores the old value for the AUTO COMMIT feature
		boolean oldAutoCommit = conn.getAutoCommit();
		
		try {
			// Disables the AUTO COMMIT
			conn.setAutoCommit(false);
			
			// Prepares the Statement
			String query = "INSERT "
					+ "INTO Offers (UserId, AuctionId, OfferedPrice, OfferDate) "
					+ "VALUES (?, ?, ?, ?)";
			
			PreparedStatement statement = null;
			ResultSet generatedKeys = null;
			
			try {
				// Compiles the Statement
				statement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				// Sets the Statement variables
				statement.setInt(1, offer.getUserId());
				statement.setInt(2, offer.getAuctionId());
				statement.setInt(3, offer.getOfferedPrice());
				statement.setTimestamp(4, Timestamp.valueOf(offer.getOfferDate()));
				
				// Runs the Statement
				statement.executeUpdate();
				
				// The Statement returns the set of ID(s) assigned to the inserted Offer(s)
				try {
					generatedKeys = statement.getGeneratedKeys();
					
					if (generatedKeys.next()) {
						// The ID that was assigned to the Offer
						offerId = generatedKeys.getInt(1);
					} else {
						// Error
						throw new SQLException("Creating Offer failed, no ID obtained.");
					}
				} finally {
					// Closes the ResultSet
					if (generatedKeys != null) {
						generatedKeys.close();
					}
				}
				
			} finally {
				// CLoses the PreparedStatement
				if (statement != null) {
					statement.close();
				}
			}
			
			// Updates the Auction's Current Highest Bid to the ID of this Offer
			AuctionDAO auctionDao = new AuctionDAO(conn);
			auctionDao.updateAuctionsHighestBid(offer.getAuctionId(), offerId);
			
			System.out.println("Updated");
			
			// Manual COMMIT
			conn.commit();
			
		} catch (SQLException e) {
			// If something goes wrong it does a ROLLBACK
			conn.rollback();
			throw e;
		} finally {
			// After finishing the INSERT; it RESETS the AUTO COMMIT to how it was before
			conn.setAutoCommit(oldAutoCommit);
		}
		
		// Returns the ID assigned to the Offer
		return offerId;
	}
	
	/*
	 * Returns a single Offer Bean based on the ID passed as parameter.
	 * Throws NoSuchOfferException if there is no Offer with the specified ID.
	 */
	public Offer getOfferById(int offerId) throws NoSuchOfferException, SQLException {
		Offer offer = null;
		
		// Prepares the Query Statement
		String query = "SELECT Offers.*, "
				+ "Users.Username AS Username "
				+ "FROM Offers "
				+ "JOIN Users ON Offers.UserId = Users.Id "
				+ "WHERE Offers.Id = ?";
		
		PreparedStatement statement = null;
		ResultSet results = null;
		
		try {
			// Compiles the Query
			statement = conn.prepareStatement(query);
			// Sets the Query variable
			statement.setInt(1, offerId);
			
			try {
				// Runs the Quert
				results = statement.executeQuery();
				
				if (results.next()) {
					// Found the Offer
					offer = buildOffer(results);
					
				} else {
					// Offer does not exist
					throw new NoSuchOfferException();
				}
				
			} finally {
				// Closes the ResultSet
				if (results != null) {
					results.close();
				}
			}
			
		} finally {
			// Closes the PreparedStatement
			if (statement != null) {
				statement.close();
			}
		}
		
		// Returns the Offer Bean
		return offer;
	}
	
	/*
	 * Returns the List of all the Offers for the Auction whose ID is passed as parameter.
	 * Returns an empty List of Offers if there is none.
	 */
	public List<Offer> getOffersForAuction(int auctionId) throws SQLException {
		List<Offer> offers = new ArrayList<>();
		
		// Creates the Query
		String query = "SELECT Offers.*, "
				+ "Users.UserName AS Username "
				+ "FROM Offers "
				+ "JOIN Users ON Offers.UserId = Users.Id "
				+ "WHERE Offers.AuctionId = ? "
				+ "ORDER BY Offers.OfferDate DESC";
		
		PreparedStatement statement = null;
		ResultSet results = null;
		
		try {
			// Compiles the Query
			statement = conn.prepareStatement(query);
			// Sets the Query variable
			statement.setInt(1, auctionId);
			
			try {
				// Runs the Query
				results = statement.executeQuery();
				
				while (results.next()) {
					// Builds the Bean object
					Offer offer = buildOffer(results);
					
					// Adds it to the List
					offers.add(offer);
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
		
		return offers;
	}
}

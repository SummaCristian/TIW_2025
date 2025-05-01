package it.polimi.tiw.DAOs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polimi.tiw.beans.Offer;

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
		Offer offer = new Offer(
			results.getInt("Id"),
			results.getInt("UserId"),
			results.getString("Username"),
			results.getInt("AuctionId"),
			results.getInt("OfferedPrice"),
			results.getTimestamp("OfferDate")
		);
		
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
				+ "Users.FirstName AS Username "
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
				results.close();
			}
		} finally {
			// Closes the PreparedStatement
			statement.close();
		}
		
		return offers;
	}
}

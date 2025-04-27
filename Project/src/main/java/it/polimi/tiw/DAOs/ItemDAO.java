package it.polimi.tiw.DAOs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polimi.tiw.beans.*;

/*
 * This class acts as a DAO (Data Access Object) for the Items Table in the Database.
 * It's responsible for creating and executing the Queries that involve this specific Table.
 */
public class ItemDAO {
	// The connection to the Database
	private Connection conn;
	
	public ItemDAO(Connection conn) {
		this.conn = conn;
	}
	
	/*
	 * Creates the Bean from the data retrieved from a Query's ResultSet
	 */
	private Item buildItem(ResultSet results) throws SQLException {
		// Builds the Image associated to this Item
		Image image = new Image(
			results.getInt("ImageId"),
			results.getString("FileName"),
			results.getString("FilePath")
		);
		
		// Builds the Item itself, passing the Image as one of its parameters
		Item item = new Item(
			results.getInt("Id"),
			results.getString("ItemName"),
			results.getString("ItemDescription"),
			results.getInt("Price"),
			image,
			results.getInt("CreatorId"),
			results.getInt("AuctionId")
		);
		
		return item;
	}
	
	/*
	 * Returns a List of all Items belonging to the Auction passed as parameter.
	 * Returns an empty List if there is no Item to be found.
	 */
	public List<Item> getItemsInAuction(int auctionId) throws SQLException {
		// Prepares the List to return
		List<Item> items = new ArrayList<>();
		
		// Prepares the Query Statement
		String query = "SELECT Items.*, Images.* "
				+ "FROM Items "
				+ "JOIN Images ON Items.ImageId = Images.Id "
				+ "WHERE Items.AuctionId = ?";
		
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
					Item item = buildItem(results);
					
					// Adds the Bean into the List
					items.add(item);
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
		return items;
	}
}

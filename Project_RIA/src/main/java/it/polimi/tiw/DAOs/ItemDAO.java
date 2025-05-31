package it.polimi.tiw.DAOs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
			results.getObject("AuctionId", Integer.class) // Could be NULL
		);
		
		
		return item;
	}
	
	/*
	 * Inserts the data passed as parameters into the Database as an Item
	 */
	public int insert(Item item) throws SQLException {
		// The ID assigned to the Item by the Database
		int itemId = 0;
		
		// Stores the old value for the AUTO COMMIT feature
		boolean oldAutoCommit = conn.getAutoCommit();
		
		try {
			// Disables AUTO COMMIT
			conn.setAutoCommit(false);
			
			// Prepares the Statement
			String query = "INSERT "
					+ "INTO Items (ItemName, ItemDescription, Price, ImageId, CreatorId) "
					+ "VALUES (?, ?, ?, ?, ?)";
			
			PreparedStatement statement = null;
			
			try {
				// Compiles the Statement
				statement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				// Sets the Statement variables
				statement.setString(1, item.getItemName());
				statement.setString(2, item.getItemDescription());
				statement.setInt(3, item.getPrice());
				
				// Image could be NULL or an actual ID
	            if (item.getImage() != null) {
	            	// There already it an Image, put the ID into the statement
	                statement.setInt(4, item.getImage().getId());
	            } else {
	            	// No Image, put NULL into that field
	                statement.setNull(4, java.sql.Types.INTEGER);
	            }
				
				statement.setInt(5, item.getCreatorId());
				
				// Runs the Statement
				statement.executeUpdate();
				
				// The Statement returns the set of ID(s) assigned to the inserted Item(s)
				try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
				    if (generatedKeys.next()) {
				    	// The ID that was assigned to the Item
				        itemId = generatedKeys.getInt(1);
				    } else {
				    	// Error
				        throw new SQLException("Creating Item failed, no ID obtained.");
				    }
				}
			} finally {
				// Closes the PreparedStatement
				if (statement != null) {
					statement.close();
				}
			}
			
			// Manual COMMIT
			conn.commit();
		} catch (SQLException e) {
			// If something goes wrong it does a ROLLBACK
			conn.rollback();
			throw e;
		} finally {
			// After finishing the INSERT, it RESETS the AUTO COMMIT to how it was before
			conn.setAutoCommit(oldAutoCommit);
		}
		
		// Returns the ID assigned to the Item
		return itemId;
	}
	
	/*
	 * Updates the Items.AuctionId in a list of Items to the ID passed as parameter
	 * DOES NOT TOUCH AUTO-COMMIT.
	 * DOES NOT COMMIT.
	 * DOES NOT ROLLBACK.
	 * These things MUST be handled by the caller.
	 */
	public void updateItemsAuctionId(List<Item> items, int auctionId) throws SQLException {
		// Prepares the Statement
		String query = "UPDATE Items SET AuctionId = ? WHERE Id = ?";
		
		PreparedStatement statement = null;
		
		try {
			// Compiles the Statement
			statement = conn.prepareStatement(query);
			
			// Creates a copy of the Statement for each Item to update
			for (Item item: items) {
				// Sets the Statement variables
	            statement.setInt(1, auctionId);
	            statement.setInt(2, item.getId());
	            
	            // Adds the Statement to the Batch
	            statement.addBatch();
	        }
			
			// Executes ALL the Statements in the Batch
	        statement.executeBatch();
	        
		} finally {
			// Closes the PreparedStatement
			if (statement != null) {
				statement.close();
			}
		}
	}
	
	/*
	 * Updates an Item record by adding an Image to it
	 */
	public void addImageToItem(int itemId, int imageId) throws SQLException {
		// Stores the old value for the AUTO COMMIT feature
		boolean oldAutoCommit = conn.getAutoCommit();
		
		try {
			// Disables AUTO COMMIT
			conn.setAutoCommit(false);
			
			// Prepares the Statement
			String query = "UPDATE Items "
					+ "SET ImageId = ? "
					+ "WHERE Id = ?";
			
			PreparedStatement statement = null;
			
			try {
				// Compiles the Statement
				statement = conn.prepareStatement(query);
				// Sets the Statement variables
				statement.setInt(1, imageId);
				statement.setInt(2, itemId);
				
				// Runs the Statement
				statement.executeUpdate();
				
			} finally {
				// Closes the PreparedStatement
				if (statement != null) {
					statement.close();
				}
			}
			
			// Manual COMMIT
			conn.commit();
		} catch (SQLException e) {
			// If something goes wrong it does a ROLLBACK
			conn.rollback();
			throw e;
		} finally {
			// After finishing the INSERT, it RESETS the AUTO COMMIT to how it was before
			conn.setAutoCommit(oldAutoCommit);
		}
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
		return items;
	}
	
	/*
	 * Returns a List of all Items whose ID is contained in the List passed as parameterr.
	 * Returns an empty List if there is no Item to be found.
	 */
	public List<Item> getItemsByIDs(List<Integer> itemIds) throws SQLException {
		// Prepares the List to return
		List<Item> items = new ArrayList<>();
		
		// Edge case: empty list. No need to run a query for that
		if (itemIds == null || itemIds.isEmpty()) {
			return items;
		}
		
		// Prepares the Query Statement
		
		// Turns the List of IDs into a String of PLACEHOLDERS (?), separated by COMMA (,)
		String placeholders = itemIds.stream()
                						.map(id -> "?")
                						.collect(Collectors.joining(", "));
		
		String query = "SELECT Items.*, Images.* "
				+ "FROM Items "
				+ "JOIN Images ON Items.ImageId = Images.Id "
				+ "WHERE Items.Id IN (" + placeholders + ")";
		
		PreparedStatement statement = null;
		ResultSet results = null;
		
		try {
			// Compiles the Query
			statement = conn.prepareStatement(query);
			// Sets all the Query variables
			for (int i = 0; i < itemIds.size(); i++) {
				statement.setInt(i + 1, itemIds.get(i));
			}
			
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
		return items;
	}
	
	/*
	 * Returns a List of all Items NOT belonging to any Auction created by the 
	 * User whose ID is passed as parameter.
	 * Returns an empty List if there is no Item to be found.
	 */
	public List<Item> getAvailableItemsForUserId(int userId) throws SQLException {
		// Prepares the List to return
		List<Item> items = new ArrayList<>();
		
		// Prepares the Query Statement
		String query = "SELECT Items.*, Images.* "
				+ "FROM Items "
				+ "JOIN Images ON Items.ImageId = Images.Id "
				+ "WHERE Items.AuctionId IS NULL "
				+ "AND Items.CreatorId = ?";
		
		PreparedStatement statement = null;
		ResultSet results = null;
		
		try {
			// Compiles the Query
			statement = conn.prepareStatement(query);
			// Sets the Query variable
			statement.setInt(1, userId);
			
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
		return items;
	}
}

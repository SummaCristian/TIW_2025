package it.polimi.tiw.DAOs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.sql.Statement;

import it.polimi.tiw.beans.Image;
import it.polimi.tiw.exceptions.NoSuchImageException;

/*
 * This class acts as a DAO (Data Access Object) for the Images Table in the DB.
 * It's responsible for creating and executing Queries that involve this specific Table.
 */
public class ImageDAO {
	// The connection to the Database
	private Connection conn;
	
	public ImageDAO(Connection conn) {
		this.conn = conn;
	}
	
	/*
	 * Build the Bean from the data retrieved from a Query's ResultSet
	 */
	private Image buildImage(ResultSet results) throws SQLException {
		Image image = new Image(
			results.getInt("Id"),
			results.getString("FileName"),
			results.getString("FilePath")
		);
		
		return image;
	}
	
	/*
	 * Returns the Image with the ID passed as parameter.
	 * Throws NoSuchImageException if no Image exists with such ID.
	 */
	public Image getImageById(int id) throws NoSuchImageException, SQLException {
		Image image = null;
		
		String query = "SELECT * FROM Images WHERE Id = ?";
		
		PreparedStatement statement = null;
		ResultSet results = null;
		
		try {
			// Compiles the Query
			statement = conn.prepareStatement(query);
			// Sets the QUery variable
			statement.setInt(1, id);
			
			try {
				// Runs the Query
				results = statement.executeQuery();
				
				if (results.next()) {
					// Found the Image
					image = buildImage(results);
				} else {
					// Image not found
					throw new NoSuchImageException();
				}
				
			} finally {
				// Closes the ResultSet
				results.close();
			}
			
		} finally {
			// CLoses the PreparedStatement
			statement.close();
		}
		
		return image;
	}
	
	/*
	 * Inserts the Image's File Path and Name passed as parameters inside the Database.
	 * Returns the Image's ID assigned by the DB.
	 */
	public int insert(String fileName, String filePath) throws SQLException {
		String query = "INSERT INTO Images (FileName, FilePath) VALUES (?, ?)";

		// Compiles the Statement
		PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
		// Sets the Statement variables
		stmt.setString(1, fileName);
		stmt.setString(2, filePath);

		// Runs the Statement
		int affectedRows = stmt.executeUpdate();

		// Check if it went well
		if (affectedRows == 0) {
			// Error
		    throw new SQLException("Creating image failed, no rows affected.");
		}
		
		// The Statement returns the set of ID(s) assigned to the Inserted Image(s)
		try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
		    if (generatedKeys.next()) {
		    	// The ID that was assigned to the Image
		        int imageId = generatedKeys.getInt(1);
		        return imageId;
		    } else {
		    	// Error
		        throw new SQLException("Creating image failed, no ID obtained.");
		    }
		}
	}
	
	/*
	 * Deletes the Image with the ID passed as parameter from the Database.
	 * Throws NoSuchImageException if there is no Image with that ID.
	 */
	public void delete(int imageId) throws SQLException, NoSuchImageException {
		String query = "DELETE FROM Images WHERE Id = ?";
		
		PreparedStatement statement = null;
		
		try {
			// Compiles the Statement
			statement = conn.prepareStatement(query);
			// Sets the Statemetn variable
			statement.setInt(1, imageId);
			
			// Runs the Query
			int affectedRow = statement.executeUpdate();
			
			if (affectedRow == 0) {
				// The Image was NOT in the DB
				throw new NoSuchImageException();
			}
			
		} finally {
			// Closes the PreparedStatement
			statement.close();
		}
	}
}

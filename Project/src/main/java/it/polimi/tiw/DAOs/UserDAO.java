package it.polimi.tiw.DAOs;

import java.sql.*;
import it.polimi.tiw.beans.User;
import it.polimi.tiw.exceptions.*;

/*
 * This class acts as a DAO (Data Access Object) for the Users Table in the DB.
 * It's responsible for creating and executing the Queries that involve this specific Table.
 */
public class UserDAO {
    private Connection conn;

    public UserDAO(Connection conn) {
        this.conn = conn;
    }

    /*
     * Performs a credentials check to authenticate a User.
     * Receives two Strings, the User's username and password, and checks whether or not the
     * User exists, and if his password is correct.
     * Returns the User if the authentication process is successful.
     * Throws UserNotFoundException if no User exists with such username.
     * Throws IncorrectPasswordException if the password does not match the correct one.
     */
    public User checkCredentials(String username, String password) throws SQLException, IncorrectPasswordException, UserNotFoundException {
    	// Prepares the Query statement
        String query = "SELECT * FROM Users WHERE Username = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);

            // Runs the Query
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                	// Found the User
                	
                	// Checks the password
                	String correctPsw = rs.getString("Psw");
                	if (!correctPsw.equals(password)) {
                		// Incorrect password, throws the exception
                		throw new IncorrectPasswordException();
                	}
                	
                	// Creates the User Bean with the data extracted from the DB.
                    User user = new User();
                    user.setId(rs.getInt("Id"));
                    user.setUsername(rs.getString("Username"));
                    // The Password is not stored in the Bean, it never leaves the DB or DAO for security reasons.
                    user.setFirstName(rs.getString("FirstName"));
                    user.setSurname(rs.getString("Surname"));
                    user.setAddress(rs.getString("Address"));
                    // Returns the User
                    return user;
                } else {
                	// User not found, throws the exception
                    throw new UserNotFoundException();
                }
            }
        }
    }
}
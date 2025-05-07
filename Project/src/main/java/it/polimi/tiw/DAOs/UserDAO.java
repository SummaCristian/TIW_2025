package it.polimi.tiw.DAOs;

import java.sql.*;
import it.polimi.tiw.beans.User;
import it.polimi.tiw.exceptions.*;
import it.polimi.tiw.utils.PasswordUtil;

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
     * Builds the Bean from the data retrieved from a Query's ResultSet
     */
    private User buildUser(ResultSet results) throws SQLException {
    	User user = new User(
           	results.getInt("Id"),
            results.getString("Username"),
            results.getString("FirstName"),
            results.getString("Surname"),
            results.getString("Address")
        );
    	
    	return user;
    }

    /*
     * Performs a credentials check to authenticate a User.
     * Receives two Strings, the User's username and password, and checks whether or not the
     * User exists, and if his password is correct.
     * Returns the User if the authentication process is successful.
     * Throws UserNotFoundException if no User exists with such username.
     * Throws IncorrectPasswordException if the password does not match the correct one.
     */
    public User checkCredentials(String username, String password) throws SQLException, IncorrectPasswordException, UserNotFoundException, MissingParametersException {
    	// Checks if the credentials are non-null and not empty
    	if (username == null || username.isBlank() || password == null || password.isBlank()) {
			// Missing some parameters
			throw new MissingParametersException();
		}
    	
    	// Encrypts the password passed as parameter (coming from the Request) 
    	// using the same algorithm used during SignUp
    	String hashedPassword = PasswordUtil.hashPassword(password);
    	
    	// Prepares the Query statement
        String query = "SELECT * FROM Users WHERE Username = ?";
        PreparedStatement statement = null;
        ResultSet results = null;
        try {
        	// Compiles the Query
        	statement = conn.prepareStatement(query);
        	// Sets the Query variable
        	statement.setString(1, username);

            try {
            	// Runs the Query
            	results = statement.executeQuery();
            	
                if (results.next()) {
                	// Found the User
                	
                	// Checks the password
                	String correctPsw = results.getString("Psw");
                	
                	if (!correctPsw.equals(hashedPassword)) {
                		// Incorrect password, throws the exception
                		throw new IncorrectPasswordException();
                	}
                	
                	// Builds the Bean object
                	User user = buildUser(results);
                    
                    // Returns the User
                    return user;
                } else {
                	// User not found, throws the exception
                    throw new UserNotFoundException();
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
    }
    
    /*
     * Checks whether or not the combination of Username and Password(s) passed as parameters
     * are a valid combination before adding the User to the DB.
     * This method returns nothing if the validation went through successfully.
     *
     * Throws:
     * - DuplicateUsernameException if the Username already exists inside the DB.
     * - MismatchingPasswordsException if the 2 passwords are different.
     * - MissingParametersException if any of the 3 parameters is null or "".
     */
    public void validateCredentials(String username, String password1, String password2)
            throws MissingParametersException, MismatchingPasswordsException, DuplicateUsernameException, SQLException {

        // Checks if all 3 parameters are valid (not null or blank)
        if (username == null || username.isBlank() ||
            password1 == null || password1.isBlank() ||
            password2 == null || password2.isBlank()) {
            throw new MissingParametersException();
        }

        // Checks if the Username already exists in the DB
        String query = "SELECT COUNT(*) FROM Users WHERE Username = ?";
        
        PreparedStatement statement = null;
        ResultSet results = null;
        
        try {
        	// Compiles the Query
        	statement = conn.prepareStatement(query);
        	// Sets the Query variable
        	statement.setString(1, username);

            try {
            	// Runs the Query
            	results = statement.executeQuery();
            	
                if (results.next() && results.getInt(1) > 0) {
                    // User already exists with this username
                    throw new DuplicateUsernameException();
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

        // Checks if the two passwords match
        if (!password1.equals(password2)) {
        	// The passwords are different
            throw new MismatchingPasswordsException();
        }
        
        // If it reaches here, everything was fine and the credentials are to be considered validated.
    }
    
    /*
     * Inserts the User passed as parameter inside the Database.
     * Since User Beans can't contain the actual password, it must be passed as parameter too.
     * Passwords are hashed inside this method, NEEDS to be passed in clear.
     */
    public void insert(User user, String password) throws SQLException {
    	// Encrypts the password
    	String hashedPassword = PasswordUtil.hashPassword(password);
    	
    	// Stores the old value for the AUTO COMMIT feature
    	boolean oldAutoCommit = conn.getAutoCommit();
    	
    	try {
    		// Disables AUTO COMMIT
    		conn.setAutoCommit(false);
    		
    		// Prepares the Statement
        	String query = "INSERT INTO Users (Username, Psw, FirstName, Surname, Address) VALUES (?, ?, ?, ?, ?)";
        	
        	PreparedStatement statement = null;

            try {
            	// Compiles the Statement
            	statement = conn.prepareStatement(query);
            	// Sets the Statement variables
            	statement.setString(1, user.getUsername());
            	statement.setString(2, hashedPassword);
            	statement.setString(3, user.getFirstName());
            	statement.setString(4, user.getSurname());
            	statement.setString(5, user.getAddress());

            	// Runs the Statement
            	statement.executeUpdate();
            } finally {
            	// Closes the PreparedStatement
            	if(statement != null) {
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

}
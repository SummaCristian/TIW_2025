package it.polimi.tiw.exceptions;

public class UserNotFoundException extends Exception {
    
    public UserNotFoundException() {
        super("No User found with this username");
    }

    public UserNotFoundException(String message) {
        super(message);
    }
}
package it.polimi.tiw.exceptions;

public class DuplicateUsernameException extends Exception {
    
    public DuplicateUsernameException() {
        super("Incorrect password");
    }

    public DuplicateUsernameException(String message) {
        super(message);
    }
}
package it.polimi.tiw.exceptions;

public class MismatchingPasswordsException extends Exception {
    
    public MismatchingPasswordsException() {
        super("Password and Confirm Password where not the same");
    }

    public MismatchingPasswordsException(String message) {
        super(message);
    }
}
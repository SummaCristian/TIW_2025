package it.polimi.tiw.exceptions;

public class MismatchingPasswordsException extends Exception {
    
    public MismatchingPasswordsException() {
        super("Incorrect password");
    }

    public MismatchingPasswordsException(String message) {
        super(message);
    }
}
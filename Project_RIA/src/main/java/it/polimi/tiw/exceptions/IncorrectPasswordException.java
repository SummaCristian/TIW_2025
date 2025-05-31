package it.polimi.tiw.exceptions;

public class IncorrectPasswordException extends Exception {
    
    public IncorrectPasswordException() {
        super("Incorrect password");
    }

    public IncorrectPasswordException(String message) {
        super(message);
    }
}
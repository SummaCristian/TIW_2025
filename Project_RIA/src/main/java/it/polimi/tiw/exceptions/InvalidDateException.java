package it.polimi.tiw.exceptions;

public class InvalidDateException extends Exception {
    
    public InvalidDateException() {
        super("Invalid Date inside the request");
    }

    public InvalidDateException(String message) {
        super(message);
    }
}
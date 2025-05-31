package it.polimi.tiw.exceptions;

public class NegativeIncrementException extends Exception {
    
    public NegativeIncrementException() {
        super("An Auction's Minimum Increment can't be negative");
    }

    public NegativeIncrementException(String message) {
        super(message);
    }
}
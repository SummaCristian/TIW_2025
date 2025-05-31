package it.polimi.tiw.exceptions;

public class NoItemsSelectedException extends Exception {
    
    public NoItemsSelectedException() {
        super("No Items selected");
    }

    public NoItemsSelectedException(String message) {
        super(message);
    }
}
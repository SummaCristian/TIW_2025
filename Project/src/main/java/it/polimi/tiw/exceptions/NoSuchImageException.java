package it.polimi.tiw.exceptions;

public class NoSuchImageException extends Exception {
    
    public NoSuchImageException() {
        super("No Image exists with this ID");
    }

    public NoSuchImageException(String message) {
        super(message);
    }
}
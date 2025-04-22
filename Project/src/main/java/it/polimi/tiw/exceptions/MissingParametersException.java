package it.polimi.tiw.exceptions;

public class MissingParametersException extends Exception {
    
    public MissingParametersException() {
        super("Incorrect password");
    }

    public MissingParametersException(String message) {
        super(message);
    }
}
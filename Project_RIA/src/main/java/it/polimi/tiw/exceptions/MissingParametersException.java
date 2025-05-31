package it.polimi.tiw.exceptions;

public class MissingParametersException extends Exception {
    
    public MissingParametersException() {
        super("Something is missing from the requested data");
    }

    public MissingParametersException(String message) {
        super(message);
    }
}
package it.polimi.tiw.exceptions;

public class NotOwningItemException extends Exception {
    
    public NotOwningItemException() {
        super("One of the Items selected does not belong to the User");
    }

    public NotOwningItemException(String message) {
        super(message);
    }
}
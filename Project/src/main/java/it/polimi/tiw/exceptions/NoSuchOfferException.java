package it.polimi.tiw.exceptions;

public class NoSuchOfferException extends Exception {

	public NoSuchOfferException() {
		super("No Offer can be found with the specified ID");
	}
	
	public NoSuchOfferException(String message) {
		super(message);
	}
}

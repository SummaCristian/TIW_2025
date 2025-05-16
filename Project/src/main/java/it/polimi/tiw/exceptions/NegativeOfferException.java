package it.polimi.tiw.exceptions;

public class NegativeOfferException extends Exception {

	public NegativeOfferException() {
		super("Offers can't be negative or 0");
	}
	
	public NegativeOfferException(String message) {
		super(message);
	}
}

package it.polimi.tiw.exceptions;

public class LessThanBasePriceException extends Exception {
	
	public LessThanBasePriceException() {
		super("Offers can't be less than the Auction's Base Price");
	}
	
	public LessThanBasePriceException(String message) {
		super(message);
	}
}

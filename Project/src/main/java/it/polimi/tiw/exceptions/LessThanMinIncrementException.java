package it.polimi.tiw.exceptions;

public class LessThanMinIncrementException extends Exception {

	public LessThanMinIncrementException() {
		super("This Offer's increment is less than the minimum allowed for this Auction");
	}
	
	public LessThanMinIncrementException(String message) {
		super(message);
	}
}

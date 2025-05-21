package it.polimi.tiw.exceptions;

public class NotOwningAuctionException extends Exception {

	public NotOwningAuctionException() {
		super("The selected User is not the owner of the selected Auction.");
	}
	
	public NotOwningAuctionException(String message) {
		super(message);
	}
}

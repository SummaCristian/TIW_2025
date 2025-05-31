package it.polimi.tiw.exceptions;

public class AuctionNotExpiredYetException extends Exception {

	public AuctionNotExpiredYetException() {
		super("The Auction is not expired yet");
	}
	
	public AuctionNotExpiredYetException(String message) {
		super(message);
	}
}

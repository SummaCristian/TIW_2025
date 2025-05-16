package it.polimi.tiw.exceptions;

public class ClosedAuctionException extends Exception {

	public ClosedAuctionException() {
		super("The Auction is already closed");
	}
	
	public ClosedAuctionException(String message) {
		super(message);
	}
}

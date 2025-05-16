package it.polimi.tiw.exceptions;

public class UserOwnAuctionException extends Exception{
	
	public UserOwnAuctionException() {
		super("The User is attempting to make an Offer to one of his own Auctions");
	}
	
	public UserOwnAuctionException(String message) {
		super(message);
	}
}

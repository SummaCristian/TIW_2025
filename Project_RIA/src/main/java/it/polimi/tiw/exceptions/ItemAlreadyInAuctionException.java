package it.polimi.tiw.exceptions;

public class ItemAlreadyInAuctionException extends Exception {
    
    public ItemAlreadyInAuctionException() {
        super("One of the Items selected is already part of another Auction");
    }

    public ItemAlreadyInAuctionException(String message) {
        super(message);
    }
}
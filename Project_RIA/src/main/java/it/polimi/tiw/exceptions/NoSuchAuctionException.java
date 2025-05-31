package it.polimi.tiw.exceptions;

public class NoSuchAuctionException extends Exception {
    
    public NoSuchAuctionException() {
        super("No Auction exists with this ID");
    }

    public NoSuchAuctionException(String message) {
        super(message);
    }
}
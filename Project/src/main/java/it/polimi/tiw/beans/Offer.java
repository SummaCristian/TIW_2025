package it.polimi.tiw.beans;

import java.sql.Timestamp;

/*
 * This is a Java Bean corresponding to the Database's Offers table.
 * Each Offer is made of a unique ID, a price and the date and time it's been made.
 * It also references the User who made it and the Auction it's related to.
 */
public class Offer {
    private int id;
    private int userId;
    private int auctionId;
    private int offeredPrice;
    private Timestamp offerDate;
    
    private String username;
    
    // --- Constructors ---
    
    // Empty Constructor
    public Offer() {}
    
    // Full Constructor
    public Offer(
    	int id,
    	int userId,
    	String username,
    	int auctionId,
    	int offeredPrice,
    	Timestamp offerDate
    ) {
    	this.id = id;
    	this.userId = userId;
    	this.auctionId = auctionId;
    	this.offeredPrice = offeredPrice;
    	this.offerDate = offerDate;
    	
    	this.setUsername(username);
    }

    // --- Getters and Setters ---

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getAuctionId() {
        return auctionId;
    }

    public void setAuctionId(int auctionId) {
        this.auctionId = auctionId;
    }

    public int getOfferedPrice() {
        return offeredPrice;
    }

    public void setOfferedPrice(int offeredPrice) {
        this.offeredPrice = offeredPrice;
    }

    public Timestamp getOfferDate() {
        return offerDate;
    }

    public void setOfferDate(Timestamp offerDate) {
        this.offerDate = offerDate;
    }

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
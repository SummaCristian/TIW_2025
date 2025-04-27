package it.polimi.tiw.beans;

import java.sql.Date;
import java.util.List;

/*
 * This is a Java Bean corresponding to the Database's Auctions table.
 * An Auction is defined as a set of Items, with a base price, a minimum increment and a closing date.
 * It also references the User who created it, and if it's closed, the User who bought it and at what price,
 * or the current highest offer, if not closed yet.
 */
public class Auction {
	// Auction data
    private int id;
    private int basePrice;
    private int minIncrement;
    private Integer highestBid;    // Nullable
    private Date closingDate;
    private int sellerId;
    private boolean isSold;
    private Integer buyerId;       // Nullable
    private Integer finalPrice;    // Nullable
    
    // Seller User data
    private String sellerUsername;
    
    // Buyer User data
    private String buyerUsername;	// Nullable
    private String buyerAddress;	// Nullable
    
    // Remaining Time (ClosingDate - Login Time), in a formatted String form
    private String remainingTime;
    
    // Items inside the Auction
    private List<Item> items;
    
    // --- Constructors ---
    
    // Empty Constructor
    public Auction() {}
    
    // Full Constructor
    public Auction(
    	int id,
    	int basePrice,
    	int minIncrement,
    	Integer highestBid,
    	Date closingDate,
    	int sellerId,
    	boolean isSold,
    	Integer buyerId,
    	Integer finalPrice,
    	String sellerUsername,
    	String buyerUsername,
    	String buyerAddress,
    	String remainingTime,
    	List<Item> items
    ) {
    	this.id = id;
    	this.basePrice = basePrice;
    	this.minIncrement = minIncrement;
    	this.highestBid = highestBid;
    	this.closingDate = closingDate;
    	this.sellerId = sellerId;
    	this.isSold = isSold;
    	this.buyerId = buyerId;
    	this.finalPrice = finalPrice;
    	
    	this.sellerUsername = sellerUsername;
    	this.buyerUsername = buyerUsername;
    	this.buyerAddress = buyerAddress;
    	
    	this.remainingTime = remainingTime;
    	
    	this.items = items;
    }

    // --- Getters and Setters ---

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(int basePrice) {
        this.basePrice = basePrice;
    }

    public int getMinIncrement() {
        return minIncrement;
    }

    public void setMinIncrement(int minIncrement) {
        this.minIncrement = minIncrement;
    }

    public Integer getHighestBid() {
        return highestBid;
    }

    public void setHighestBid(Integer highestBid) {
        this.highestBid = highestBid;
    }

    public Date getClosingDate() {
        return closingDate;
    }

    public void setClosingDate(Date closingDate) {
        this.closingDate = closingDate;
    }

    public int getSellerId() {
        return sellerId;
    }

    public void setSellerId(int sellerId) {
        this.sellerId = sellerId;
    }

    public boolean isSold() {
        return isSold;
    }

    public void setSold(boolean isSold) {
        this.isSold = isSold;
    }

    public Integer getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(Integer buyerId) {
        this.buyerId = buyerId;
    }

    public Integer getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(Integer finalPrice) {
        this.finalPrice = finalPrice;
    }

	public String getSellerUsername() {
		return sellerUsername;
	}

	public void setSellerUsername(String sellerUsername) {
		this.sellerUsername = sellerUsername;
	}

	public String getBuyerUsername() {
		return buyerUsername;
	}

	public void setBuyerUsername(String buyerUsername) {
		this.buyerUsername = buyerUsername;
	}

	public String getBuyerAddress() {
		return buyerAddress;
	}

	public void setBuyerAddress(String buyerAddress) {
		this.buyerAddress = buyerAddress;
	}

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}

	public String getRemainingTime() {
		return remainingTime;
	}

	public void setRemainingTime(String remainingTime) {
		this.remainingTime = remainingTime;
	}
}
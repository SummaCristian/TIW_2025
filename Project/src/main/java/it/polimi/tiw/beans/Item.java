package it.polimi.tiw.beans;

/*
 * This is a Java Bean corresponding to the Database's Items table.
 * An Item is made of a unique ID, a name, a description and a price.
 * It also references the User who created it, the associated Image and the Auction that contains it,
 * if there is any.
 */
public class Item {
    private int id;
    private String itemName;
    private String itemDescription;
    private int price;
    private int imageId;
    private int creatorId;
    private Integer auctionId; // Nullable

    // --- Getters and Setters ---

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public int getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(int creatorId) {
        this.creatorId = creatorId;
    }

    public Integer getAuctionId() {
        return auctionId;
    }

    public void setAuctionId(Integer auctionId) {
        this.auctionId = auctionId;
    }
}
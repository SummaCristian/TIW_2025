CREATE DATABASE IF NOT EXISTS Progetto_Tiw;
USE Progetto_Tiw;

-- Stores User's informations and login credentials
CREATE TABLE Users (
		Id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
        -- Login Username
        Username VARCHAR(255) NOT NULL UNIQUE,
        -- Password
        Psw VARCHAR(255) NOT NULL,
        -- Anagraphics
        FirstName VARCHAR(255) NOT NULL CHECK (LENGTH(TRIM(FirstName)) > 0),
        Surname VARCHAR(255) NOT NULL CHECK (LENGTH(TRIM(Surname)) > 0),
        -- User Address, used for shipping of Auctioned Items
        Address VARCHAR(255) NOT NULL CHECK (LENGTH(TRIM(Address)) > 0)
);

-- Stores the File names and paths for Image resources of Items
CREATE TABLE Images (
	Id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    -- Name of the image file
    FileName VARCHAR(255) NOT NULL CHECK (LENGTH(TRIM(FileName)) > 0),
    -- Path inside the FileSystem to locate the file
    FilePath VARCHAR(255) NOT NULL UNIQUE CHECK (LENGTH(TRIM(FilePath)) > 0)
);

-- Stores all the Auctions created by Users, with all related data
CREATE TABLE Auctions (
	Id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    -- Starting Price for the Auction, calculated as the Sum of all the Item's Prices
    BasePrice INT NOT NULL CHECK (BasePrice > 0),
    -- Minimum Allowed Increment for Offers
    MinIncrement INT NOT NULL CHECK (MinIncrement > 0),
    -- Current Highest Offer
    -- NULL if no Offers have been made yet
    HighestBid INT CHECK (HighestBid IS NULL OR HighestBid >= 0),
    -- Ending Date for the Auction
    ClosingDate DATE NOT NULL,
    -- The ID of the User who created the Auction
    SellerId INT NOT NULL,
    -- A Boolean value to determine whether the Auction has been closed or not
    IsSold BOOLEAN DEFAULT FALSE NOT NULL,
    -- The ID of the User who won the Auction
    BuyerId INT,
    -- The Price of the winning Offer
    FinalPrice INT CHECK (FinalPrice IS NULL OR FinalPrice >= 0),
    -- Foreign Keys
    FOREIGN KEY (BuyerId) REFERENCES Users(Id)
		-- If the buyer User is deleted, the Auction is not deleted
        ON DELETE SET NULL
        ON UPDATE CASCADE,
	FOREIGN KEY (SellerId) REFERENCES Users(Id)
		-- Users with Auctions can't delete their profile
		ON DELETE RESTRICT
        ON UPDATE CASCADE
);

/*
	Stores the Items created by the Users
	Items can be part of an Auction or not 
*/
CREATE TABLE Items (
	Id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    -- The Name of the Item
    ItemName VARCHAR(255) NOT NULL CHECK (LENGTH(TRIM(ItemName)) > 0),
    -- The Description of the Item
    ItemDescription VARCHAR(1023) NOT NULL CHECK (LENGTH(TRIM(ItemDescription)) > 0),
    -- The Price of the Item
    Price INT NOT NULL CHECK (Price > 0),
    -- The ID of the Image of the Item, used to retrieve the image FilePath
    ImageId INT NOT NULL,
    -- The ID of the User who created the Item
    CreatorId INT NOT NULL,
    /*
		The ID of the Auction in which the Item is being sold.
		An Item can only reference 1 Auction, so no duplicates allowed.
		It can also be NULL, meaning the Item does not belong to any Auction yet.
	*/
    AuctionId INT,
    -- Foreign Keys
    FOREIGN KEY (ImageId) REFERENCES Images(Id)
		-- Images can't be deleted if they belong to an Item
		ON DELETE RESTRICT
        ON UPDATE CASCADE,
    FOREIGN KEY (CreatorId) REFERENCES Users(Id)
		ON DELETE CASCADE
        ON UPDATE CASCADE,
    FOREIGN KEY (AuctionId) REFERENCES Auctions(Id)
		/*
			If an Auction is Deleted, the Items are not deleted,
			but marked as not in an Auction
		*/
        ON DELETE SET NULL
        ON UPDATE CASCADE
);

-- Stores the Offers made by the Users inside Auctions
CREATE TABLE Offers (
    Id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    -- The ID of the User who made the Offer
    UserId INT NOT NULL,
    -- The ID of the Auction where the Offered has been made
    AuctionId INT NOT NULL,
    -- The Price offered by the User
    OfferedPrice INT NOT NULL CHECK (OfferedPrice > 0),
    -- The Date and Time the Offer has been made
    OfferDate DATETIME NOT NULL,
    -- Foreign Keys
    FOREIGN KEY (UserId) REFERENCES Users(Id)
		ON DELETE CASCADE
        ON UPDATE CASCADE,
	FOREIGN KEY (AuctionId) REFERENCES Auctions(Id)
		ON DELETE CASCADE
        ON UPDATE CASCADE
);

-- Indexes for performance
CREATE INDEX idx_offers_auction ON Offers(AuctionId);
CREATE INDEX idx_offers_user ON Offers(UserId);
CREATE INDEX idx_items_auction ON Items(AuctionId);
CREATE INDEX idx_items_creator ON Items(CreatorId);
CREATE INDEX idx_items_image ON Items(ImageId);
CREATE INDEX idx_auctions_seller ON Auctions(SellerId);
CREATE INDEX idx_auctions_buyer ON Auctions(BuyerId);



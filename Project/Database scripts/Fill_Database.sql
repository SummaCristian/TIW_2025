USE Progetto_Tiw;

-- Creates some User profiles
-- Note: Psw: '9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08' == 'test' (hashed)
INSERT INTO Users (Username, Psw, FirstName, LastName, Address) 
	VALUES ('Test', '9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08', 'Test', 'Test', 'Test');
    
INSERT INTO Users (Username, Psw, FirstName, LastName, Address)
	VALUES ('Test2', '9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08', 'Test', 'Test', 'Test');

INSERT INTO Users (Username, Psw, FirstName, LastName, Address)
	VALUES ('Test3', '9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08', 'Test', 'Test', 'Test');
    
-- Auction 1 - User 1
INSERT INTO Auctions (BasePrice, MinIncrement, HighestBidId, ClosingDate, SellerId, IsSold, BuyerId, FinalPrice)
	VALUES (350, 10, NULL, '2025-08-01 08:00:00', 1, FALSE, NULL, NULL);

INSERT INTO Images (FileName, FilePath) VALUES ('item_1.jpg', 'images/uploaded_images/item_1.jpg');
INSERT INTO Items (ItemName, ItemDescription, Price, ImageId, CreatorId, AuctionId)
	VALUES ('Casio Watch', 'Vintage Casio Digital Watch, model A155W Module 593, 1980.', 150, 1, 1, 1);

INSERT INTO Images (FileName, FilePath) VALUES ('item_2.jpg', 'images/uploaded_images/item_2.jpg');
INSERT INTO Items (ItemName, ItemDescription, Price, ImageId, CreatorId, AuctionId)
	VALUES ('Polaroid SX-70', 'Classic folding instant camera with original leather case, 1972 model.', 200, 2, 1, 1);
    
-- Auction 2 - User 1
INSERT INTO Auctions (BasePrice, MinIncrement, HighestBidId, ClosingDate, SellerId, IsSold, BuyerId, FinalPrice)
	VALUES (300, 10, NULL, '2025-08-01 08:00:00', 1, FALSE, NULL, NULL);

INSERT INTO Images (FileName, FilePath) VALUES ('item_3.jpg', 'images/uploaded_images/item_3.jpg');
INSERT INTO Items (ItemName, ItemDescription, Price, ImageId, CreatorId, AuctionId)
	VALUES ('Nikon F3', 'Professional 35mm SLR with Nikkor 50mm f/1.4 lens, excellent condition.', 300, 3, 1, 2);
    
-- Auction 3 - User 2
INSERT INTO Auctions (BasePrice, MinIncrement, HighestBidId, ClosingDate, SellerId, IsSold, BuyerId, FinalPrice)
	VALUES (280, 10, NULL, '2025-09-01 08:00:00', 2, FALSE, NULL, NULL);
    
INSERT INTO Images (FileName, FilePath) VALUES ('item_4.jpg', 'images/uploaded_images/item_4.jpg');
INSERT INTO Items (ItemName, ItemDescription, Price, ImageId, CreatorId, AuctionId)
	VALUES ('Game Boy Color', 'Fully working Game Boy Color with Pokémon Red cartridge, minor wear.', 100, 4, 2, 3);
    
INSERT INTO Images (FileName, FilePath) VALUES ('item_5.jpg', 'images/uploaded_images/item_5.jpg');
INSERT INTO Items (ItemName, ItemDescription, Price, ImageId, CreatorId, AuctionId) 
	VALUES ('Sony Walkman WM-FX197', 'Cassette player from early 2000s, working radio and headphone jack.', 60, 5, 2, 3);

INSERT INTO Images (FileName, FilePath) VALUES ('item_6.jpg', 'images/uploaded_images/item_6.jpg');
INSERT INTO Items (ItemName, ItemDescription, Price, ImageId, CreatorId, AuctionId)
	VALUES ('Apple iPod Classic (5th Gen, 80GB)', 'Working iPod Classic with original charger and cable, minor scratches.', 120, 6, 2, 3);
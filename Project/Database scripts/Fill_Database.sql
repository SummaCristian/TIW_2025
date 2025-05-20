USE Progetto_Tiw;

-- Creates some User profiles
-- Note: Psw: '9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08' == 'test' (hashed)

-- 1
INSERT INTO Users (Username, Psw, FirstName, LastName, Address) 
	VALUES ('Doctor1', '9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08', 'William', 'Hartnell', 'Test');

-- 2
INSERT INTO Users (Username, Psw, FirstName, LastName, Address)
	VALUES ('Doctor2', '9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08', 'Patrick', 'Troughton', 'Test');

-- 3
INSERT INTO Users (Username, Psw, FirstName, LastName, Address)
	VALUES ('Doctor3', '9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08', 'Jon', 'Pertwee', 'Test');

-- 4
INSERT INTO Users (Username, Psw, FirstName, LastName, Address) 
	VALUES ('Doctor4', '9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08', 'Tom', 'Baker', 'Test');

-- 5
INSERT INTO Users (Username, Psw, FirstName, LastName, Address) 
	VALUES ('Doctor5', '9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08', 'Peter', 'Davison', 'Test');

-- 6
INSERT INTO Users (Username, Psw, FirstName, LastName, Address) 
	VALUES ('Doctor6', '9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08', 'Colin', 'Baker', 'Test');
    
-- 7
INSERT INTO Users (Username, Psw, FirstName, LastName, Address) 
	VALUES ('Doctor7', '9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08', 'Sylvester', 'McCoy', 'Test');
    
-- 8
INSERT INTO Users (Username, Psw, FirstName, LastName, Address) 
	VALUES ('Doctor8', '9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08', 'Paul', 'McGann', 'Test');
    
-- 9
INSERT INTO Users (Username, Psw, FirstName, LastName, Address) 
	VALUES ('Doctor9', '9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08', 'Christopher', 'Eccleston', 'Test');
    
-- 10
INSERT INTO Users (Username, Psw, FirstName, LastName, Address) 
	VALUES ('Doctor10', '9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08', 'David', 'Tennant', 'Test');

-- 11
INSERT INTO Users (Username, Psw, FirstName, LastName, Address) 
	VALUES ('Doctor11', '9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08', 'Matt', 'Smith', 'Test');
    
-- 12
INSERT INTO Users (Username, Psw, FirstName, LastName, Address) 
	VALUES ('Doctor12', '9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08', 'Peter', 'Capaldi', 'Test');

-- 13
INSERT INTO Users (Username, Psw, FirstName, LastName, Address) 
	VALUES ('Doctor13', '9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08', 'Jodie', 'Whittaker', 'Test');

-- 14
INSERT INTO Users (Username, Psw, FirstName, LastName, Address) 
	VALUES ('Doctor14', '9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08', 'David', 'Tennant', 'Test');

-- 15
INSERT INTO Users (Username, Psw, FirstName, LastName, Address) 
	VALUES ('Doctor15', '9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08', 'Ncuti', 'Gatwa', 'Test');

-- 16 (Test)
INSERT INTO Users (Username, Psw, FirstName, LastName, Address) 
	VALUES ('Test', '9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08', 'Test', 'Test', 'Test');
    
-- Auction 1 - User 1
INSERT INTO Auctions (BasePrice, MinIncrement, HighestBidId, ClosingDate, SellerId, IsSold, BuyerId, FinalPrice)
	VALUES (330, 10, NULL, '2025-08-01 08:00:00', 1, FALSE, NULL, NULL);

INSERT INTO Images (FileName, FilePath) VALUES ('item_1.jpg', 'images/uploaded_images/item_1.jpg');
INSERT INTO Items (ItemName, ItemDescription, Price, ImageId, CreatorId, AuctionId)
	VALUES ('Pocket Watch (Antique)', 'Early 20th century mechanical pocket watch, silver-plated.', 180, 1, 1, 1);

INSERT INTO Images (FileName, FilePath) VALUES ('item_2.jpg', 'images/uploaded_images/item_2.jpg');
INSERT INTO Items (ItemName, ItemDescription, Price, ImageId, CreatorId, AuctionId)
	VALUES ('Vintage British Overcoat', 'Wool trench coat, London-made, early 60s style.', 150, 2, 1, 1);
    
INSERT INTO Offers (UserId, AuctionId, OfferedPrice, OfferDate) VALUES (15, 1, 340, '2025-05-18 09:02:00');
INSERT INTO Offers (UserId, AuctionId, OfferedPrice, OfferDate) VALUES (12, 1, 350, '2025-05-18 09:05:00');
INSERT INTO Offers (UserId, AuctionId, OfferedPrice, OfferDate) VALUES (11, 1, 360, '2025-05-18 09:14:00');
INSERT INTO Offers (UserId, AuctionId, OfferedPrice, OfferDate) VALUES (13, 1, 370, '2025-05-18 09:16:00');
INSERT INTO Offers (UserId, AuctionId, OfferedPrice, OfferDate) VALUES (8, 1, 380, '2025-05-18 09:22:00');
UPDATE Auctions SET HighestBidId = LAST_INSERT_ID() WHERE Id = 1;

-- Auction 2 - User 2
INSERT INTO Auctions (BasePrice, MinIncrement, HighestBidId, ClosingDate, SellerId, IsSold, BuyerId, FinalPrice)
	VALUES (90, 10, NULL, '2025-08-01 08:00:00', 1, FALSE, NULL, NULL);

INSERT INTO Images (FileName, FilePath) VALUES ('item_3.jpg', 'images/uploaded_images/item_3.jpg');
INSERT INTO Items (ItemName, ItemDescription, Price, ImageId, CreatorId, AuctionId)
	VALUES ('Recorder (Woodwind Instrument)', 'Vintage soprano recorder in wood, 1960s educational model.', 90, 3, 1, 2);

INSERT INTO Offers (UserId, AuctionId, OfferedPrice, OfferDate) VALUES (5, 2, 90, '2025-05-18 08:34:00');
INSERT INTO Offers (UserId, AuctionId, OfferedPrice, OfferDate) VALUES (10, 2, 100, '2025-05-18 08:44:00');
INSERT INTO Offers (UserId, AuctionId, OfferedPrice, OfferDate) VALUES (5, 2, 111, '2025-05-18 08:45:21');
UPDATE Auctions SET HighestBidId = LAST_INSERT_ID() WHERE Id = 2;

-- Auction 3 - User 2
INSERT INTO Auctions (BasePrice, MinIncrement, HighestBidId, ClosingDate, SellerId, IsSold, BuyerId, FinalPrice)
	VALUES (60, 10, NULL, '2025-09-01 08:00:00', 2, TRUE, 1, 100);
    
INSERT INTO Images (FileName, FilePath) VALUES ('item_4.jpg', 'images/uploaded_images/item_4.jpg');
INSERT INTO Items (ItemName, ItemDescription, Price, ImageId, CreatorId, AuctionId)
	VALUES ('The War Games Script Replica', 'Fan-made bound script copy from classic Doctor Who serial.', 60, 4, 2, 3);

INSERT INTO Offers (UserId, AuctionId, OfferedPrice, OfferDate) VALUES (1, 3, 70, '2025-05-18 09:02:00');
INSERT INTO Offers (UserId, AuctionId, OfferedPrice, OfferDate) VALUES (11, 3, 80, '2025-05-18 09:08:00');
INSERT INTO Offers (UserId, AuctionId, OfferedPrice, OfferDate) VALUES (13, 3, 90, '2025-05-18 09:13:00');
INSERT INTO Offers (UserId, AuctionId, OfferedPrice, OfferDate) VALUES (1, 3, 100, '2025-05-18 09:17:00');
UPDATE Auctions SET HighestBidId = LAST_INSERT_ID() WHERE Id = 3;

-- Auction 4 - User 3
INSERT INTO Auctions (BasePrice, MinIncrement, HighestBidId, ClosingDate, SellerId, IsSold, BuyerId, FinalPrice)
	VALUES (200, 10, NULL, '2025-08-01 08:00:00', 3, FALSE, NULL, NULL);
    
INSERT INTO Images (FileName, FilePath) VALUES ('item_5.jpg', 'images/uploaded_images/item_5.jpg');
INSERT INTO Items (ItemName, ItemDescription, Price, ImageId, CreatorId, AuctionId)
	VALUES ('Velvet Smoking Jacket', 'Elegant wine-red velvet jacket, 1970s style.', 60, 5, 3, 4);

INSERT INTO Images (FileName, FilePath) VALUES ('item_6.jpg', 'images/uploaded_images/item_6.jpg');
INSERT INTO Items (ItemName, ItemDescription, Price, ImageId, CreatorId, AuctionId)
	VALUES ('Bessie Diecast Model', '1:24 scale replica of Third Doctor\'s yellow car.', 100, 6, 3, 4);

INSERT INTO Images (FileName, FilePath) VALUES ('item_7.jpg', 'images/uploaded_images/item_7.jpg');
INSERT INTO Items (ItemName, ItemDescription, Price, ImageId, CreatorId, AuctionId)
	VALUES ('UNIT Pass Replica', 'Fictional military pass with retro lanyard', 40, 7, 3, 4);

INSERT INTO Offers (UserId, AuctionId, OfferedPrice, OfferDate) VALUES (10, 4, 200, '2025-05-18 09:00:00');
INSERT INTO Offers (UserId, AuctionId, OfferedPrice, OfferDate) VALUES (5, 4, 210, '2025-05-18 09:06:00');
INSERT INTO Offers (UserId, AuctionId, OfferedPrice, OfferDate) VALUES (8, 4, 220, '2025-05-18 09:10:00');
INSERT INTO Offers (UserId, AuctionId, OfferedPrice, OfferDate) VALUES (9, 4, 230, '2025-05-18 09:15:00');
UPDATE Auctions SET HighestBidId = LAST_INSERT_ID() WHERE Id = 4;

-- Auction 5 - User 4
INSERT INTO Auctions (BasePrice, MinIncrement, HighestBidId, ClosingDate, SellerId, IsSold, BuyerId, FinalPrice)
	VALUES (150, 10, NULL, '2025-08-01 08:00:00', 4, FALSE, NULL, NULL);
    
INSERT INTO Images (FileName, FilePath) VALUES ('item_8.jpg', 'images/uploaded_images/item_8.jpg');
INSERT INTO Items (ItemName, ItemDescription, Price, ImageId, CreatorId, AuctionId)
	VALUES ('Knit Scarf', 'Iconic multicolor scarf, handmade by fan.', 80, 8, 4, 5);

INSERT INTO Images (FileName, FilePath) VALUES ('item_9.jpg', 'images/uploaded_images/item_9.jpg');
INSERT INTO Items (ItemName, ItemDescription, Price, ImageId, CreatorId, AuctionId)
	VALUES ('BBC VHS Box Set', 'Doctor Who Season 12 original BBC VHS tapes.', 70, 9, 4, 5);

INSERT INTO Offers (UserId, AuctionId, OfferedPrice, OfferDate) VALUES (6, 5, 150, '2025-05-18 09:00:00');
INSERT INTO Offers (UserId, AuctionId, OfferedPrice, OfferDate) VALUES (7, 5, 160, '2025-05-18 09:07:00');
UPDATE Auctions SET HighestBidId = LAST_INSERT_ID() WHERE Id = 5;

-- Auction 6 - User 4
INSERT INTO Auctions (BasePrice, MinIncrement, HighestBidId, ClosingDate, SellerId, IsSold, BuyerId, FinalPrice)
	VALUES (45, 10, NULL, '2025-08-01 08:00:00', 4, FALSE, NULL, NULL);

INSERT INTO Images (FileName, FilePath) VALUES ('item_10.jpg', 'images/uploaded_images/item_10.jpg');
INSERT INTO Items (ItemName, ItemDescription, Price, ImageId, CreatorId, AuctionId)
	VALUES ('Brown Felt Fedora', 'Vitnage hat in a great condition.', 45, 10, 4, 6);

-- No offers

-- Auction 7 - User 5
INSERT INTO Auctions (BasePrice, MinIncrement, HighestBidId, ClosingDate, SellerId, IsSold, BuyerId, FinalPrice)
	VALUES (250, 10, NULL, '2025-08-01 08:00:00', 5, FALSE, NULL, NULL);
    
INSERT INTO Images (FileName, FilePath) VALUES ('item_11.jpg', 'images/uploaded_images/item_11.jpg');
INSERT INTO Items (ItemName, ItemDescription, Price, ImageId, CreatorId, AuctionId)
	VALUES ('Cricket Bat (Vintage)', 'Slazenger bat, 1980s era, good condition.', 130, 11, 5, 7);
    
INSERT INTO Images (FileName, FilePath) VALUES ('item_12.jpg', 'images/uploaded_images/item_12.jpg');
INSERT INTO Items (ItemName, ItemDescription, Price, ImageId, CreatorId, AuctionId)
	VALUES ('Edwardian Coat (Replica)', 'Off-white coat with red piping, cosplay-grade.', 120, 12, 5, 7);

INSERT INTO Offers (UserId, AuctionId, OfferedPrice, OfferDate) VALUES (2, 7, 250, '2025-05-18 09:04:00');
INSERT INTO Offers (UserId, AuctionId, OfferedPrice, OfferDate) VALUES (8, 7, 260, '2025-05-18 09:09:00');
UPDATE Auctions SET HighestBidId = LAST_INSERT_ID() WHERE Id = 7;

-- Auction 8 - User 5
INSERT INTO Auctions (BasePrice, MinIncrement, HighestBidId, ClosingDate, SellerId, IsSold, BuyerId, FinalPrice)
	VALUES (40, 10, NULL, '2025-08-01 08:00:00', 5, FALSE, NULL, NULL);
    
INSERT INTO Images (FileName, FilePath) VALUES ('item_13.jpg', 'images/uploaded_images/item_13.jpg');
INSERT INTO Items (ItemName, ItemDescription, Price, ImageId, CreatorId, AuctionId)
	VALUES ('Soundtrack Cassette', 'Official DW 80s soundtrack, rare format.', 40, 13, 5, 8);

INSERT INTO Offers (UserId, AuctionId, OfferedPrice, OfferDate) VALUES (9, 8, 40, '2025-05-18 09:01:00');
INSERT INTO Offers (UserId, AuctionId, OfferedPrice, OfferDate) VALUES (12, 8, 50, '2025-05-18 09:03:00');
INSERT INTO Offers (UserId, AuctionId, OfferedPrice, OfferDate) VALUES (14, 8, 60, '2025-05-18 09:08:00');
UPDATE Auctions SET HighestBidId = LAST_INSERT_ID() WHERE Id = 8;

-- Auciton 9 - User 6
INSERT INTO Auctions (BasePrice, MinIncrement, HighestBidId, ClosingDate, SellerId, IsSold, BuyerId, FinalPrice)
	VALUES (150, 10, NULL, '2025-08-01 08:00:00', 6, FALSE, NULL, NULL);

INSERT INTO Offers (UserId, AuctionId, OfferedPrice, OfferDate) VALUES (10, 9, 150, '2025-05-18 09:00:00');
INSERT INTO Offers (UserId, AuctionId, OfferedPrice, OfferDate) VALUES (13, 9, 160, '2025-05-18 09:10:00');
UPDATE Auctions SET HighestBidId = LAST_INSERT_ID() WHERE Id = 9;
    
INSERT INTO Images (FileName, FilePath) VALUES ('item_14.jpg', 'images/uploaded_images/item_14.jpg');
INSERT INTO Items (ItemName, ItemDescription, Price, ImageId, CreatorId, AuctionId)
	VALUES ('Patchwork Coat Replica', 'Bold colorful coat, custom fan-made, L size.', 150, 14, 6, 9);

-- Auction 10 - User 6
INSERT INTO Auctions (BasePrice, MinIncrement, HighestBidId, ClosingDate, SellerId, IsSold, BuyerId, FinalPrice)
	VALUES (250, 10, NULL, '2025-08-01 08:00:00', 6, FALSE, NULL, NULL);
    
INSERT INTO Images (FileName, FilePath) VALUES ('item_15.jpg', 'images/uploaded_images/item_15.jpg');
INSERT INTO Items (ItemName, ItemDescription, Price, ImageId, CreatorId, AuctionId)
	VALUES ('Portable TV Radio Combo', 'Gadget with working screen and FM, 80s tech.', 70, 15, 6, 10);

INSERT INTO Images (FileName, FilePath) VALUES ('item_16.jpg', 'images/uploaded_images/item_16.jpg');
INSERT INTO Items (ItemName, ItemDescription, Price, ImageId, CreatorId, AuctionId)
	VALUES ('ZX Spectrum Console', 'Vintage 8-bit home computer, UK model.', 180, 16, 6, 10);

INSERT INTO Offers (UserId, AuctionId, OfferedPrice, OfferDate) VALUES (1, 10, 250, '2025-05-18 09:03:00');
INSERT INTO Offers (UserId, AuctionId, OfferedPrice, OfferDate) VALUES (7, 10, 260, '2025-05-18 09:05:00');
INSERT INTO Offers (UserId, AuctionId, OfferedPrice, OfferDate) VALUES (11, 10, 270, '2025-05-18 09:09:00');
UPDATE Auctions SET HighestBidId = LAST_INSERT_ID() WHERE Id = 10;

-- Auction 11 - User 7
INSERT INTO Auctions (BasePrice, MinIncrement, HighestBidId, ClosingDate, SellerId, IsSold, BuyerId, FinalPrice)
	VALUES (205, 10, NULL, '2025-08-01 08:00:00', 7, FALSE, NULL, NULL);
    
INSERT INTO Images (FileName, FilePath) VALUES ('item_17.jpg', 'images/uploaded_images/item_17.jpg');
INSERT INTO Items (ItemName, ItemDescription, Price, ImageId, CreatorId, AuctionId)
	VALUES ('Panama Hat', 'Light hat with inner silk lining, cosplay-quality', 50, 17, 7, 11);

INSERT INTO Images (FileName, FilePath) VALUES ('item_18.jpg', 'images/uploaded_images/item_18.jpg');
INSERT INTO Items (ItemName, ItemDescription, Price, ImageId, CreatorId, AuctionId)
	VALUES ('Vintage Chemistry Set', 'Complete science set from 60s, in wooden case.', 90, 18, 7, 11);

INSERT INTO Images (FileName, FilePath) VALUES ('item_19.jpg', 'images/uploaded_images/item_19.jpg');
INSERT INTO Items (ItemName, ItemDescription, Price, ImageId, CreatorId, AuctionId)
	VALUES ('Question Mark Vest', 'Fan-made, accurate to screen design.', 65, 19, 7, 11);

INSERT INTO Offers (UserId, AuctionId, OfferedPrice, OfferDate) VALUES (9, 11, 205, '2025-05-18 09:01:00');
INSERT INTO Offers (UserId, AuctionId, OfferedPrice, OfferDate) VALUES (6, 11, 215, '2025-05-18 09:18:00');
UPDATE Auctions SET HighestBidId = LAST_INSERT_ID() WHERE Id = 11;

-- Auction 12 - User 8
INSERT INTO Auctions (BasePrice, MinIncrement, HighestBidId, ClosingDate, SellerId, IsSold, BuyerId, FinalPrice)
	VALUES (370, 10, NULL, '2025-08-01 08:00:00', 8, FALSE, NULL, NULL);
    
INSERT INTO Images (FileName, FilePath) VALUES ('item_20.jpg', 'images/uploaded_images/item_20.jpg');
INSERT INTO Items (ItemName, ItemDescription, Price, ImageId, CreatorId, AuctionId)
	VALUES ('Leather Diary Replica', 'Aged leather journal with sketches and aged paper.', 70, 20, 8, 12);

INSERT INTO Images (FileName, FilePath) VALUES ('item_21.jpg', 'images/uploaded_images/item_21.jpg');
INSERT INTO Items (ItemName, ItemDescription, Price, ImageId, CreatorId, AuctionId)
	VALUES ('Kodak Retina Camera', 'Folding 35mm film camera, German make', 160, 21, 8, 12);

INSERT INTO Images (FileName, FilePath) VALUES ('item_22.jpg', 'images/uploaded_images/item_22.jpg');
INSERT INTO Items (ItemName, ItemDescription, Price, ImageId, CreatorId, AuctionId)
	VALUES ('Victorian Overcoat', 'Dark green overcoat, cosplay quality.', 140, 22, 8, 12);

INSERT INTO Offers (UserId, AuctionId, OfferedPrice, OfferDate) VALUES (14, 12, 370, '2025-05-18 09:02:00');
INSERT INTO Offers (UserId, AuctionId, OfferedPrice, OfferDate) VALUES (11, 12, 380, '2025-05-18 09:19:00');
INSERT INTO Offers (UserId, AuctionId, OfferedPrice, OfferDate) VALUES (6, 12, 390, '2025-05-18 09:34:00');
UPDATE Auctions SET HighestBidId = LAST_INSERT_ID() WHERE Id = 12;

-- Auction 13 - User 9
INSERT INTO Auctions (BasePrice, MinIncrement, HighestBidId, ClosingDate, SellerId, IsSold, BuyerId, FinalPrice)
	VALUES (245, 10, NULL, '2025-08-01 08:00:00', 9, FALSE, NULL, NULL);
    
INSERT INTO Images (FileName, FilePath) VALUES ('item_23.jpg', 'images/uploaded_images/item_23.jpg');
INSERT INTO Items (ItemName, ItemDescription, Price, ImageId, CreatorId, AuctionId)
	VALUES ('Black Leather Jacket', 'Urban-style real leather jacket, 2000s', 120, 23, 9, 13);

INSERT INTO Images (FileName, FilePath) VALUES ('item_24.jpg', 'images/uploaded_images/item_24.jpg');
INSERT INTO Items (ItemName, ItemDescription, Price, ImageId, CreatorId, AuctionId)
	VALUES ('Series 1 DVD Box Set', 'Complete 2005 DW reboot series on DVD.', 60, 24, 9, 13);
    
INSERT INTO Images (FileName, FilePath) VALUES ('item_25.jpg', 'images/uploaded_images/item_25.jpg');
INSERT INTO Items (ItemName, ItemDescription, Price, ImageId, CreatorId, AuctionId)
	VALUES ('Sony CD Walkman', 'Portable CD player, includes earbuds.', 45, 25, 9, 13);

INSERT INTO Offers (UserId, AuctionId, OfferedPrice, OfferDate) VALUES (8, 13, 245, '2025-05-18 09:03:00');
INSERT INTO Offers (UserId, AuctionId, OfferedPrice, OfferDate) VALUES (7, 13, 255, '2025-05-18 09:21:00');
UPDATE Auctions SET HighestBidId = LAST_INSERT_ID() WHERE Id = 13;

-- Auction 14 - User 10
INSERT INTO Auctions (BasePrice, MinIncrement, HighestBidId, ClosingDate, SellerId, IsSold, BuyerId, FinalPrice)
	VALUES (395, 30, NULL, '2025-08-01 08:00:00', 10, FALSE, NULL, NULL);
    
INSERT INTO Images (FileName, FilePath) VALUES ('item_26.jpg', 'images/uploaded_images/item_26.jpg');
INSERT INTO Items (ItemName, ItemDescription, Price, ImageId, CreatorId, AuctionId)
	VALUES ('Converse High-Tops (Red)', 'Worn red Converse sneakers, size 42.', 50, 26, 10, 14);

INSERT INTO Images (FileName, FilePath) VALUES ('item_27.jpg', 'images/uploaded_images/item_27.jpg');
INSERT INTO Items (ItemName, ItemDescription, Price, ImageId, CreatorId, AuctionId)
	VALUES ('Brown Pinstripe Jacket', 'Lightweight cosplay version of 10th Doctor’s suit.', 90, 27, 10, 14);
    
INSERT INTO Images (FileName, FilePath) VALUES ('item_28.jpg', 'images/uploaded_images/item_28.jpg');
INSERT INTO Items (ItemName, ItemDescription, Price, ImageId, CreatorId, AuctionId)
	VALUES ('Canon A520 Digital Camera', 'Compact 2000s camera, 4MP, works well.', 40, 28, 10, 14);

INSERT INTO Images (FileName, FilePath) VALUES ('item_29.jpg', 'images/uploaded_images/item_29.jpg');
INSERT INTO Items (ItemName, ItemDescription, Price, ImageId, CreatorId, AuctionId)
	VALUES ('Sonic Screwdriver (Replica)', 'Handmade replica of the 10th Doctor\s Sonic Screwdriver.', 100, 29, 10, 14);

INSERT INTO Images (FileName, FilePath) VALUES ('item_30.jpg', 'images/uploaded_images/item_30.jpg');
INSERT INTO Items (ItemName, ItemDescription, Price, ImageId, CreatorId, AuctionId)
	VALUES ('Journal of Impossible Things (Replica)', 'Hardcover leather-look replica of the journal seen in \'Human Nature\'. Includes fan sketches and aged pages.', 65, 30, 10, 14);
    
INSERT INTO Images (FileName, FilePath) VALUES ('item_31.jpg', 'images/uploaded_images/item_31.jpg');
INSERT INTO Items (ItemName, ItemDescription, Price, ImageId, CreatorId, AuctionId)
	VALUES ('Tardis Diary (Replica)', 'Replica of the Doctor\'s and River Song\'s travel diary.', 50, 31, 10, 14);

INSERT INTO Offers (UserId, AuctionId, OfferedPrice, OfferDate) VALUES (2, 14, 395, '2025-05-18 09:05:00');
INSERT INTO Offers (UserId, AuctionId, OfferedPrice, OfferDate) VALUES (9, 14, 425, '2025-05-18 09:24:00');
INSERT INTO Offers (UserId, AuctionId, OfferedPrice, OfferDate) VALUES (13, 14, 455, '2025-05-18 09:37:00');
UPDATE Auctions SET HighestBidId = LAST_INSERT_ID() WHERE Id = 14;

-- Auction 15 - User 11
INSERT INTO Auctions (BasePrice, MinIncrement, HighestBidId, ClosingDate, SellerId, IsSold, BuyerId, FinalPrice)
	VALUES (115, 10, NULL, '2025-08-01 08:00:00', 11, FALSE, NULL, NULL);
    
INSERT INTO Images (FileName, FilePath) VALUES ('item_32.jpg', 'images/uploaded_images/item_32.jpg');
INSERT INTO Items (ItemName, ItemDescription, Price, ImageId, CreatorId, AuctionId)
	VALUES ('Red Bow Tie Set', 'Pack of 3 vintage bow ties, 60s–80s era.', 35, 32, 11, 15);

INSERT INTO Images (FileName, FilePath) VALUES ('item_33.jpg', 'images/uploaded_images/item_33.jpg');
INSERT INTO Items (ItemName, ItemDescription, Price, ImageId, CreatorId, AuctionId)
	VALUES ('Pocket Watch (Quartz)', 'Steampunk style, working battery, ornate design.', 50, 33, 11, 15);

INSERT INTO Images (FileName, FilePath) VALUES ('item_34.jpg', 'images/uploaded_images/item_34.jpg');
INSERT INTO Items (ItemName, ItemDescription, Price, ImageId, CreatorId, AuctionId)
	VALUES (' Fez (Replica)', 'Red felt fez hat, screen-accurate replica inspired by \'The Big Bang\' and \'The Impossible Astronaut\'.', 30, 34, 11, 15);

INSERT INTO Offers (UserId, AuctionId, OfferedPrice, OfferDate) VALUES (2, 15, 115, '2025-05-18 09:06:00');
INSERT INTO Offers (UserId, AuctionId, OfferedPrice, OfferDate) VALUES (14, 15, 125, '2025-05-18 09:26:00');
INSERT INTO Offers (UserId, AuctionId, OfferedPrice, OfferDate) VALUES (3, 15, 135, '2025-05-18 09:42:00');
UPDATE Auctions SET HighestBidId = LAST_INSERT_ID() WHERE Id = 15;

-- Auction 16 - User 12
INSERT INTO Auctions (BasePrice, MinIncrement, HighestBidId, ClosingDate, SellerId, IsSold, BuyerId, FinalPrice)
	VALUES (270, 10, NULL, '2025-08-01 08:00:00', 12, FALSE, NULL, NULL);
    
INSERT INTO Images (FileName, FilePath) VALUES ('item_35.jpg', 'images/uploaded_images/item_35.jpg');
INSERT INTO Items (ItemName, ItemDescription, Price, ImageId, CreatorId, AuctionId)
	VALUES ('Dark Velvet Coat', 'Plum velvet coat with red lining, tailored cosplay.', 160, 35, 12, 16);
    
INSERT INTO Images (FileName, FilePath) VALUES ('item_36.jpg', 'images/uploaded_images/item_36.jpg');
INSERT INTO Items (ItemName, ItemDescription, Price, ImageId, CreatorId, AuctionId)
	VALUES ('Mini Electric Guitar', 'Short-scale black guitar, 80s model, with strap.', 110, 36, 12, 16);

INSERT INTO Offers (UserId, AuctionId, OfferedPrice, OfferDate) VALUES (3, 16, 270, '2025-05-18 09:07:00');
INSERT INTO Offers (UserId, AuctionId, OfferedPrice, OfferDate) VALUES (5, 16, 280, '2025-05-18 09:25:00');
UPDATE Auctions SET HighestBidId = LAST_INSERT_ID() WHERE Id = 16;

-- Auction 17 - User 13
INSERT INTO Auctions (BasePrice, MinIncrement, HighestBidId, ClosingDate, SellerId, IsSold, BuyerId, FinalPrice)
	VALUES (115, 10, NULL, '2025-08-01 08:00:00', 13, FALSE, NULL, NULL);
    
INSERT INTO Images (FileName, FilePath) VALUES ('item_37.jpg', 'images/uploaded_images/item_37.jpg');
INSERT INTO Items (ItemName, ItemDescription, Price, ImageId, CreatorId, AuctionId)
	VALUES ('Rainbow-Stripe Coat', 'Light cosplay coat with stripes, fan-made.', 90, 37, 13, 17);

INSERT INTO Images (FileName, FilePath) VALUES ('item_38.jpg', 'images/uploaded_images/item_38.jpg');
INSERT INTO Items (ItemName, ItemDescription, Price, ImageId, CreatorId, AuctionId)
	VALUES ('Ear Cuff Jewelry', 'Costume jewelry in sci-fi style.', 25, 38, 13, 17);

INSERT INTO Offers (UserId, AuctionId, OfferedPrice, OfferDate) VALUES (6, 17, 115, '2025-05-18 09:08:00');
INSERT INTO Offers (UserId, AuctionId, OfferedPrice, OfferDate) VALUES (1, 17, 125, '2025-05-18 09:27:00');
UPDATE Auctions SET HighestBidId = LAST_INSERT_ID() WHERE Id = 17;

-- Auction 18 - User 14
INSERT INTO Auctions (BasePrice, MinIncrement, HighestBidId, ClosingDate, SellerId, IsSold, BuyerId, FinalPrice)
	VALUES (200, 10, NULL, '2025-08-01 08:00:00', 14, FALSE, NULL, NULL);
    
INSERT INTO Images (FileName, FilePath) VALUES ('item_39.jpg', 'images/uploaded_images/item_39.jpg');
INSERT INTO Items (ItemName, ItemDescription, Price, ImageId, CreatorId, AuctionId)
	VALUES ('Long Coat', '14th Doctor Coat replica', 110, 39, 14, 18);
    
INSERT INTO Images (FileName, FilePath) VALUES ('item_40.jpg', 'images/uploaded_images/item_40.jpg');
INSERT INTO Items (ItemName, ItemDescription, Price, ImageId, CreatorId, AuctionId)
	VALUES ('TARDIS replica', 'TARDIS replica, made in wood, 1:200.', 40, 40, 14, 18);

INSERT INTO Images (FileName, FilePath) VALUES ('item_41.jpg', 'images/uploaded_images/item_41.jpg');
INSERT INTO Items (ItemName, ItemDescription, Price, ImageId, CreatorId, AuctionId)
	VALUES ('Sonic Screwdriver (replica)', 'Replica of 14th\'s Sonic Screwdriver, from the 60th Anniversary Specials', 50, 41, 14, 18);

INSERT INTO Offers (UserId, AuctionId, OfferedPrice, OfferDate) VALUES (6, 18, 200, '2025-05-18 09:10:00');
INSERT INTO Offers (UserId, AuctionId, OfferedPrice, OfferDate) VALUES (12, 18, 210, '2025-05-18 09:29:00');
INSERT INTO Offers (UserId, AuctionId, OfferedPrice, OfferDate) VALUES (1, 18, 220, '2025-05-18 09:46:00');
UPDATE Auctions SET HighestBidId = LAST_INSERT_ID() WHERE Id = 18;

-- Auciton 19 - User 15
INSERT INTO Auctions (BasePrice, MinIncrement, HighestBidId, ClosingDate, SellerId, IsSold, BuyerId, FinalPrice)
	VALUES (240, 10, NULL, '2025-08-01 08:00:00', 15, FALSE, NULL, NULL);
    
INSERT INTO Images (FileName, FilePath) VALUES ('item_42.jpg', 'images/uploaded_images/item_42.jpg');
INSERT INTO Items (ItemName, ItemDescription, Price, ImageId, CreatorId, AuctionId)
	VALUES ('Retro Colorblock Jacket', '2020s streetwear style, cotton blend', 50, 42, 15, 19);
    
INSERT INTO Images (FileName, FilePath) VALUES ('item_43.jpg', 'images/uploaded_images/item_43.jpg');
INSERT INTO Items (ItemName, ItemDescription, Price, ImageId, CreatorId, AuctionId)
	VALUES ('Sonic Screwdriver', 'Replica of 15th\'s Sonic Screwdriver', 90, 43, 15, 19);

INSERT INTO Images (FileName, FilePath) VALUES ('item_44.jpg', 'images/uploaded_images/item_44.jpg');
INSERT INTO Items (ItemName, ItemDescription, Price, ImageId, CreatorId, AuctionId)
	VALUES ('Jukebox', 'Replica of the Jukebox inside 15th\'s TARDIS', 100, 44, 15, 19);

INSERT INTO Offers (UserId, AuctionId, OfferedPrice, OfferDate) VALUES (2, 19, 240, '2025-05-18 09:12:00');
INSERT INTO Offers (UserId, AuctionId, OfferedPrice, OfferDate) VALUES (10, 19, 250, '2025-05-18 09:28:00');
UPDATE Auctions SET HighestBidId = LAST_INSERT_ID() WHERE Id = 19;

-- Auciton 20 - User 16 (Test)
INSERT INTO Auctions (BasePrice, MinIncrement, HighestBidId, ClosingDate, SellerId, IsSold, BuyerId, FinalPrice)
	VALUES (350, 10, NULL, '2025-08-01 08:00:00', 16, FALSE, NULL, NULL);

INSERT INTO Images (FileName, FilePath) VALUES ('item_45.jpg', 'images/uploaded_images/item_45.jpg');
INSERT INTO Items (ItemName, ItemDescription, Price, ImageId, CreatorId, AuctionId)
	VALUES ('Casio Watch', 'Vintage Casio Digital Watch, model A155W Module 593, 1980.', 150, 45, 16, 20);

INSERT INTO Images (FileName, FilePath) VALUES ('item_46.jpg', 'images/uploaded_images/item_46.jpg');
INSERT INTO Items (ItemName, ItemDescription, Price, ImageId, CreatorId, AuctionId)
	VALUES ('Polaroid SX-70', 'Classic folding instant camera with original leather case, 1972 model.', 200, 46, 16, 20);
    
-- Auction 21 - User 16 (Test)
INSERT INTO Auctions (BasePrice, MinIncrement, HighestBidId, ClosingDate, SellerId, IsSold, BuyerId, FinalPrice)
	VALUES (300, 10, NULL, '2025-08-01 08:00:00', 16, FALSE, NULL, NULL);

INSERT INTO Images (FileName, FilePath) VALUES ('item_47.jpg', 'images/uploaded_images/item_47.jpg');
INSERT INTO Items (ItemName, ItemDescription, Price, ImageId, CreatorId, AuctionId)
	VALUES ('Nikon F3', 'Professional 35mm SLR with Nikkor 50mm f/1.4 lens, excellent condition.', 300, 47, 16, 21);
    
-- Auction 22 - User 16 (Test)
INSERT INTO Auctions (BasePrice, MinIncrement, HighestBidId, ClosingDate, SellerId, IsSold, BuyerId, FinalPrice)
	VALUES (280, 10, NULL, '2025-09-01 08:00:00', 16, FALSE, NULL, NULL);
    
INSERT INTO Images (FileName, FilePath) VALUES ('item_48.jpg', 'images/uploaded_images/item_48.jpg');
INSERT INTO Items (ItemName, ItemDescription, Price, ImageId, CreatorId, AuctionId)
	VALUES ('Game Boy Color', 'Fully working Game Boy Color with Pokémon Red cartridge, minor wear.', 100, 48, 16, 22);
    
INSERT INTO Images (FileName, FilePath) VALUES ('item_49.jpg', 'images/uploaded_images/item_49.jpg');
INSERT INTO Items (ItemName, ItemDescription, Price, ImageId, CreatorId, AuctionId) 
	VALUES ('Sony Walkman WM-FX197', 'Cassette player from early 2000s, working radio and headphone jack.', 60, 49, 16, 22);

INSERT INTO Images (FileName, FilePath) VALUES ('item_50.jpg', 'images/uploaded_images/item_50.jpg');
INSERT INTO Items (ItemName, ItemDescription, Price, ImageId, CreatorId, AuctionId)
	VALUES ('Apple iPod Classic (5th Gen, 80GB)', 'Working iPod Classic with original charger and cable, minor scratches.', 120, 50, 16, 22);
    
-- ITEMS NOT IN AUCTIONS

-- User 1
INSERT INTO Images (FileName, FilePath) VALUES ('item_51.jpg', 'images/uploaded_images/item_51.jpg');
INSERT INTO Items (ItemName, ItemDescription, Price, ImageId, CreatorId, AuctionId)
	VALUES ('Vintage Monocle in Case', 'Classic optical monocle with brass rim, early 20th century', 90, 51, 1, NULL);
    
INSERT INTO Images (FileName, FilePath) VALUES ('item_52.jpg', 'images/uploaded_images/item_52.jpg');
INSERT INTO Items (ItemName, ItemDescription, Price, ImageId, CreatorId, AuctionId)
	VALUES ('Eton Collar Shirt', 'Formal white shirt with removable collar, Edwardian style.', 70, 52, 1, NULL);

INSERT INTO Images (FileName, FilePath) VALUES ('item_53.jpg', 'images/uploaded_images/item_53.jpg');
INSERT INTO Items (ItemName, ItemDescription, Price, ImageId, CreatorId, AuctionId)
	VALUES ('Pocket Compass', 'Small working compass in brass casing, worn leather pouch.', 25, 53, 1, NULL);

-- User 2
INSERT INTO Images (FileName, FilePath) VALUES ('item_54.jpg', 'images/uploaded_images/item_54.jpg');
INSERT INTO Items (ItemName, ItemDescription, Price, ImageId, CreatorId, AuctionId)
	VALUES ('Bowler Hat (Vintage)', 'British black bowler hat, 60s era, worn but stylish.', 50, 54, 2, NULL);

INSERT INTO Images (FileName, FilePath) VALUES ('item_55.jpg', 'images/uploaded_images/item_55.jpg');
INSERT INTO Items (ItemName, ItemDescription, Price, ImageId, CreatorId, AuctionId)
	VALUES ('Tele-Snap print set', 'Fan-printed photo stills from lost episodes', 25, 55, 2, NULL);

-- User 3
INSERT INTO Images (FileName, FilePath) VALUES ('item_56.jpg', 'images/uploaded_images/item_56.jpg');
INSERT INTO Items (ItemName, ItemDescription, Price, ImageId, CreatorId, AuctionId)
	VALUES ('Frilled Dress Shirt', '70s frilled shirt, cosplay replica', 65, 56, 3, NULL);
    
INSERT INTO Images (FileName, FilePath) VALUES ('item_57.jpg', 'images/uploaded_images/item_57.jpg');
INSERT INTO Items (ItemName, ItemDescription, Price, ImageId, CreatorId, AuctionId)
	VALUES ('TARDIS Console Panel prop', 'Replica of one panel with faux switches and lights.', 150, 57, 3, NULL);

INSERT INTO Images (FileName, FilePath) VALUES ('item_58.jpg', 'images/uploaded_images/item_58.jpg');
INSERT INTO Items (ItemName, ItemDescription, Price, ImageId, CreatorId, AuctionId)
	VALUES ('UNIT Patch Set', 'Sew-on patches modeled after UNIT uniforms.', 20, 58, 3, NULL);

-- User 4
INSERT INTO Images (FileName, FilePath) VALUES ('item_59.jpg', 'images/uploaded_images/item_59.jpg');
INSERT INTO Items (ItemName, ItemDescription, Price, ImageId, CreatorId, AuctionId)
	VALUES ('Jelly Babies in Collectible Tin', 'Decorative Tin with Jelly Babies replica', 30, 59, 4, NULL);

INSERT INTO Images (FileName, FilePath) VALUES ('item_60.jpg', 'images/uploaded_images/item_60.jpg');
INSERT INTO Items (ItemName, ItemDescription, Price, ImageId, CreatorId, AuctionId)
	VALUES ('K9 Figurine', 'Small metal model of K9, 4th Doctor\'s robot-dog companion', 25, 60, 4, NULL);

INSERT INTO Images (FileName, FilePath) VALUES ('item_61.jpg', 'images/uploaded_images/item_61.jpg');
INSERT INTO Items (ItemName, ItemDescription, Price, ImageId, CreatorId, AuctionId)
	VALUES ('Time Capsule Clock', 'Analog clock, styles after Doctor Who\'s iconic time travel graphics.', 55, 61, 4, NULL);

-- User 5
INSERT INTO Images (FileName, FilePath) VALUES ('item_62.jpg', 'images/uploaded_images/item_62.jpg');
INSERT INTO Items (ItemName, ItemDescription, Price, ImageId, CreatorId, AuctionId)
	VALUES ('Stick of Celery (Resin Replica)', 'Lifelike prop stick of celery, cosplay safe.', 15, 62, 5, NULL);

INSERT INTO Images (FileName, FilePath) VALUES ('item_63.jpg', 'images/uploaded_images/item_63.jpg');
INSERT INTO Items (ItemName, ItemDescription, Price, ImageId, CreatorId, AuctionId)
	VALUES ('Panama Waistcoat (Replica)', 'Light cotton vest with simple trim, fan-made.', 55, 63, 5, NULL);

-- User 6
INSERT INTO Images (FileName, FilePath) VALUES ('item_64.jpg', 'images/uploaded_images/item_64.jpg');
INSERT INTO Items (ItemName, ItemDescription, Price, ImageId, CreatorId, AuctionId)
	VALUES ('Checkered Vest', 'Patchwork vest with bold color blocks', 60, 64, 6, NULL);

INSERT INTO Images (FileName, FilePath) VALUES ('item_65.jpg', 'images/uploaded_images/item_65.jpg');
INSERT INTO Items (ItemName, ItemDescription, Price, ImageId, CreatorId, AuctionId)
	VALUES ('Colorful Umbrella', 'Umbrella with multicolor segments, curved handle.', 35, 65, 6, NULL);

INSERT INTO Images (FileName, FilePath) VALUES ('item_66.jpg', 'images/uploaded_images/item_66.jpg');
INSERT INTO Items (ItemName, ItemDescription, Price, ImageId, CreatorId, AuctionId)
	VALUES ('Sixth Doctor Action Figure', '5-inch collectible by Character Options.', 25, 66, 6, NULL);

-- User 7
INSERT INTO Images (FileName, FilePath) VALUES ('item_67.jpg', 'images/uploaded_images/item_67.jpg');
INSERT INTO Items (ItemName, ItemDescription, Price, ImageId, CreatorId, AuctionId)
	VALUES ('Question Mark Umbrella', 'Prop umbrella with red question mark handle.', 60, 67, 7, NULL);

INSERT INTO Images (FileName, FilePath) VALUES ('item_68.jpg', 'images/uploaded_images/item_68.jpg');
INSERT INTO Items (ItemName, ItemDescription, Price, ImageId, CreatorId, AuctionId)
	VALUES ('Plaid Scarf', 'Muted-color plaid scarf, screen-accurate style.', 25, 68, 7, NULL);

-- User 8
INSERT INTO Images (FileName, FilePath) VALUES ('item_69.jpg', 'images/uploaded_images/item_69.jpg');
INSERT INTO Items (ItemName, ItemDescription, Price, ImageId, CreatorId, AuctionId)
	VALUES ('Old Pocket Flask', 'Metal hip flask with initials engraving.', 40, 69, 8, NULL);

INSERT INTO Images (FileName, FilePath) VALUES ('item_70.jpg', 'images/uploaded_images/item_70.jpg');
INSERT INTO Items (ItemName, ItemDescription, Price, ImageId, CreatorId, AuctionId)
	VALUES ('Steampunk Goggles', 'Leather strap, brass frame cosplay goggles.', 45, 70, 8, NULL);

-- User 9
INSERT INTO Images (FileName, FilePath) VALUES ('item_71.jpg', 'images/uploaded_images/item_71.jpg');
INSERT INTO Items (ItemName, ItemDescription, Price, ImageId, CreatorId, AuctionId)
	VALUES ('TARDIS Keychain', 'Keychain with TARDIS pendant.', 15, 71, 9, NULL);

-- User 10
INSERT INTO Images (FileName, FilePath) VALUES ('item_72.jpg', 'images/uploaded_images/item_72.jpg');
INSERT INTO Items (ItemName, ItemDescription, Price, ImageId, CreatorId, AuctionId)
	VALUES ('3D Glasses (Paper replica)', 'Blue and Red lens paper glasses, fan replica', 10, 72, 10, NULL);

INSERT INTO Images (FileName, FilePath) VALUES ('item_73.jpg', 'images/uploaded_images/item_73.jpg');
INSERT INTO Items (ItemName, ItemDescription, Price, ImageId, CreatorId, AuctionId)
	VALUES ('Psychic Paper Card Holder', 'Psychic Paper Replica with Card Holder', 35, 73, 10, NULL);

-- User 11
INSERT INTO Images (FileName, FilePath) VALUES ('item_74.jpg', 'images/uploaded_images/item_74.jpg');
INSERT INTO Items (ItemName, ItemDescription, Price, ImageId, CreatorId, AuctionId)
	VALUES ('Red Suspenders', 'Red elastic suspenders, metal clips.', 15, 74, 11, NULL);

INSERT INTO Images (FileName, FilePath) VALUES ('item_75.jpg', 'images/uploaded_images/item_75.jpg');
INSERT INTO Items (ItemName, ItemDescription, Price, ImageId, CreatorId, AuctionId)
	VALUES ('Stetson', 'Brown Stetson replica, for cosplay use.', 25, 75, 11, NULL);

INSERT INTO Images (FileName, FilePath) VALUES ('item_76.jpg', 'images/uploaded_images/item_76.jpg');
INSERT INTO Items (ItemName, ItemDescription, Price, ImageId, CreatorId, AuctionId)
	VALUES ('Series 6 Blu-Ray Box Set', 'Doctor Who Series 6 in HD, Part 1', 50, 76, 11, NULL);

-- User 12
INSERT INTO Images (FileName, FilePath) VALUES ('item_77.jpg', 'images/uploaded_images/item_77.jpg');
INSERT INTO Items (ItemName, ItemDescription, Price, ImageId, CreatorId, AuctionId)
	VALUES ('Sonic Sunglasses (Replica)', 'Black Ray-Ban-style sunglasses, replica', 40, 77, 12, NULL);

INSERT INTO Images (FileName, FilePath) VALUES ('item_78.jpg', 'images/uploaded_images/item_78.jpg');
INSERT INTO Items (ItemName, ItemDescription, Price, ImageId, CreatorId, AuctionId)
	VALUES ('DW Poster', '12th Doctor\'s Poster', 30, 78, 12, NULL);

-- User 13
INSERT INTO Images (FileName, FilePath) VALUES ('item_79.jpg', 'images/uploaded_images/item_79.jpg');
INSERT INTO Items (ItemName, ItemDescription, Price, ImageId, CreatorId, AuctionId)
	VALUES ('Rainbox T-Shirt', 'Black T-Shirt with rainbow, size M, unisex', 25, 79, 13, NULL);

INSERT INTO Images (FileName, FilePath) VALUES ('item_80.jpg', 'images/uploaded_images/item_80.jpg');
INSERT INTO Items (ItemName, ItemDescription, Price, ImageId, CreatorId, AuctionId)
	VALUES ('13th Doctor LEGO Minifigure', 'Custom LEGO Mini-figure of 13th Doctor.', 65, 80, 13, NULL);

-- User 14
INSERT INTO Images (FileName, FilePath) VALUES ('item_81.jpg', 'images/uploaded_images/item_81.jpg');
INSERT INTO Items (ItemName, ItemDescription, Price, ImageId, CreatorId, AuctionId)
	VALUES ('14th Doctor Poster', 'Official Poster for the 60th Anniversary Specials, featuring David Tennant\'s return as The Doctor.', 20, 81, 14, NULL);

-- User 15
INSERT INTO Images (FileName, FilePath) VALUES ('item_82.jpg', 'images/uploaded_images/item_82.jpg');
INSERT INTO Items (ItemName, ItemDescription, Price, ImageId, CreatorId, AuctionId)
	VALUES ('DW Beanie', 'Doctor Who branded black beanie.', 20, 82, 15, NULL);

-- User 16 (test)
INSERT INTO Images (FileName, FilePath) VALUES ('item_83.jpg', 'images/uploaded_images/item_83.jpg');
INSERT INTO Items (ItemName, ItemDescription, Price, ImageId, CreatorId, AuctionId)
	VALUES ('iPhone 3G', 'Used iPhone 3G, functioning but without original box and accessories.', 300, 83, 16, NULL);

INSERT INTO Images (FileName, FilePath) VALUES ('item_84.jpg', 'images/uploaded_images/item_84.jpg');
INSERT INTO Items (ItemName, ItemDescription, Price, ImageId, CreatorId, AuctionId)
	VALUES ('iMac G3', 'Original iMac G3, late 1999 model, fully working, mint condition.', 420, 84, 16, NULL);
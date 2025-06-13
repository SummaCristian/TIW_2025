// ==========================
// Helper Functions
// ==========================


// ==========================
// Setup Event Listeners
// ==========================


// ==========================
// Form Validation Functions
// ==========================



// ==========================
// Init
// ==========================


// Initializes the dynamic components to some preset values, for debugging purposes (TEMPORARY)
const title = document.getElementById("selectedAuctionTitle")
const price = document.getElementById("selectedAuctionPrice")
const buyerUsername = document.getElementById("selectedAuctionBuyerUsername")
const buyerAddress = document.getElementById("selectedAuctionBuyerAddress")
const basePrice = document.getElementById("selectedAuctionBasePrice")
const minIncrement = document.getElementById("selectedAuctionMinIncrement")
const sellerUsername = document.getElementById("selectedAuctionSellerUsername")
const remainingTime = document.getElementById("selectedAuctionRemainingTime")
const closingDate = document.getElementById("selectedAuctionClosingDate")
const offerMinIncrement = document.getElementById("selectedAuction-OfferMinIncrement")

title.textContent = "Auction #1"
price.textContent = "500€"
buyerUsername.textContent = "Name Surname"
buyerAddress.textContent = "Address St 4, City, Region"
basePrice.textContent = "100"
minIncrement.textContent = "10"
sellerUsername.textContent = "you"
remainingTime.textContent = "1d, 2h, 35min"
closingDate.textContent = "01/01/2026"
offerMinIncrement.textContent = "10"
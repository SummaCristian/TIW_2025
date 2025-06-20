/**
 * This file contains JS functions able to 
 * build the UI elements for dynamically fetched portions of the UI.
 * These functions can be called from other JS files, supposedly the ones
 * handling the requests to fetch the data.
 */

import { showAuctionPopup, user } from "./home.js";

const SVG_NS = "http://www.w3.org/2000/svg";

// ==========================
// Top Level Components
// ==========================

// Returns the Card component for an Auction's data, to show in a List
export function buildAuctionCard(auction, noItemsText) {
	// The top level container	
	const listItem = document.createElement("li");

	const innerContainer = document.createElement("div");
	
	// Header
	innerContainer.append(buildAuctionCardHeader(auction));

	// Items section header
	innerContainer.append(buildItemsSectionHeader(auction, true));

	// Items inside the Auction
	innerContainer.append(buildCompactItemsListForAuction(auction, noItemsText));

	listItem.append(innerContainer);
	
	// Return the whole Component
	return listItem;
}

// ==========================
// Specific sub-components
// ==========================

// Returns a component of class "auction-header"
function buildAuctionHeader() {
	const header = document.createElement("div");
	header.className = "auction-header";
	
	return header;
}

// Returns a <div> element containing some <p> elements with an Auction's Header data
function buildAuctionCardHeader(auction) {
	// Outer container
	const header = buildAuctionHeader();
	
	// Inner Left container
	const containerLeft = document.createElement("div");
	
	// Title
	const title = document.createElement("p");
	title.className = "auction-detail";
	title.textContent = `Auction #${auction.id ?? 0}`;
	containerLeft.append(title);

	// Base Price
	const basePrice = document.createElement("p");
	basePrice.className = "item-detail-secondary";
	basePrice.textContent = `Base price: ${auction.basePrice ?? 0}€`;
	containerLeft.append(basePrice);
	
	// Final Price OR Current Highest Bid
	const finalPrice = document.createElement("p");
	finalPrice.className = "auction-detail auction-detail-secondary";
	finalPrice.textContent = auction.isSold === true 
		? "Final Price:" 
		: "Current Highest Bid:";
	containerLeft.append(finalPrice);
	
	const pricePill = buildOfferPill(auction?.highestBid?.offeredPrice ?? null);
	containerLeft.append(pricePill);
	
	// Seller info
	const seller = document.createElement("p");
	seller.className = "auction-detail auction-detail-secondary";
	seller.textContent = `Sold by ${auction?.sellerUsername ?? "Unknown"}`;
	containerLeft.append(seller);
	
	
	// Put everything into the outer container
	header.append(containerLeft);



	// Inner Right container
	const containerRight = document.createElement("div");

	// Indicator for the Auction's Open or Closed status
	containerRight.append(buildOpenClosedPillIndicator(!auction?.isSold ?? true));

	// Remaining Time
	containerRight.append(buildRemainingTimeIndicator(auction?.remainingTime ?? "Unknown"));

	header.append(containerRight);
	
	// Return the whole component
	return header;
	
}

// Returns an header component to put on top of the List of Items
function buildItemsSectionHeader(auction, showButton) {
	// The container
	const container = document.createElement("div");
	container.className = "auction-header";

	// Section title
	const titleContainer = document.createElement("div");
	titleContainer.className = "title-with-image";

	const icon = createSvg();
	icon.append(createPath("M2.97 12.92A2 2 0 0 0 2 14.63v3.24a2 2 0 0 0 .97 1.71l3 1.8a2 2 0 0 0 2.06 0L12 19v-5.5l-5-3-4.03 2.42Z"));
	icon.append(createPath("m7 16.5-4.74-2.85"));
	icon.append(createPath("m7 16.5 5-3"));
	icon.append(createPath("M7 16.5v5.17"));
	icon.append(createPath("M12 13.5V19l3.97 2.38a2 2 0 0 0 2.06 0l3-1.8a2 2 0 0 0 .97-1.71v-3.24a2 2 0 0 0-.97-1.71L17 10.5l-5 3Z"));
	icon.append(createPath("m17 16.5-5-3"));
	icon.append(createPath("m17 16.5 4.74-2.85"));
	icon.append(createPath("M17 16.5v5.17"));
	icon.append(createPath("M7.97 4.42A2 2 0 0 0 7 6.13v4.37l5 3 5-3V6.13a2 2 0 0 0-.97-1.71l-3-1.8a2 2 0 0 0-2.06 0l-3 1.8Z"));
	icon.append(createPath("M12 8 7.26 5.15"));
	icon.append(createPath("m12 8 4.74-2.85"));
	icon.append(createPath("M12 13.5V8"));
	titleContainer.append(icon);

	const textGroup = document.createElement("div");
	textGroup.className = "text-group";
	
	const title = document.createElement("h2");
	title.className = "subsection-title";
	title.textContent = "Items";
	textGroup.append(title);

	const subtitle = document.createElement("p");
	subtitle.className = "header-description";
	subtitle.textContent = "Products included in this auction";
	textGroup.append(subtitle);

	titleContainer.append(textGroup);
	
	container.append(titleContainer);

	// Button to open details for the Auctions
	if (showButton === true) {
		const detailsButton = document.createElement("button");
		detailsButton.className = "button-primary";

		const btnText = document.createElement("p");
		btnText.textContent = "See more";
		detailsButton.append(btnText);

		const btnIcon = createSvg();
		btnIcon.append(createPath("m9 19 6-6-6-6"));
		detailsButton.append(btnIcon);
		
		// Adds the callback function to handle the click
		detailsButton.addEventListener("click", () => {
			// Shows the popup with the Auction's details
			showAuctionPopup(auction);
		});

		container.append(detailsButton);
	}	

	// Return the whole component
	return container;
}

// Returns a component dedicated to displaying the list of Items inside an Auction.
// Items are displayed in their compact card style.
function buildCompactItemsListForAuction(auction, defaultTextMessage) {
	// Initialize the container to null, will be filled based on the content
	var container = null;

	// Items
	if (auction?.items.length > 0) {
		// At least 1 Item inside the Auction

		const itemList = document.createElement("ul");
		itemList.className = "item-list";

		// Iterate over all the Items inside the Auction
		for (const item of auction?.items ?? []) {
			itemList.append(buildCompactItemCard(item));
		}

		// Assign this component to the container
		container = itemList;

	} else {
		// No Items in the Auction -> Default Text
		const defaultText = document.createElement("p");
		defaultText.className = "no-element-in-list";
		defaultText.textContent = defaultTextMessage ?? "No auctions available";
		
		// Assign this component to the container
		container = defaultText;
	}

	// Return the whole component
	return container;
}

// Returns a card component dedicated to display an Item's data in Compact form.
function buildCompactItemCard(item) {
	const container = document.createElement("li");
	container.className = "item-container";

	// Image
	const textGroup = document.createElement("div");
	textGroup.className = "text-group";

	const image = document.createElement("img");
	image.className = "item-image";
	image.src = item?.image?.filePath ?? "/resources/placeholder.svg";
	image.alt= item?.image?.filePath != null
		? "Item Image"
		: "No Image Available";
	textGroup.append(image);
	
	// Item Name
	const itemName = document.createElement("p");
	itemName.className = "item-detail";
	itemName.textContent = `${item?.itemName ?? "Unknown name"}€`;
	textGroup.append(itemName);

	// Item Price
	const itemPrice = document.createElement("p");
	itemPrice.className = "item-detail-secondary";
	itemPrice.textContent = item?.price ?? "Unknown price";
	textGroup.append(itemPrice);

	// Item ID
	const itemId = document.createElement("p");
	itemId.className = "item-detail-tertiary";
	itemId.textContent = `Item #${item?.id ?? 0}`;
	textGroup.append(itemId);

	container.append(textGroup);

	// Return the whole component
	return container;
}


// Returns a card component dedicated to display an Item's data in Expanded form.
function buildExpandedItemCard(item) {
	const container = document.createElement("li");
	container.className = "item-container-large";

	// Image
	const textGroup = document.createElement("div");
	textGroup.className = "text-group";

	const image = document.createElement("img");
	image.className = "item-image-large";
	image.src = item?.image?.filePath ?? "/resources/placeholder.svg";
	image.alt= item?.image?.filePath != null
		? "Item Image"
		: "No Image Available";
	textGroup.append(image);

	// Item Name
	const itemName = document.createElement("p");
	itemName.className = "item-detail";
	itemName.textContent = item?.itemName ?? "Unknown name";
	textGroup.append(itemName);

	// Item Description
	const itemDescription = document.createElement("p");
	itemDescription.className = "item-detail-tertiary";
	itemDescription.textContent = item?.itemDescription ?? "Unknown description";
	textGroup.append(itemDescription);

	// Item Price
	const itemPrice = document.createElement("p");
	itemPrice.className = "item-detail-secondary";
	itemPrice.textContent = item?.price ?? "Unknown price";
	textGroup.append(itemPrice);

	// Item ID
	const itemId = document.createElement("p");
	itemId.className = "item-detail-tertiary";
	itemId.textContent = `Item #${item?.id ?? 0}`;
	textGroup.append(itemId);

	container.append(textGroup);

	// Return the whole component
	return container;
}

// Returns a card component dedicated to display an Item's data in the Checkbox form
export function buildCheckBoxItemCard(item) {
	const container = document.createElement("li");

	// The actual input method to allow selection
	const input = document.createElement("input");
	input.type = "checkbox";
	input.name = "itemIds";
	input.value = item?.id ?? -1;
	input.id = `item-${item?.id ?? -1}`;
	input.className = "item-checkbox";
	input.hidden = true;
	container.append(input);

	// The visible portion of the input selector
	const label = document.createElement("label");
	label.setAttribute("for", `item-${item?.id ?? -1}`);
	label.className = "item-container";

	// The checkmark icon to indicate if it's selected or not
	const innerContainer = document.createElement("div");
	innerContainer.className = "checkmark-overlay";

	const checkmark = createSvg();
	checkmark.setAttribute("class", "checkmark-icon");
	checkmark.append(createPolyline("20 6 9 17 4 12"));
	innerContainer.append(checkmark);

	label.append(innerContainer);

	// Item card
	const textGroup = document.createElement("div");
	textGroup.className = "text-group";

	// Image
	const image = document.createElement("img");
	image.className = "item-image";
	image.src = item?.image?.filePath ?? "/resources/placeholder.svg";
	image.alt= item?.image?.filePath != null
		? "Item Image"
		: "No Image Available";
	textGroup.append(image);

	// Data
	const name = document.createElement("p");
	name.className = "item-detail";
	name.textContent = item?.itemName ?? "Unknown item";
	textGroup.append(name);

	const price = document.createElement("p");
	price.className = "item-detail-secondary";
	price.textContent = `${item?.price ?? "Unknown"}€`;
	textGroup.append(price);

	const id = document.createElement("p");
	id.className = "item-detail-tertiary";
	id.textContent = `Item #${item?.id ?? -1}`;
	textGroup.append(id);

	label.append(textGroup);

	container.append(label);


	// Return the whole component
	return container;
}

// Returns a <div> element displaying an integer price into a pill-shaped component.
// If the data passed is null or invalid, the pill will be red and say "No offers yet..."
// Otherwise, it will be green and gold colored, with the value inside.
function buildOfferPill(value) {
	const container = document.createElement("div");

	if (typeof value === "number" && !isNaN(value)) {
		// Value is a number and it's valid, continue
		container.className = "offer-pill";

		const icon = createSvg();
		icon.append(createCircle(12, 12, 10));
		icon.append(createPath("M16 8h-6a2 2 0 1 0 0 4h4a2 2 0 1 1 0 4H8"));
		icon.append(createPath("M12 18V6"));
		container.append(icon);

		const valueTxt = document.createElement("p");
		valueTxt.textContent = `${value}€`;
		container.append(valueTxt);

	} else {
		// Invalid value or null, return fallback UI variant instead
		container.className = "offer-pill offer-pill-no-offers";

		const icon = createSvg();
		icon.append(createCircle(12, 12, 10));
		icon.append(createPath("M17 8h-6a2 2 0 1 0 0 4h4a2 2 0 1 1 0 4H8"));
		icon.append(createPath("M12 18V6"));
		container.append(icon);

		const valueTxt = document.createElement("p");
		valueTxt.textContent = "No offers yet...";
		container.append(valueTxt);
	}

	// Return the whole component
	return container;
}

// Returns the Open/Closed status component
function buildOpenClosedPillIndicator(isOpen) {
	const container = document.createElement("div");
	container.className = "closed-status-container";

	const innerContainer = document.createElement("div");
	
	innerContainer.className = isOpen === true
		? "closed-status closed-status-open"
		: "closed-status closed-status-closed";
	
	if (isOpen === true) {
		// Open
		const icon = createSvg();
		icon.append(createRect(18, 11, 3, 11, 2, 2));
		icon.append(createPath("M7 11V7a5 5 0 0 1 9.9-1"));
		innerContainer.append(icon);

		const text = document.createElement("p");
		text.textContent = "Open";
		innerContainer.append(text);
	} else {
		// Closed
		const icon = createSvg();
		icon.append(createRect(18, 11, 3, 11, 2, 2));
		icon.append(createPath("M7 11V7a5 5 0 0 1 9.9-1"));
		innerContainer.append(icon);

		const text = document.createElement("p");
		text.textContent = "Closed";
		innerContainer.append(text);
	}

	container.append(innerContainer);

	// Return the whole component
	return container;
}

// Returns the Auction's Remaining Time Pill indicator
function buildRemainingTimeIndicator(value) {
	// Container
	const container = document.createElement("div");
	container.className = "remaining-time-container";

	// Section title
	const title = document.createElement("p");
	title.className = "auction-detail auction-detail-secondary";
	title.textContent = "Remaining time:"
	container.append(title);

	// Pill container
	const pill = document.createElement("div");
	pill.className = "remaining-time-pill";
	
	// Icon
	const icon = createSvg();
	icon.append(createPath("M5 22h14"));
	icon.append(createPath("M5 2h14"));
	icon.append(createPath("M17 22v-4.172a2 2 0 0 0-.586-1.414L12 12l-4.414 4.414A2 2 0 0 0 7 17.828V22"));
	icon.append(createPath("M7 2v4.172a2 2 0 0 0 .586 1.414L12 12l4.414-4.414A2 2 0 0 0 17 6.172V2"));
	pill.append(icon);

	// Text
	const text = document.createElement("p");
	text.textContent = value;
	pill.append(text);

	container.append(pill);

	// Return the whole component
	return container;
}

/*

*/
function buildOfferBubble(offer) {
	const container = document.createElement("li");
	container.className = offer.userId === user.id 
		? "offer-bubble offer-bubble-own"
		: "offer-bubble";

	const innerContainer = document.createElement("div");

	const detailsContainer = document.createElement("div");
	detailsContainer.className = offer.userId === user.id
		? "offer-details-container offer-bubble-own"
		: "offer-details-container";
	
	// User Icon
	const icon = createSvg();
	icon.append(createCircle(12, 12, 10));
	icon.append(createCircle(12, 10, 3));
	icon.append(createPath("M7 20.662V19a2 2 0 0 1 2-2h6a2 2 0 0 1 2 2v1.662"));
	detailsContainer.append(icon);

	// User data
	const userDataContainer = document.createElement("div");
	userDataContainer.className = "offer-details-text-container";

	// Username
	const username = document.createElement("p");
	username.className = "offer-username";
	username.textContent = offer?.username ?? "Unknown User";
	userDataContainer.append(username);

	// Offer ID + DateTime
	const offerId = offer?.id ?? -1;
	const offerDate = formatDateTimeShort(offer?.offerDate ?? "");
	
	const offerIdDate = document.createElement("p");
	offerIdDate.className = "offer-details";
	offerIdDate.textContent = `#${offerId} • ${offerDate}`;
	userDataContainer.append(offerIdDate);

	detailsContainer.append(userDataContainer);
	innerContainer.append(detailsContainer);

	// Offer content
	const contentContainer = document.createElement("div");
	contentContainer.className = offer.userId === user.id
		? "offer-content offer-content-own"
		: "offer-content";

	const pill = document.createElement("div");
	pill.className = offer.userId === user.id
		? "offer-pill offer-pill-own"
		: "offer-pill";
	
	const pillIcon = createSvg();
	pillIcon.append(createCircle(12, 12, 10));
	pillIcon.append(createPath("M16 8h-6a2 2 0 1 0 0 4h4a2 2 0 1 1 0 4H8"));
	pillIcon.append(createPath("M12 18V6"));
	pill.append(pillIcon);

	const pillValue = document.createElement("p");
	pillValue.textContent = `${offer?.offeredPrice ?? "Unknown"}€`;
	pill.append(pillValue);

	contentContainer.append(pill);

	innerContainer.append(contentContainer);

	container.append(innerContainer);

	// Return the whole component
	return container;
}


// =================================================
// Utility functions for even smaller components
// =================================================

// Returns an initialized SVG container, ready to receive the content to draw inside.
// CSS and ViewBox are already initialized here.
function createSvg() {
	const svg = document.createElementNS(SVG_NS, "svg");
	svg.setAttribute("class", "icon");
	svg.setAttribute("viewBox", "0 0 24 24");

	return svg;
}

// Returns an initialized circle element for SVGs with the data passed as parameters
function createCircle(centerX, centerY, radius) {
	const circle = document.createElementNS(SVG_NS, "circle");
	circle.setAttribute("cx", centerX);
	circle.setAttribute("cy", centerY);
	circle.setAttribute("r", radius);

	return circle;
}

// Returns an initialized path for SVGs with the string passed as parameter
function createPath(d) {
	const path = document.createElementNS(SVG_NS, "path");
	path.setAttribute("d", d);

	return path;
}

// Returns an initialized rect for SVGs with the data passed as parameters
function createRect(width, height, x, y, rx, ry) {
	const rect = document.createElementNS(SVG_NS, "rect");
	rect.setAttribute("width", width);
	rect.setAttribute("height", height);
	rect.setAttribute("x", x);
	rect.setAttribute("y", y);
	rect.setAttribute("rx", rx);
	rect.setAttribute("ry", ry);

	return rect;
}

// Returns an initialized polyline for SVGs with the data passed as parameter
function createPolyline(points) {
	const polyline = document.createElementNS(SVG_NS, "polyline");
	polyline.setAttribute("points", points);

	return polyline;
}

// =================
// Logic functions
// =================

/*
	Updates all the data inside the Auction Details Popup with the 
	data from the Auction passed as parameter.
	Then it calls the callback function to signal its completion.
*/
export function updateAuctionPopup(auction, callback) {
	// Grab all the fields that need to be updated
	const title = document.getElementById("selectedAuctionTitle");
	const finalPriceLabel = document.getElementById("selectedAuctionFinalPrice");
	const highestBidLabel = document.getElementById("selectedAuctionHighestBid");
	const pricePill = document.getElementById("selectedAuctionPricePill");
	const price = document.getElementById("selectedAuctionPrice");
	const noOffersPill = document.getElementById("selectedAuctionNoOffersPill");
	const buyerContainer = document.getElementById("selectedAuctionBuyerInfoBox");
	const buyerUsername = document.getElementById("selectedAuctionBuyerUsername");
	const buyerAddress = document.getElementById("selectedAuctionBuyerAddress");
	const basePrice = document.getElementById("selectedAuctionBasePrice");
	const minIncrement = document.getElementById("selectedAuctionMinIncrement");
	const sellerUsername = document.getElementById("selectedAuctionSellerUsername");
	const closedStatus = document.getElementById("selectedAuctionClosedPill");
	const openStatus = document.getElementById("selectedAuctionOpenPill");
	const remainingTimePill = document.getElementById("selectedAuctionRemainingTimePill");
	const remainingTime = document.getElementById("selectedAuctionRemainingTime");
	const openUntilLabel = document.getElementById("selectedAuctionClosingDateTitle-Open");
	const closedOnLabel = document.getElementById("selectedAuctionClosingDateTitle-Closed");
	const closingDate = document.getElementById("selectedAuctionClosingDate");
	const itemList = document.getElementById("selectedAuctionItemList");
	const noItemMessage = document.getElementById("selectedAuctionNoItemsMessage");
	const closeAuctionButton = document.getElementById("selectedAuctionCloseButton");
	const offersScrollview = document.getElementById("selectedAuctionOfferScrollview");
	const offerList = document.getElementById("selectedAuctionOfferList");
	const noOfferMessage = document.getElementById("selectedAuctionOfferEmptyContainer");
	const offerOverlay = document.getElementById("selectedAuctionOfferOverlay");
	const offerOverlayMinIncrement = document.getElementById("selectedAuction-OfferMinIncrement");
	const makeOfferForm = document.getElementById("selectedAuction-MakeOfferForm");

	// Update all the texts

	// Title
	title.textContent = `Auction #${auction?.id ?? -1}`;

	// Final Price or Highest Bid
	if (auction?.isSold === true) {
		// Final Price
		finalPriceLabel.style.display = "block";
		highestBidLabel.style.display = "none";
	} else {
		// Current Highest Bid
		finalPriceLabel.style.display = "none";
		highestBidLabel.style.display = "block";
	}
	
	if (typeof auction?.highestBid?.offeredPrice === "number" && !isNaN(auction?.highestBid?.offeredPrice)) {
		// Show the offered price
		pricePill.style.display = "flex";
		price.textContent = `${auction?.highestBid?.offeredPrice}€`;
		noOffersPill.style.display = "none";
	} else {
		// Show the "No offer" pill
		pricePill.style.display = "none";
		noOffersPill.style.display = "flex";
	}

	// Buyer
	if (auction?.isSold === true && typeof auction?.buyerUsername === "string") {
		// Show the Buyer's data
		buyerContainer.style.display = "block";
		buyerUsername.textContent = auction?.buyerUsername ?? "Unknown User";
		buyerAddress.textContent = auction?.buyerAddress ?? "Unknown address";
	} else {
		// Hides the Buyer's data
		buyerContainer.style.display = "none";
	}

	// Base Price
	basePrice.textContent = auction?.basePrice ?? "Unknown";

	// Minimum Increment
	minIncrement.textContent = auction?.minIncrement ?? "Unknown";

	// Seller
	sellerUsername.textContent = auction?.sellerUsername ?? "Unknown user";

	// Open/Closed status
	if (auction?.isSold === true) {
		// Closed
		openStatus.style.display = "none";
		closedStatus.style.display = "flex";
	} else {
		// Open
		openStatus.style.display = "flex";
		closedStatus.style.display = "none";
	}

	// Remaining Time
	if (typeof auction?.remainingTime === "string" && auction?.remainingTime != "Closed") {
		// Not closed yet
		remainingTimePill.style.display = "flex";
		remainingTime.textContent = auction?.remainingTime ?? "Unknown";
		
	} else {
		// Closed, don't show it
		remainingTimePill.style.display = "none";
	}

	// Closing Date
	if (auction?.isSold === true) {
		// Closed -> Closed on X
		openUntilLabel.style.display = "none";
		closedOnLabel.style.display = "block";
	} else {
		// Open -> Open until X
		openUntilLabel.style.display = "block";
		closedOnLabel.style.display = "none";
	}
	
	closingDate.textContent = formatDateTimeLong(auction?.closingDate) ?? "Unknown";
		
	// Add the Items in their section
	const items = auction?.items ?? [];
	
	if (items.length > 0) {
		// Empties the Items that could already be there
		itemList.innerHTML = "";
		
		// There are Items
		for (const item of items) {
			// Builds a card component for each Item
			itemList.append(buildExpandedItemCard(item));
		}
		itemList.style.display = "grid";

		// Hides the "No items" text
		noItemMessage.style.display = "none";

	} else {
		// No Items
		itemList.style.display = "none";
		noItemMessage.style.display = "block";
	}
	
	// Empties the Offers that could be there
	offerList.innerHTML = "";

	// Add the Offers in their section
	if (auction?.offers.length > 0) {
		// There are offers
		// Shows the Offers scrollview
		offersScrollview.style.display = "block";
		// Hides the default message
		noOfferMessage.style.display = "none";
		// Adds the new Offers
		for (const offer of auction.offers) {
			offerList.append(buildOfferBubble(offer));
		}
	} else {
		// No offers
		offersScrollview.style.display = "none;"
		noOfferMessage.style.display = "block";
	}

	// Checks if it needs to display the "Make Offer" form
	if (auction?.sellerId === user.id ?? true) {
		// User is the owner -> Can't make offers to its own Auction
		offerOverlay.style.display = "none";
	} else {
		// Not the owner -> Can make offers
		offerOverlay.style.display = "block";
	}

	offerOverlayMinIncrement.textContent = auction?.minIncrement ?? "Unknown";
	
	
	// Calls the callback function
	callback();
}

// Returns the date passed in a String formatted as dd mmm yyyy - HH:mm
function formatDateTimeLong(isoString) {
  if (!isoString) return "";

  const date = new Date(isoString);

  const day = date.getDate().toString().padStart(2, "0");
  const month = date.toLocaleString("en-US", { month: "short" }).toLowerCase(); // e.g. "jan"
  const year = date.getFullYear();

  const hours = date.getHours().toString().padStart(2, "0");
  const minutes = date.getMinutes().toString().padStart(2, "0");

  return `${day} ${month} ${year} - ${hours}:${minutes}`;
}

// Returns the date passed in a String formatted as dd MMM HH:mm
function formatDateTimeShort(isoString) {
  if (!isoString) return "";

  const date = new Date(isoString);

  const day = date.getDate().toString().padStart(2, "0");
  const month = date.toLocaleString("en-US", { month: "short" }); // e.g. "Aug"
  const hours = date.getHours().toString().padStart(2, "0");
  const minutes = date.getMinutes().toString().padStart(2, "0");

  return `${day} ${month} ${hours}:${minutes}`;
}

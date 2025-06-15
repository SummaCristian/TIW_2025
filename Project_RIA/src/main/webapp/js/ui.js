/**
 * This file contains JS functions able to 
 * build the UI elements for dynamically fetched portions of the UI.
 * These functions can be called from other JS files, supposedly the ones
 * handling the requests to fetch the data.
 */

const SVG_NS = "http://www.w3.org/2000/svg";

// ==========================
// Top Level Components
// ==========================

// Returns the Card component for an Auction's data, to show in a List
export function buildAuctionCard(auction, noItemsText) {
	// The top level container
	const container = document.createElement("div");
	
	// Header
	container.append(buildAuctionCardHeader(auction));

	// Items inside the Auction
	container.append(buildCompactItemsListForAuction(auction, noItemsText));
	
	// Return the whole Component
	return container;
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
	title.textContent = `Auction ${auction.id ?? 0}`;
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
	seller.textContent = `Sold by ${auction?.seller?.username ?? "Unknown"}`;
	containerLeft.append(seller);
	
	
	// Put everything into the outer container
	header.append(containerLeft);



	// Inner Right container
	const containerRight = document.createElement("div");

	// Indicator for the Auction's Open or Closed status
	containerRight.append(buildOpenClosedPillIndicator(auction?.isSold ?? false));

	// Remaining Time
	containerRight.append(buildRemainingTimeIndicator(auction?.remainingTime ?? "Unknown"));
	
	// Return the whole component
	return header;
	
}

// Returns a component dedicated to displaying the list of Items inside an Auction.
// Items are displayed in their compact card style.
function buildCompactItemsListForAuction(auction, defaultTextMessage) {
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
	subtitle.textContent = "Products included in this closed auction";
	textGroup.append(subtitle);

	titleContainer.append(textGroup);

	// Button to open details for the Auctions
	const detailsButton = document.createElement("button");
	detailsButton.className = "button-primary";

	const btnText = document.createElement("p");
	btnText.textContent = "See more";
	detailsButton.append(btnText);

	const btnIcon = createSvg();
	btnIcon.append(createPath("m9 19 6-6-6-6"));
	detailsButton.append(btnIcon);

	titleContainer.append(detailsButton);

	container.append(titleContainer);

	// Items
	if (auction?.items.size > 0) {
		// At least 1 Item inside the Auction

		const itemList = document.createElement("ul");
		itemList.className = "item-list";

		// Iterate over all the Items inside the Auction
		for (const item of auction?.items ?? []) {
			itemList.append(buildCompactItemCard(item));
		}

		container.append(itemList);

	} else {
		// No Items in the Auction -> Default Text
		const defaultText = document.createElement("p");
		defaultText.className = "no-element-in-list";
		defaultText.textContent = defaultTextMessage ?? "No auctions available";
		container.append(defaultText);
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
	itemName.textContent = item?.itemName ?? "Unknown name";
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
	
	container.className = isOpen === true
		? "closed-status closed-status-open"
		: "closed-status closed-status-closed";
	
	if (isOpen === true) {
		// Open
		const icon = createSvg();
		icon.append(createRect(18, 11, 3, 11, 2, 2));
		icon.append(createPath("M7 11V7a5 5 0 0 1 0.0-1"));
		container.append(icon);

		const text = document.createElement("p");
		text.textContent = "Open";
		container.append(text);
	} else {
		// Closed
		const icon = createSvg();
		icon.append(createRect(18, 11, 3, 11, 2, 2));
		icon.append(createPath("M7 11V7a5 5 0 0 1 9.9-1"));
		container.append(icon);

		const text = document.createElement("p");
		text.textContent = "Closed";
		container.append(text);
	}

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



// =================================================
// Utility functions for even smaller components
// =================================================

// Returns an initialized SVG container, ready to receive the content to draw inside.
// CSS and ViewBox are already initialized here.
function createSvg() {
	const svg = document.createElementNS(SVG_NS, "svg");
	svg.className = "icon";
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
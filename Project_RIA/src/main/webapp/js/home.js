// Imports the other JS files needed
import { setCookie, getCookie, deleteCookie } from './cookies.js';

import {
  fetchUser,
  refreshOpenAuctions,
  refreshClosedAuctions,
  refreshWonAuctions,
  refreshVisitedAuctions,
  searchAuctions,
  fetchAuction,
  refreshAvailableItems,
  createAuction,
  createItem,
  makeOffer,
  closeAuction
} 
  from './api.js';
  
import { updateAuctionPopup } from './ui.js';

// Constants
export let user = null;

let makeOfferEventListener = null;
let closeAuctionEventListener = null;

let successTimeout;
let errorTimeout;

export let FIRST_TIME_ACCESS_KEY = "isFirstTimeAccess";
export let WAS_LAST_ACTION_AUCTION_CREATION_KEY = "wasLastAuctionAuctionCreation";
export let VISITED_AUCTIONS_KEY = "visitedAuctions";

// ==========================
// Helper Functions
// ==========================

/*
	Adds an Auction's ID to the Cookie saving the recently visited ones.
*/
function addAuctionToVisited(id) {
    const list = getCookie(VISITED_AUCTIONS_KEY);
    let ids = [];

    if (list) {
        // Copies the existing values into an array
        ids = list.split(",").map(n => parseInt(n)).filter(n => !isNaN(n));
    }

    // Remove the ID if already in the list
    ids = ids.filter(n => n !== id);

    // Add the ID to the start (most recent)
    ids.unshift(id);

    // Keeps the length to 15 Auctions max.
    if (ids.length > 15) ids = ids.slice(0, 15);

    // Save back into the cookie
    setCookie(VISITED_AUCTIONS_KEY, ids.join(","), 30);
}


// Displays a success message in the appropriate popup
function displaySuccess(statusCode, message) {
  // Clears the previpus timeout (if any), in case this success is overwriting another popup message
  clearTimeout(successTimeout);

  const popup = document.getElementById("successMessagePopup");
  const codeTxt = document.getElementById("successStatusCode");
  const messageTxt = document.getElementById("successMessage");

  // Reset any previous animation classes
  popup.classList.remove("fade-out-up");
  void popup.offsetWidth;
  popup.classList.add("fade-in-down");


  // Sets the Status Code
  codeTxt.textContent = getStatusText(statusCode);
  // Sets the Message
  messageTxt.textContent = message;

  // Shows the popup
  popup.classList.remove("popup-message");

  // Sets a timer so that the popup is hidden after 3 seconds
  // After 3s, fade out
  setTimeout(() => {
    popup.classList.remove("fade-in-down");
    popup.classList.add("fade-out-up");

    // Optional: hide after animation completes (0.4s)
    setTimeout(() => {
      popup.classList.add("popup-message");
    }, 400);
  }, 3000);
}

// Displays an error message in the appropriate popup
// Displays a success message in the appropriate popup
function displayError(statusCode, message) {
  // Clears the previpus timeout (if any), in case this success is overwriting another popup message
  clearTimeout(errorTimeout);

  const popup = document.getElementById("errorMessagePopup");
  const codeTxt = document.getElementById("errorStatusCode");
  const messageTxt = document.getElementById("errorMessage");

  // Reset any previous animation classes
  popup.classList.remove("fade-out-up");
  void popup.offsetWidth;
  popup.classList.add("fade-in-down");


  // Sets the Status Code
  codeTxt.textContent = getStatusText(statusCode);
  // Sets the Message
  messageTxt.textContent = message;

  // Shows the popup
  popup.classList.remove("popup-message");

  // Sets a timer so that the popup is hidden after 3 seconds
  // After 3s, fade out
  setTimeout(() => {
    popup.classList.remove("fade-in-down");
    popup.classList.add("fade-out-up");

    // Optional: hide after animation completes (0.4s)
    setTimeout(() => {
      popup.classList.add("popup-message");
    }, 400);
  }, 3000);
}

// Hides all popups
function hideAllPopups() {
	document.getElementById("successMessagePopup").style.display = "none";
	document.getElementById("errorMessagePopup").style.display = "none";
}

// Converts a Status Code into a legible String representation
function getStatusText(code) {
  switch (code) {
    case 200: return "Success";
    case 201: return "Created";
    case 400: return "Bad Request";
    case 401: return "Unauthorized";
    case 403: return "Forbidden";
    case 404: return "Not Found";
    case 409: return "Conflict";
    case 500: return "Internal Server Error";
    default: return "Unknown Error";
  }
}


// Sets the date of Tomorrow, same time as now, into this Picker in the Auction Creation Form (SellPage)
function setTomorrowDateInPicker() {
    const datePicker = document.getElementById("closingDate");

    if (!datePicker) return; // safety check

    const today = new Date();

    // Add 1 day
    today.setDate(today.getDate() + 1);

    // Format to YYYY-MM-DD
    const year = today.getFullYear();
    const month = String(today.getMonth() + 1).padStart(2, '0');
    const day = String(today.getDate()).padStart(2, '0');

    const formattedDate = `${year}-${month}-${day}`;

    // Set the value
    datePicker.value = formattedDate;
}

/*
	Triggers the showing of the Auction Details Popup, that is filled with all the
	info from the Auction passed as parameter and then shown on top of the current UI.
*/
export function showAuctionPopup(auction) {
	const popupOverlay = document.getElementById("popup-overlay");

  // Fetches the updated Auction data
  fetchAuction(auction.id, (error, newAuction) => {
	if (!auction?.isSold) {
		// Saves the Auction's ID into the Cookie (only if it's still Open)
		addAuctionToVisited(auction.id);
	}
	
		
    // Once the auction data has been fetched, we call the UI function to put the data in the UI
    updateAuctionPopup(newAuction, () => {
		// Sets the Form inside the Popup to the correct Auction
		initMakeOfferForm(newAuction);
		
        // Once the UI has been updated, show the Popup to the User
        popupOverlay.style.display = "flex";
		
		document.body.style.overflow = "hidden";    // Disable scroll
    });
  });
}



// ==========================
// Setup Event Listeners
// ==========================

// Auction Creation Form
function initAuctionCreationForm() {
  const form = document.getElementById("auctionCreationForm");
  const errorMessage = document.getElementById("auctionCreationFormErrorMessage");


  form.addEventListener("submit", (event) => {
    event.preventDefault();

    // Checks if the data is valid
    const error = validateAuctionCreation(form);
    
    if (error == null) {
      // No error, the request can be sent
      errorMessage.textContent = "";
      
      createAuction(form, (error, statusCode) => {
          if (error) {
            displayError(statusCode, error.message);
          } else {
            displaySuccess(statusCode, "Auction created successfully!");

            // Refreshes the Open Auctions List
            refreshOpenAuctions();
            // Refreshes the Available Items in the Auction Creation Form
            refreshAvailableItems();
			
			// Resets the Form
			form.reset();
			
			// Updates the Cookie
			setCookie(WAS_LAST_ACTION_AUCTION_CREATION_KEY, "true", 30); // Resets the expiry too
          }
      });

    } else {
      // Error, display it in the UI
      errorMessage.textContent = error;
    }
  });
}

// Item Creation Form
function initItemCreationForm() {
  const form = document.getElementById("itemCreationForm");
  const errorMessage = document.getElementById("itemCreationFormErrorMessage");

  form.addEventListener("submit", (event) => {
      event.preventDefault();

      // Checks if the data is valid
      const error = validateItemCreation(form);

      if (error == null) {
          // No error, the request can be sent
          errorMessage.textContent = "";

          createItem(form, (error, statusCode) => {
              if (error) {
                displayError(statusCode, error.message);
              } else {
                displaySuccess(statusCode, "Item created successfully!");

                // Refreshes the Available Items in the Auction Creation Form
                refreshAvailableItems();
				
                // Resets the Form
                form.reset();
              }
          });


      } else {
          // Error, display it in the UI
          errorMessage.textContent = error;
      }
  });
}

// Close Auction Button
function initCloseAuctionButton(auction) {
  const button = document.getElementById("selectedAuctionCloseButton");

  if (closeAuctionEventListener != null) {
    button.removeEventListener("submit", closeAuctionEventListener);
  }

  closeAuctionEventListener = function(event) {
    event.preventDefault();

    // Validate the data
    const error = validateCloseAuction(auction);

    if (error === null) {
      // No error, the request can be sent
      closeAuction(auction.id, (error, statusCode) => {
          if (error) {
            displayError(statusCode, error.message);
          } else {
            displaySuccess(statusCode, "Auction closed successfully!");
          }

          fetchAuction(auction.id, (error, updatedAuction) => {
            if (error) {
              displayError(500, "Failed to refresh auction.");
              return;
            }

            updateAuctionPopup(updatedAuction, () => {
              initCloseAuctionButton(updatedAuction);
            });
          });
      });
    } else {
      displayError(403, error);
    }
  }
}

// Make Offer Form
function initMakeOfferForm(auction) {
  const form = document.getElementById("selectedAuction-MakeOfferForm");
  const errorMessage = document.getElementById("selectedAuction-OfferError");

  if (makeOfferEventListener != null) {
  	form.removeEventListener("submit", makeOfferEventListener);
  }
  
  makeOfferEventListener = function(event)   {
    event.preventDefault();

    // Validate the data
    const error = validateOffer(form, auction);
	
    if (error == null) {
      // No error, extract the offer value
      const offer = parseInt(form.offer.value.trim(), 10);

      // Send the request to make the offer
      makeOffer(auction.id, offer, (error, statusCode) => {
        if (error) {
          displayError(statusCode, error.message);
        } else {
          displaySuccess(statusCode, "Offer sent successfully!");
		  
        // Refresh the auction data
        fetchAuction(auction.id, (error, updatedAuction) => {
						
            if (error) {
              displayError(500, "Failed to refresh auction.");
              return;
            }

            updateAuctionPopup(updatedAuction, () => {
              form.reset(); // Reset the form only after successful update
              errorMessage.textContent = ""; // Clear any previous errors
            });
			
			      initMakeOfferForm(updatedAuction);
          });
        }
      });
    } else {
      // Validation failed
      errorMessage.textContent = error;
    }
  }
  
  form.addEventListener("submit", makeOfferEventListener);
}

// The Selected Auction popup window
document.addEventListener("DOMContentLoaded", () => {
  const popupOverlay = document.getElementById("popup-overlay");
  const popupClose = document.getElementById("popup-close");

  function openPopup() {
    popupOverlay.style.display = "flex";
  }

  function closePopup() {
    popupOverlay.style.display = "none";
	
	document.body.style.overflow = "";          // Restore scroll
	
	// Update the Visited Auctions list
	refreshVisitedAuctions();
  }

  popupClose.addEventListener("click", closePopup);

  // Sets an EventListener on the outside layer too, so that clicking outside popup-content closes it
  popupOverlay.addEventListener("click", (event) => {
    if (event.target === popupOverlay) {
      closePopup();
    }
  });

  // Initially hide the popup (in case it's shown by default)
  closePopup();

  // Search Field in Buy Page
  const searchForm = document.querySelector(".search-form-compact");
  
  searchForm.addEventListener("submit", (event) => {
    event.preventDefault();
	
	// Shows the Search Result section
	const container = document.getElementById("searchResultsContainer");
	container.style.display = "block";

    const queryInput = searchForm.querySelector("#query");
    const query = queryInput.value.trim();

    if (query.length > 0) {
      // Not empty, performs the Search
      searchAuctions(query);
    }
  });
  
  // Initially hides the search results section
  document.getElementById("searchResultsContainer").style.display = "none";

});

// TabBar
// Initializes the tab switching behavior
export function initPillTabBar(tabSelector = ".pill-tab", contentSelector = ".tab-content", indicatorSelector = ".pill-indicator") {
  const tabs = document.querySelectorAll(tabSelector);
  const contents = document.querySelectorAll(contentSelector);
  const indicator = document.querySelector(indicatorSelector);

  function activateTab(tab) {
    // Activate selected tab
    tabs.forEach(t => t.classList.remove("active"));
    tab.classList.add("active");

    // Move and resize indicator
    indicator.style.width = `${tab.offsetWidth}px`;
    indicator.style.transform = `translateX(${tab.offsetLeft}px)`;

    // Show related content
    const target = tab.getAttribute("data-tab");
    contents.forEach(content => {
    	content.classList.toggle("active-tab", content.id === target);
    });

    // Refresh data in the selected tab
    switch (target) {
      case "sellPage":
        refreshOpenAuctions();
        refreshClosedAuctions();
		refreshAvailableItems();
        break;
      case "buyPage":
        refreshWonAuctions();
        refreshVisitedAuctions();
		break;
    }
  }

  // Setup click listeners
  tabs.forEach(tab => {
    tab.addEventListener("click", () => activateTab(tab));
  });

  // Init on first tab
  
  // Checks if it's the first time the User accesses the website
  const isFirstAccess = getCookie(FIRST_TIME_ACCESS_KEY) === null;

  // Write the cookie
  setCookie(FIRST_TIME_ACCESS_KEY, "false", 30);
  
  if (isFirstAccess) {
    // First time
    // Show Sell Page
	activateTab(tabs[0]);
  } else {
    // Not first time
    
	const didCreateAnAuction = getCookie(WAS_LAST_ACTION_AUCTION_CREATION_KEY) === "true";
	
	if (didCreateAnAuction) {
		// Show Sell Page
		activateTab(tabs[0]);
		
		// Reset the value, this one gets consumed
		deleteCookie(WAS_LAST_ACTION_AUCTION_CREATION_KEY);
	} else {
		// Show Buy Page
		activateTab(tabs[1]);
	}
  }
}

// Welcome Text
function initWelcomeText() {
	document.getElementById("welcomeText").textContent = `Welcome, ${user.username}!`;
}



// ==========================
// Form Validation Functions
// ==========================

// Validates the data inserted into the Item Creation Form.
// Returns `null` if everything is valid, otherwise returns a string error message.
function validateAuctionCreation(form) {
  const minIncrement = form.minIncrement.value.trim();
  const closingDate = form.closingDate.value.trim();
  const itemCheckboxes = form.querySelectorAll('input[name="itemIds"]:checked');

  const sellerId = user.id;

  // Missing parameters
  if (!minIncrement || !closingDate || !sellerId) {
    return "Missing required data. Please fill all fields.";
  }

  if (itemCheckboxes.length === 0) {
    return "You must select at least one item for the auction.";
  }

  // Number parsing
  const incrementValue = parseInt(minIncrement, 10);
  if (isNaN(incrementValue)) {
    return "Minimum increment must be a valid number.";
  }

  if (incrementValue <= 0) {
    return "Minimum increment must be greater than 0.";
  }

  // Date parsing
  const dateValue = new Date(closingDate);
  const now = new Date();

  if (isNaN(dateValue.getTime())) {
    return "Invalid date format.";
  }

  if (dateValue <= now) {
    return "Closing date must be in the future.";
  }

  // Item checkbox validation
  for (const checkbox of itemCheckboxes) {
    if (!checkbox.value || checkbox.value.trim() === "") {
      return "One or more selected items are invalid.";
    }
  }

  if (itemCheckboxes.length == 0) {
    return "No items selected. You need at least 1 to create an Auction!"; 
  }

  // Everything is valid -> Return null as error
  return null;
}

// Validates the data inserted into the Item Creation Form.
// Returns `null` if everything is valid, otherwise returns a string error message.
function validateItemCreation(form) {
    const name = form.itemName?.value.trim();
    const description = form.itemDescription?.value.trim();
    const price = form.price?.value.trim();
    const image = form.image?.files[0];

    // Check all fields are present
    if (!name || !description || !price || !image) {		
        return "All fields are required.";
    }

    // Price should be a positive integer
    const priceInt = parseInt(price, 10);
    if (isNaN(priceInt) || priceInt <= 0) {
        return "Price must be a positive number.";
    }

    // Optional: check image extension
    const allowedExtensions = ["jpg", "jpeg", "png", "gif", "webp"];
    const extension = image.name.split('.').pop().toLowerCase();
    if (!allowedExtensions.includes(extension)) {
        return "Image must be a JPG, PNG, GIF, or WEBP file.";
    }

    // Optional: check image size (e.g., max 5MB)
    const maxSize = 5 * 1024 * 1024;
    if (image.size > maxSize) {
        return "Image file is too large (max 5MB).";
    }

    // Passed all checks
    return null;
}

// Validates the data inserted into the Make Offer Form.
// Returns 'null' if everything is valid, otherwise returns a string error message.
function validateOffer(form, auction) {
  const offerInput = form.offer;
  const offerValue = offerInput.value.trim();

  // Missing offer
  if (!offerValue) {
    return "Missing offer amount. Please enter a value.";
  }

  // Parse the number
  const offer = parseInt(offerValue, 10);
  if (isNaN(offer)) {
    return "Offer must be a valid number.";
  }

  if (offer <= 0) {
    return "You can't offer 0€ or less.";
  }

  if (auction.isSold || new Date(auction.closingDate) <= new Date()) {
    return "This Auction is closed and can't accept any more Offers.";
  }

  if (offer < auction.basePrice) {
    return "You can't offer less than this Auction's base price.";
  }

  if (
    auction.highestBid &&
    offer < auction.highestBid.offeredPrice + auction.minIncrement
  ) {
    return "The Offer is too low for this Auction. Please try again...";
  }

  // All validations passed
  return null;
}

// Validates the possibility of closing an Auction.
// Returns 'null' if everything is valid, otherwise returns a string error message.
function validateCloseAuction(auction) {
  const userId = user.id;

  // Ownership
  if (auction?.sellerId != userId) {
    return "You can't close other people's Auctions!";
  }

  // Closing Date already passed
  if (new Date(auction.closingDate) > new Date()) {
    return "This Auction can't be closed before its Closing Date.";
  }

  if (auction?.isSold) {
    return "This Auction is already closed.";
  }

  // All validations passed
  return null;
}



// ==========================
// Init
// ==========================
fetchUser((data) => {
  user = data;
  
  initWelcomeText();
  initPillTabBar()

  
});

setTomorrowDateInPicker()

// Refreshes the content on load
refreshOpenAuctions();
refreshClosedAuctions();
refreshWonAuctions();
refreshAvailableItems();
refreshVisitedAuctions();

// Initializes the Forms
initAuctionCreationForm();
initItemCreationForm();
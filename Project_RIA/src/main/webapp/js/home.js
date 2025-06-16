// Imports the other JS files needed
import { fetchUser, refreshOpenAuctions, refreshClosedAuctions, refreshWonAuctions, searchAuctions, fetchAuction } from './api.js';
import { updateAuctionPopup } from './ui.js';

// Constants
export let user = null;

// ==========================
// Helper Functions
// ==========================

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
  fetchAuction(auction.id, (error, auction) => {
    // Once the auction data has been fetched, we call the UI function to put the data in the UI
    updateAuctionPopup(auction, () => {
        // Once the UI has been updated, show the Popup to the User
        popupOverlay.style.display = "flex";
    });
  });
}



// ==========================
// Setup Event Listeners
// ==========================

// The Selected Auction popup window
document.addEventListener("DOMContentLoaded", () => {
  const popupOverlay = document.getElementById("popup-overlay");
  const popupClose = document.getElementById("popup-close");

  function openPopup() {
    popupOverlay.style.display = "flex";
  }

  function closePopup() {
    popupOverlay.style.display = "none";
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
        break;
      case "buyPage":
        refreshWonAuctions();
		break;
    }
  }

  // Setup click listeners
  tabs.forEach(tab => {
    tab.addEventListener("click", () => activateTab(tab));
  });

  // Init on first tab
  // TODO: Remember the last one the User visited
  if (tabs.length > 0) {
    activateTab(tabs[0]);
  }
}



// ==========================
// Form Validation Functions
// ==========================



// ==========================
// Init
// ==========================
fetchUser((data) => {
  user = data;
  
  console.log(user);
});

initPillTabBar()
setTomorrowDateInPicker()

// Refreshes the content on load
refreshOpenAuctions();
refreshClosedAuctions();
refreshWonAuctions();
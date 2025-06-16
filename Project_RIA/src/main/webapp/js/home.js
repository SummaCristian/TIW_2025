// Imports the other JS files needed
import { refreshOpenAuctions, refreshClosedAuctions, refreshWonAuctions } from './api.js';

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
initPillTabBar()
setTomorrowDateInPicker()

// Refreshes the content on load
refreshOpenAuctions();
refreshClosedAuctions();
refreshWonAuctions();
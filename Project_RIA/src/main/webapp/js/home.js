// ==========================
// Helper Functions
// ==========================


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
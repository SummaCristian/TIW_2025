/**
 * This file contains all the functions dedicated to fetching the internal APIs 
 * and rendering the receiving data into the page.
 */

import { buildAuctionCard, buildCheckBoxItemCard } from './ui.js';
import { user, VISITED_AUCTIONS_KEY } from './home.js';
import { getCookie, setCookie } from './cookies.js';

// =====================
// Main Function
// =====================

// Creates and sends an XMLHttpRequest to the Server at the URL passed as parameter,
// and attaches the parameter function as the callback to that request.
// Does so using GET.
function fetchDataGET(url, callback) {
    // Creates the request
    const xhr = new XMLHttpRequest();

    // Sets the request to the GET method and async
    xhr.open('GET', url, true);
    // Sets the allowed response format to JSON
    xhr.setRequestHeader('Accept', 'application/json');

    // Attaches the internal callback function
    xhr.onreadystatechange = function () {
        // Checks the status
        if (xhr.readyState === XMLHttpRequest.DONE) {
            // Completed, checks the result
            if (xhr.status >= 200 && xhr.status < 300) {
                // Successful, tries to read the data
                try {
                    // Exctracts a JSON file from the body of the response
                    const response = JSON.parse(xhr.responseText);
                    // Executes the callback passed as parameter on the JSON obtaines
                    callback(null, response);
                } catch (err) {
                    // Error in parsing the JSON
					console.log(err);
					
                    callback(new Error('Failed to parse response as JSON'));
                }
            } else if (xhr.status == 401) {
				// Unauthorized, redirect to LoginPage
				window.location.href = 'login';
			} else {
                // The Request has failed
                callback(new Error(`Request failed with status ${xhr.status}`));
            }
        }
    };

    // Sends the request
    xhr.send();
}

// Creates and sends an XMLHttpRequest to the Server at the URL passed as parameter,
// and attaches the parameter function as the callback to that request.
// Does so using POST.function fetchDataPOST(url, data, callback) 
function fetchDataPOST(url, data, callback) {
    const xhr = new XMLHttpRequest();

    // Open the request
    xhr.open('POST', url, true);

    // Check if data is FormData
    const isFormData = data instanceof FormData;

    // Set headers
    if (!isFormData) {
        xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    }
    xhr.setRequestHeader('Accept', 'application/json');

    // Set up response handler
    xhr.onreadystatechange = function () {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status >= 200 && xhr.status < 300) {
                try {
                    const response = JSON.parse(xhr.responseText);
                    callback(null, response);
                } catch (err) {
                    callback(new Error("Failed to parse JSON response"));
                }
            } else if (xhr.status == 401) {
				// Unauthorized, redirect to LoginPage
				window.location.href = 'login';
			} else {
                const errorMsg = xhr.responseText || `Request failed with status ${xhr.status}`;
                callback(new Error(errorMsg), null);
            }
        }
    };

    // Send the request
    if (isFormData) {
        xhr.send(data);
    } else {
        const body = new URLSearchParams();
        for (const key in data) {
            const value = data[key];
            if (Array.isArray(value)) {
                for (const v of value) {
                    body.append(key, v);
                }
            } else {
                body.append(key, value);
            }
        }
        xhr.send(body.toString());
    }
}


// Creates and sends an XMLHttpRequest to the Server at the URL passed as parameter,
// and attaches the parameter function as the callback to that request.
// Does so using POST.
// Handles both normal POST and multipart/form-data.
function doActionPOST(url, data, callback) {
    const xhr = new XMLHttpRequest();

    // Configure the request
    xhr.open('POST', url, true);

    // Only set JSON headers if data is a plain object (not FormData)
    const isFormData = data instanceof FormData;
    if (!isFormData) {
        xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
        xhr.setRequestHeader('Accept', '*/*'); // No JSON expected
    }

    // Response handler
    xhr.onreadystatechange = function () {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            // Successful response
            if (xhr.status >= 200 && xhr.status < 300) {
                callback(null, xhr.status);
            } else if (xhr.status == 401) {
				// Unauthorized, redirect to LoginPage
				window.location.href = 'login';
			} else {
                // Return error with message from body if any
                const errorMessage = xhr.responseText || `Request failed with status ${xhr.status}`;
                callback(new Error(errorMessage), xhr.status);
            }
        }
    };

    // Send body
    if (isFormData) {
        xhr.send(data);
    } else {
        // Encode the body (support arrays like itemIds[])
        const body = new URLSearchParams();
        for (const key in data) {
            const value = data[key];
            if (Array.isArray(value)) {
                for (const v of value) {
                    body.append(key, v);
                }
            } else {
                body.append(key, value);
            }
        }

        // Send the request
        xhr.send(body.toString());
    }
}


// =================
// FETCH FUNCTIONS
// =================

/*
    Fetches the User data from the Server's session passes it to the
    callback function passed as argument.
*/
export function fetchUser(callback) {
    var user = null;

    // Defines the API URL
    const url = "/Project_TIW_RIA/api/users/user";

    const internalCallback = function(error, data) {
        // Check if there was an error
        if (error) {
            console.log("Failed to fetch the User: ", error.message);
            callback(null);
            return;
        }
        
        // Run the callback function
        callback(data);
    }

    // Call the fetchDataGET with the internal callback
    fetchDataGET(url, internalCallback);
}

/*
    Makes a request to the server for the Auctions whose ID is inside the cookie,
    then proceeds to dynamically build the UI elements to display those Auctions and
    add them to the DOM in the appropriate list.
*/
export function refreshVisitedAuctions() {
    const auctionsList = getCookie(VISITED_AUCTIONS_KEY);

    if (auctionsList) {
        // Found the list

        // Convert string "12,34,56" → [12, 34, 56]
        const auctionIds = auctionsList
            .split(",")
            .map(id => parseInt(id))
            .filter(id => !isNaN(id));

         // Created the data payload
        const data = { auctionIds: auctionIds };
        
        // Defines the URL
        const url = "/Project_TIW_RIA/api/auctions/visited";

        const callback = function(error, data) {
            const list = document.getElementById("visitedAuctionsList");
            const emptyListMessage = document.getElementById("noVisitedAuctionsText");

            const noItemsText ="No items in this auction";

            // Clear the previous content before repopulating or refreshing
            list.innerHTML = "";

            // Checks if there was a error
            if (error) {
               // Logs
                console.error("Failed to fetch visited auctions:", error.message);
                // Hides the List
                list.style.display = "none";
                // Shows the default text
                emptyListMessage.style.display = "block";
                // Changes the text in the default text to the error message
                emptyListMessage.textContent = "Could not load visited auctions.";
            return;
            }

            if (Array.isArray(data) && data.length === 0) {
                // No Auctions received

                // Hides the List
                list.style.display = "none";
                // Shows the default text
                emptyListMessage.textContent = "You haven't seen any Auction yet...";
                emptyListMessage.style.display = "block";
            } else {
                // Auctions received

                // Hides the default text
                emptyListMessage.style.display = "none"
				
				let wasListChanged = false;

                // Populates the List
                for (const auction of data) {
					// Checks if the Auction is still open or not
					if (auction.isSold) {
					    // Remove closed auction from cookie
					    const index = auctionIds.indexOf(auction.id);
					    if (index !== -1) {
					        auctionIds.splice(index, 1);
					    }
						
						wasListChanged = true;
						
					} else {
						// Still open, renders the UI
                    	list.append(buildAuctionCard(auction, noItemsText));
					}
					
					list.style.display = "block";

                }
				
				if (wasListChanged) {
					setCookie(VISITED_AUCTIONS_KEY, auctionIds.join(","), 30);
				}
            }
            
        };

        // Call the fetchDataPost function
        fetchDataPOST(url, data, callback);

    } else {
        // No visited Auctions
        document.getElementById("noVisitedAuctionsText").textContent = "You haven't seen any Auction yet...";
    }
}

/*
    Makes a request to the server for the updated Open Auctions data,
    then proceeds to dynamically build the UI elements to display those Auctions and
    adds them to the DOM in the appropriate list.
*/
export function refreshOpenAuctions() {
    // Defines the API URL
    const url = "/Project_TIW_RIA/api/auctions/open";
    
    // Defines the callback function to handle the results
    const callback = function (error, data) {
        const container = document.getElementById("openAuctionsContainer");
        const list = document.getElementById("openAuctionsList");
        const emptyListMessage = document.getElementById("noOpenAuctionsText");

        const noItemsText ="No items in this auction";

        // Clear the previous content before repopulating or refreshing
        list.innerHTML = "";

        // Checks if there is an error before looking at the data
        if (error) {
            // Logs
            console.error("Failed to fetch open auctions:", error.message);
            // Hides the List
            list.style.display = "none";
            // Shows the default text
            emptyListMessage.style.display = "block";
            // Changes the text in the default text to the error message
            emptyListMessage.textContent = "Could not load auctions.";
            return;
        }


        if (Array.isArray(data) && data.length === 0) {
            // No Auctions received

            // Hides the List
            list.style.display = "none";
            // Shows the default text
            emptyListMessage.textContent = "No open auctions available";
            emptyListMessage.style.display = "block";
        } else {
            // Auctions received

            // Hides the default text
            emptyListMessage.style.display = "none"

            // Populates the List
            for (const auction of data) {
                list.append(buildAuctionCard(auction, noItemsText));
            }
			
			list.style.display = "block";
        }
    }

    // Makes the Request
    fetchDataGET(url, callback);
}

/*
    Makes a request to the server for the updated Closed Auctions data,
    then proceeds to dynamically build the UI elements to display those Auctions and
    adds them to the DOM in the appropriate list.
*/
export function refreshClosedAuctions() {
    // Defines the API URL
    const url = "/Project_TIW_RIA/api/auctions/closed";
    
    // Defines the callback function to handle the results
    const callback = function (error, data) {
        const container = document.getElementById("closedAuctionsContainer");
        const list = document.getElementById("closedAuctionsList");
        const emptyListMessage = document.getElementById("noClosedAuctionsText");

        const noItemsText ="No items in this auction";

        // Clear the previous content before repopulating or refreshing
        list.innerHTML = "";

        // Checks if there is an error before looking at the data
        if (error) {
            // Logs
            console.error("Failed to fetch closed auctions:", error.message);
            // Hides the List
            list.style.display = "none";
            // Shows the default text
            emptyListMessage.style.display = "block";
            // Changes the text in the default text to the error message
            emptyListMessage.textContent = "Could not load auctions.";
            return;
        }


        if (Array.isArray(data) && data.length === 0) {
            // No Auctions received

            // Hides the List
            list.style.display = "none";
            // Shows the default text
            emptyListMessage.textContent = "No closed auctions available";
            emptyListMessage.style.display = "block";
        } else {
            // Auctions received

            // Hides the default text
            emptyListMessage.style.display = "none"

            // Populates the List
            for (const auction of data) {
                list.append(buildAuctionCard(auction, noItemsText));
            }
			
			list.style.display = "block";
        }
    }

    // Makes the Request
    fetchDataGET(url, callback);
}

/*
    Makes a request to the server for the updated Won Auctions data,
    then proceeds to dynamically build the UI elements to display those Auctions and
    adds them to the DOM in the appropriate list.
*/
export function refreshWonAuctions() {
    // Defines the API URL
    const url = "/Project_TIW_RIA/api/auctions/won";
    
    // Defines the callback function to handle the results
    const callback = function (error, data) {
        const container = document.getElementById("wonAuctionsContainer");
        const list = document.getElementById("wonAuctionsList");
        const emptyListMessage = document.getElementById("noWonAuctionsText");

        const noItemsText ="No items in this auction";

        // Clear the previous content before repopulating or refreshing
        list.innerHTML = "";

        // Checks if there is an error before looking at the data
        if (error) {
            // Logs
            console.error("Failed to fetch won auctions:", error.message);
            // Hides the List
            list.style.display = "none";
            // Shows the default text
            emptyListMessage.style.display = "block";
            // Changes the text in the default text to the error message
            emptyListMessage.textContent = "Could not load auctions.";
            return;
        }


        if (Array.isArray(data) && data.length === 0) {
            // No Auctions received

            // Hides the List
            list.style.display = "none";
            // Shows the default text
            emptyListMessage.textContent = "No won auctions available";
            emptyListMessage.style.display = "block";
        } else {
            // Auctions received

            // Hides the default text
            emptyListMessage.style.display = "none"

            // Populates the List
            for (const auction of data) {
                list.append(buildAuctionCard(auction, noItemsText));
            }
			
			list.style.display = "block";
        }
    }

    // Makes the Request
    fetchDataGET(url, callback);
}

/*
    Makes a request to the server for results of a Search query
    then proceeds to dynamically build the UI elements to display those results Auctions and
    adds them to the DOM in BuyPage's Search Results list
*/
export function searchAuctions(query) {
    // Defines the API URL
    const encodedQuery = encodeURIComponent(query);
    const url = `/Project_TIW_RIA/api/auctions/search?query=${encodedQuery}`;

    // Defines the callback function to handle the results
    const callback = function (error, data) {
        const container = document.getElementById("searchResultsContainer");
        const list = document.getElementById("searchResultsList");
        const emptyListMessage = document.getElementById("searchResultsNoAuctionText");

        const noItemsText ="No items in this auction";

        // Clear the previous content before repopulating or refreshing
        list.innerHTML = "";

        // Checks if there is an error before looking at the data
        if (error) {
            // Logs
            console.error("Failed to fetch search results:", error.message);
            // Hides the List
            list.style.display = "none";
            // Shows the default text
            emptyListMessage.style.display = "block";
            // Changes the text in the default text to the error message
            emptyListMessage.textContent = "Could not load auctions.";
            return;
        }


        if (Array.isArray(data) && data.length === 0) {
            // No Auctions received

            // Hides the List
            list.style.display = "none";
            // Shows the default text
            emptyListMessage.textContent = "No results";
            emptyListMessage.style.display = "block";
        } else {
            // Auctions received

            // Hides the default text
            emptyListMessage.style.display = "none"
			
			// Shows the List
			list.style.display = "block";

            // Populates the List
            for (const auction of data) {
                list.append(buildAuctionCard(auction, noItemsText));
            }
        }
    }

    // Makes the Request
    fetchDataGET(url, callback);
}

// Fetches an Auction's data and calls the callback function to handle it
export function fetchAuction(auctionId, callback) {
	// Defines the API URL
	if (typeof auctionId === "number" && !isNaN(auctionId)) {		
		const url = `/Project_TIW_RIA/api/auctions/details?id=${auctionId}`;
		
		// Defines the callback function to handle the results
		const internalCallback = function (error, data) {
			// Checks if there is an error before looking at the data
	        if (error) {
	            // Logs
	            console.error("Failed to fetch search results:", error.message);
				// Returns the error
				callback(error, null);
	            return;
	        } else {
				// Handles the data received				
				callback(null, data);
			}
		}
		
		// Makes the Request
		fetchDataGET(url, internalCallback);
	} else {
		// Invalid ID
		console.error("Invalid auction ID:", auctionId);
	}
}

/*
    Makes a request to the server for the updated Available Items data,
    then proceeds to dynamically build the UI elements to display those Items and
    adds them to the DOM in the "Create Auction" Form.
*/
export function refreshAvailableItems() {
    // Defines the API URL
    const url = "/Project_TIW_RIA/api/items/available";
    
    // Defines the callback function to handle the results
    const callback = function (error, data) {
        const container = document.getElementById("auctionCreationForm");
        const list = document.getElementById("auctionCreationItemList");
        const emptyListMessage = document.getElementById("auctionCreationNoItemsText");

        // Clear the previous content before repopulating or refreshing
        list.innerHTML = "";

        // Checks if there is an error before looking at the data
        if (error) {
            // Logs
            console.error("Failed to fetch :", error.message);
            // Hides the List
            list.style.display = "none";
            // Shows the default text
            emptyListMessage.style.display = "block";
            // Changes the text in the default text to the error message
            emptyListMessage.textContent = "Could not load items.";
            return;
        }


        if (Array.isArray(data) && data.length === 0) {
            // No Auctions received

            // Hides the List
            list.style.display = "none";
            // Shows the default text
            emptyListMessage.textContent = "No available items. You need at least one to create an auction.";
            emptyListMessage.style.display = "block";
        } else {
            // Auctions received

            // Hides the default text
            emptyListMessage.style.display = "none"

            // Populates the List
            for (const item of data) {
                list.append(buildCheckBoxItemCard(item));
            }
			
			list.style.display = "flex";
        }
    }

    // Makes the Request
    fetchDataGET(url, callback);
}

// ===================
// ACTION FUNCTIONS
// ===================

// Makes an API request to create a new Auction with the data in the parameteres.
// Assumes client-side data validation has already been passed.
export function createAuction(form, callback) {
    const url = "/Project_TIW_RIA/CreateAuction";

     // Extract data from the form inputs
    const minIncrement = form.minIncrement.value.trim();
    const closingDate = form.closingDate.value.trim();
    const sellerId = user.id;

    // Get all selected item checkboxes
    const itemCheckboxes = form.querySelectorAll('input[name="itemIds"]:checked');
    const itemIds = Array.from(itemCheckboxes).map(cb => cb.value);

    // Build the payload
    const data = {
        minIncrement: minIncrement,
        closingDate: closingDate,
        sellerId: sellerId,
        itemIds: itemIds
    };
    
    doActionPOST(url, data, callback);
}

// Makes an API request to create a new Item with the data in the parameteres.
// Assumes client-side data validation has already been passed.
export function createItem(form, callback) {
    const url = "/Project_TIW_RIA/CreateItem";

    // Build the FormData object
    const data = new FormData();

    // Extract form values
    data.append("itemName", form.itemName.value.trim());
    data.append("itemDescription", form.itemDescription.value.trim());
    data.append("price", form.price.value.trim());
    data.append("creatorId", user.id);

    // Append image file if provided
    const imageInput = form.image;
    if (imageInput.files.length > 0) {
        data.append("image", imageInput.files[0]);
    }

    doActionPOST(url, data, callback);
}

// Makes an API request to create a new Offer to a certain Auction, signaled 
// by the AuctionId argument.
// Assumes client-side data validation has already been passed.
export function makeOffer(auctionId, offer, callback) {
    const url = "/Project_TIW_RIA/MakeOffer";

    // Builds the data
    const data = {
        userId: user.id,
        auctionId: auctionId,
        offer: offer
    };

    doActionPOST(url, data, callback);
}

// Makes an API request to close an Auction, signaled by the AuctionId argument.
// Assumes client-side data validation has already been passed.
export function closeAuction(auctionId, callback) {
    const url = "/Project_TIW_RIA/CloseAuction";

    // Builds the data
    const data = {
        userId: user.id,
        auctionId: auctionId
    }

    doActionPOST(url, data, callback);
}
/**
 * This file contains all the functions dedicated to fetching the internal APIs 
 * and rendering the receiving data into the page.
 */

import { buildAuctionCard } from './ui.js';

// =====================
// Main Function
// =====================

// Creates and sends an XMLHttpRequest to the Server at the URL passed as parameter,
// and attaches the parameter function as the callback to that request.
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
            } else {
                // The Request has failed
                callback(new Error(`Request failed with status ${xhr.status}`));
            }
        }
    };

    // Sends the request
    xhr.send();
}

export function refreshOpenAuctions() {
    // Defines the API URL
    const url = "/Project_TIW_RIA/api/auctions/open";
    
    // Defines the callback function to handle the results
    const callback = function (error, data) {
        const container = document.getElementById("openAuctionsContainer");
        const list = document.getElementById("openAuctionsList");
        const emptyListMessage = document.getElementById("noOpenAuctionsText")

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
        }
    }

    // Makes the Request
    fetchDataGET(url, callback);
}
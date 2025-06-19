/**
 * This file contains all the functions dedicated to interacting with browser cookies,
 * setting them with an expiration date and retrieving their data when needed.
 */

import { user } from "./home.js";

/*
	Appends the User's ID to the Cookie Key, so that Cookies are not shared between Users
*/
function getScopedCookieKey(name) {
    return `${name}-${user?.id ?? "anonymous"}`;
}


/*
    Creates a new cookie using the name as key, and initializes it to the value
    passed as argument.
    If a number of daysToExpire is passed, the cookie is also set with an expirationDate,
    otherwise it's not.
*/
export function setCookie(name, value, daysToExpire) {
	const scopedName = getScopedCookieKey(name);
	
    if (daysToExpire) {
        const expires = new Date(Date.now() + daysToExpire * 864e5).toUTCString();
        
        document.cookie = `${scopedName}=${encodeURIComponent(value)}; expires=${expires}; path=/`;
    } else {
        document.cookie = `${scopedName}=${encodeURIComponent(value)}; path=/`;
    }

}

/*
    Returns the value saved into the cookie whose key is passed as argument.
    Returns 'null' if the Cookie doesn't exist.
*/
export function getCookie(name) {
	const scopedName = getScopedCookieKey(name);
	
    const cookies = document.cookie.split("; ");

    for (const c of cookies) {
        const [key, ...rest] = c.split("=");
        const value = rest.join("=");
        if (key === scopedName) return decodeURIComponent(value).toLowerCase();
    }

    return null;
}


/*
    Deletes a cookie whose key is passed as argument.
*/
export function deleteCookie(name) {
	const scopedName = getScopedCookieKey(name);
	
    // To delete it, sets the expiration date to Epoch time
    document.cookie = `${scopedName}=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/`;
}

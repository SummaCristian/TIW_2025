/**
 * This file contains all the functions dedicated to interacting with browser cookies,
 * setting them with an expiration date and retrieving their data when needed.
 */


/*
    Creates a new cookie using the name as key, and initializes it to the value
    passed as argument.
    If a number of daysToExpire is passed, the cookie is also set with an expirationDate,
    otherwise it's not.
*/
export function setCookie(name, value, daysToExpire) {
    if (daysToExpire) {
        const expires = new Date(Date.now() + daysToExpire * 864e5).toUTCString();
        
        document.cookie = `${name}=${encodeURIComponent(value)}; expires=${expires}; path=/`;
    } else {
        document.cookie = `${name}=${encodeURIComponent(value)}; path=/`;
    }

}

/*
    Returns the value saved into the cookie whose key is passed as argument.
    Returns 'null' if the Cookie doesn't exist.
*/
export function getCookie(name) {
    const cookies = document.cookie.split("; ");

    for (const c of cookies) {
        const [key, ...rest] = c.split("=");
        const value = rest.join("=");
        if (key === name) return decodeURIComponent(value);
    }

    return null;
}


/*
    Deletes a cookie whose key is passed as argument.
*/
export function deleteCookie(name) {
    // To delete it, sets the expiration date to Epoch time
    document.cookie = `${name}=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/`;
}

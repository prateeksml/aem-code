import { getCookie, setCookie } from "../../../../../js/util/kpmg/kpmg-utils";

//NOTE: Only place variables and methods/functions here that are shared by Search

//Set max terms allowed
const maxTermsAllowed = 10;

const defaultCookieID = "previous_search_terms";

let terms = [];

let languageCode = "";
let countryCode = "";

let cookieName = "";

const setLanguageCode = (code) => (languageCode = code);
const setCountryCode = (code) => (countryCode = code);

const getCookieTerms = () => {
  //Construct cookie name
  cookieName = `${defaultCookieID}_${countryCode ?? ""}_${languageCode ?? ""}`;

  //Get cookie
  const cookie = getCookie(cookieName) ?? false;

  //Log whether cookie was found or not
  !cookie
    ? console.log(
        `CookieUtils: getCookieTerms: No cookie by the name of ${cookieName} was found!`
      )
    : console.log(`CookieUtils: getCookieTerms: Cookie found: `, cookie);

  const cookieTerms = cookie.terms;
  //If terms key is not present in the object
  //then assign terms to an empty array
  return cookieTerms ? (terms = cookieTerms) : (terms = []);
};

function saveTermInCookie(searchTerm) {
  const currentTerms = getCookieTerms();

  console.log("CookieUtils: saveTermInCookie: currentTeams: ", currentTerms);

  //Init new empty array to store updated cookie terms
  let newCookieTermsArray = [];

  //Construct cookie name
  const cookieName =
    countryCode && languageCode
      ? `${defaultCookieID}_${countryCode}_${languageCode}`
      : defaultCookieID;

  //Check if current search term is already in the terms array
  const termFound = !!terms.find((term) => term === searchTerm);

  //If term is not found in cookie terms array
  //Add it and set new cookie
  if (!termFound) {
    newCookieTermsArray = [...terms, searchTerm];
  }

  //Set the cookie
  setCookie(cookieName, JSON.stringify({ terms: newCookieTermsArray }));

  //Update terms object with new terms
  terms = newCookieTermsArray;

  console.log("CookieUtils: saveTermInCookie: new terms: ", terms);
}

export { getCookieTerms, saveTermInCookie, setLanguageCode, setCountryCode };

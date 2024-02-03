import { registerComponent } from "./../../../../components/js/component";

import {
  setLanguageCode,
  setCountryCode,
  getCookieTerms,
} from "./classes/utils/cookie-utils";

import { SearchAutoComplete } from "./classes/auto-complete";

class Search {
  constructor(config) {
    try {
      this.SEARCH = config.element;

      if (!this.SEARCH) {
        console.error(
          `Search: Init: Cannot find [data-cmp-search] make sure it is present on the parent search element`
        );
      }

      this.SELECTORS = config.selectors;

      this.UI = {
        searchSection: this.SEARCH.querySelector(this.SELECTORS.searchSection),
        searchButton: this.SEARCH.querySelector(this.SELECTORS.searchButton),
        cancelButton: this.SEARCH.querySelector(this.SELECTORS.cancelButton),
      };
    } catch (err) {
      console.error("Search: error: ", err);
    }

    //Get all data attributes and assign them to global variables
    const { languageCode, countryCode } = this.SEARCH.dataset;

    //Set Language Code for Cookie Name
    setLanguageCode(languageCode);

    //Set Country Code for Cookie Name
    setCountryCode(countryCode);

    //INFO: Initializing autocomplete functionality
    const searchAutoComplete = new SearchAutoComplete(this.SEARCH);

    this.onSearchButtonClick = () => {
      this.UI.searchSection.classList.add("cmp-search__section--active");

      setTimeout(() => {
        searchAutoComplete.show();
      }, 500);
    };

    this.onCancelSearchClick = () => {
      this.UI.searchSection.classList.remove("cmp-search__section--active");
    };

    //Add Event Listeners
    // this.UI.searchButton.addEventListener(
    //   "click",
    //   this.onSearchButtonClick,
    //   true
    // );
    // this.UI.cancelButton.addEventListener(
    //   "click",
    //   this.onCancelSearchClick,
    //   true
    // );
  }
}

registerComponent(Search, {
  selectors: {
    self: "[data-cmp-search]",
    searchButton: "[data-cmp-search-button]",
    cancelButton: "[data-cmp-search-cancel-button]",
    searchSection: "[data-cmp-search-section]",
  },
});

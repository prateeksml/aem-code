import accessibleAutocomplete from "accessible-autocomplete";

import { PreviousSearch } from "./previous-search";

import { saveTermInCookie } from "./utils/cookie-utils";

import {
  getKeyID,
  sanitizeInputValue,
  createElement,
} from "../../../../js/util/kpmg/kpmg-utils";

export class SearchAutoComplete {
  //INFO: This is the initializing function that will
  //build the functionality for this sub component

  constructor(root) {
    this.SEARCH = root;

    const autoCompleteID = "cmp-search-auto-complete";
    const autoCompleteActiveClass = `${autoCompleteID}--active`;

    this.STATIC_PROPS = {
      autoCompleteID,
      autoCompleteActiveClass,
      minSearchLength: 3,
    };

    //INFO: This object contains the attributes to target the UI
    this.SELECTORS = {
      searchInput: `#${this.STATIC_PROPS.autoCompleteID}`,
      searchInputLabel: "[data-cmp-search-auto-complete-input-label]",
      autoComplete: "[data-cmp-search-auto-complete]",
    };

    //INFO this object will be used to store ui elements
    this.UI = {
      searchInputLabel: this.SEARCH.querySelector(
        this.SELECTORS.searchInputLabel
      ),
      autoComplete: this.SEARCH.querySelector(this.SELECTORS.autoComplete),
    };

    //Set initial searchInputLength
    this.searchInputValue = "";

    //Initialize accessibleAutocomplete
    this.initAutoComplete(this);

    //Get search input after autocomplete initialized and add it to UI object
    this.UI.searchInput = this.SEARCH.querySelector(this.SELECTORS.searchInput);

    //Create clear button
    this.createClearButton();

    //INFO: Init Search Previous Search
    this.previousSearch = new PreviousSearch(this.SEARCH);

    this.clear = () => {
      this.clearSearchInput();
    };

    this.focus = () => {
      this.focusSearchInput();
    };

    //Shows autocomplete input
    this.show = () => {
      this.UI.autoComplete.classList.add(
        this.STATIC_PROPS.autoCompleteActiveClass
      );
      this.focusSearchInput();
    };

    this.hide = () => {
      this.UI.autoComplete.classList.remove(
        this.STATIC_PROPS.autoCompleteActiveClass
      );
      this.blurSearchInput();
      this.clearSearchInput();
    };

    //Attach Events
    this.UI.clearButton.addEventListener(
      "click",
      this.onClearButtonClick,
      true
    );

    this.SEARCH.addEventListener(
      "keyup",
      (evt) => {
        //Get human readable key ID
        const keyID = getKeyID(evt);

        //Get current search input length
        let inputLength = this.UI.searchInput.value.length;

        //Declare const to target the class list of clear button
        const clearButtonClassList = this.UI.clearButton.classList;
        const clearButtonActiveClass =
          "cmp-search-auto-complete__input-clear--active";

        //Hide/show previous search based on search input length
        if (inputLength > this.STATIC_PROPS.minSearchLength) {
          //Hide Previous Search
          this.previousSearch.hide();
        } else {
          this.previousSearch.show();
        }

        //Hide/show clear button based on search input length
        inputLength === 0
          ? clearButtonClassList.remove(clearButtonActiveClass)
          : clearButtonClassList.add(clearButtonActiveClass);

        //If user hits enter on their keyboard
        if (keyID === "enter") {
          this.saveTerm();
        }
      },
      true
    );
  }

  initAutoComplete(main) {
    const autoCompleteID = main.STATIC_PROPS.autoCompleteID;
    const autoComplete = main.UI.autoComplete;
    const placeholderText = main.UI.searchInputLabel.dataset["placeholderText"];

    //Create config object that will be passed to autocomplete
    const autoCompleteDefaultConfig = {
      element: autoComplete,
      cssNamespace: autoCompleteID,
      id: autoCompleteID,
      displayMenu: "overlay",
      placeholder: placeholderText,
      showNoOptionsFound: false,
      minLength: main.STATIC_PROPS.minSearchLength,
      name: autoCompleteID,
      onConfirm: () => this.saveTerm,
      source: (term, populateResults) =>
        main.typeAheadSearchResults(main, term, populateResults),
    };

    //Initialize accessible autocomplete
    accessibleAutocomplete(autoCompleteDefaultConfig);
  }

  typeAheadSearchResults(main, term, populateResults) {
    const placeholderTypeAheadSource = [
      "Acquisition or Disposal Structuring",
      "Acquisition Strategy",
      "Chief Global Strategist",
      "Chief Strategy Officer",
      "Corporate Tax Advisory",
      "Corporate Tax Stuff",
    ];

    const { suggestEndpoint } = main.SEARCH.dataset;

    this.searchInputValue = sanitizeInputValue(term);

    console.log("typeAheadSearchResults: ", this.searchInputValue);

    this.previousSearch.buildList();

    // NOTE: For some reason to suggestEndpoint returns a string with two quote characters
    if (suggestEndpoint.length > 2) {
      //We have a suggested endpoint available
      // const url = suggestEndpoint(),
      //   body = JSON.stringify({ demand: searchInputValue }),
      //   method = "post";
      // //TODO: make this fetch logic reusable in kpmg-utils
      // fetch(url, {
      //   body,
      //   method,
      //   headers: { "content-type": "application/json" },
      // })
      //   .then((res) => {
      //     res
      //       .json()
      //       .then((data) => {
      //         const dataPromptText = data.prompt_text ?? [];
      //         if (Array.isArray(dataPromptText)) {
      //           populateResults(dataPromptText);
      //         }
      //       })
      //       .catch((err) => {
      //         console.error("Search: typeAheadSearchResults: ", err);
      //       });
      //   })
      //   .catch((err) => {
      //     console.error("search: typeAheadSearchResults: ", err);
      //   });
    } else {
      console.log("HERE");
      const filteredResults = placeholderTypeAheadSource.filter((result) => {
        const resultString = result.toLowerCase();
        if (resultString.indexOf(this.searchInputValue.toLowerCase()) !== -1) {
          return result;
        }
      });
      if (filteredResults.length > 0) {
        populateResults(filteredResults);
      }
    }
  }

  saveTerm() {
    console.log("Save Term: ", this.searchInputValue);
    //Save term in cookie
    saveTermInCookie(this.searchInputValue);
    //Clear input
    this.clearSearchInput();
  }

  createClearButton() {
    //Create the clear button that will appear in the newly created input field
    const clearButton = createElement("button", {
      classList: "cmp-search-auto-complete__input-clear",
      textContent: "cancel",
      attributes: [{ "aria-label": "Clear" }],
    });
    this.UI.autoComplete.appendChild(clearButton);
    this.UI.clearButton = clearButton;
  }

  focusSearchInput() {
    this.UI.searchInput.focus();
  }

  blurSearchInput() {
    this.UI.searchInput.blur();
  }

  clearSearchInput() {
    this.UI.searchInput.value = "";
  }

  onClearButtonClick(evt) {
    evt.preventDefault();
    this.clearSearchInput();
    //Return focus to input field
    setTimeout(() => {
      this.UI.searchInput.focus();
    }, 250);
  }
}

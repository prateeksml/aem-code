import { registerComponent } from "./component";
import accessibleAutocomplete from "accessible-autocomplete";
import { getCookie, createCookie } from "./util/CookieUtil";
import {
  saveTermInCookieAndRedirect,
  previousSearchCookieName,
  isPublisher,
  typeAheadSearchResults,
  removeTags,
  inputValueTemplate,
  suggestionTemplate,
  sanitizeData,
} from "./util/CommonUtils";
import {
  GetSearchInfoForDataLayer,
  HaveSearchInputEvents,
  SaveSearchInfoForDataLayer,
} from "./datalayer";

const cookieName = sanitizeData(previousSearchCookieName());
const minSearchLength = 3;

function searchInputAutocomplete(config) {
  const urlParams = new URLSearchParams(window.location.search);
  const myParam = urlParams.get("q") || "";
  const $wrapper = config.element;
  let placeholderText = $wrapper.querySelector(
    ".cmp-search-results-input__input-label"
  ).dataset.placeholder;
  accessibleAutocomplete({
    element: $wrapper,
    id: "cmp-search-results-input__input-box", // To match it to the existing <label>.
    source: typeAheadSearchResults,
    displayMenu: "overlay",
    placeholder: placeholderText,
    showNoOptionsFound: false,
    minLength: minSearchLength,
    name: "autocomplete-search-results-input",
    onConfirm: sanitizeDataAndRedirect,
    defaultValue: myParam,
    templates: {
      inputValue: inputValueTemplate,
      suggestion: suggestionTemplate,
    },
  });
  const inputClearButton = document.createElement("button");
  inputClearButton.classList.add("cmp-search-results__input-clear");
  inputClearButton.textContent = "cancel";
  inputClearButton.setAttribute("aria-label", "Clear");
  $wrapper.appendChild(inputClearButton);

  function sanitizeDataAndRedirect(inputValue) {
    if (inputValue) {
      let sanitizedInputValue = removeTags(inputValue);
      let typedTerm = (
          GetSearchInfoForDataLayer("inputTerm") || ""
        ).toLowerCase(),
        autocompleteValue = sanitizedInputValue.toLowerCase();
      if (autocompleteValue != typedTerm) {
        SaveSearchInfoForDataLayer("searchMethod", "autocomplete");
      }

      let count = GetSearchInfoForDataLayer("searchCount") || 0;
      SaveSearchInfoForDataLayer("searchCount", ++count);
      saveTermInCookieAndRedirect(sanitizedInputValue);
    }
  }
}
function SearchInput(config) {
  const inputElement = config.element.querySelector(config.selectors.input);
  const emptySearchElement = config.element.querySelector(
    config.selectors.empty_search_block
  );
  const previousSearchElement = config.element.querySelector(
    config.selectors.previous_search_block
  );
  const previousSearchListItemsElement = config.element.querySelector(
    config.selectors.previous_search_list_items_block
  );
  const inputClearCTA = config.element.querySelector(
    config.selectors.input_clear_cta
  );
  inputClearCTA.classList.add("active");

  //Hide previous search and other blocks when the focus left
  config.element.addEventListener("focusout", function (event) {
    if (!config.element.contains(event.relatedTarget)) {
      document.querySelector(
        ".cmp-search-results-input__previous-search__screen-reader-only"
      ).innerHTML = "";
      emptySearchElement.classList.remove("active");
      previousSearchElement.classList.remove("active");
    }
  });

  config.element.addEventListener("keyup", function (event) {
    let element = event.target;
    let keycode = event.keyCode ? event.keyCode : event.which;
    if (keycode === 13) {
      previousSearchListener(element, event);
    }
  });
  config.element.addEventListener("mousedown", function (event) {
    let element = event.target;
    previousSearchListener(element, event);
  });
  function previousSearchListener(element, event) {
    //Previous Search Remove Functionality
    if (
      element.classList.contains(
        "cmp-search-results-input__previous-search--remove-cta"
      )
    ) {
      let term = element.dataset.term;
      event.preventDefault();
      let previousSearchTermsArray = JSON.parse(getCookie(cookieName));
      if (previousSearchTermsArray.includes(term)) {
        previousSearchTermsArray = previousSearchTermsArray.filter(
          (item) => item !== term
        );
      }
      let newSearchTermsString = JSON.stringify(previousSearchTermsArray);
      createCookie(cookieName, newSearchTermsString, 365);
      element.parentElement.remove();
      inputElement.focus();
      if (!previousSearchTermsArray.length) {
        document.querySelector(
          ".cmp-search-results-input__previous-search__screen-reader-only"
        ).innerHTML = "";
        previousSearchElement.classList.remove("active");
      }
    }
    if (
      element.classList.contains(
        "cmp-search-results-input__previous-search--text-cta"
      )
    ) {
      previousSearchElement.classList.remove("active");
      setTimeout(function () {
        inputElement.value = element.innerText;
        inputClearCTA.classList.add("active");
      });
      saveTermInCookieAndRedirect(element.innerText);
      let count = GetSearchInfoForDataLayer("searchCount") || 0;
      SaveSearchInfoForDataLayer("searchCount", ++count);
      SaveSearchInfoForDataLayer("searchMethod", "previous-search");
    }
  }

  function validateAndShowPreviousSearchBlock() {
    if (
      getCookie(cookieName) &&
      getCookie(cookieName) !== "[]" &&
      !emptySearchElement.classList.contains("active") &&
      inputElement.value.trim().length < minSearchLength
    ) {
      let previousSearchTermsString = getCookie(cookieName);
      previousSearchListItemsElement.innerHTML = "";
      JSON.parse(previousSearchTermsString).forEach(function (item) {
        const list = document.createElement("li");
        list.classList.add("cmp-search-results-input__previous-search-item");
        const cancelButton = document.createElement("button");
        cancelButton.classList.add(
          "cmp-search-results-input__previous-search--remove-cta"
        );
        cancelButton.textContent = "close";
        cancelButton.setAttribute("aria-label", "Remove " + item);
        cancelButton.setAttribute("data-term", item);
        const previousSearchItemButton = document.createElement("button");
        previousSearchItemButton.classList.add(
          "cmp-search-results-input__previous-search--text-cta"
        );
        previousSearchItemButton.textContent = item;
        list.appendChild(cancelButton);
        list.appendChild(previousSearchItemButton);
        previousSearchListItemsElement.prepend(list);
      });
      previousSearchElement.classList.add("active");
      let message = document.querySelector(
        ".cmp-search-results-input__previous-search__screen-reader-only"
      ).dataset.message;
      message = message.replace(
        "{number}",
        JSON.parse(previousSearchTermsString).length
      );
      document.querySelector(
        ".cmp-search-results-input__previous-search__screen-reader-only"
      ).textContent = message;
    }
  }

  inputElement.addEventListener("focus", function () {
    validateAndShowPreviousSearchBlock();
  });

  inputClearCTA.addEventListener("click", function () {
    HaveSearchInputEvents(config.element, "clear");
    inputElement.focus();
    inputClearCTA.classList.remove("active");
    setTimeout(function () {
      inputElement.value = "";
      validateAndShowPreviousSearchBlock();
    });
  });

  inputElement.addEventListener("keyup", function (e) {
    let keycode = e.keyCode ? e.keyCode : e.which;
    let inputValue = inputElement.value.trim();

    if (inputElement.value !== "") {
      inputClearCTA.classList.add("active");
    } else {
      inputClearCTA.classList.remove("active");
    }
    if (keycode === 13) {
      if (inputValue) {
        setTimeout(function () {
          inputElement.value = inputValue;
          inputClearCTA.classList.add("active");
        });
        let count = GetSearchInfoForDataLayer("searchCount") || 0;
        SaveSearchInfoForDataLayer("searchCount", ++count);
        SaveSearchInfoForDataLayer("searchMethod", "typed");
        saveTermInCookieAndRedirect(inputValue);
      } else {
        //show empty search message and hide previous search block
        document.querySelector(
          ".cmp-search-results-input__previous-search__screen-reader-only"
        ).innerHTML = "";
        previousSearchElement.classList.remove("active");
        inputElement.value = "";
        emptySearchElement.classList.add("active");
      }
    } else {
      if (keycode === 27) {
        document.querySelector(
          ".cmp-search-results-input__previous-search__screen-reader-only"
        ).innerHTML = "";
        emptySearchElement.classList.remove("active");
        previousSearchElement.classList.remove("active");
      }
      //Hide empty search message
      emptySearchElement.classList.remove("active");

      //hide previous search block once typeahead is triggered
      if (inputValue.length >= minSearchLength) {
        document.querySelector(
          ".cmp-search-results-input__previous-search__screen-reader-only"
        ).innerHTML = "";
        previousSearchElement.classList.remove("active");
      } else {
        if (keycode !== 27) {
          //Show previous search block once typeahead is removed
          validateAndShowPreviousSearchBlock();
        }
      }
    }
  });
}

if (isPublisher()) {
  registerComponent(searchInputAutocomplete, {
    selectors: {
      self: ".cmp-search-results-input__autocomplete-wrapper",
    },
  });
}
if (isPublisher()) {
  registerComponent(SearchInput, {
    selectors: {
      self: ".cmp-search-results-input",
      input: "#cmp-search-results-input__input-box",
      empty_search_block: ".cmp-search-results-input__empty-search-wrapper",
      previous_search_block:
        ".cmp-search-results-input__previous-search-wrapper",
      previous_search_list_items_block:
        ".cmp-search-results-input__previous-search-items",
      input_clear_cta: ".cmp-search-results__input-clear",
    },
  });
}

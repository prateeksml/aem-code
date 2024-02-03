import { createElement } from "../../../../js/util/kpmg/kpmg-utils";
import { getCookieTerms } from "./utils/cookie-utils";

export class PreviousSearch {
  constructor(root) {
    this.SEARCH = root;

    //INFO: This object will contain static props
    this.PROPS = {
      previousSearchActiveClass: "cmp-search__previous-search--active",
      previousSearchListActiveClass: "cmp-search__previous-search-list--active",
    };

    this.SELECTORS = {
      previousSearch: "[data-cmp-search-previous-search]",
      previousSearchList: "[data-cmp-search-previous-search-list]",
      previousSearchMessage:
        "[data-cmp-search-previous-search-screen-reader-only]",
    };

    this.UI = {
      previousSearch: this.SEARCH.querySelector(this.SELECTORS.previousSearch),
      previousSearchList: this.SEARCH.querySelector(
        this.SELECTORS.previousSearchList
      ),
      previousSearchMessage: this.SEARCH.querySelector(
        this.SELECTORS.previousSearchMessage
      ),
    };

    //Public Methods
    this.show = () => {
      this.UI.previousSearch.classList.add(
        this.PROPS.previousSearchActiveClass
      );
      return true;
    };

    this.hide = () => {
      this.UI.previousSearch.classList.remove(
        this.PROPS.previousSearchActiveClass
      );
      return false;
    };
  }

  buildList() {
    const cookieTerms = getCookieTerms();

    //Declare const to target the class list of previous search list
    const previousSearchListClass = this.UI.previousSearchList.classList;
    const previousSearchMessage = this.UI.previousSearchMessage;

    //Clear previous search list items and rebuild
    this.UI.previousSearchList.replaceChildren();

    console.log("PreviousSearch: buildList: cookieTerms: ", cookieTerms);

    if (cookieTerms.length > 0) {
      //Get number of previous search in array and update screen reader msg
      const { message } = previousSearchMessage.dataset;

      const newMessage = message.replace("{number}", cookieTerms.length);
      previousSearchMessage.textContent = newMessage;

      //Cycle through previous search terms in cookieTerms
      cookieTerms.forEach((term) => {
        //For each term
        //Create list item
        const list = createElement("li", {
          classList: "cmp-search__previous-search--item",
        });

        //Create cancel button to show in input field
        const cancelButton = createElement("button", {
          classList: "cmp-search__previous-search--remove-cta",
          textContent: "close",
          attributes: [
            { "aria-label": `Remove ${term}` },
            { "data-term": term },
          ],
        });

        //Create previous search item button
        const previousSearchItemButton = createElement("button", {
          classList: "cmp-search__previous-search--text-cta",
          textContent: term,
        });

        //Add both newly created buttons to list item
        list.appendChild(cancelButton);
        list.appendChild(previousSearchItemButton);

        //Put list item at top of ul
        this.UI.previousSearchList.prepend(list);
      });

      //Show previous search list
      previousSearchListClass.add(this.PROPS.previousSearchListActiveClass);
    } else {
      //Hide previous search list
      previousSearchListClass.remove(this.PROPS.previousSearchListActiveClass);
    }
  }
}

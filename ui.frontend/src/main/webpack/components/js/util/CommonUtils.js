import { ShowSearchErrorMsgOverlay } from "../common-message-overlay";
import {
  GetSearchInfoForDataLayer,
  HaveSearchResultListEvents,
  HaveSearchResultLoadEvents,
  SaveSearchInfoForDataLayer,
} from "../datalayer";
import { getCookie, createCookie } from "./CookieUtil";
export function sanitizeData(str) {
  if (str) {
    return str.replace(/[@#<>=`%'";:&~^!*+?^${}()|\/[\]\\]/g, "");
  }
}
let countryCode,
  languageCode,
  mode,
  searchResultsPageURL,
  suggestURL,
  searchResultsURL,
  resultsPerPage = 10;
const userAgent = navigator.userAgent.toLowerCase();
const checkMobile =
  /mobile|android|iphone|ipad|ipod|blackberry|iemobile|opera mini/i.test(
    userAgent
  );
const observer = new IntersectionObserver((entries) => {
  entries.forEach((entry) => {
    if (entry.isIntersecting) {
      if (
        isMobile() &&
        document.querySelector(
          ".cmp-search-results__pagination--next-button"
        ) &&
        !document.querySelector(".cmp-search-results__pagination--next-button")
          .disabled
      ) {
        document
          .querySelector(".cmp-search-results__pagination--next-button")
          .click();
      }
    }
  });
});

if (document.querySelector(".cmp-search")) {
  countryCode = document.querySelector(".cmp-search").dataset.countryCode;
  languageCode = document.querySelector(".cmp-search").dataset.languageCode;
  mode = document.querySelector(".cmp-search").dataset.runMode;
  searchResultsPageURL =
    document.querySelector(".cmp-search").dataset.searchResultPage;
  suggestURL = document.querySelector(".cmp-search").dataset.suggestEndpoint;
  searchResultsURL =
    document.querySelector(".cmp-search").dataset.searchEndpoint;
  resultsPerPage =
    document.querySelector(".cmp-search").dataset.paginationLimit;
}

export function isMobile() {
  return checkMobile;
}

export function inputValueTemplate(result) {
  if (result) {
    return removeTags(result);
  }
}

export function suggestionTemplate(result) {
  return result;
}

export function removeTags(str) {
  if (str === null || str === "") {
    return false;
  } else {
    str = str.toString();
  }

  // Regular expression to identify HTML tags in
  // the input string. Replacing the identified
  // HTML tag with a null string.
  return str.replace(/(<([^>]+)>)/gi, "");
}

export function isPublisher() {
  if (mode === "publish") {
    return true;
  } else {
    return false;
  }
}
export function getCountryCode() {
  if (countryCode) {
    return countryCode;
  }
}

export function getLanguageCode() {
  if (languageCode) {
    return languageCode;
  }
}

export function searchEndpoint() {
  return searchResultsURL + getCountryCode() + "_" + getLanguageCode();
}

export function suggestEndpoint() {
  return suggestURL + getCountryCode() + "_" + getLanguageCode();
}

export function getQueryParams() {
  const urlParams = new URLSearchParams(window.location.search);
  return urlParams;
}

export function updateQueryParams(searchTerm, pageNumber, filter) {
  let newQueryString = "?q=" + searchTerm;
  if (filter) {
    newQueryString += "&kpmg_doc_type=" + filter;
  }
  if (pageNumber) {
    newQueryString += "&page=" + pageNumber;
  }
  window.history.pushState(null, "", newQueryString);
}

export function typeAheadSearchResults(inputTerm, populateResults) {
  const url = suggestEndpoint(),
    body = JSON.stringify({ demand: inputTerm }),
    method = "post";
  SaveSearchInfoForDataLayer("inputTerm", inputTerm);
  fetch(url, { body, method, headers: { "content-type": "application/json" } })
    .then((response) => {
      response
        .json()
        .then((data) => {
          if (data && data.prompt_text && Array.isArray(data.prompt_text)) {
            populateResults(data.prompt_text);
          }
        })
        .catch((e) => {
          console.log("Error Occured During Parsing Data - " + e);
          ShowSearchErrorMsgOverlay();
        });
    })
    .catch((er) => {
      console.log("Error Occured During ES Call - " + er);
      ShowSearchErrorMsgOverlay();
    });
}

export function previousSearchCookieName() {
  let previousSearchCookieName = "previous_search_terms";
  const countryCode = getCountryCode();
  const languageCode = getLanguageCode();
  if (countryCode && languageCode) {
    previousSearchCookieName =
      previousSearchCookieName + "_" + countryCode + "_" + languageCode;
  }
  return previousSearchCookieName;
}

export function saveTermInCookieAndRedirect(inputValue, redirectPage) {
  const currentPageURL = window.location.pathname;
  const cookieName = previousSearchCookieName();
  if (inputValue) {
    let sanitizedValue = sanitizeData(inputValue);
    const redirectURL = searchResultsPageURL + "?q=" + sanitizedValue;
    let previousSearchTermsString = "[]";
    if (getCookie(cookieName)) {
      previousSearchTermsString = getCookie(cookieName);
    }
    let previousSearchTermsArray = JSON.parse(previousSearchTermsString);
    //Check if the term is already available if so remove the term and add again at last position
    if (
      previousSearchTermsArray.filter((str) =>
        str.toLowerCase().includes(sanitizedValue.toLowerCase())
      )
    ) {
      previousSearchTermsArray = previousSearchTermsArray.filter(
        (item) => item.toLowerCase() !== sanitizedValue.toLowerCase()
      );
    }
    //remove 1st item if the serach terms are more than 10
    if (
      previousSearchTermsArray &&
      previousSearchTermsArray.length &&
      previousSearchTermsArray.length >= 10
    ) {
      previousSearchTermsArray.shift();
    }
    if (sanitizedValue) {
      previousSearchTermsArray.push(sanitizedValue);
      let newSearchTermsString = JSON.stringify(previousSearchTermsArray);
      createCookie(cookieName, newSearchTermsString, 365);
    }
    if (redirectPage || currentPageURL != searchResultsPageURL) {
      //redirecting to search results page
      window.location.href = encodeURI(redirectURL);
    } else {
      setTimeout(function () {
        document.querySelector("#cmp-search-results-input__input-box").blur();
      }, 100);
      updateQueryParams(sanitizedValue, 1);
      document.querySelector(".cmp-search-results__list").innerHTML = "";
      document.querySelector(
        ".cmp-search-results__filters-container"
      ).scrollLeft = 0;
      fetchResults(sanitizedValue, 1, "ALL");
    }
  }
}

const $countDesktopLabel = document.querySelector(
  ".cmp-search-results__count--desktop-label"
);
const countDesktopText = $countDesktopLabel
  ? $countDesktopLabel.dataset.text
  : "";

const $countMobileLabel = document.querySelector(
  ".cmp-search-results__count--mobile-label span"
);
function updateResultCount(totalResults, itemsFrom) {
  $countDesktopLabel.innerText = "";
  if (countDesktopText && totalResults > 0) {
    let itemsTo = resultsPerPage;
    if (itemsFrom === 0 || !itemsFrom) {
      itemsFrom = 1;
    }
    itemsTo = itemsFrom + (resultsPerPage - 1);
    if (itemsTo > totalResults) {
      itemsTo = totalResults;
    }
    let countDesktopTextNew = countDesktopText.replace(
      "{current_page}",
      itemsFrom + " - " + itemsTo
    );
    countDesktopTextNew = countDesktopTextNew.replace(
      "{total_results}",
      totalResults
    );
    $countDesktopLabel.innerText = countDesktopTextNew;
  }
  $countMobileLabel.innerText = totalResults;
  if (countDesktopText && totalResults == 0) {
    $countDesktopLabel.innerText = document.querySelector(
      ".cmp-search-results__count--mobile-label"
    ).innerText;
  }
  if (countDesktopText && totalResults < itemsFrom) {
    $countMobileLabel.innerText = 0;
    $countDesktopLabel.innerText = document.querySelector(
      ".cmp-search-results__count--mobile-label"
    ).innerText;
  }
}

export function fetchResults(inputTerm, pageNumber, filter) {
  if (
    document
      .querySelector(".cmp-search-results-input .autocomplete__menu")
      .classList.contains("autocomplete__menu--visible")
  ) {
    document
      .querySelector(".cmp-search-results-input .autocomplete__menu")
      .classList.remove("autocomplete__menu--visible");
    document
      .querySelector(".cmp-search-results-input .autocomplete__menu")
      .classList.add("autocomplete__menu--hidden");
  }
  const $resultListContainer = document.querySelector(
    ".cmp-search-results__list"
  );
  const upcomingEventLabel = $resultListContainer.dataset.upcomingLabel;
  const pastEventLabel = $resultListContainer.dataset.pastLabel;
  if (!filter) {
    filter = "ALL";
  }

  if (filter) {
    document
      .querySelectorAll(".cmp-search-results__filters--cta")
      .forEach((button) => {
        const filterType = button.dataset.filterType;
        if (filterType === filter.toLowerCase()) {
          button.classList.add("selected");
        } else {
          button.classList.remove("selected");
        }
      });
  }
  let items_from = 0;
  let article_type;

  if (pageNumber && pageNumber > 1) {
    items_from = parseInt(pageNumber - 1 + "1");
  }

  if (filter && filter !== "ALL") {
    article_type = filter;
  }
  const url = searchEndpoint(),
    body = JSON.stringify({
      items_from: items_from > resultsPerPage ? items_from - 1 : items_from,
      demand: inputTerm,
      document_type: article_type,
    }),
    method = "post";
  fetch(url, {
    body,
    method,
    headers: { "content-type": "application/json" },
  })
    .then((response) => {
      response
        .json()
        .then((data) => {
          console.log(data);
          HaveSearchResultLoadEvents(
            document.querySelector(".cmp-search-results"),
            {
              basicFilter: SaveSearchInfoForDataLayer(
                "basicFilter",
                filter ?? "all"
              ),
              inputTerm: GetSearchInfoForDataLayer("inputTerm"),
              pageCount: SaveSearchInfoForDataLayer(
                "pageCount",
                pageNumber ?? 1
              ),
              searchPage: "site search",
              searchTerm: SaveSearchInfoForDataLayer("searchTerm", inputTerm),
              numResults: SaveSearchInfoForDataLayer(
                "numResults",
                data.results.total_count
              ),
              searchCount: GetSearchInfoForDataLayer("searchCount") ?? 1,
              searchMethod: GetSearchInfoForDataLayer("searchMethod") ?? "",
            }
          );

          if (!isMobile()) {
            $resultListContainer.innerHTML = "";
          }
          document.getElementById("cmp-search-results__pagination").innerHTML =
            "";
          document
            .getElementById("cmp-search-results__pagination")
            .classList.remove("active");
          for (let item of data.results.results) {
            if (
              item.resource.document_type === "GENERIC" ||
              item.resource.document_type === "INSIGHT"
            ) {
              createArticleTile(item, $resultListContainer);
            }
            if (item.resource.document_type === "EVENT") {
              createEventTile(
                item,
                $resultListContainer,
                upcomingEventLabel,
                pastEventLabel
              );
            }
            if (item.resource.document_type === "CONTACT") {
              createContactTile(item, $resultListContainer);
            }
            //highlight filter pill
            if (filter) {
              document
                .querySelectorAll(".cmp-search-results__filters--cta")
                .forEach((button) => {
                  const filterType = button.dataset.filterType;
                  if (filterType === filter.toLowerCase()) {
                    button.classList.add("selected");
                  } else {
                    button.classList.remove("selected");
                  }
                });
            }
          }
          updateResultCount(data.results.total_count, items_from);
          if (data.results.total_count == 0) {
            $resultListContainer.innerHTML = "";
            document
              .getElementById("cmp-search-results__no-results")
              .classList.add("active");
          } else {
            document
              .getElementById("cmp-search-results__no-results")
              .classList.remove("active");
          }
          //show pagination if the results are greater than 10
          if (data.results.total_count > resultsPerPage) {
            pagination(data.results.total_count, pageNumber);
            if (isMobile()) {
              const targetElement = document.querySelector(
                ".cmp-search-results__list"
              ).lastChild;
              observer.observe(targetElement);
            }
          }
          if (
            document
              .querySelector(".cmp-search-results-input .autocomplete__menu")
              .classList.contains("autocomplete__menu--visible")
          ) {
            document
              .querySelector(".cmp-search-results-input .autocomplete__menu")
              .classList.remove("autocomplete__menu--visible");
            document
              .querySelector(".cmp-search-results-input .autocomplete__menu")
              .classList.add("autocomplete__menu--hidden");
          }
        })
        .catch((e) => {
          console.log("Error Occured During Parsing Data - " + e);
          ShowSearchErrorMsgOverlay();
        });
    })
    .catch((er) => {
      console.log("Error Occured During ES Call - " + er);
      ShowSearchErrorMsgOverlay();
    });
}
function pagination(totalResults, currentPage) {
  if (!currentPage || currentPage <= 0) {
    currentPage = 1;
  }
  let maxNumbers = 5;
  let minPages = 7;
  const totalPages = Math.ceil(totalResults / resultsPerPage);
  if (totalPages < maxNumbers) {
    maxNumbers = totalPages;
  }
  let startPage, endPage;

  if (totalPages <= maxNumbers) {
    startPage = 1;
    endPage = maxNumbers;
  } else if (totalPages == minPages) {
    startPage = 1;
    endPage = minPages;
  } else {
    if (currentPage <= maxNumbers) {
      startPage = 1;
      endPage = maxNumbers;
    } else if (currentPage + 2 >= totalPages) {
      startPage = totalPages - maxNumbers + 1;
      endPage = totalPages;
    } else {
      startPage = currentPage - 2;
      endPage = currentPage;
    }
  }
  // create pagination buttons
  let pagination = "";
  if (currentPage == 1) {
    pagination +=
      "<button class='cmp-search-results__pagination--previous-button' disabled></button>";
  } else {
    pagination +=
      "<button class='cmp-search-results__pagination--previous-button'></button>";
  }
  if (startPage > 1) {
    pagination +=
      '<button class="cmp-search-results__pagination--number-button">1</button>';
    if (startPage > 2) {
      pagination += "<span>...</span>";
    }
  }
  for (let i = startPage; i <= endPage; i++) {
    if (i == currentPage) {
      pagination += `<button class="active cmp-search-results__pagination--number-button">${i}</button>`;
    } else {
      pagination += `<button class="cmp-search-results__pagination--number-button">${i}</button>`;
    }
  }
  if (endPage < totalPages) {
    if (endPage < totalPages - 1) {
      pagination += "<span>...</span>";
    }
    pagination += `<button class="cmp-search-results__pagination--number-button">${totalPages}</button>`;
  }
  if (currentPage == totalPages) {
    pagination +=
      "<button class='cmp-search-results__pagination--next-button' disabled></button>";
  } else {
    pagination +=
      "<button class='cmp-search-results__pagination--next-button'></button>";
  }

  if (currentPage <= totalPages) {
    // display pagination buttons
    document
      .getElementById("cmp-search-results__pagination")
      .classList.add("active");
    document.getElementById("cmp-search-results__pagination").innerHTML =
      pagination;
  }
}

function createArticleTile(data, $resultListContainer) {
  const resultTitle = data.feature.title
      ? data.feature.title
      : data.resource.title,
    resultDescription = data.feature.description
      ? data.feature.description
      : data.resource.description;
  const listItem = document.createElement("li");
  listItem.classList.add("cmp-search-results__list-item");

  const title = document.createElement("h6");
  title.classList.add("cmp-search-results__list-item--title");

  const link = document.createElement("a");
  link.classList.add("cmp-search-results__list-item--link");
  link.href = data.resource.qualified_url ? data.resource.qualified_url : "";
  link.innerHTML = resultTitle ? resultTitle : "";

  const datetime = document.createElement("div");
  datetime.classList.add("cmp-search-results__list-item--datetime");
  datetime.textContent = data.resource.filter_date
    ? data.resource.filter_date
    : "";

  const description = document.createElement("div");
  description.classList.add("cmp-search-results__list-item--description");
  description.innerHTML = resultDescription ? resultDescription : "";

  title.appendChild(link);
  listItem.appendChild(title);
  listItem.appendChild(datetime);
  listItem.appendChild(description);

  $resultListContainer.appendChild(listItem);
  let position = $resultListContainer.querySelectorAll(
    "li.cmp-search-results__list-item"
  ).length;
  link.addEventListener("click", () =>
    ResultTileClickDatalayerEvent(
      link.innerText,
      link.href,
      position,
      listItem,
      "article"
    )
  );
}
function createEventTile(
  data,
  $resultListContainer,
  upcomingEventLabel,
  pastEventLabel
) {
  const resultTitle = data.feature.title
      ? data.feature.title
      : data.resource.title,
    resultDescription = data.feature.description
      ? data.feature.description
      : data.resource.description;
  const eventEndTime = new Date(data.resource.event_end_time);
  const currentTime = new Date();
  const timeDifference = eventEndTime - currentTime;
  const listItem = document.createElement("li");
  listItem.classList.add(
    "cmp-search-results__list-item",
    "cmp-search-results__event-list-item"
  );

  const div1 = document.createElement("div");
  const div2 = document.createElement("div");
  div2.classList.add("cmp-search-results__list-item-event-status");
  if (timeDifference > 0) {
    div2.textContent = upcomingEventLabel ? upcomingEventLabel : "";
  } else {
    div2.textContent = pastEventLabel ? pastEventLabel : "";
  }

  const h6 = document.createElement("h6");
  h6.classList.add("cmp-search-results__list-item--title");

  const a = document.createElement("a");
  a.classList.add("cmp-search-results__list-item--link");
  a.href = data.resource.qualified_url ? data.resource.qualified_url : "";
  a.innerHTML = resultTitle ? resultTitle : "";

  const div3 = document.createElement("div");
  div3.classList.add("cmp-search-results__list-item--datetime");

  const span1 = document.createElement("span");
  span1.textContent = data.resource.event_type ? data.resource.event_type : "";

  const span2 = document.createElement("span");
  span2.textContent =
    (data.resource.event_start_time ? data.resource.event_start_time : "") +
    " - " +
    (data.resource.event_end_time ? data.resource.event_end_time : "");

  const div4 = document.createElement("div");
  div4.classList.add("cmp-search-results__list-item--description");
  div4.innerHTML = resultDescription ? resultDescription : "";

  div1.appendChild(div2);
  h6.appendChild(a);
  div3.appendChild(span1);
  div3.appendChild(span2);
  div1.appendChild(h6);
  div1.appendChild(div3);
  div1.appendChild(div4);

  listItem.appendChild(div1);

  if (data.resource.image_url) {
    const div5 = document.createElement("div");
    div5.classList.add("cmp-search-results__event-list-item--image-container");
    const img = document.createElement("img");
    img.classList.add("cmp-search-results__event-list-item--image");
    img.src = data.resource.image_url + ":cq5dam-web-1120-684?wid=164&hei=123";
    img.alt = data.resource.image_alt_text ? data.resource.image_alt_text : "";
    img.loading = "lazy";

    div5.appendChild(img);
    listItem.appendChild(div5);
  }
  $resultListContainer.appendChild(listItem);
  let position = $resultListContainer.querySelectorAll(
    "li.cmp-search-results__list-item"
  ).length;
  a.addEventListener("click", () =>
    ResultTileClickDatalayerEvent(
      a.innerText,
      a.href,
      position,
      listItem,
      "events"
    )
  );
}

function createContactTile(data, $resultListContainer) {
  const listItem = document.createElement("li");
  listItem.classList.add(
    "cmp-search-results__list-item",
    "cmp-search-results__contact-list-item"
  );
  let position =
    $resultListContainer.querySelectorAll("li.cmp-search-results__list-item")
      .length + 1;
  const div1 = document.createElement("div");

  const link = document.createElement("a");
  link.classList.add("cmp-search-results__list-item--image-link");
  link.href = data.resource.qualified_url;

  const img = document.createElement("img");
  img.src = data.resource.image_url + ":cq5dam-web-320-320?wid=80&hei=80";
  img.classList.add("cmp-search-results__contact-list-item--image");
  img.alt = data.resource.image_alt_text;
  img.loading = "lazy";

  link.appendChild(img);
  div1.appendChild(link);

  const div2 = document.createElement("div");

  const h6 = document.createElement("h6");
  h6.classList.add("cmp-search-results__list-item--title");

  const link2 = document.createElement("a");
  link2.classList.add("cmp-search-results__list-item--link");
  link2.href = data.resource.qualified_url ? data.resource.qualified_url : "";
  link2.innerHTML =
    (data.resource.contact_salutation
      ? data.resource.contact_salutation + "."
      : "") +
    " " +
    (data.resource.contact_first_name ? data.resource.contact_first_name : "") +
    " " +
    (data.resource.contact_last_name ? data.resource.contact_last_name : "");

  h6.appendChild(link2);
  div2.appendChild(h6);

  const div3 = document.createElement("div");
  div3.classList.add("cmp-search-results__list-item--jobtitle");
  if (data.resource.contact_job_title) {
    const span1 = document.createElement("span");
    span1.textContent = data.resource.contact_job_title;
    div3.appendChild(span1);
  }

  if (data.resource.contact_country) {
    const span2 = document.createElement("span");
    span2.textContent = data.resource.contact_country
      ? data.resource.contact_country
      : "";

    div3.appendChild(span2);
  }
  div2.appendChild(div3);

  const ul = document.createElement("ul");
  ul.classList.add("cmp-search-results__list-item--icons-container");

  if (data.resource.contact_email_address) {
    const li1 = document.createElement("li");
    li1.classList.add("cmp-search-results__list-item--icon");

    const link3 = document.createElement("a");
    link3.classList.add("material-icon", "material-icon-email");
    link3.href = "mailto:" + data.resource.contact_email_address;
    link3.setAttribute("aria-label", "Mail");
    link3.textContent = "email";

    //addtional attributes required for People Contact Formn
    let contact = data.resource;
    link3.setAttribute("data-first-name", contact.contact_first_name);
    link3.setAttribute("data-last-name", contact.contact_last_name);
    link3.setAttribute("data-contact-path", contact.qualified_url);
    link3.setAttribute("triggers-dialog-modal", true);

    li1.appendChild(link3);
    ul.appendChild(li1);
    link3.addEventListener("click", () =>
      ResultTileClickDatalayerEvent(
        link2.innerText,
        link3.href,
        position,
        listItem,
        "contact",
        "email"
      )
    );
  }
  if (data.resource.contact_telephone_number) {
    const li2 = document.createElement("li");
    li2.classList.add("cmp-search-results__list-item--icon");

    const link4 = document.createElement("a");
    link4.classList.add("material-icon", "material-icon-phone");
    link4.href = "tel:" + data.resource.contact_telephone_number;
    link4.setAttribute("aria-label", "Call");
    link4.textContent = "call";

    li2.appendChild(link4);
    ul.appendChild(li2);

    link4.addEventListener("click", () =>
      ResultTileClickDatalayerEvent(
        link2.innerText,
        link4.href,
        position,
        listItem,
        "contact",
        "phone"
      )
    );
  }

  const li3 = document.createElement("li");
  li3.classList.add("cmp-search-results__list-item--icon");

  const link5 = document.createElement("a");
  link5.classList.add("icon-linkedin");
  link5.href = "javascript:void(0)";
  link5.setAttribute("aria-label", "Linkedin");

  li3.appendChild(link5);
  ul.appendChild(li3);

  const li4 = document.createElement("li");
  li4.classList.add("cmp-search-results__list-item--icon");

  const link6 = document.createElement("a");
  link6.classList.add("icon-facebook");
  link6.href = "javascript:void(0)";
  link6.setAttribute("aria-label", "Facebook");

  li4.appendChild(link6);
  ul.appendChild(li4);

  div2.appendChild(ul);

  const div4 = document.createElement("div");
  div4.classList.add("cmp-search-results__list-item--description");
  div4.innerHTML = data.resource.contact_bio ? data.resource.contact_bio : "";

  div2.appendChild(div4);

  listItem.appendChild(div1);
  listItem.appendChild(div2);

  link.addEventListener("click", () =>
    ResultTileClickDatalayerEvent(
      link2.innerText,
      link.href,
      position,
      listItem,
      "contact",
      "image"
    )
  );

  link2.addEventListener("click", () =>
    ResultTileClickDatalayerEvent(
      link2.innerText,
      link2.href,
      position,
      listItem,
      "contact",
      "title"
    )
  );

  link5.addEventListener("click", () =>
    ResultTileClickDatalayerEvent(
      link2.innerText,
      link5.href,
      position,
      listItem,
      "contact",
      "linkedin"
    )
  );

  link6.addEventListener("click", () =>
    ResultTileClickDatalayerEvent(
      link2.innerText,
      link6.href,
      position,
      listItem,
      "contact",
      "facebook"
    )
  );

  $resultListContainer.appendChild(listItem);
}

/**
 *
 * @param {string} title title value, contact name for contact tile
 * @param {string} url the url value of clicked link,eg: tel:12312 or http:/domain.com/contact/mr-x.com
 * @param {number} position position of search result tile in current page,3 if clicked on 3rd tile.
 * @param {HTMLLIElement} tileRoot
 * @param { "article" | "events" | "other" | "contact" } tileType
 * @param {"image" | "title" | "email" | "phone" | "linkein" | "facebook"} linkType
 */
function ResultTileClickDatalayerEvent(
  title,
  url,
  position,
  tileRoot,
  tileType = "contact",
  linkType = "title"
) {
  /**@type { import('../datalayer.js').SearchResultClickInfo} */
  let searchInfo = {
    basicFilter: GetSearchInfoForDataLayer("basicFilter"),
    inputTerm: GetSearchInfoForDataLayer("inputTerm"),
    pageCount: GetSearchInfoForDataLayer("pageCount") ?? 1,
    resultPosition: position,
    searchPage: "site search",
    tileType: tileType,
    searchTerm: GetSearchInfoForDataLayer("searchTerm"),
    numResults: GetSearchInfoForDataLayer("numResults"),
    searchCount: GetSearchInfoForDataLayer("searchCount") ?? 1,
    searchMethod: GetSearchInfoForDataLayer("searchMethod") ?? "",
  };

  let parentCmp = tileRoot.parentElement.closest(
    '[id][class^="cmp-"]:not([id=""])'
  );
  let pid = parentCmp.id || "";
  if (tileType == "contact") {
    title = `${linkType}-${title}`;
  }
  HaveSearchResultListEvents(pid, title, url, searchInfo);
}

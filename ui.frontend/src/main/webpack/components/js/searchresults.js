import { InitSearchResultErrorOverlay } from "./common-message-overlay";
import { registerComponent } from "./component";
import {
  isPublisher,
  fetchResults,
  updateQueryParams,
  getQueryParams,
  isMobile,
} from "./util/CommonUtils";

function searchFiltersSlider(config) {
  const $filtersContainer = config.element;
  if ($filtersContainer) {
    //Filters slider
    loadSlider();
    window.addEventListener("resize", loadSlider);
    const $filterButtons = $filtersContainer.querySelectorAll(
      ".cmp-search-results__filters--cta"
    );

    $filterButtons.forEach((button) => {
      button.addEventListener("focus", () => {
        // Calculate the left offset of the focused button relative to the $filtersContainer + 60px padding
        const buttonOffsetLeft = button.offsetLeft + 60;

        // Calculate the right boundary of the visible area within the $filtersContainer
        const visibleAreaRight =
          $filtersContainer.scrollLeft + $filtersContainer.clientWidth;

        // Check if the focused button is partially or fully hidden to the left
        if (buttonOffsetLeft - 60 < $filtersContainer.scrollLeft) {
          // Scroll the $filtersContainer to make the button fully visible on the left
          $filtersContainer.scrollLeft = buttonOffsetLeft - button.clientWidth;
        } else if (buttonOffsetLeft + button.clientWidth > visibleAreaRight) {
          // Scroll the $filtersContainer to make the button fully visible on the right
          $filtersContainer.scrollLeft =
            buttonOffsetLeft +
            button.clientWidth -
            $filtersContainer.clientWidth;
        }
      });
    });
    function loadSlider() {
      const $filtersPrevBtn = document.getElementById(
        config.selectors.filtersPreviousButton
      );
      const $filtersNextBtn = document.getElementById(
        config.selectors.filtersNextButton
      );

      $filtersPrevBtn.addEventListener("click", slidePrev);
      $filtersNextBtn.addEventListener("click", slideNext);

      $filtersContainer.addEventListener("scroll", handleScroll);
      handleScroll();

      function handleScroll() {
        const containerWidth = $filtersContainer.clientWidth;
        const scrollWidth = $filtersContainer.scrollWidth;
        const scrollLeft = $filtersContainer.scrollLeft;

        $filtersPrevBtn.style.display = scrollLeft > 0 ? "block" : "none";
        $filtersNextBtn.style.display =
          scrollWidth > scrollLeft + containerWidth ? "block" : "none";
      }

      function slidePrev() {
        $filtersContainer.scrollBy({
          left: -$filtersContainer.clientWidth,
          behavior: "smooth",
        });
      }

      function slideNext() {
        $filtersContainer.scrollBy({
          left: $filtersContainer.clientWidth,
          behavior: "smooth",
        });
      }
    }
  }
}
function searchResults(config) {
  setTimeout(() => InitSearchResultErrorOverlay(config));
  const searchTerm = getQueryParams().get("q");
  const docType = getQueryParams().get("kpmg_doc_type");
  let pageNumber = getQueryParams().get("page");
  const $element = config.element;
  const $filterButtons = $element.querySelectorAll(
    config.selectors.filterButtons
  );
  const $paginationContainer = document.getElementById(
    config.selectors.paginationContainer
  );
  const $resultListContainer = document.querySelector(
    config.selectors.resultListContainer
  );
  if (pageNumber) {
    pageNumber = parseInt(pageNumber);
  }

  if (searchTerm) {
    if (isMobile()) {
      fetchResults(searchTerm, 1, docType);
      updateQueryParams(searchTerm, 1, docType);
    } else {
      fetchResults(searchTerm, pageNumber, docType);
    }
  }

  $paginationContainer.addEventListener("click", function (event) {
    if (
      event.target.classList.contains(config.selectors.paginationPrevButton)
    ) {
      let searchTerm = getQueryParams().get("q");
      let docType = getQueryParams().get("kpmg_doc_type");
      let pageNumber = getQueryParams().get("page");
      let newPageNumber;
      if (pageNumber && pageNumber > 1) {
        newPageNumber = parseInt(pageNumber) - 1;
      } else {
        newPageNumber = 1;
      }
      updateQueryParams(searchTerm, newPageNumber, docType);
      fetchResults(searchTerm, newPageNumber, docType);
      document
        .querySelector(".cmp-container")
        .scrollIntoView({ behavior: "smooth", block: "start" });
    }
    if (
      event.target.classList.contains(config.selectors.paginationNextButton)
    ) {
      let searchTerm = getQueryParams().get("q");
      let docType = getQueryParams().get("kpmg_doc_type");
      let pageNumber = getQueryParams().get("page");
      let newPageNumber;
      if (pageNumber) {
        newPageNumber = parseInt(pageNumber) + 1;
      } else {
        newPageNumber = 2;
      }
      updateQueryParams(searchTerm, newPageNumber, docType);

      fetchResults(searchTerm, newPageNumber, docType);
      document
        .querySelector(".cmp-container")
        .scrollIntoView({ behavior: "smooth", block: "start" });
    }
    if (
      event.target.classList.contains(config.selectors.paginationNumberButton)
    ) {
      let pageNumber = parseInt(event.target.textContent);
      let searchTerm = getQueryParams().get("q");
      let docType = getQueryParams().get("kpmg_doc_type");
      updateQueryParams(searchTerm, pageNumber, docType);

      fetchResults(searchTerm, pageNumber, docType);
      if (!isMobile()) {
        document
          .querySelector(".cmp-container")
          .scrollIntoView({ behavior: "smooth", block: "start" });
      }
    }
  });

  $filterButtons.forEach((button) => {
    button.addEventListener("click", () => {
      $filterButtons.forEach((btn) => {
        if (btn !== button) {
          btn.classList.remove("selected");
        }
      });
      if (button.dataset.filterType) {
        let pageNumber = 1;
        let searchTerm = getQueryParams().get("q");
        let docType = button.dataset.filterType.toUpperCase();
        $resultListContainer.innerHTML = "";
        updateQueryParams(searchTerm, pageNumber, docType);
        fetchResults(searchTerm, pageNumber, docType);
      }
      button.classList.add("selected");
    });
  });
  if (window.history && window.history.pushState) {
    window.addEventListener("popstate", function () {
      let searchTerm = getQueryParams().get("q");
      let docType = getQueryParams().get("kpmg_doc_type");
      let pageNumber = getQueryParams().get("page");
      fetchResults(searchTerm, pageNumber, docType);
      document.querySelector("#cmp-search-results-input__input-box").value =
        searchTerm;
    });
  }
}
if (isPublisher()) {
  registerComponent(searchResults, {
    selectors: {
      self: ".cmp-search-results",
      countDesktopLabel: ".cmp-search-results__count--desktop-label",
      countMobileLabel: ".cmp-search-results__count--mobile-label span",
      resultListContainer: ".cmp-search-results__list",
      filterButtons: ".cmp-search-results__filters--cta",
      paginationContainer: "cmp-search-results__pagination",
      paginationNextButton: "cmp-search-results__pagination--next-button",
      paginationNumberButton: "cmp-search-results__pagination--number-button",
      paginationPrevButton: "cmp-search-results__pagination--previous-button",
    },
  });
}

registerComponent(searchFiltersSlider, {
  selectors: {
    self: ".cmp-search-results__filters-container",
    filtersNextButton: "cmp-search-results__filters--next-btn",
    filtersPreviousButton: "cmp-search-results__filters--prev-btn",
  },
});

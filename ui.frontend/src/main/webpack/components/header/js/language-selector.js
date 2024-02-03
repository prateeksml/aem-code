import { registerComponent } from "../../js/component";

function LanguageSelector() {
  const languageSelectorToggleBtn = document.querySelector(
    `[data-cmp-langselector='toggle-btn']`
  );
  const languageSelectorBox = document.querySelector(
    `[data-cmp-langselector="side-navbar"]`
  );
  const filterSearchInput = languageSelectorBox.querySelector(
    `[data-cmp-langselector="fliter-search-input"]`
  );
  const languageSelectorBoxCloseBtn = languageSelectorBox.querySelector(
    `[data-cmp-langselector="close-btn"]`
  );
  const filterSearchResetBtn = languageSelectorBox.querySelector(
    `[data-cmp-langselector="fliter-cancel"]`
  );
  const filterSearchMessage = languageSelectorBox.querySelector(
    `[data-cmp-langselector="fliter-message"]`
  );
  const listOfAllCountries = languageSelectorBox.querySelector(
    `[data-cmp-langselector="list-of-all-countries"]`
  );

  // show hide language selector on all click events
  function languageSelectorHandler(e) {
    const currentElm = e.target.getAttribute("data-cmp-langselector");
    if (
      languageSelectorToggleBtn.isSameNode(e.target) ||
      languageSelectorToggleBtn.contains(e.target) ||
      currentElm == "toggle-btn" ||
      e.target.closest(`[data-cmp-langselector="toggle-btn"]`)
    ) {
      e.preventDefault();
      if (languageSelectorBox.classList.contains("active")) {
        closelanguageSelectorBox();
      } else {
        openlanguageSelectorBox();
      }
      return;
    }
    // If a click happens in the language selector box- no action needed.
    if (
      languageSelectorBox.isSameNode(e.target) ||
      languageSelectorBox.contains(e.target) ||
      e.target.closest(`[data-cmp-langselector="side-navbar"]`)
    ) {
      return;
      // Anywhere click- close language selector box.
    } else {
      closelanguageSelectorBox();
    }
  }

  // close language selector on esc key press
  document.addEventListener("keydown", (e) => {
    e = e || window.event;
    if (e.keyCode == 27) {
      if (languageSelectorBox.classList.contains("active")) {
        closelanguageSelectorBox();
      }
    }
  });

  // close the language selector box on mobile on button click.
  languageSelectorBoxCloseBtn.addEventListener("click", () => {
    closelanguageSelectorBox();
  });

  // reset the filter in X button click inside the textbox
  filterSearchResetBtn.addEventListener("click", () => {
    clearFilter();
  });

  // filter countries based on text input value
  filterSearchInput.addEventListener("keyup", () => {
    filterCountries();
  });

  function closelanguageSelectorBox() {
    languageSelectorBox.classList.remove("active");
    languageSelectorToggleBtn.classList.remove("active");
    clearFilter();
  }

  function openlanguageSelectorBox() {
    languageSelectorBox.classList.add("active");
    languageSelectorToggleBtn.classList.add("active");
  }

  function clearFilter() {
    filterSearchResetBtn.classList.remove("show");
    filterSearchMessage.classList.remove("show");
    filterSearchInput.value = "";
    let allCountries = listOfAllCountries.getElementsByTagName("li");
    for (let i = 0; i < allCountries.length; i++) {
      allCountries[i].removeAttribute("style");
    }
  }

  function filterCountries() {
    let filter,
      eachList,
      aLink,
      text,
      liCount = 0;
    filter = filterSearchInput.value.toUpperCase();
    eachList = listOfAllCountries.getElementsByTagName("li");
    filterSearchResetBtn.classList.add("show");
    // start filtering on 3 character or backspace entry
    if (event.keyCode == 8 || filter.length >= 3) {
      for (let i = 0; i < eachList.length; i++) {
        aLink = eachList[i].getElementsByTagName("a")[0];
        text = aLink.getAttribute("title");
        if (text.toUpperCase().indexOf(filter) > -1) {
          eachList[i].style.display = "";
          liCount++;
        } else {
          eachList[i].style.display = "none";
        }
      }
      if (liCount <= 0) {
        filterSearchMessage.classList.add("show");
      } else {
        filterSearchMessage.classList.remove("show");
      }
    }
    if (filter.length <= 0) {
      clearFilter();
    }
  }
  //TODO clean up this file and re-instate this handler
  //document.addEventListener("click", languageSelectorHandler);
}

registerComponent(LanguageSelector, {
  selectors: {
    self: ".language-selector__side-navbar",
    toggleButton: ".language-selector__toggle-btn",
  },
});

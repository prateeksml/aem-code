import { registerComponent } from "./../../js/component";

import {
  docRoot,
  breakpoint,
  getViewportWidth,
  query,
} from "../../js/util/kpmg/kpmg-utils";

import "./search/search";
import "./language-selector";

class Header {
  constructor(config) {
    //Create a central object for calling UI elements
    this.UI = {
      main: config.element,
      selectors: config.selectors,
    };

    //Get desktop-medium breakpoint
    this.desktopMediumBreakpoint = breakpoint["desktop-medium"];

    //Create variables that will change over time
    this.selectedSubMenu;
    this.selectedSubMenuBack;
    this.isMainNavOpen = false;

    this.viewportWidth = getViewportWidth();

    //Used to determine is user is on desktop medium viewport
    //TODO: Move this function into global utils as it can be reused elsewhere
    this.isDesktopMedium = () =>
      this.viewportWidth >= this.desktopMediumBreakpoint ? true : false;

    this.toggle = () => {
      //Toggles Main Nav to hide/show
      this.isMainNavOpen ? this.closeMainNav() : this.openMainNav();
    };

    //Get main nav
    query(this.UI.selectors.nav).then((nav) => (this.UI["nav"] = nav));

    //Get mobile header
    query(this.UI.selectors.mobileHeader).then(
      (mobileHeader) => (this.UI["mobileHeader"] = mobileHeader)
    );

    //Get mainNavListItems
    query(this.UI.selectors.mainNavListItems).then((mainNavListItems) => {
      this.UI["mainNavListItems"] = mainNavListItems;
      this.UI.mainNavListItems.forEach((mainNavLink) => {
        mainNavLink.addEventListener(
          "click",
          (evt) => {
            this.onMenuLinkClick(evt, this);
          },
          true
        );
      });
    });

    //Get mobile toggle
    query(this.UI.selectors.mobileToggle).then((mobileToggle) => {
      this.UI["mobileMenuToggle"] = mobileToggle;
      //Attach click event to toggle
      this.UI.mobileMenuToggle.addEventListener("click", this.toggle, true);
    });

    //Get mobileMenuItemBar
    query(this.UI.selectors.mobileMenuItemBar).then(
      (mobileMenuItemBar) => (this.UI["mobileMenuItemBar"] = mobileMenuItemBar)
    );

    //Get main menu close button
    query(this.UI.selectors.mobileMenuCloseButton).then(
      (mobileMenuCloseButton) => {
        this.UI["mobileMenuCloseButton"] = mobileMenuCloseButton;
        this.UI.mobileMenuCloseButton.addEventListener(
          "click",
          this.toggle,
          true
        );
      }
    );

    //If viewport size changes, recalculate viewportWidth
    window.addEventListener("resize", () => {
      this.viewportWidth = getViewportWidth();
    });
  }

  openMainNav() {
    console.log("Header: openMainNav: ", this);
    try {
      this.UI.nav.classList.add("cmp-main-nav--open");
      this.UI.mobileMenuItemBar.classList.add("cmp-menu-item-bar--open");
      docRoot.classList.add("no-scroll");
      this.isMainNavOpen = true;
    } catch (err) {
      console.error("Header: openMainNav: error: ", err);
    }
  }

  closeMainNav() {
    this.UI.nav.classList.remove("cmp-main-nav--open");
    this.UI.mobileMenuItemBar.classList.remove("cmp-menu-item-bar--open");
    docRoot.classList.remove("no-scroll");

    if (this.selectedSubMenu) {
      //Close submenu if open
      this.hideSubMenu();
    }
    this.isMainNavOpen = false;
  }

  onMenuLinkClick(evt) {
    //For desktop medium close previous selected sub menu if active
    if (this.isDesktopMedium() && this.selectedSubMenu) {
      this.hideSubMenu();
    }

    //Get newly selected sub menu to set to active
    const selectedLink = evt.currentTarget;
    this.selectedSubMenu = selectedLink.querySelector(
      this.UI.selectors.subMenu
    );

    //Check if link clicked has a
    //sub menu associated with it
    if (this.selectedSubMenu) {
      //This is in case submenu  link is accidentally
      //set to an anchor with href
      evt.preventDefault();

      //Target back button in selected sub menu
      this.selectedSubMenuBack = this.selectedSubMenu.querySelector(
        this.UI.selectors.subMenuBack
      );
      this.showSubMenu();
    }
  }

  showSubMenu() {
    //Add active class to parent class of selected sub menu
    this.selectedSubMenu.classList.add("cmp-sub-menu--active");
    //Add click listener to selected sub menu back button
    this.selectedSubMenuBack.addEventListener(
      "click",
      (evt) => {
        this.hideSubMenu(evt);
      },
      true
    );
  }

  hideSubMenu() {
    const selectedSubMenuClassList = this.selectedSubMenu.classList;
    // //Remove active class to parent class of selected sub menu
    selectedSubMenuClassList.remove("cmp-sub-menu--active");
    //Remove click listener to selected sub menu back button
    this.selectedSubMenuBack.removeEventListener(
      "click",
      this.hideSubMenu,
      true
    );

    //Check if breakpoint is below desktop-medium
    if (!this.isDesktopMedium()) {
      //Apply slide out animation on selected sub menu
      selectedSubMenuClassList.add("cmp-sub-menu--deactivate");
      //Remove deactivate class after 750ms or 0.75s
      setTimeout(() => {
        selectedSubMenuClassList.remove("cmp-sub-menu--deactivate");
      }, 750);
    }
  }
}

//Register component and selectors, note that we are targeting
//data attributes to avoid breakage if a css class name is changed later
registerComponent(Header, {
  selectors: {
    self: "[data-cmp-header-main-nav-bar]",
    nav: "[data-cmp-main-nav]",
    mainNavListItems: "[data-cmp-main-nav-list-item]",
    mobileToggle: "[data-cmp-mobile-toggle]",
    mobileHeader: "[data-cmp-main-nav-mobile-header]",
    mobileMenuCloseButton: "[data-cmp-main-nav-mobile-header-close]",
    subMenu: "[data-cmp-sub-menu]",
    subMenuBack: "[data-cmp-sub-menu-back]",
    mobileMenuItemBar: "[data-cmp-menu-item-bar-mobile='true']",
  },
});

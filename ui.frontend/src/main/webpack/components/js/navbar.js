import { registerComponent } from "./component";

function NavBar() {
  console.log("DEPLOY TO QA SUCCESSFUL: SEPT 19, 2023");

  const menu = document.querySelector(`[data-cmp-navbar="cmp-navbar"]`);
  const topHeaderBar = document.querySelector(
    `[data-cmp-header="header-top-banner"]`
  );
  const mobileHeader = menu.querySelector(
    `[data-cmp-navbar="cmp-navbar__mobile-head"]`
  );
  const backToMenu = menu.querySelector(`[data-cmp-navbar="back"]`);
  const menuTrigger = document.querySelector(
    `[data-cmp-navbar="cmp-navbar-mobile-toggler"]`
  );
  const closeMenu = menu.querySelector(`[data-cmp-navbar="close"]`);
  const allSubMenus = menu.querySelectorAll(`[data-cmp-navbar="sub-menu"]`);
  const allHasChildren = menu.querySelectorAll(
    `[data-cmp-navbar="has-children"]`
  );

  const subMenus = menu.querySelectorAll(".cmp-navbar__main-menu .sub-menu ");
  let subMenu;

  function menuLinkClickHandler(evt) {
    // on Large screens only- where menu links are visible
    if (!menu.classList.contains("active")) {
      // Toggle the Sub Menu when clicked on the Main Menu Links.
      if (evt.target.hasAttribute("data-cmp-navbar-link")) {
        const hasChildren = evt.target.parentNode.hasAttribute(
          "data-cmp-navbar-has-children"
        );
        if (hasChildren && evt.target.parentNode.classList.contains("active")) {
          evt.target.parentNode.classList.remove("active");
        } else {
          closeSubMenu();
          evt.target.parentNode.classList.add("active");
        }
        return;
      }
      // If a click happens somewhere outside the Main Menu Links/ Sub Menu, close sub menu.
      else if (!evt.target.hasAttribute("data-cmp-navbar-sub-menu")) {
        closeSubMenu();
      }
      return;
    } else {
      // Small Screen Only- where menu links comes under hamburger
      if (evt.target.closest(`[data-cmp-navbar="has-children"]`)) {
        const child = evt.target.closest(`[data-cmp-navbar="has-children"]`);
        showSubMenu(child);
      }
    }
  }
  // close sub-menu on esc key press
  document.onkeydown = function (e) {
    e = e || window.event;
    if (e.keyCode == 27) {
      closeSubMenu();
    }
  };

  //scroll event to hide and show back button
  subMenus.forEach((elem) => {
    elem.addEventListener("scroll", function () {
      let scroll = this.scrollTop;
      if (scroll <= 10) {
        backToMenu.classList.add("active");
        mobileHeader.classList.add("active");
      } else {
        backToMenu.classList.remove("active");
        mobileHeader.classList.remove("active");
      }
    });
  });

  // mobile view close menu X
  closeMenu.addEventListener("click", () => {
    toggleMenu();
  });

  // mobile back to main menu
  backToMenu.addEventListener("click", () => {
    hideSubMenu();
  });

  // mobile hamburger menu toggle
  menuTrigger.addEventListener("click", () => {
    toggleMenu();
  });

  function toggleMenu() {
    backToMenu.classList.remove("active");
    mobileHeader.classList.remove("active");
    menu.classList.toggle("active");
    document.documentElement.classList.toggle("no-scroll");
  }
  function cloneTopHeaderMobile() {
    const cloneTopHeader = topHeaderBar.cloneNode(true);
    menu.appendChild(cloneTopHeader);
  }

  function closeSubMenu() {
    allSubMenus.forEach((s) => {
      s.classList.remove("active");
      s.removeAttribute("style");
    });
    allHasChildren.forEach((c) => {
      c.classList.remove("active");
    });
  }

  // mobile animated show hide sub menu
  function showSubMenu(hasChildren) {
    try {
      subMenu = hasChildren.querySelector(`[data-cmp-navbar="sub-menu"]`);
      subMenu.classList.add("active");
      subMenu.style.animation = "slideLeft 0.5s ease forwards";
      mobileHeader.classList.add("active");
    } catch (error) {}
  }

  // mobile animated show hide sub menu
  function hideSubMenu() {
    try {
      subMenu.style.animation = "slideRight 0.5s ease forwards";
      setTimeout(() => {
        subMenu.classList.remove("active");
      }, 300);
      mobileHeader.classList.remove("active");
    } catch (error) {}
  }

  window.onresize = function () {
    hideSubMenu();
    closeSubMenu();
    menu.classList.remove("active");
    document.documentElement.classList.remove("no-scroll");
    if (this.innerWidth > 991 && menu.classList.contains("active")) {
      toggleMenu();
    }
  };

  window.addEventListener("click", menuLinkClickHandler);

  window.onload = () => {
    cloneTopHeaderMobile();
  };
}

registerComponent(NavBar, {
  selectors: {
    self: ".cmp-header-bottom-banner__nav",
  },
});

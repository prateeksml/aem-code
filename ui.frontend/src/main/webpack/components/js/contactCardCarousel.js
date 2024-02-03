import { registerComponent } from "./component";

const CAROUSEL_SELECTOR = ".contact-carousel";

function isCarouselItem($element) {
  return $element.classList.contains("cmp-carousel__item");
}

function isActiveCarouselItem($element) {
  return $element.classList.contains("cmp-carousel__item--active");
}

function setControlsMargin($element) {
  const viewportWidth = window.innerWidth;
  const controls = $element.querySelector(".cmp-carousel__action--previous");
  const indicators = $element.querySelectorAll(".cmp-carousel__indicator");
  const indicatorsList = [...indicators].length;
  const controlsMargin =
    viewportWidth < 768 ? `${indicatorsList * 22}` : `${indicatorsList * 24}`;
  controls.style.marginRight = `${controlsMargin}px`;
}

/**
 * Run callback when a carousel item is active. Callback takes active item element.
 * @param {*} carouselEl
 * @param {*} callback
 */
function onCarouselItemActive(carouselEl, callback) {
  const MutationObserver =
    window.MutationObserver ||
    window.WebKitMutationObserver ||
    window.MozMutationObserver;
  const observer = new MutationObserver(function (mutations) {
    mutations.forEach(function (mutation) {
      const elementChanged = mutation.target;
      if (isActiveCarouselItem(elementChanged)) {
        callback(elementChanged);
      }
    });
  });
  // start observing
  observer.observe(carouselEl, {
    subtree: true,
    childList: true,
    characterData: true,
    attributes: true,
    attributeFilter: ["class"],
  });
}

function CardCarousel(config) {
  const $element = config.element;
  setControlsMargin($element);
  onCarouselItemActive($element, function ($carouselItem) {
    const allItems = [...$carouselItem.parentElement.children];
    const activeItemIndex = allItems.indexOf($carouselItem);
    allItems.forEach((item) => {
      if (isCarouselItem(item)) {
        item.style.transform = `translateX(-${activeItemIndex * 100}%)`;
      }
    });
  });
}

registerComponent(CardCarousel, {
  selectors: {
    self: CAROUSEL_SELECTOR,
  },
});

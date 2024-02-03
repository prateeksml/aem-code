import { registerComponent } from "./component";

const STANDARD_TEASER_CAROUSEL_CLASS = "standardteaser-carousel";
const CMP_CAROUSEL_SELECTOR = ".cmp-carousel";
const INDICATORS_SELECTOR = ".cmp-carousel__indicators";
const PREVIOUS_ACTION_SELECTOR = ".cmp-carousel__action--previous";

function moveIndicatorsBetweenNavigationButtons($carousel) {
  if ($carousel) {
    const $indicators = $carousel.querySelector(INDICATORS_SELECTOR);
    const $previousAction = $carousel.querySelector(PREVIOUS_ACTION_SELECTOR);
    if ($previousAction && $indicators) {
      $previousAction.after($indicators); // move indicators after the previous button
    }
  }
}

function StandardTeaserCarousel(config) {
  const $carousel = config.element;
  //ensure it is standard teaser carousel
  if (
    $carousel?.parentElement?.classList.contains(STANDARD_TEASER_CAROUSEL_CLASS)
  ) {
    moveIndicatorsBetweenNavigationButtons($carousel);
  }
}

registerComponent(StandardTeaserCarousel, {
  selectors: {
    self: CMP_CAROUSEL_SELECTOR,
  },
});

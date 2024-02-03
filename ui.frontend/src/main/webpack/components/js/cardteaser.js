import { registerComponent } from "./component";
import {
  pushDatalayerHideEvent,
  pushDatalayerShowEvent,
} from "./util/DatalaterUtil";

function CardTeaser(config) {
  // in this example, clicking a teaser logs it.
  const $element = config.element;
  const $datalayerParent = $element.closest("[data-cmp-data-layer]");
  const $learnMore = $element.querySelector(config.selectors.learnMore);
  const $close = $element.querySelector(config.selectors.close);
  if (!$learnMore) {
    return;
  }

  function flip(e) {
    e.preventDefault();
    let timeout = null;
    // flip front
    $element.classList.toggle("cmp-teaser__content--flipped");
    // back card flip
    const cardDescription = $element.querySelector(".cmp-teaser__description");
    cardDescription.classList.toggle("backcard-flip");

    // to manage the opacity styles on back flipping to front
    if (timeout) {
      clearTimeout(timeout);
      timeout = null;
    }
  }
  $learnMore.addEventListener("click", (e) => {
    flip(e);
    pushDatalayerShowEvent($datalayerParent);
  });
  $close.addEventListener("click", (e) => {
    flip(e);
    pushDatalayerHideEvent($datalayerParent);
  });
}

registerComponent(CardTeaser, {
  selectors: {
    self: ".cardteaser .cmp-teaser__content",
    learnMore: ".cmp-teaser__action-flip",
    close: ".cmp-teaser__button-close",
  },
});

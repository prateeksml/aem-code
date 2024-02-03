import { registerPageResizeHandler } from "./util/PageResizeUtil";
import { registerComponent } from "./component";
import { getComputedCSSValue } from "./util/StyleUtil";

const UTILS_BLEED_RIGHT = "utils-bleed-right";
// const UTILS_BLEED_LEFT = "utils-bleed-left";

const isRightBleed = (el) => el.classList.contains(UTILS_BLEED_RIGHT);

// handles horizontal bleed for the registered components
function HorizontalBleed(config) {
  const container = config.element;
  const content = container.querySelector(config.selectors.content);

  const resizeBleed = () => {
    const containerBoundingRect = container.getBoundingClientRect();
    const contentBoundingRect = content.getBoundingClientRect();

    const position = isRightBleed(container) ? "right" : "left";

    // left/right side difference bewteen container and content elements
    let difference =
      contentBoundingRect[position] - containerBoundingRect[position];

    const padding = getComputedCSSValue(content, `padding-${position}`);
    const finalBleedWidth = Math.abs(difference) + parseInt(padding, 10);
    // set the css variable used by the css for bleed width
    container.style.setProperty(
      "--horizontal-bleed-width",
      `${finalBleedWidth}px`
    );
  };

  resizeBleed(); // do it first time
  registerPageResizeHandler(resizeBleed); // do it on resize
}

// Register for Section Container
registerComponent(HorizontalBleed, {
  selectors: {
    self: ".section.container",
    content: ".section.container > .cmp-container",
  },
});

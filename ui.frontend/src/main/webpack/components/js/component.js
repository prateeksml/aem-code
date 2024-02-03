import { onDocumentReady } from "./util/DocumentUtil";

/**
 * Create a component instance for the provided config and element
 * @param {Function} ComponentFunction
 * @param {Object} config
 * @param {HTMLElement} element
 */
function createComponentInstance(ComponentFunction, config, element) {
  // alread initialized
  if (element.dataset.componentInitialized) {
    console.debug(
      `refusing to init component with selector ${config.self} that is already initialized`
    );
    return;
  }
  // already attempted to initialize and errored
  if (element.dataset.componentError) {
    console.debug(
      `refusing to init component with selector ${config.self} that has attempted to initialize and errored. See logs.`
    );
    return;
  }

  try {
    // init component
    new ComponentFunction(Object.assign(config, { element: element }));
    element.dataset.componentInitialize = true; // initialized
  } catch (e) {
    console.error("error while initializing component: ", e);
    element.dataset.componentError = true;
  }
}

/**
 * el.querySelectorAll(selector) does not include el itself. hence this helper.
 * @param {HTMLElement} el
 * @param {string} selector
 */
function querySelectorAllIncludeSelf(el, selector) {
  if (el.querySelectorAll && selector) {
    // make sure it's an element and selector not empty
    return [el, ...el.querySelectorAll(selector)].filter((el) =>
      el.matches(selector)
    );
  }
}

/**
 * Component Registry
 */
const ComponentRegistry = {};

function setupMutationObserver() {
  let MutationObserver =
    window.MutationObserver ||
    window.WebKitMutationObserver ||
    window.MozMutationObserver;
  let observer = new MutationObserver(function (mutations) {
    mutations.forEach(function (mutation) {
      // needed for IE
      let nodesArray = [].slice.call(mutation.addedNodes);
      if (nodesArray.length > 0) {
        nodesArray
          .filter((node) => !!node.querySelectorAll)
          .forEach(function (addedNode) {
            // init all components from the registry
            Object.keys(ComponentRegistry).forEach((selector) => {
              const { ComponentFunction, config } = ComponentRegistry[selector];

              let elementsArray = querySelectorAllIncludeSelf(
                addedNode,
                selector
              );
              elementsArray.forEach(function (element) {
                createComponentInstance(ComponentFunction, config, element);
              });
            });
          });
      }
    });
  });
  // start observing
  observer.observe(document.body, {
    subtree: true,
    childList: true,
    characterData: true,
  });
}
// setup one mutation observer for components.
setupMutationObserver();

export function registerComponent(ComponentFunction, config) {
  onDocumentReady(function () {
    const selfSelector = config?.selectors?.self;
    // ensure config has self selector
    if (!selfSelector) {
      console.error(
        "config.selectors.self not provided, Component will not initialize"
      );
    }

    // find all elements with the self selector, init them with the ComponentFunction.
    let elements = document.querySelectorAll(selfSelector);
    for (let i = 0; i < elements.length; i++) {
      createComponentInstance(ComponentFunction, config, elements[i]);
    }

    if (ComponentRegistry[selfSelector]) {
      console.warn(
        `Component with selector ${selfSelector} already registered.`
      );
    } else {
      ComponentRegistry[selfSelector] = {
        ComponentFunction,
        config,
      };
    }
  });
}

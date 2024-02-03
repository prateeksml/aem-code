/**
 * Page Resize Observer
 * Provide an API for listening to page resize event.
 */

const pageResizeObserver = new ResizeObserver(() => {
  if (!window.kpmgResizeHandlers) {
    return; // exit, no handlers
  }
  // call all handlers
  window.kpmgResizeHandlers.forEach((handler) => {
    handler();
  });
});

// observe immediately
pageResizeObserver.observe(document.body, { box: "border-box" });

/**
 * Register page resize handler
 * @param {Function} handler
 * @returns {void} nothing
 */
export const registerPageResizeHandler = (handler) => {
  if (!window.kpmgResizeHandlers) {
    window.kpmgResizeHandlers = [];
  }
  window.kpmgResizeHandlers.push(handler);
};

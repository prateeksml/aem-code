/**
 * Rns cb when document is ready
 * @param {Function} cb
 */
export function onDocumentReady(cb) {
  if (document.readyState !== "loading") {
    cb();
  } else {
    document.addEventListener("DOMContentLoaded", cb);
  }
}

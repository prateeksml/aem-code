export const getComputedCSSValue = (el, cssProperty) =>
  window.getComputedStyle(el, null).getPropertyValue(cssProperty);

(function(){
  function isHidden(el) {
    return !(el.offsetWidth || el.offsetHeight || el.getClientRects().length);
  }
  $(window).adaptTo("foundation-registry").register("foundation.validation.validator", {
    selector: "[data-foundation-validation^='multifield-max']",
    validate: function(el) {
      if (isHidden(el)) {
        return; // no need to validate hidden fields.
      }
      var validationName = el.getAttribute("data-validation")
      var max = validationName.replace("multifield-max-", "");
      max = parseInt(max);
      if (el.items.length > max){
          return "Max allowed list item is "+ max
      }
    }
  });
})();


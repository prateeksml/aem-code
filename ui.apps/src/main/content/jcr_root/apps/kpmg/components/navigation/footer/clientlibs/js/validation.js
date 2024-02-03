/* global jQuery, Coral */
(function ($, Coral) {
  "use strict";
  var registry = $(window).adaptTo("foundation-registry");

  // Validator for required for multifield max and min items
  registry.register("foundation.validation.validator", {
    selector: "[data-validation=coral-multifield]",
    validate: function (element) {
      var el = $(element);
      let max =parseInt( el.data("max-items"));
      let min =parseInt( el.data("min-items"));
      let items = el.children("coral-multifield-item").length;

        let categoryName=el.data("category");
       let categoryValue= $("input[name='./items/others/title']").val();

        if(categoryName=="others" && categoryValue!=""){
            min=1;
        }

      if (items > max) {
        return `Maximum ${max} items allowed`;
      }
      if (items < min) {
        return `Minimum ${min} items is required.`;
      }

    },
  });
})(jQuery, Coral);
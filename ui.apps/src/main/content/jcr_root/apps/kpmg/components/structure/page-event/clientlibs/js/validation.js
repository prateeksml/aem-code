/* global jQuery, Coral */
(function ($, Coral) {
  "use strict";
  var registry = $(window).adaptTo("foundation-registry");

  registry.register("foundation.validation.validator", {
    selector: "[data-validation=coral-date-validation]",
    validate: function (e) {
       var foundationUI = $(window).adaptTo('foundation-ui');
        var eventStartDate = $('[name="./eventStartTimeAndDate"]').val();
        var eventEndDate = $('[name="./eventEndTimeAndDate"]').val();
        if((eventStartDate!= '') && (eventEndDate!= '')){
            if((eventEndDate < eventStartDate) || (eventEndDate == eventStartDate)){
                return "Start date greater than end date";           
            }
        }
    },
  });
})(jQuery, Coral);
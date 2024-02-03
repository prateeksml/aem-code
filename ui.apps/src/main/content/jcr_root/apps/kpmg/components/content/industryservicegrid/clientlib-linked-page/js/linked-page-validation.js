/* global jQuery, Coral */
(function ($, Coral) {
  "use strict";
  var registry = $(window).adaptTo("foundation-registry");

  function getPagePath() {
    if (Granite && Granite.author && Granite.author.page && Granite.author.page.path){
      return Granite.author.page.path;
    } else {
      // when dialog is open in full screen (smaller devices)
      var expectedPathStart = '/content/kpmgpublic';
      var expectedPathEnd = 'jcr:content';
      var pathBetween = location.href.match(expectedPathStart + "(.*?)" + expectedPathEnd)[1];
      return expectedPathStart + pathBetween;
    }
  }

  // Validator to check whether input path in the multifield
  //pathfield is child page of current page or not
  registry.register("foundation.validation.validator", {
    selector: "[data-kpmg-picker-strategy='child-pages']",
    validate: function (element) {
      var el = $(element);
      var authoredPath = $(el).find("coral-taglist").find("coral-tag").attr("value");
      var $pagePath = getPagePath();
      if ((authoredPath == "") || (authoredPath != "" && !authoredPath.startsWith($pagePath + '/') && (authoredPath != null))) {
           return `Entered path is not a child of current page ${$pagePath}`;;
      }
      return null;
    },
  });
})(jQuery, Coral);
(function(window, document, $) {
    "use strict";
    $(document).on("dialog-loaded", function(e) {
        var pageTitle =$(document).attr('title');
        var fieldSelectorSiteTitle = $('.heroTitle');
        updateTitle(fieldSelectorSiteTitle, pageTitle);
    });

    function updateTitle(fieldSelector, value) {
        if (value) {
            var title=$(document).find("[name='./heroTitle']");
            if(title.val()==''){
    			 title.val(value);
            }
        }
    }


})(window, document, Granite.$);
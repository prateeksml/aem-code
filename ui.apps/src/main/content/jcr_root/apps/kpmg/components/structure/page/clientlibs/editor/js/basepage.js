(function(window, document, $) {
    "use strict";
    $(document).on("dialog-loaded", function(e) {
        var pageTitle = $(document).find("[name='./jcr:title']").val();
        var pageFeatureImagepath = $(document).find("[name='./cq:featuredimage/fileReference']").val();
        var imgDescRequiredCheckbox = document.querySelector('[name="./cq:featuredimage/imgDescRequired"]');
        var url = window.location.href;
        var searchParams = new URLSearchParams(url.substring(url.indexOf('?')));
        var pagePath = searchParams.get('item');
        var metaOgImageValue= $(document).find("[name='./metaOgImage']").val()
        var pageMetadataPath = pagePath + ".2.json";
        $.getJSON(pageMetadataPath)
            .done(
                function(metadata) {
                    var isChecked= metadata['jcr:content']['cq:featuredimage']['imgDescRequired'];
                    if (isChecked == "undefined" || isChecked == null) {
                        $(imgDescRequiredCheckbox).attr('checked', true);
                    }
                });

        var fieldSelectorSiteName = $('.ogsitename');
        var fieldSelectorSiteType = $('.ogsitetype');
        var fieldSelectorSiteTitle = $('.ogsitetitle');
        var fieldSelectorSiteImage = $('.ogsiteimage');
        changeTextFieldState(fieldSelectorSiteName, "KPMG");
        changeTextFieldState(fieldSelectorSiteType, "website");
        updateTitle(fieldSelectorSiteTitle, pageTitle);
		 if(metaOgImageValue==''){
            updateImage(fieldSelectorSiteImage, pageFeatureImagepath);
        }
    });

    function changeTextFieldState(fieldSelector, value) {
        var field = $(fieldSelector);
        field.attr('readonly', "true");
        var textfield = fieldSelector.adaptTo("foundation-field");
        textfield.setValue(value);
    }

    function updateTitle(fieldSelector, value) {
        if (value) {
            var field = $(fieldSelector);
            var textfield = fieldSelector.adaptTo("foundation-field");
            if(textfield.getValue()=='') {
                textfield.setValue(value);
            }
        }
    }

    function updateImage(fieldSelector, value) {
        if (value) {
            var field = $(fieldSelector);
            var textfield = fieldSelector.adaptTo("foundation-field");
            textfield.setValue(value);
        }
    }

})(window, document, Granite.$);

$(document).on('foundation-field-change', '.featuredimage',function(e) {
var value=$(document).find("[name='./cq:featuredimage/fileReference']").val(); 
      var metaOgImage=$(document).find("[name='./metaOgImage']");
      metaOgImage.val(value);

   });
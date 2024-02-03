(function (window, $) {
    'use strict';
  
    var RichTextMaxLengthValidation= function () {
        var CONST = {
            TARGET_GRANITE_UI: '.coral-RichText-editable',
            ERROR_MESSAGE: 'Your text length is {0} but character limit is {1}!',
        };
 
        function init() {
            // register the validator which includes the validate algorithm
            $(window).adaptTo('foundation-registry').register('foundation.validation.validator', {
                selector: CONST.TARGET_GRANITE_UI,
                validate: function (el) {
                    var $rteField = $(el);
                    var $field = $rteField.closest('.richtext-container').find('input.rich-custom');
                    var maxLength = $field.data('maxlength');
                    var textLength = $rteField.text().trim().length;
                    if (maxLength && textLength > maxLength) {
                        return Granite.I18n.get(CONST.ERROR_MESSAGE, [textLength, maxLength]);
                    }
                    return null;
                }
            });
            // execute Jquery Validation onKeyUp
            $(document).on('keyup', CONST.TARGET_GRANITE_UI, function (e) {
                executeJqueryValidation($(this));
            });
        }
 
        function executeJqueryValidation(el) {
            var validationApi = el.adaptTo('foundation-validation');
            if (validationApi) {
                validationApi.checkValidity();
                validationApi.updateUI();
            }
        }
       return {
            init: init
        }
    }();
    RichTextMaxLengthValidation.init();
})(window, Granite.$);
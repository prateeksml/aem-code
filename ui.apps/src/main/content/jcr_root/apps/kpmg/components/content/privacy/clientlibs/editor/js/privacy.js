(function ($, channel, Coral) {
  "use strict";

  var EDIT_DIALOG = ".cmp-form-privacy__editDialog";
  var CHECKBOX_REQUIRED = ".cmp-form-privacy__required";
  const REQ_MSG_WRAPPER = '.cmp-form-privacy__requiredmessage';
  var REQUIREDMESSAGE_FIELD = ".cmp-form-privacy__requiredmessage_field";
  /**
   * Toggles the display of the given element based on the actual and the expected values.
   * If the actualValue is equal to the expectedValue , then the element is shown,
   * otherwise the element is hidden.
   *
   * @param {HTMLElement} element The html element to show/hide.
   * @param {*} expectedValue The value to test against.
   * @param {*} actualValue The value to test.
   */
  function checkAndDisplay(element, expectedValue, actualValue) {
    if (expectedValue === actualValue) {
      element.show();
    } else {
      element.hide();
    }
  }

  function CheckAndMakeRequired(fieldClass, v) {
    $(fieldClass).prop('required', !!v);
  }

  /**
  * Toggles the visibility of the required message input field based on the "required" input field.
  * If the "required" field is set, the required message field is shown,
  * otherwise it is hidden.
  *
  * @param {HTMLElement} dialog The dialog on which the operation is to be performed.
  */
  function handleRequiredMessage(dialog) {
    var component = dialog.find(CHECKBOX_REQUIRED)[0];
    var requiredMessage = dialog.find(REQ_MSG_WRAPPER);
    
    CheckAndMakeRequired(REQUIREDMESSAGE_FIELD, component.checked);
    checkAndDisplay(requiredMessage, true, component.checked);
    
    component.on("change", function () {
      checkAndDisplay(requiredMessage, true, component.checked);
      CheckAndMakeRequired(REQUIREDMESSAGE_FIELD, component.checked);
    });
  }


  /**
  * Initialise the conditional display of the various elements of the dialog.
  *
  * @param {HTMLElement} dialog The dialog on which the operation is to be performed.
  */
  function initialise(dialog) {
    dialog = $(dialog);
    handleRequiredMessage(dialog);
  }

  channel.on("foundation-contentloaded", function (e) {
    if ($(e.target).find(EDIT_DIALOG).length > 0) {
      Coral.commons.ready(e.target, function (component) {
        initialise(component);
      });
    }
  });

})(jQuery, jQuery(document), Coral);
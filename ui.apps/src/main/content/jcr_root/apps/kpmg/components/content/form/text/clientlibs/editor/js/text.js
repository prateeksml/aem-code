(function ($, channel, Coral) {
  "use strict";

  var EDIT_DIALOG = ".cmp-form-text__editDialog";
  var TEXTFIELD_TYPES = ".cmp-form-text__types";
  var TEXTFIELD_ROWS = ".cmp-form-text__rows";
  var TEXTFIELD_REQUIRED = ".cmp-form-text__required";
  var TEXTFIELD_CONSTRAINTMESSAGE = ".cmp-form-text__constraintmessage";
  var TEXTFIELD_CONSTRAINTMESSAGE_FIELD = ".cmp-form-text__constraintmessage_field";
  var TEXTFIELD_REQUIREDMESSAGE = ".cmp-form-text__requiredmessage";
  var TEXTFIELD_REQUIREDMESSAGE_FIELD = ".cmp-form-text__requiredmessage_field";
  var TEXTFIELD_READONLY = ".cmp-form-text__readonly";
  var TEXTFIELD_READONLYSELECTED_ALERT = ".cmp-form-text__readonlyselected-alert";
  var TEXTFIELD_REQUIREDSELECTED_ALERT = ".cmp-form-text__requiredselected-alert";
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
  * Toggles the visibility of the Text field number of rows input field based on the type of the text field.
  * If the type is textarea, the number of rows field is shown, otherwise it is hidden.
  *
  * @param {HTMLElement} dialog The dialog on which the operation is to be performed.
  */
  function handleTextarea(dialog) {
    var component = dialog.find(TEXTFIELD_TYPES)[0];
    var textfieldRows = dialog.find(TEXTFIELD_ROWS);
    checkAndDisplay(textfieldRows,
      "textarea",
      component.value);
    component.on("change", function () {
      checkAndDisplay(textfieldRows,
        "textarea",
        component.value);
    });
  }

  function handleConstraintMessage(dialog) {
    var constraintMessage = dialog.find(TEXTFIELD_CONSTRAINTMESSAGE);
    CheckAndMakeRequired(TEXTFIELD_CONSTRAINTMESSAGE_FIELD,true);
    checkAndDisplay(constraintMessage,
      true,
      true);
  }

  /**
  * Toggles the visibility of the required message input field based on the "required" input field.
  * If the "required" field is set, the required message field is shown,
  * otherwise it is hidden.
  *
  * @param {HTMLElement} dialog The dialog on which the operation is to be performed.
  */
  function handleRequiredMessage(dialog) {
    var component = dialog.find(TEXTFIELD_REQUIRED)[0];
    var requiredMessage = dialog.find(TEXTFIELD_REQUIREDMESSAGE);
    
    CheckAndMakeRequired(TEXTFIELD_REQUIREDMESSAGE_FIELD,component.checked);

    checkAndDisplay(requiredMessage,
      true,
      component.checked);
    component.on("change", function () {
      checkAndDisplay(requiredMessage,
        true,
        component.checked);
        CheckAndMakeRequired(TEXTFIELD_REQUIREDMESSAGE_FIELD,component.checked);
    });
  }

  /**
  * Handles the exclusion between the two checkbox components.
  * Specifically, out of the two components, only one can be in checked state at a time.
  * If component1 is "checked" and the component2 is also in checked state, the component2 is unchecked,
  * and the alert is displayed.
  *
  * @param {HTMLElement} component1 The component which on being "checked" should uncheck(if in checked state) the component2.
  * @param {HTMLElement} component2 The component which should not be in checked state along with component1.
  * @param {HTMLElement} alert The alert to show if both the component2 is in checked state when the component1 is being "checked".
  */
  function handleExclusion(component1, component2, alert) {
    component1.on("change", function () {
      if (this.checked && component2.checked) {
        alert.show();
        component2.set("checked", false, true);
      }
    });
  }

  /**
  * Initialise the conditional display of the various elements of the dialog.
  *
  * @param {HTMLElement} dialog The dialog on which the operation is to be performed.
  */
  function initialise(dialog) {
    dialog = $(dialog);
    handleTextarea(dialog);
    handleConstraintMessage(dialog);
    handleRequiredMessage(dialog);

    var readonly = dialog.find(TEXTFIELD_READONLY)[0];
    var required = dialog.find(TEXTFIELD_REQUIRED)[0];
    handleExclusion(readonly,
      required,
      dialog.find(TEXTFIELD_REQUIREDSELECTED_ALERT)[0]);
    handleExclusion(required,
      readonly,
      dialog.find(TEXTFIELD_READONLYSELECTED_ALERT)[0]);
  }

  let newInit = (function () {
    const CLS_REGEX_DROPDOWN = '.cmp-form-text__regex-list',
      CLS_IS_CUSTOM_REGEX = '.cmp-form-text__is-custom-regex',
      CLS_CUSTOM_REGEX_HELP_MSG = '.cmp-form-text__custom-regex-hlp-mgs',
      CLS_CUSTOM_REGEX_VALUE = '.cmp-form-text__custom-regex-value',
      CLS_CUSTOM_REGEX_VALUE_CMP = '.cmp-form-text__custom-regex-value-cmp',
      CLS_CUSTOM_REGEX_MESSAGE = '.cmp-form-text__custom-regex-msg',
      CLS_CUSTOM_REGEX_MESSAGE_CMP = '.cmp-form-text__custom-regex-msg-cmp';
    let root = null;
    function Init(dialog) {
      root = $(dialog);


      root.find(CLS_CUSTOM_REGEX_VALUE).hide();
      root.find(CLS_CUSTOM_REGEX_MESSAGE).hide();
      CheckAndMakeRequired(CLS_CUSTOM_REGEX_MESSAGE_CMP,false);
      root.find(CLS_CUSTOM_REGEX_HELP_MSG).hide();


      let component = root.find(CLS_IS_CUSTOM_REGEX)[0];
      component && component.on('change', _onCustomRegex);
      _onCustomRegex.call(component);

      component = root.find(CLS_REGEX_DROPDOWN)[0];
      component && component.on('change', _onRegexSelected);
      _onRegexSelected.call(component);;

    }
    function _onRegexSelected() {
      var constraintMessage = root.find(TEXTFIELD_CONSTRAINTMESSAGE);
      CheckAndMakeRequired(TEXTFIELD_CONSTRAINTMESSAGE_FIELD,this.selectedItem.value);
      checkAndDisplay(constraintMessage,true,!!this.selectedItem.value);
    }
    function _onCustomRegex(e) {
      checkAndDisplay(root.find(CLS_CUSTOM_REGEX_VALUE), true, this.checked);
      checkAndDisplay(root.find(CLS_CUSTOM_REGEX_MESSAGE), true, this.checked);
      CheckAndMakeRequired(CLS_CUSTOM_REGEX_MESSAGE_CMP, this.checked);
      checkAndDisplay(root.find(CLS_CUSTOM_REGEX_HELP_MSG), true, this.checked);
    }
    return Init;
  })();

  channel.on("foundation-contentloaded", function (e) {
    if ($(e.target).find(EDIT_DIALOG).length > 0) {
      Coral.commons.ready(e.target, function (component) {
        initialise(component);
        newInit(component);
      });
    }
  });

})(jQuery, jQuery(document), Coral);
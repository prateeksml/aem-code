import { registerComponent } from "./component";
import { isPublisher } from "./util/CommonUtils";
import {
  FormSubmitHandler,
  ToggleSubmitButtonState,
  ToggleContinueButtonState,
} from "./digital-form-submit";
import { InitFormTextFields } from "./digital-forms-text";
import { InstantiateFileUpload } from "./digital-form-file-upload";
import { InitForFormConfirmation } from "./common-message-overlay";
import {
  HandleDatalayerEventsFormStart,
  HandleEventFormSubmit,
} from "./datalayer";
import { FormValueChangedListener } from "./form-utils";
/**
 * @param {HTMLFormElement} root
 */
function CheckboxValidityHandler(root) {
  //assumptions: no dynamically created element,
  //everything already present within the form
  let inputs = root.querySelectorAll(
    '.privacy input.custom[type="checkbox"][required]'
  );
  //,.options input.custom[type="checkbox"][required]
  let handler = (e) => {
    e.target.setCustomValidity("");
    if (e.target.checked) {
      e.target.classList.remove("invalid");
      e.preventDefault();
      _hideErrorMessage(e.target);
    } else if (e.target.required) {
      e.target.classList.add("invalid");
      e.preventDefault();
      _showErrorMessage(e.target);
    }
  };
  inputs.forEach((input) => {
    if (input.required) {
      input.addEventListener("invalid", handler);
      input.addEventListener("change", (e) => {
        handler(e);
        // let forAttr = e.target.name || e.target.id;
        // if (!forAttr) {
        //   return;
        // }
        // let lbl = e.target.parentNode.querySelector('[for="' + forAttr + '"');
        // if (lbl) {
        //   lbl.setAttribute("aria-checked", e.target.checked);
        // }
      });
      // input.addEventListener("focus", (e) => {
      //   //privacy control
      //   let forAttr = e.target.name || e.target.id;
      //   if (!forAttr) {
      //     return;
      //   }
      //   // let lbl = e.target.parentNode.querySelector('[for="' + forAttr + '"');
      //   // if (lbl) {
      //   //   lbl.setAttribute("aria-focused", true);
      //   // }
      // });
      // input.addEventListener("blur", (e) => {
      //   //privacy control
      //   let forAttr = e.target.name || e.target.id;
      //   if (!forAttr) {
      //     return;
      //   }
      //   // let lbl = e.target.parentNode.querySelector('[for="' + forAttr + '"');
      //   // if (lbl) {
      //   //   lbl.setAttribute("aria-focused", false);
      //   // }
      // });
    }
  });
  root.addEventListener("reset", () =>
    inputs.forEach((input) => {
      input.classList.remove("invalid");
      _hideErrorMessage(input);
    })
  );
  function _hideErrorMessage(input) {
    const msgArea = input.parentElement.querySelector(".err-msg.show");
    if (!msgArea) {
      return;
    }
    msgArea.setAttribute("aria-hidden", true);
    msgArea.classList.remove("show");
  }
  function _showErrorMessage(input) {
    const msgArea = input.parentElement.querySelector(".err-msg");
    if (!msgArea) {
      return;
    }
    const msg = input.dataset.cmpRequiredMessage;
    let span = msgArea.querySelector(".prepend-icon-info");
    if (!span) {
      span = document.createElement("span");
      msgArea.appendChild(span);
      span.className = "prepend-icon-info";
    }
    //clear
    while (span.firstChild) {
      span.removeChild(span.lastChild);
    }
    //update msg
    span.appendChild(document.createTextNode(msg));
    msgArea.classList.add("show");
    msgArea.setAttribute("aria-hidden", false);
  }
}

/**
 * @param {HTMLFormElement} root
 */
function RadioInputValidityHandler(root) {
  //assumption: no dynamically created element,all elements
  //already present within the form
  //and required attribute is not dynamically changed
  let radios = root.querySelectorAll(
    '.options input[required][type="radio"][name]:not([disabled])'
  );

  radios.forEach((radio) => {
    let radioGroup = root.querySelectorAll(
      '.options input[required][type="radio"][name="' +
        radio.name +
        '"]:not([custom-validity-handled]):not([disabled])'
    );
    radioGroup.forEach((radioGroupItem) => {
      radioGroupItem.addEventListener("invalid", (e) => {
        e.preventDefault(); //no default tooltip
        e.target.setCustomValidity(" "); //announced as invalid
        e.target.classList.add("invalid");
      });
      radioGroupItem.addEventListener("change", (e) => {
        e.target.setCustomValidity(""); //change to valid state
        radioGroup.forEach((item) => item.classList.remove("invalid"));
      });
      radioGroupItem.setAttribute("custom-validity-handled", true);
    });
  });
  root.addEventListener("reset", () =>
    radios.forEach((radio) => {
      radio.classList.remove("invalid");
      radio.setCustomValidity("");
    })
  );
}

export function DataMapping(form) {
  const inputString = form.dataset.mapping;

  // Remove the surrounding curly braces and split into individual key-value pairs
  const keyValuePairs = inputString.slice(1, -1).split(", ");

  // Initialize an empty object
  const resultObject = {};

  // Process each key-value pair and add it to the object
  keyValuePairs.forEach((keyValuePair) => {
    const [key, value] = keyValuePair.split("=");
    resultObject[key] = value;
  });
  return resultObject;
}

export function AutoSelectCountryHandler(form) {
  const url = "/akamai/geo-tracker.html";
  /**@type {HTMLSelectElement} */
  let field = form[DataMapping(form).country];
  if (!field || field.tagName !== "SELECT") {
    return;
  }
  fetch(url, { method: "post" }).then((response) => {
    //here response can be 200 or even 404 or some other error code,
    //we just need to take header value
    let CountryCode = response.headers.get("countrycode");
    if (typeof CountryCode != "string") {
      return;
    }

    CountryCode = CountryCode.replace(/\s/gim, "");
    if (CountryCode.lastIndexOf(",") > -1) {
      CountryCode = CountryCode.split(",").pop();
    }

    let isPreSelected = false,
      selectIndex = -1;
    CountryCode = CountryCode.toLowerCase();
    for (let i = 0; i < field.options.length; i++) {
      const element = field.options[i];
      if (element.selected) {
        isPreSelected = true;
      }
      if (element.value.toLowerCase() == CountryCode) {
        selectIndex = i;
        break;
      }
    }
    if (selectIndex > -1) {
      if (isPreSelected) {
        //todo:check with PO, what action to be done
      }
      field.selectedIndex = selectIndex;
      let option = field.options[field.selectedIndex];
      if (option) {
        option.dataset.autoSelected = true;
      }
    }
  });
}
/**
 * @param {HTMLFormElement} form
 */
function HandleFormReset(form) {
  //custom event
  form.addEventListener("form-submit-success", () =>
    ToggleSubmitButtonState(form, true)
  );
  form.addEventListener("form-submit-error", () =>
    ToggleSubmitButtonState(form, true)
  );
  form.addEventListener("form-continue-error", () =>
    ToggleContinueButtonState(form, true)
  );
  if (form.dataset.formType == "peopleContact") {
    form.addEventListener("form-submit-success", () => form.reset());
    // form.addEventListener("form-submit-error", () => form.reset());
  }
}

function HandleFocusInvalidElement(form) {
  form.addEventListener("focus-invalid-elements", (evt) => {
    let invalid = form.querySelectorAll(
      ":invalid:not(fieldset),select.invalid:not(fieldset),textarea.invalid"
    );

    //for rfp form, continue button click
    if (evt.detail && evt.detail.fields && evt.detail.fields.length) {
      let fieldSet = evt.detail.fields;
      invalid = Array.prototype.filter.call(
        invalid,
        (a) => fieldSet.indexOf(a.name) > -1
      );
    }

    if (invalid.length) {
      invalid[0].focus();
    }
  });
}

function DigitalFormComponent(config) {
  /**@type {HTMLFormElement} */
  let root = config.element;
  HandleFocusInvalidElement(root);
  FormValueChangedListener.Watch(root);
  InitFormTextFields(root);
  CheckboxValidityHandler(root);
  RadioInputValidityHandler(root);
  new FormSubmitHandler(root);
  InstantiateFileUpload(root);
  HandleFormReset(root);
  InitForFormConfirmation(config);
  if (root.dataset.formType != "peopleContact") {
    HandleDatalayerEventsFormStart(root, root.dataset.formType);
    setTimeout(() =>
      root.dispatchEvent(new CustomEvent("custom-event-form-start"))
    );
  }
  if (root.dataset.formType === "rfp") {
    let $firstContainer = root.querySelector(".container");
    if ($firstContainer) {
      $firstContainer.classList.add("show");
    }
  }
  HandleEventFormSubmit(root);
  AutoSelectCountryHandler(root);
}

export class DropdownValidator {
  /**
   * @param {HTMLFormElement} form
   */
  constructor(form) {
    this.ui = { form };
    this._attachEvents();
    this.AllDropdownsValid = true;
  }

  /**
   * @private
   */
  _attachEvents() {
    let selectElements = this.ui.form.querySelectorAll(
      ".cmp-form-options--drop-down select[required]"
    );
    selectElements.forEach((item) =>
      item.addEventListener("change", (e) => {
        this.HideErrorMessage(e.target);
        item.classList.remove("invalid");
      })
    );
    this.ui.form.addEventListener("reset", () => {
      this.ui.form
        .querySelectorAll(".cmp-form-options--drop-down select[required]")
        .forEach((item) => {
          this.HideErrorMessage(item);
          item.classList.remove("invalid");
        });
    });
  }

  /**
   * @private
   * @param {HTMLSelectElement} element
   */
  ShowErrorMessage(element) {
    /**@type {HTMLElement} */
    let fieldSet = element.parentElement?.parentElement?.parentElement;
    if (!fieldSet) {
      return;
    }
    let msgArea = fieldSet.querySelector(".err-msg"),
      msg = fieldSet.dataset.cmpRequiredMessage || "";
    if (!msgArea) {
      return;
    }

    let span = msgArea.querySelector("span");
    if (!span) {
      span = document.createElement("span");
      msgArea.appendChild(span);
      span.className = "prepend-icon-info";
    }

    //clear
    while (span.firstChild) {
      span.removeChild(span.lastChild);
    }

    //update msg
    span.appendChild(document.createTextNode(msg));

    msgArea.classList.add("show");
    msgArea.setAttribute("aria-hidden", false);
  }

  /**
   * @private
   * @param {HTMLSelectElement} element
   */
  HideErrorMessage(element) {
    /**@type {HTMLElement} */
    let fieldSet = element.parentElement?.parentElement?.parentElement;
    if (!fieldSet) {
      return;
    }
    let msgArea = fieldSet.querySelector(".err-msg.show");
    if (!msgArea) {
      return;
    }
    msgArea.setAttribute("aria-hidden", true);
    msgArea.classList.remove("show");
  }
  /**
   * @private
   * @param {HTMLSelectElement} element
   * @param {HTMLOptionElement} option
   */
  _isDropdownDefaultValue(element) {
    return element.value == "default";
  }
  /**
   * @returns {boolean} true if all dropdowns have a valid value selected.
   */
  Validate() {
    /**@type {Array<HTMLSelectElement>} */
    let selectElements = this.ui.form.querySelectorAll(
        ".cmp-form-options--drop-down select[required]"
      ),
      flag = true;
    this.AllDropdownsValid = true;
    for (let i = 0; i < selectElements.length; i++) {
      const selectItem = selectElements[i];
      selectItem.classList.remove("invalid");
      this.HideErrorMessage(selectItem);
      if (
        selectItem.selectedIndex < 0 ||
        this._isDropdownDefaultValue(selectItem)
      ) {
        flag = false;
        this.ShowErrorMessage(selectItem);
        selectItem.classList.add("invalid");
        continue;
      }
    }
    this.AllDropdownsValid = flag;
    return this.AllDropdownsValid;
  }
}
export class TextAreaPatternValidator {
  /**
   * @param {HTMLFormElement} form
   */
  constructor(form) {
    this.ui = { form };
    this.AllFieldsValid = true;
    this._attachEvents();
  }
  get FieldSelector() {
    return ".cmp-form-text textarea[data-pattern]";
  }
  /**
   * @private
   */
  _attachEvents() {
    //already taken care by form-text validation
    // let elements = this.ui.form.querySelectorAll(this.FieldSelector);
    // elements.forEach((item) =>
    //   item.addEventListener("change", (e) => {
    //     item.classList.remove("invalid");
    //   })
    // );
    // this.ui.form.addEventListener("reset", () => {
    //   this.ui.form
    //     .querySelectorAll(this.FieldSelector)
    //     .forEach((item) => {
    //       item.classList.remove("invalid");
    //     });
    // });
  }
  /**
   * @private
   * @param {HTMLTextAreaElement} element
   */
  ShowErrorMessage(element) {
    let msg = element?.parentElement?.dataset?.cmpConstraintMessage;
    FormErrorMessage.Show(element, msg || "", element.parentElement);
  }

  /**
   * @private
   * @param {HTMLTextAreaElement} element
   */
  HideErrorMessage(element) {
    FormErrorMessage.Hide(element, element.parentElement);
  }
  /**
   * @private
   * @param {HTMLTextAreaElement} element
   */
  _isValidValue(element) {
    if (element.value.length == 0) {
      return true; //let the required validation take care of it
    }

    try {
      return new RegExp(element.dataset.pattern).test(element.value);
    } catch (error) {
      console.warn(error);
    }
    return false;
  }
  /**
   * @returns {boolean} true if all textarea fields a valid value.
   */
  Validate() {
    /**@type {Array<HTMLTextAreaElement>} */
    let selectElements = this.ui.form.querySelectorAll(this.FieldSelector),
      flag = true;
    this.AllFieldsValid = true;
    for (let i = 0; i < selectElements.length; i++) {
      const _item = selectElements[i];
      this.HideErrorMessage(_item);
      if (!this._isValidValue(_item)) {
        flag = false;
        this.ShowErrorMessage(_item);
        continue;
      }
    }
    this.AllFieldsValid = flag;
    return this.AllFieldsValid;
  }
}

class FormErrorMessage {
  /**
   * @param {HTMLInputElement|HTMLSelectElement|HTMLTextAreaElement} element target element
   * @param {string} msg  error message to display
   * @param {HTMLDivElement} parentDiv - element contianer div within which the error message div resides
   */
  static Show(element, msg, parentDiv) {
    if (element) {
      element.classList.add("invalid");
      //element.setCustomValidity(msg);//todo: prevent default to hide detault tooltip
    }

    /**@type {HTMLElement} */
    if (!parentDiv) {
      return;
    }
    let msgArea = parentDiv.querySelector(".err-msg");
    if (!msgArea) {
      return;
    }

    let span = msgArea.querySelector("span");
    if (!span) {
      span = document.createElement("span");
      msgArea.appendChild(span);
      span.className = "prepend-icon-info";
    }

    //clear
    while (span.firstChild) {
      span.removeChild(span.lastChild);
    }

    //update msg
    span.appendChild(document.createTextNode(msg));

    msgArea.classList.add("show");
    msgArea.setAttribute("aria-hidden", false);
  }

  /**
   * @param {HTMLInputElement|HTMLSelectElement|HTMLTextAreaElement} element target element
   * @param {HTMLDivElement} parentDiv
   */
  static Hide(element, parentDiv) {
    if (element) {
      element.classList.remove("invalid");
      //element.setCustomValidity("");//todo: prevent default to hide detault tooltip
    }
    if (!parentDiv) {
      return;
    }

    let msgArea = parentDiv.querySelector(".err-msg.show");
    if (!msgArea) {
      return;
    }
    msgArea.setAttribute("aria-hidden", true);
    msgArea.classList.remove("show");
  }
}
if (isPublisher()) {
  registerComponent(DigitalFormComponent, {
    selectors: {
      self: ".cmp-digital-form",
    },
  });
} else {
  if (
    document.querySelector(".cmp-digital-form") &&
    document.querySelector(".cmp-digital-form").dataset.formType === "rfp"
  ) {
    let $firstContainer = document
      .querySelector(".cmp-digital-form")
      .querySelector(".container");
    if ($firstContainer) {
      $firstContainer.classList.add("show");
    }
  }
}

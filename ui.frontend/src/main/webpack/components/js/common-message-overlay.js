import {
  HandleFormErrorOverlayAppear,
  HandleFormErrorOverlayClose,
  HandleFormWarnningOverlayAppear,
  HandleFormWarnningOverlayClick,
} from "./datalayer";
import { FormValueChangedListener } from "./form-utils";
import { SimpleModalUI } from "./people-contact-form";

class ConfirmationMessageOverlay extends SimpleModalUI {
  constructor() {
    super(`cmn-msg-overlay-${++ConfirmationMessageOverlay.InstanceId}`);
    this.ui.root.classList.add("cmn-msg-overlay");
  }

  _newCloseButtonUI(parent) {
    let div = document.createElement("div"),
      cancelBtn = document.createElement("button"),
      continue_btn = document.createElement("button");

    cancelBtn.className = "btn btn-cancel";
    cancelBtn.append("Cancel");
    div.appendChild(cancelBtn);

    continue_btn.className = "btn btn-continue";
    continue_btn.append("Continue");
    div.appendChild(continue_btn);

    div.className = "section buttons";
    parent.appendChild(div);
    if (!this._buttons) {
      this._buttons = {
        /**@type {HTMLButtonElement} */
        cancel: null,
        /**@type {HTMLButtonElement} */
        continue: null,
      };
    }
    this._buttons.continue = continue_btn;
    this._buttons.cancel = cancelBtn;
    this._onTabConfirmationButtons();
    return cancelBtn;
  }

  _onTabConfirmationButtons() {
    this._buttons.cancel.addEventListener("keydown", (e) => {
      if (e.key == "Tab") {
        //doesn't matter if shift key is also pressed
        this._buttons.continue.focus();
        e.preventDefault();
      }
    });
    this._buttons.continue.addEventListener("keydown", (e) => {
      if (e.key == "Tab") {
        //doesn't matter if shift key is also pressed
        this._buttons.cancel.focus();
        e.preventDefault();
      }
    });
  }
  setButtonText(msgCancel = "Cancel", msgContinue = "Continue") {
    let btn = this._buttons.cancel;
    while (btn.firstChild) {
      btn.removeChild(btn.lastChild);
    }
    btn.appendChild(document.createTextNode(msgCancel));

    btn = this._buttons.continue;
    while (btn.firstChild) {
      btn.removeChild(btn.lastChild);
    }
    btn.appendChild(document.createTextNode(msgContinue));
  }
  AddContent(div) {
    let content = this.ui.root.querySelector(".people-contact-modal__content");
    let section = this.ui.root.querySelector(".section.buttons");
    if (content) {
      content.insertBefore(div, section);
    }
  }
}
/**@type {ConfirmationMessageOverlay} */
let formConfirmationInstance, onCloseCallback;
export function InitForFormConfirmation(config) {
  /**@type {HTMLFormElement} */
  let form = config.element;
  InitForFormSubmitError(config);
  /**@type {HTMLElement} */
  let text = form.querySelector(".warning-overlay-msg .text");
  if (!text) {
    return;
  }
  let instance = (formConfirmationInstance = new ConfirmationMessageOverlay());
  instance.AddContent(text);
  let lblby = text.querySelector("h1,h2,h3,h4,h5,h6,p");
  if (lblby) {
    if (!lblby.id) {
      lblby.id = `msg-overlay-desc-id-${form.id}`;
    }
    instance.setLabelledby(`${lblby.id}`);
  }
  instance.setButtonText(
    text.dataset.buttonTextCancel,
    text.dataset.buttonTextContinue
  );
  let isFormStarted = false,
    isValueChanged = false;
  FormValueChangedListener.getInstance(form).addChangeListener(
    (s) => (isValueChanged = s)
  );

  form.addEventListener("custom-event-form-start", () => {
    isFormStarted = true;
  });

  form.addEventListener(
    "custom-event-form-closed",
    () => (isFormStarted = false)
  );
  form.addEventListener("form-submit-success", () => (isFormStarted = false));
  // form.addEventListener(
  //   "form-submit-error",
  //   () => (isFormStarted = false)
  // );

  /**
   * @param {Event} e click event
   * @this HTMLAnchorElement
   */
  function _OnLinkClick(e) {
    if (isFormStarted && isValueChanged) {
      HandleFormWarnningOverlayAppear(form.dataset.formType);
      e.preventDefault();
      instance.showModal(this);
      instance._buttons.cancel.focus();
    }
  }
  instance._buttons.continue.addEventListener("click", () => {
    HandleFormWarnningOverlayClick(form.dataset.formType, "continue");
    instance.hideModal("continue");
    isFormStarted = isValueChanged = false;
    let link = instance.currentActivator;
    if (link && link.click) {
      setTimeout(() => link.click()); //continue the navigation
    }
  });
  instance._buttons.cancel.addEventListener("click", () =>
    HandleFormWarnningOverlayClick(form.dataset.formType, "cancel")
  );

  //href should not be empty
  //should not start with #, mailto: , tel: &  javascript:
  let links = document.querySelectorAll(
    'a[href]:not([href^="#"]):not([href=""])' +
      ':not([href^="mailto:"]):not([href^="tel:"])' +
      ':not([href^="javascript:"]):not([target="_blank"])'
  );

  links.forEach((e) => e.addEventListener("click", _OnLinkClick));
  return instance;
}
function onConfirmationModalClosed(e) {
  if (onCloseCallback) {
    let cb = onCloseCallback; //to clear safely, then call
    onCloseCallback = null;
    cb(e);
  }
}
export function ShowFormConfirmationOveraly(element, focusBtn, closeCallback) {
  if (!formConfirmationInstance) {
    if (closeCallback) {
      closeCallback();
    }
    return;
  }
  onCloseCallback = closeCallback;
  formConfirmationInstance.off("modal-closed", onConfirmationModalClosed);
  formConfirmationInstance.on("modal-closed", onConfirmationModalClosed);
  formConfirmationInstance.showModal(element);
  if (focusBtn) {
    formConfirmationInstance._buttons.cancel.focus();
  }
}

class ErrorMessageOverlay extends SimpleModalUI {
  constructor(cls) {
    super(`err-msg-overlay-${++ErrorMessageOverlay.InstanceId}`);
    this.ui.root.classList.add("cmn-msg-overlay", "err-msg-overlay");
    if (cls) {
      this.ui.root.classList.add(cls);
    }
  }
  _newCloseButtonUI(parent) {
    let div = document.createElement("div"),
      closeBtn = document.createElement("button");

    closeBtn.className = "btn btn-close";
    closeBtn.append("Close");
    div.appendChild(closeBtn);

    div.className = "section buttons";
    parent.appendChild(div);

    /**@type {HTMLButtonElement} */
    this._closeBtn = closeBtn;
    this._onTabCloseButton(closeBtn);
    return closeBtn;
  }
  _onTabCloseButton(closeBtn) {
    closeBtn.addEventListener("keydown", (e) => {
      if (e.key == "Tab") {
        e.preventDefault();
        closeBtn.focus();
      }
    });
  }
  setButtonText(text) {
    while (this._closeBtn.firstChild) {
      this._closeBtn.removeChild(this._closeBtn.lastChild);
    }
    this._closeBtn.appendChild(document.createTextNode(text));
  }
  AddContent() {
    return ConfirmationMessageOverlay.prototype.AddContent.apply(
      this,
      arguments
    );
  }
}
/**
 * @type {ErrorMessageOverlay}
 */
let formSubmitErrorOverlay;
export function ShowFormErrorOverlay(ElementToFocusforForm) {
  if (formSubmitErrorOverlay) {
    HandleFormErrorOverlayAppear(formSubmitErrorOverlay._form_type);
    formSubmitErrorOverlay.showModal(ElementToFocusforForm);
    formSubmitErrorOverlay.ui.closeBtn.focus();
  }
}
function InitForFormSubmitError(config) {
  /**@type {HTMLFormElement} */
  let form = config.element;

  /**@type {HTMLDivElement} */
  let div = form.querySelector(".submit-error-message .text-msg");
  if (!div) {
    return;
  }

  let text = form.querySelector(".submit-error-message .text-msg .text");
  if (!text) {
    return;
  }
  let instance = (formSubmitErrorOverlay = new ErrorMessageOverlay());
  instance._form_type = form.dataset.formType;
  instance.AddContent(div);
  let lblby = text.querySelector("h1,h2,h3,h4,h5,h6");
  if (lblby) {
    if (!lblby.id) {
      lblby.id = `err-msg-overlay-heading-${form.id}`;
    }
    instance.setLabelledby(`${lblby.id}`);
  }
  instance.setButtonText(text.dataset.buttonTextClose);
  instance.on("modal-closed", () =>
    HandleFormErrorOverlayClose(form.dataset.formType)
  );
}

let instanceForSearchResult;
export function ShowSearchErrorMsgOverlay(elementToFocus) {
  instanceForSearchResult && instanceForSearchResult.showModal(elementToFocus);
}

export function InitSearchResultErrorOverlay(config) {
  /**@type {HTMLDivElement} */
  let div = config.element.querySelector(".error-msg-for-overlay  .text-msg");
  if (!div) {
    return;
  }
  let text = div.querySelector(".text-msg .text");
  if (!text) {
    return;
  }

  let instance = (instanceForSearchResult = new ErrorMessageOverlay());
  instance.AddContent(div);

  let lblby = text.querySelector("h1,h2,h3,h4,h5,h6");
  if (lblby) {
    if (!lblby.id) {
      let id = config.element.id;
      if (!id) {
        id = ErrorMessageOverlay.InstanceId + 100 + 1;
      }
      lblby.id = `err-msg-overlay-heading-${
        config.element.id ? config.element.id : id
      }`;
    }
    instance.setLabelledby(`${lblby.id}`);
  }
  instance.setButtonText(text.dataset.buttonTextClose);

  // elementToFocusOnCloseForSearch = document.querySelector(
  //   "#cmp-search-results-input__input-box"
  // );
}

import { registerComponent } from "./component";
import { isPublisher } from "./util/CommonUtils";
import { MessageUpdater } from "./digital-form-submit";
import { AutoSelectCountryHandler } from "./digital-forms";
import { FormValueChangedListener } from "./form-utils";
import { ShowFormConfirmationOveraly } from "./common-message-overlay";
import {
  HandleDatalayerEventsFormStart,
  HandleFormWarnningOverlayAppear,
  HaveDataLayerEvent,
} from "./datalayer";
/**@type {PeopleContactFormModal} */
let modalUI;
let _FormContainerHTML;
const _find = (e, s) => e.querySelector(s),
  _create = (n) => document.createElement(n);
function ShowPeopleContactForm(element, contact_info) {
  modalUI.showModal(element, contact_info);
}

export class SimpleModalUI {
  static InstanceId = 0;
  constructor(rootId) {
    this.rootSelectorId = rootId;
    this.lockScroll = true;
    this.ui = {
      /**@type {HTMLButtonElement} */
      closeBtn: null,
      /**@type {HTMLDivElement} */
      root: null,
    };
    this.ui.root = this._initModal(this.rootSelector);
    this.currentActivator = null;
    this._attachEvents();
    this._noScrollClassName = `no-scroll-${++SimpleModalUI.InstanceId}`;
  }
  on(name, handler) {
    this.ui.root.addEventListener("custom-event-" + name, handler);
  }
  off(name, handler) {
    if (handler) {
      this.ui.root.removeEventListener("custom-event-" + name, handler);
    } else {
      this.ui.root.removeEventListener("custom-event-" + name);
    }
  }
  _initModal() {
    let root = _find(document.body, "#" + this.rootSelectorId);
    if (!root) {
      root = this._newModalUI(this.rootSelectorId);
      document.body.appendChild(root);
    }
    return root;
  }
  _newModalUI(id) {
    let modal = _create("div"),
      content = _create("div");

    modal.className = "people-contact-modal";
    modal.setAttribute("aria-hidden", "true");
    modal.setAttribute("aria-modal", "true");
    modal.setAttribute("role", "dialog");
    modal.setAttribute("id", id);

    content.className = "people-contact-modal__content";
    this.ui.closeBtn = this._newCloseButtonUI(content);

    modal.appendChild(content);
    return modal;
  }
  showModal(activator) {
    this.prepareContent(...arguments);
    this.currentActivator = activator;
    if (this.lockScroll) {
      document.body.classList.add(this._noScrollClassName);
    }
    this.ui.root.classList.add("show");
    this.ui.root.setAttribute("aria-hidden", "false");
    this.addCloseHandlers();
    this.ui.root.dispatchEvent(new CustomEvent("custom-event-modal-opened"));
  }
  hideModal(data) {
    this.ui.root.classList.remove("show");
    this.ui.root.setAttribute("aria-hidden", "true");
    document.body.classList.remove(this._noScrollClassName);
    if (this.currentActivator && this.currentActivator.focus) {
      // this.currentActivator.scrollIntoView &&
      //   this.currentActivator.scrollIntoView();
      this.currentActivator.focus();
    }
    this.removeCloseHandlers();
    this.ui.root.dispatchEvent(
      new CustomEvent("custom-event-modal-closed", { detail: data })
    );
  }
  prepareContent() {
    return true;
  }
  setLabelledby(selector) {
    this.ui.root.setAttribute("aria-labelledby", selector);
  }
  _newCloseButtonUI(content) {
    let closeDiv = _create("div"),
      closeBtn = _create("button");
    closeBtn.className = "close-btn close-btn-top";
    closeDiv.className = "top-section";
    closeDiv.appendChild(closeBtn);
    closeBtn.setAttribute("aria-label", "Close");
    closeBtn.setAttribute("title", "Close");
    content.appendChild(closeDiv);
    return closeBtn;
  }
  _attachEvents() {
    this._escKeyDown = this._escKeyDown.bind(this);
    this._closeBtnClick = this._closeBtnClick.bind(this);
  }
  removeCloseHandlers() {
    document.removeEventListener("keydown", this._escKeyDown);
    this.ui.closeBtn.removeEventListener("click", this._closeBtnClick);
  }
  _closeBtnClick() {
    this.hideModal();
  }
  addCloseHandlers() {
    document.addEventListener("keydown", this._escKeyDown);
    this.ui.closeBtn.addEventListener("click", this._closeBtnClick);
  }
  _escKeyDown(e) {
    //just the escape key, nothing else
    if (e.altKey || e.ctrlKey || e.shiftKey || e.key != "Escape") {
      return;
    }
    this.hideModal();
  }
}
class PeopleContactFormModal extends SimpleModalUI {
  constructor(rootId) {
    super(rootId);
    this.currentXFPath = "";
    /**@type {HTMLDivElement} */
    this.ui.formContainer = null;
    this._createInitialHTML();
    this.onTabCloseButton();
    this.ValueChangedChecker = function () {
      return false;
    };
    this.isValueChanged = false;
  }
  _attachLinkClick(root) {
    //href should not be empty
    //should not start with #, mailto: , tel: &  javascript:
    let links = root.querySelectorAll(
      'a[href]:not([href^="#"]):not([href=""])' +
        ':not([href^="mailto:"]):not([href^="tel:"])' +
        ':not([href^="javascript:"]):not([target="_blank"])'
    );
    links /*= Array.prototype.filter.call(
      links,
      (link) => String(link.target).replace(/\s/g, "").length != 0
    );
    links*/
      .forEach((e) =>
        e.addEventListener("click", (evt) => {
          this._onLikClick(evt, e);
        })
      );
  }
  _createInitialHTML() {
    let content = this.ui.root.querySelector(".people-contact-modal__content");
    let div = _create("div");
    div.className = "contact-form-container";
    content.appendChild(div);
    this.ui.formContainer = div;
  }
  showModal(activator) {
    this.prepareContent(...arguments);
    this.currentActivator = activator;
  }
  _setTitleMessage(element, msg, fName, lName) {
    if (!element || !msg) {
      return;
    }
    MessageUpdater.updateWithName(element, fName, lName, msg);
  }
  _setFormValues(info) {
    let form = _find(this.ui.formContainer, "form");
    if (!form) {
      return;
    }
    AutoSelectCountryHandler(form);
    this._setTitleMessage(
      _find(
        form,
        ".title h1,.title h2,.title h3,.title h4,.title h5,.title h6"
      ),
      info.title,
      info.firstName,
      info.lastName
    );
    /**@type {HTMLInputElement} */
    let input = _find(form, 'input[name="contactPath"][type="hidden"]');
    if (!input) {
      input = _create("input");
      (input.name = "contactPath"), (input.type = "hidden");
      (input.required = true), form.appendChild(input);
    }
    input.value = info.contactPath;
    let btn = _find(this.ui.root, ".close-btn-top");
    if (btn) {
      let lbl = form.dataset.closeButtonTitle || "Close Dialog Modal";
      btn.setAttribute("title", lbl);
      btn.setAttribute("aria-label", lbl);
      btn.style.display = "inline-block";
    }
  }
  _showInternal() {
    this.ui.root.classList.add("show");
    this.ui.root.setAttribute("aria-hidden", "false");
    document.body.classList.add(this._noScrollClassName);
    this.ui.root.scrollTop = 0;
    _find(this.ui.root, ".close-btn-top").focus();
    this.ui.root.dispatchEvent(new CustomEvent("custom-event-modal-opened"));
    let form = this.ui.formContainer.querySelector("form");
    this.addCloseHandlers();
    if (form) {
      form.dispatchEvent(new CustomEvent("custom-event-form-closed"));
    }
  }
  prepareContent() {
    let node = this.ui.formContainer;
    let [, contact_info] = arguments;
    if (node.querySelector(".cmp-digital-form")) {
      this._setFormValues(contact_info);
      this._showInternal();
      return true;
    }

    node.appendChild(_FormContainerHTML);
    let title = _find(_FormContainerHTML, ".cmp-title__text");
    if (title) {
      title.setAttribute("id", "cmp-title__text");
    }
    this._setFormValues(contact_info);
    this.setLabelledby("cmp-title__text");
    this.onTabSubmitButton();
    this._attachLinkClick(_FormContainerHTML);
    this.onTabThankYouMessage();
    this._showInternal();
    this.ValueChangedChecker = FormValueChangedListener.getInstance(
      node.querySelector("form")
    ).addChangeListener((s) => (this.isValueChanged = s));
  }
  onTabCloseButton() {
    let closeBtn = _find(this.ui.root, ".close-btn-top");
    closeBtn &&
      closeBtn.addEventListener("keydown", (e) => {
        if (e.shiftKey && e.key == "Tab") {
          let submitElement =
            this.ui.formContainer.querySelector('[type="submit"]'); //input or button
          let anchor = this.ui.formContainer.querySelector(
            ".form-result-container.show a"
          );

          if (anchor) {
            anchor.focus && anchor.focus();
            e.preventDefault();
          } else if (submitElement) {
            submitElement.focus && submitElement.focus();
            e.preventDefault();
          }
        }
      });
  }
  onTabSubmitButton() {
    let submitElement = this.ui.formContainer.querySelector('[type="submit"]'); //input or button
    submitElement.addEventListener("keydown", (e) => {
      if (!e.shiftKey && e.key == "Tab") {
        //todo: handle shift tab
        _find(this.ui.root, ".close-btn-top").focus();
        e.preventDefault();
      }
    });
  }
  onTabThankYouMessage() {
    // let anchor = this.ui.formContainer.querySelector(
    //   ".form-result-container.show a"
    // );
    // btn &&
    //   btn.addEventListener("keydown", (e) => {
    //     if (e.key == "Tab") {
    //       //handles shift Tab as well
    //       _find(this.ui.root, ".close-btn-top").focus();
    //       e.preventDefault();
    //     }
    //   });

    /**@type {HTMLButtonElement} */
    let button = _find(this.ui.formContainer, ".close-btn-bottom");
    // let button = _find(this.ui.formContainer, '.form-result-container a.button');
    if (!button) {
      return;
    }
    button.addEventListener("click", (e) => {
      e.preventDefault();
      this.hideModal();
    });

    /**@type {HTMLFormElement} */
    let form = _find(this.ui.formContainer, "form");
    if (!form) {
      return;
    }
    form.addEventListener("form-submit-success", () => {
      _find(this.ui.root, ".close-btn-top").style.display = "none";
      button.removeEventListener("keydown", this._handleBottomCloseBtnKeyDown);
      button.addEventListener("keydown", this._handleBottomCloseBtnKeyDown);
    });
  }
  _handleBottomCloseBtnKeyDown(e) {
    if (e.key == "Tab") {
      //there is only one button, so dont change focus.
      e.preventDefault();
    }
  }
  _hideModalInternal(data) {
    this.ui.root.classList.remove("show");
    this.ui.root.setAttribute("aria-hidden", "true");
    document.body.classList.remove(this._noScrollClassName);
    if (this.currentActivator && this.currentActivator.focus) {
      // this.currentActivator.scrollIntoView &&
      //   this.currentActivator.scrollIntoView();
      this.currentActivator.focus();
    }
    this.removeCloseHandlers();
    this.ui.root.dispatchEvent(
      new CustomEvent("custom-event-modal-closed", { detail: data })
    );
  }
  hideModal(data) {
    if (this.isValueChanged) {
      this._showConfirmationDilog(this.ui.closeBtn, true, (e) => {
        if (e.detail == "continue") {
          this._hideModalInternal(data);
        }
      });
    } else {
      this._hideModalInternal("");
    }
  }
  _showConfirmationDilog(focusBackToElement, focusCloseBtn, cbOnClose) {
    /**@type {HTMLFormElement} */
    let form = _find(this.ui.formContainer, "form");
    if (form) {
      HandleFormWarnningOverlayAppear(form.dataset.formType);
    }
    ShowFormConfirmationOveraly(focusBackToElement, focusCloseBtn, cbOnClose);
  }
  /**
   * @param {HTMLAnchorElement} link
   */
  _onLikClick(event, link) {
    if (this.isValueChanged) {
      event && event.preventDefault && event.preventDefault();
      this._showConfirmationDilog(link, true, (e) => {
        if (e.detail == "continue") {
          this.isValueChanged = false;
          //click again. this time , flag will be false
          link.click && link.click();
        }
      });
      return;
    }
    this._hideModalInternal(""); //hide and let the page load
  }
}

function PeopleContactFormHandler(config) {
  let root = config.element;
  /**@type {HTMLAnchorElement} */
  let link = root.querySelector("[triggers-dialog-modal]");
  if (!link) {
    return;
  }
  if (!modalUI) {
    modalUI = new PeopleContactFormModal("people-contact-modal-root");
    let url = getFragmentUrl();
    url && preloadForm(url); //preload form in background
    modalUI.on("modal-closed", onModalClosed);
    modalUI.on("modal-opened", _onModalOpened);
  }
  link.setAttribute("aria-haspopup", "dialog");
  link.setAttribute("role", "button");
  link.addEventListener("click", _onLinkClick);
}
const customEventInfo = { contact: "" };
function _onModalOpened(e) {
  /**@type {HTMLDivElement} */
  let root = e.target;
  /**@type {HTMLFormElement} */
  let form = root.querySelector(".cmp-digital-form");
  if (!form) {
    return;
  }
  form.dispatchEvent(new CustomEvent("custom-event-form-start"));
  HandleDatalayerEventsFormStart(
    form,
    form.dataset.formType || "peopleContact",
    customEventInfo
  );
}
function _onLinkClick(e) {
  //contact path will be either the data attribute in case of search result contact tiles
  //or the current page path in case of people profile page.

  customEventInfo.contact = "";
  const { firstName, lastName, contactPath = window.location } = this.dataset;
  if (!_FormContainerHTML) {
    return;
  }
  //only when we show modal, we prevent default.
  e.preventDefault();
  let form = _find(_FormContainerHTML, "form");
  let titleMessage = form ? form.dataset.formTitle : "";
  customEventInfo.contact = `${firstName} ${lastName}`;
  ShowPeopleContactForm(this, {
    firstName,
    lastName,
    contactPath,
    title: titleMessage,
  });
}
function onModalClosed(e) {
  /**@type {HTMLDivElement} */
  let root = e.target;
  /**@type {HTMLFormElement} */
  let form = root.querySelector(".cmp-digital-form");
  if (form) {
    form.reset();
  }
  /**@type {HTMLDivElement} */
  let resultSection = root.querySelector(".form-result-container");
  form.classList.remove("hide");
  resultSection && resultSection.classList.remove("show");
  HaveDataLayerEvent({
    event: "cmp:formClose",
    eventInfo: {
      path: "component.people-contact-form-",
    },
    component: {
      "people-contact-form-": {
        "dc:title": "people contact form",
      },
    },
  });
}

function getFragmentUrl() {
  const element = _find(document, "[data-people-contact-form-url]");
  return element.getAttribute("data-people-contact-form-url") || "";
}

async function preloadForm(url) {
  return fetch(url).then((response) => {
    response.text().then((text) => {
      let div = _create("div");
      div.innerHTML = text;
      let fragment = document.createDocumentFragment();
      fragment.append(div);
      _FormContainerHTML = fragment.querySelector(".cmp-container>.aem-Grid");
    }); //todo: handle error
  });
}

if (isPublisher()) {
  registerComponent(PeopleContactFormHandler, {
    selectors: {
      self: ".cmp-contact-card,ul.cmp-search-results__list-item--icons-container",
    },
  });
}

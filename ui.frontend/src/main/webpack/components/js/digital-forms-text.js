"use strict";

let NS = "cmp";
let IS = "formText";
let IS_DASH = "form-text";

let selectors = {
  self: "[data-" + NS + '-is="' + IS + '"]',
};

let properties = {
  /**
   * A validation message to display if there is a type mismatch between the user input and expected input.
   *
   * @type {String}
   */
  constraintMessage: "",
  /**
   * A validation message to display if no input is supplied, but input is expected for the field.
   *
   * @type {String}
   */
  requiredMessage: "",
};
function readData(element) {
  let data = element.dataset;
  let options = [];
  let capitalized = IS;
  capitalized = capitalized.charAt(0).toUpperCase() + capitalized.slice(1);
  let reserved = ["is", "hook" + capitalized];

  for (let key in data) {
    if (Object.prototype.hasOwnProperty.call(data, key)) {
      let value = data[key];

      if (key.indexOf(NS) === 0) {
        key = key.slice(NS.length);
        key = key.charAt(0).toLowerCase() + key.substring(1);

        if (reserved.indexOf(key) === -1) {
          options[key] = value;
        }
      }
    }
  }

  return options;
}

class FormText {
  constructor(config) {
    if (config.element) {
      // prevents multiple initialization
      config.element.removeAttribute("data-" + NS + "-is");
    }

    this._cacheElements(config.element);
    this._setupProperties(config.options);

    this._elements.input.addEventListener(
      "invalid",
      this._onInvalid.bind(this)
    );
    this._elements.input.addEventListener("input", this._onInput.bind(this));

    this._handleCharCountUpdate();
  }
  _handleCharCountUpdate() {
    let pTag = this._elements.self.querySelector(".char-count");
    if (!pTag) {
      return;
    }
    let ElemForused = pTag.querySelector(".used");
    if (
      ElemForused &&
      !this._elements.input.disabled &&
      this._elements.input.maxLength > 0
    ) {
      this._elements.input.addEventListener("input", (evt) => {
        this._setCharCount(evt.target.value.length);
      });
      if (this._elements.input.value) {
        this._setCharCount(this._elements.input.value.length);
      }
    }
  }
  _setCharCount(count) {
    let ElemForused = this._elements.self.querySelector(".char-count .used");
    if (ElemForused) {
      ElemForused.innerHTML = count;
    }
  }
  _onInvalid(event) {
    event.target.setCustomValidity("");
    event.target.classList.add("invalid");
    if (
      event.target.validity.typeMismatch ||
      event.target.validity.patternMismatch
    ) {
      if (this._properties.constraintMessage) {
        this._showErrorMessage(this._properties.constraintMessage);
        event.preventDefault();
      }
    } else if (event.target.validity.valueMissing) {
      if (this._properties.requiredMessage) {
        this._showErrorMessage(this._properties.requiredMessage);
        event.preventDefault();
      }
    }
  }
  _hideErrorMessage() {
    let msgArea = this._elements.self.querySelector(".err-msg.show");
    if (!msgArea) {
      return;
    }
    msgArea.setAttribute("aria-hidden", true);
    msgArea.classList.remove("show");
  }
  _showErrorMessage(msg) {
    /**@type {HTMLElement} */
    let msgArea = this._elements.self.querySelector(".err-msg");
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
  _onInput(event) {
    event.target.setCustomValidity("");
    event.target.classList.remove("invalid");
    this._hideErrorMessage();
  }

  _cacheElements(wrapper) {
    this._elements = {};
    this._elements.self = wrapper;
    let hooks = this._elements.self.querySelectorAll(
      "[data-" + NS + "-hook-" + IS_DASH + "]"
    );
    for (let i = 0; i < hooks.length; i++) {
      let hook = hooks[i];
      let capitalized = IS;
      capitalized = capitalized.charAt(0).toUpperCase() + capitalized.slice(1);
      let key = hook.dataset[NS + "Hook" + capitalized];
      this._elements[key] = hook;
    }
  }

  _setupProperties(options) {
    this._properties = {};

    for (let key in properties) {
      if (Object.prototype.hasOwnProperty.call(properties, key)) {
        let property = properties[key];
        if (options && options[key] != null) {
          if (property && typeof property.transform === "function") {
            this._properties[key] = property.transform(options[key]);
          } else {
            this._properties[key] = options[key];
          }
        } else {
          this._properties[key] = properties[key]["default"];
        }
      }
    }
  }
}

/**
 *
 * @param {HTMLFormElement} root
 */
export function InitFormTextFields(root) {
  let elements = root.querySelectorAll(selectors.self);
  /**@type {Array<FormText>} */
  const instances = [];
  for (let i = 0; i < elements.length; i++) {
    instances.push(
      new FormText({ element: elements[i], options: readData(elements[i]) })
    );
  }

  let MutationObserver =
    window.MutationObserver ||
    window.WebKitMutationObserver ||
    window.MozMutationObserver;
  let body = document.querySelector("body");
  let observer = new MutationObserver(function (mutations) {
    mutations.forEach(function (mutation) {
      // needed for IE
      let nodesArray = [].slice.call(mutation.addedNodes);
      if (nodesArray.length > 0) {
        nodesArray.forEach(function (addedNode) {
          if (addedNode.querySelectorAll) {
            let elementsArray = [].slice.call(
              addedNode.querySelectorAll(selectors.self)
            );
            elementsArray.forEach(function (element) {
              instances.push(
                new FormText({ element: element, options: readData(element) })
              );
            });
          }
        });
      }
    });
  });

  observer.observe(body, {
    subtree: true,
    childList: true,
    characterData: true,
  });
  root.addEventListener("reset", () =>
    instances.forEach((instance) => {
      instance._hideErrorMessage();
      instance._elements.input.classList.remove("invalid");
      instance._setCharCount(0);
    })
  );
}

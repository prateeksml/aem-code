(function (jQuery, Coral) {
  var imageRequired = document.querySelector('[name="imageDescription"]');
  var imageDescr = document.querySelector('[name="imageAltText"]');
  if (imageRequired.checked) {
    imageDescr.setAttribute("required");
  }
  imageRequired.addEventListener("change", function () {
    var imageDescr = document.querySelector('[name="imageAltText"]');
    if (imageRequired.checked) {
      imageDescr.setAttribute("required");
    } else {
      imageDescr.removeAttribute("required");
    }
  });

  /**
   * Trigger foundation-field-change event whenever a content fragment dialog is loaded.
   */

  setTimeout(function () {
    var contentFragField = $(document).find("coral-select[name='memberFirm']");
    if (contentFragField.length > 0) {
      var foundationField = contentFragField.adaptTo("foundation-field");
      if (foundationField != undefined) {
        foundationField.setValue(foundationField.getValue());
        contentFragField.trigger("foundation-field-change");
      }
    }
  }, 700);
})(jQuery, Coral);

(function () {
  /**
   * check if the user can edit the social media multifield.
   */
  const MULTIFIELD_NAME = "socialLinks";
  const MULTIFIELD_ATTR = "data-granite-coral-multifield-name";
  const MULTIFIELD_SELECTOR = `coral-multifield[${MULTIFIELD_ATTR}="${MULTIFIELD_NAME}"]`;
  const CHECK_SERVLET = "/bin/can-edit-social-media";

  /**
   * Shows loading indicator after the provided element.
   */
  function toggleLoading(element, show) {
    const loadingClass = "kpmg-loading";
    if (show) {
      const loadingEl = document.createElement("coral-wait");
      loadingEl.classList.add(loadingClass);
      const loadingTextEl = document.createElement("span");
      loadingTextEl.innerText = "Checking your access to edit this...";
      loadingTextEl.classList.add(loadingClass);
      element.after(loadingEl, loadingTextEl);
    } else {
      const loadingEls = element.parentElement.querySelectorAll(
        `.${loadingClass}`
      );
      [].forEach.call(loadingEls, function (el) {
        el.remove();
      });
    }
  }

  /**
   * Add status after the supplied element.
   */
  function addStatus(element, variant, label) {
    const status = new Coral.Status();
    status.variant = variant;
    const labelEl = new Coral.Status.Label();;
    labelEl.innerText = label;
    status.label = labelEl;
    element.after(status);
  }

  function handleResponse(response, mutifield) {
    toggleLoading(mutifield, false);
    if (!response.ok) {
      if (response.status == 403) {
        // 403 specifically indicates user does not have access to edit.
        addStatus(
          mutifield,
          "error",
          "You do not have access to edit this field."
        );
      } else {
        // other codes indicate server error.
        addStatus(
          mutifield,
          "error",
          "There was a server error while checking your access to edit this field."
        );
      }
      mutifield.readOnly = true;
      throw Error(response.statusText);
    } else {
      addStatus(mutifield, "success", "You can edit this field.");
      mutifield.readOnly = false;
    }
  }

  // find the multifield
  const mutifield = document.querySelector(MULTIFIELD_SELECTOR);
  if (mutifield) {
    mutifield.readOnly = true; // set to read only initially, pending server response.
    toggleLoading(mutifield, true);
    
    fetch(CHECK_SERVLET)
      .then((response) => handleResponse(response, mutifield))
      .catch((err) => console.log(err));
  }
})();

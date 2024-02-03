import { ShowFormErrorOverlay } from "./common-message-overlay";
import {
  DropdownValidator,
  DataMapping,
  TextAreaPatternValidator,
} from "./digital-forms";
import { getCountryCode, getLanguageCode } from "./util/CommonUtils";
import { FormCaptcha } from "./digital-form-captcha";
import {
  HandleDatalayerEventsFormStart,
  HandleEventFormSubmitError,
} from "./datalayer";

const MESSAGE_PATTERN_FIRST_NAME = /\$FIRST_NAME/gim;
const MESSAGE_PATTERN_LAST_NAME = /\$LAST_NAME/gim;
const createRFPURL = "/rfpservice_qa/RFPReconnectService.svc/CreateNewRFP";
const modifyRFPURL = "/rfpservice_qa/RFPReconnectService.svc/ModifyRFP";
const fileDeleteURL = "/rfpservice_qa/deleteupload.axd";
const submitRFPURL = "/rfpservice_qa/RFPReconnectService.svc/SubmitRFP";
const genTokenURL = "/bin/gethmactokengenereator";
const fileUploadURL = "/rfpservice_qa/rfpupload.axd";
const getRFPDetailsURL = "/rfpservice_qa/RFPReconnectService.svc/GetRFPDetail";
let redirectURL, createdRFPId;

class MessageUpdater {
  static updateWithName(element, fName, lName, msg) {
    if (!msg) {
      msg = element.innerHTML;
    }
    msg = msg.replace(MESSAGE_PATTERN_FIRST_NAME, fName ? fName : "");
    msg = msg.replace(MESSAGE_PATTERN_LAST_NAME, lName ? lName : "");
    while (element.firstChild) {
      element.removeChild(element.lastChild);
    }
    element.appendChild(document.createTextNode(msg));
    return msg;
  }
}

class RFPService {
  constructor() {
    this.ValueChangedInFirstStepChecker = function () {
      return false;
    };
  }
  static async getAuthToken() {
    try {
      const response = await fetch(genTokenURL + "?_=" + new Date().getTime(), {
        method: "GET",
      });
      const data = await response.json();
      return data.token;
    } catch (e) {
      // FormActionShowError(e);
    }
  }

  static async getFormData(form, captchaValue) {
    let formData = new FormData(form);
    const formFieldMapping = DataMapping(form);

    // Convert form data to JSON object
    let RFPdata = {};
    RFPdata.FirstName = sanitizeHTML(formData.get(formFieldMapping.first_name));
    RFPdata.LastName = sanitizeHTML(formData.get(formFieldMapping.last_name));
    RFPdata.Email = sanitizeHTML(formData.get(formFieldMapping.email_address));
    RFPdata.Phone = sanitizeHTML(formData.get(formFieldMapping.phone));
    RFPdata.Company = sanitizeHTML(formData.get(formFieldMapping.company));
    RFPdata.JobTitle = sanitizeHTML(formData.get(formFieldMapping.role));
    RFPdata.Industry = sanitizeHTML(formData.get(formFieldMapping.industry));
    RFPdata.AlreadyClient =
      formData.get(formFieldMapping.iskpmgclient).toLowerCase() === "yes"
        ? true
        : false;
    let countryCode = sanitizeHTML(formData.get(formFieldMapping.country));
    let countryDropDown = form.querySelector(
      "[name='" + formFieldMapping.country + "']"
    );
    let countryText =
      countryDropDown.options[countryDropDown.selectedIndex].text;
    RFPdata.LocationEnglishName = countryText;
    RFPdata.LocationCode = countryCode;
    RFPdata.Territory = "";
    RFPdata.RFPPageURL = "External Source";
    RFPdata.CountryId = form.dataset.countryId ? form.dataset.countryId : "";
    RFPdata.OriginatingSiteURL =
      "/" + getCountryCode() + "/" + getLanguageCode();
    RFPdata["overview-privacy"] = "on";
    RFPdata.AnalyticId = form.dataset.analyticId ? form.dataset.analyticId : "";
    RFPdata.Submitted_via = window.location.origin;
    if (captchaValue) {
      RFPdata.CAPTCHA_VALUE = captchaValue;
    }
    return RFPdata;
  }

  static async createRFP(form, captchaValue) {
    const RFPdata = await RFPService.getFormData(form, captchaValue);
    //Convert data object to JSON string
    let jsonData = JSON.stringify(RFPdata);
    let authToken = await RFPService.getAuthToken();
    if (!authToken) {
      form.dispatchEvent(
        new CustomEvent("form-continue-error", {
          detail: { error: "Unable to get authentication token." },
        })
      );
      FormActionShowError(form, "Unable to get authentication token.");
      return;
    }

    try {
      let response = await fetch(createRFPURL, {
        method: "POST",
        body: jsonData,
        headers: {
          "Content-Type": "application/json",
          Accept: "application/json",
          AUTH_TOKEN: authToken,
        },
      });
      let responseData = await response.json();
      if (
        response.status == 200 &&
        !responseData.CreateRFPResult.ErrorInfo &&
        responseData.CreateRFPResult.RFPId
      ) {
        let continueBtn = form.querySelector('.cmp-form-button[type="button"]');
        let firstStepContainer = continueBtn.closest(".cmp-container");
        continueBtn.parentElement.remove();
        form.classList.add("show-second-step");
        createdRFPId = responseData.CreateRFPResult.RFPId;
        RFPService.ValueChangedInFirstStepChecker =
          RFPService.formDataChangedInFirstStep(firstStepContainer);
        HandleDatalayerEventsFormStart(form, form.dataset.formType, {
          step2: true,
        });
      } else {
        form.dispatchEvent(
          new CustomEvent("form-continue-error", {
            detail: {
              error: responseData.CreateRFPResult.ErrorInfo.ErrorMessage,
            },
          })
        );
        FormActionShowError(
          form,
          responseData.CreateRFPResult.ErrorInfo.ErrorMessage
        );
      }
    } catch (e) {
      form.dispatchEvent(
        new CustomEvent("form-continue-error", { detail: { error: e } })
      );
      FormActionShowError(form, e);
    }
  }

  static formDataChangedInFirstStep(element) {
    let isChanged = false;
    let items = element.querySelectorAll("input,select");
    items.forEach((field) =>
      field.addEventListener("change", () => (isChanged = true))
    );

    items = element.querySelectorAll("input");
    items.forEach((field) =>
      field.addEventListener("input", () => (isChanged = true))
    );

    return () => isChanged;
  }

  static async updateRFPInfo(rfpID, form) {
    let authToken = await RFPService.getAuthToken();
    if (!authToken) {
      form.dispatchEvent(
        new CustomEvent("form-submit-error", {
          detail: { error: "Unable to get authentication token." },
        })
      );
      FormActionShowError(form, "Unable to get authentication token.");
      return;
    }

    try {
      const modifiedData = [];
      const RFPdata = await RFPService.getFormData(form);
      for (const key in RFPdata) {
        if (RFPdata.hasOwnProperty(key)) {
          modifiedData.push({ Key: key, Value: RFPdata[key] });
        }
      }
      let submitData = {
        RFPId: rfpID,
        UpdatedFields: modifiedData,
      };
      let response = await fetch(modifyRFPURL, {
        method: "POST",
        body: JSON.stringify(submitData),
        headers: {
          "Content-Type": "application/json",
          Accept: "application/json",
          AUTH_TOKEN: authToken,
        },
      });
      let responseData = await response.json();
      if (response.status == 200 && !responseData.Result.ErrorInfo) {
        RFPService.updateRFPMessage(rfpID, form);
      } else {
        form.dispatchEvent(
          new CustomEvent("form-submit-error", {
            detail: { error: responseData.Result.ErrorInfo.ErrorMessage },
          })
        );
        FormActionShowError(form, responseData.Result.ErrorInfo.ErrorMessage);
      }
    } catch (e) {
      form.dispatchEvent(
        new CustomEvent("form-submit-error", { detail: { error: e } })
      );
      FormActionShowError(form, e);
    }
  }

  static async updateRFPMessage(rfpID, form) {
    const formFieldMapping = DataMapping(form);
    let message = sanitizeHTML(form[formFieldMapping.message].value);
    let authToken = await RFPService.getAuthToken();
    if (!authToken) {
      form.dispatchEvent(
        new CustomEvent("form-submit-error", {
          detail: { error: "Unable to get authentication token." },
        })
      );
      FormActionShowError(form, "Unable to get authentication token.");
      return;
    }

    try {
      const modifiedData = [];
      modifiedData.push({
        Key: "Message",
        Value: message,
      });
      let submitData = {
        RFPId: rfpID,
        UpdatedFields: modifiedData,
      };
      let response = await fetch(modifyRFPURL, {
        method: "POST",
        body: JSON.stringify(submitData),
        headers: {
          "Content-Type": "application/json",
          Accept: "application/json",
          AUTH_TOKEN: authToken,
        },
      });
      let responseData = await response.json();
      if (response.status == 200 && !responseData.Result.ErrorInfo) {
        RFPService.getRFPDetails(rfpID, form);
      } else {
        form.dispatchEvent(
          new CustomEvent("form-submit-error", {
            detail: { error: responseData.Result.ErrorInfo.ErrorMessage },
          })
        );
        FormActionShowError(form, responseData.Result.ErrorInfo.ErrorMessage);
      }
    } catch (e) {
      form.dispatchEvent(
        new CustomEvent("form-submit-error", { detail: { error: e } })
      );
      FormActionShowError(form, e);
    }
  }

  static async handleFileUpload(file, form, fileID, uploadedCB) {
    let cleanFilename = file.name.replace(/[^\w\s]/gi, ""),
      frmData = new FormData();
    frmData.append(cleanFilename, file);
    let authToken = await RFPService.getAuthToken();
    if (!authToken) {
      uploadedCB && uploadedCB({}, file, form, fileID);
      form.dispatchEvent(
        new CustomEvent("form-submit-error", {
          detail: { error: "Unable to get authentication token." },
        })
      );
      FormActionShowError(
        form,
        "File Upload error - Unable to get authentication token."
      );
      return;
    }

    try {
      let response = await fetch(fileUploadURL, {
        method: "POST",
        body: frmData,
        cache: "no-store",
        headers: {
          AUTH_TOKEN: authToken,
          RFPId: createdRFPId,
        },
      });
      if (response.status == 200) {
        form
          .querySelector("#" + fileID + " .file-upload__cancel-button")
          .classList.add("active");
        form.querySelector("#" + fileID + " .file-upload__loading").remove();
      } else {
        form.dispatchEvent(
          new CustomEvent("form-submit-error", {
            detail: { error: "File Upload error - " + file.name },
          })
        );
        FormActionShowError(form, "File Upload error - " + file.name);
      }
    } catch (e) {
      form.dispatchEvent(
        new CustomEvent("form-submit-error", { detail: { error: e } })
      );
      FormActionShowError(form, e);
    }
  }

  static async handleFileRemove(file, form, fileID) {
    let authToken = await RFPService.getAuthToken();
    if (!authToken) {
      form.dispatchEvent(
        new CustomEvent("form-submit-error", {
          detail: { error: "Unable to get authentication token." },
        })
      );
      FormActionShowError(form, "Unable to get authentication token.");
      return;
    }

    try {
      let response = await fetch(fileDeleteURL, {
        method: "POST",
        cache: "no-store",
        headers: {
          AUTH_TOKEN: authToken,
          RFPId: createdRFPId,
          UploadedFileName: file.name,
        },
      });
      if (response.status == 200) {
        form.querySelector("#" + fileID).remove();
      } else {
        form.dispatchEvent(
          new CustomEvent("form-submit-error", {
            detail: { error: "File Upload error - " + file.name },
          })
        );
        FormActionShowError(form, "File Upload error - " + file.name);
      }
    } catch (e) {
      form.dispatchEvent(
        new CustomEvent("form-submit-error", { detail: { error: e } })
      );
      FormActionShowError(form, e);
    }
  }

  static async handleSubmitRFP(rfpID, form) {
    let authToken = await RFPService.getAuthToken();
    if (!authToken) {
      form.dispatchEvent(
        new CustomEvent("form-submit-error", {
          detail: { error: "Unable to get authentication token." },
        })
      );
      FormActionShowError(form, "Unable to get authentication token.");
      return;
    }

    try {
      let submitData = {
        RFPId: rfpID,
      };
      let response = await fetch(submitRFPURL, {
        method: "POST",
        body: JSON.stringify(submitData),
        headers: {
          "Content-Type": "application/json",
          Accept: "application/json",
          AUTH_TOKEN: authToken,
        },
      });
      let responseData = await response.json();
      if (
        response.status == 200 &&
        !responseData.Result.ErrorInfo &&
        responseData.Result.ReferenceNo
      ) {
        let rfpReferenceNo = responseData.Result.ReferenceNo;
        localStorage.setItem("RFPRefNo", rfpReferenceNo);
        form.dispatchEvent(
          new CustomEvent("form-submit-success", { detail: { response } })
        );
        FormActionRedirect(redirectURL);
        document.querySelector("body").classList.remove("loading");
      } else {
        form.dispatchEvent(
          new CustomEvent("form-submit-error", {
            detail: { error: responseData.Result.ErrorInfo.ErrorMessage },
          })
        );
        FormActionShowError(form, responseData.Result.ErrorInfo.ErrorMessage);
      }
    } catch (e) {
      form.dispatchEvent(
        new CustomEvent("form-submit-error", { detail: { error: e } })
      );
      FormActionShowError(form, e);
    }
  }

  static async addLabelValuesToResponse(data, form) {
    let RFPData = data;
    let formElement = form;
    const formFieldMapping = DataMapping(form);
    RFPData.forEach((item) => {
      let label = "";
      if (item.Key === "FirstName") {
        label =
          formElement[formFieldMapping.first_name].getAttribute("aria-label");
      }
      if (item.Key === "LastName") {
        label =
          formElement[formFieldMapping.last_name].getAttribute("aria-label");
      }
      if (item.Key === "Email") {
        label =
          formElement[formFieldMapping.email_address].getAttribute(
            "aria-label"
          );
      }
      if (item.Key === "Phone") {
        label = formElement[formFieldMapping.phone].getAttribute("aria-label");
      }
      if (item.Key === "JobTitle") {
        label = formElement[formFieldMapping.role].getAttribute("aria-label");
      }
      if (item.Key === "Company") {
        label =
          formElement[formFieldMapping.company].getAttribute("aria-label");
      }
      if (item.Key === "Industry") {
        label =
          formElement[formFieldMapping.industry].getAttribute("aria-label");
      }
      if (item.Key === "Location") {
        label =
          formElement[formFieldMapping.country].getAttribute("aria-label");
      }
      if (item.Key === "AlreadyClient") {
        label = formElement
          .querySelector("[name='" + formFieldMapping.iskpmgclient + "']")
          .getAttribute("aria-label");
      }
      if (item.Key === "Message") {
        label =
          formElement[formFieldMapping.message].getAttribute("aria-label");
      }
      if (item.Key === "Attachments") {
        label =
          formElement[formFieldMapping.attachment].getAttribute("aria-label");
      }
      item.Text = label;
    });
    return RFPData;
  }

  static async getRFPDetails(rfpID, form) {
    let authToken = await RFPService.getAuthToken();
    if (!authToken) {
      form.dispatchEvent(
        new CustomEvent("form-submit-error", {
          detail: { error: "Unable to get authentication token." },
        })
      );
      FormActionShowError(form, "Unable to get authentication token.");
      return;
    }

    try {
      let url = `${getRFPDetailsURL}/${rfpID}?_=${new Date().getTime()}`;
      let response = await fetch(url, {
        method: "GET",
        headers: {
          AUTH_TOKEN: authToken,
        },
      });
      let responseData = await response.json();
      if (
        response.status == 200 &&
        !responseData.ResultGetRFP.ErrorInfo &&
        responseData.ResultGetRFP.RFPData
      ) {
        let RFPData = responseData.ResultGetRFP.RFPData;
        let RFPInformation = await RFPService.addLabelValuesToResponse(
          RFPData,
          form
        );
        if (RFPInformation) {
          localStorage.setItem(
            "RFPInformation",
            JSON.stringify(RFPInformation)
          );
          RFPService.handleSubmitRFP(rfpID, form);
        }
      } else {
        form.dispatchEvent(
          new CustomEvent("form-submit-error", {
            detail: { error: responseData.ResultGetRFP.ErrorInfo.ErrorMessage },
          })
        );
        FormActionShowError(
          form,
          responseData.ResultGetRFP.ErrorInfo.ErrorMessage
        );
      }
    } catch (e) {
      form.dispatchEvent(
        new CustomEvent("form-submit-error", { detail: { error: e } })
      );
      FormActionShowError(form, e);
    }
  }
}

class FormSubmitHandler {
  /**
   * @param {HTMLFormElement} formElement
   */
  constructor(formElement) {
    this.formElement = formElement;
    this.is_RFPForm = formElement.classList.contains("cmp-digital-form__rfp");
    /**@type {FormCaptcha} */
    this.FormCaptcha = new FormCaptcha(this.formElement);
    this.DropdownValidator = new DropdownValidator(formElement);
    this.TextAreaPatternValidator = new TextAreaPatternValidator(formElement);
    this._attachEvents();
  }

  CheckValidityRadioFields(fieldList) {
    let status = true; //valid
    let required = false;
    let one_checked = false;
    for (let i = 0; i < fieldList.length; i++) {
      const input = fieldList[i];
      if (input.required) {
        required = true;
      }

      input.reportValidity(); //triggers error ui
      if (input.checked) {
        one_checked = true;
      }
      if (required && one_checked) {
        break;
      }
    }

    if (required) {
      status = false;
    }
    if (one_checked) {
      status = true;
    }
    return status;
  }
  /**
   * @param {Array<string>} names names of fields to check validity for
   * @returns {boolean} true if all fields are valid, false otherwise
   * @note might trigger an 'invalid' event for any fields that are invalid.
   */
  CheckValidityForFieldList(names = []) {
    let form = this.formElement;
    return names.reduce((prev, fieldName) => {
      let status = true; //valid by default
      let field = form[fieldName]; //.reportValidity();

      if (!field) {
        return prev && status;
      }

      if (field instanceof RadioNodeList) {
        //caution:form gives RadioNodeList if there is more than one field with same name,
        //doen't matter if its of type "radio".
        status = this.CheckValidityRadioFields(field);
      } else {
        status = field.reportValidity();
      }
      return prev && status;
    }, true);
  }
  _attachEvents() {
    this.formElement.addEventListener("submit", (e) => {
      e.preventDefault();
      this.handleSubmit(e);
    });
    let submitBtn = this.formElement.querySelector('[type="submit"]');
    if (submitBtn) {
      submitBtn.addEventListener("click", () => {
        this.DropdownValidator.Validate();
        this.TextAreaPatternValidator.Validate();
        setTimeout(() => {
          this.formElement.dispatchEvent(
            new CustomEvent("focus-invalid-elements")
          );
        }, 100);
        setTimeout(() => HandleEventFormSubmitError(this.formElement), 200);
      });
    }
    if (this.is_RFPForm) {
      let continueBtn = this.formElement.querySelector(
        '.cmp-form-button[type="button"]'
      );
      if (continueBtn) {
        //continueBtn.setAttribute('disabled', true);
        continueBtn.addEventListener(
          "click",
          this.handleRFPFormContinueClick.bind(this)
        );
      }
    }
  }
  getFieldsToValidateForRFP() {
    let continueBtn = this.formElement.querySelector(
      '.cmp-form-button[type="button"]'
    );

    if (!continueBtn) {
      return [];
    }

    let container = continueBtn.closest(".cmp-container");
    if (!container) {
      return [];
    }
    let names = Object.create(null);
    container
      .querySelectorAll(
        'input[required]:not([type="hidden"]),select[required],textarea[required]'
      )
      .forEach((s) => s.name && (names[s.name] = true));
    return Object.keys(names);
  }
  handleRFPFormContinueClick() {
    let names = this.getFieldsToValidateForRFP();
    this.DropdownValidator.Validate();
    if (
      !this.DropdownValidator.AllDropdownsValid ||
      !this.CheckValidityForFieldList(names)
    ) {
      setTimeout(() => {
        this.formElement.dispatchEvent(
          new CustomEvent("focus-invalid-elements"),
          {
            detail: { fields: names },
          }
        );
      }, 100);
      setTimeout(
        () => HandleEventFormSubmitError(this.formElement, names),
        200
      );
      return;
    }
    ToggleContinueButtonState(this.formElement, false);
    redirectURL = this.formElement.dataset.redirectValue;
    if (this.FormCaptcha.IsCaptchaEnabled) {
      this.FormCaptcha.Execute(
        RFPService.createRFP.bind(undefined, this.formElement), //first param is form, 2nd is token
        this.formElement.dataset.action
      );
    } else {
      RFPService.createRFP(this.formElement);
    }
  }
  Validate() {
    if (!this.DropdownValidator.AllDropdownsValid) {
      return false;
    }

    if (!this.TextAreaPatternValidator.AllFieldsValid) {
      return false;
    }
    /**other custom validations... */
    return true;
  }
  async handleSubmit() {
    if (!this.Validate()) {
      return;
    }
    ToggleSubmitButtonState(this.formElement, false);
    if (this.is_RFPForm) {
      document.querySelector("body").classList.add("loading");
      if (RFPService.ValueChangedInFirstStepChecker()) {
        RFPService.updateRFPInfo(createdRFPId, this.formElement);
      } else {
        RFPService.updateRFPMessage(createdRFPId, this.formElement);
      }
    } else {
      if (this.FormCaptcha.IsCaptchaEnabled) {
        this.FormCaptcha.Execute((token) => {
          this.handleFormSubmit(this.formElement, token);
        }, this.formElement.dataset.action);
      } else {
        this.handleFormSubmit(this.formElement);
      }
    }
  }
  /**
   * @param {HTMLFormElement} form
   */
  async handleFormSubmit(form) {
    let response = await PostTheForm(form, (e) => {
      form.dispatchEvent(
        new CustomEvent("form-submit-error", { detail: { error: e, form } })
      );
      FormActionShowError(form, "Failed to Submit Form:" + e);
    });
    if (!response) {
      return;
    }

    if (response.status == 200) {
      if (form.dataset.redirect) {
        FormActionRedirect(form.dataset.redirectValue);
      } else {
        FormActionShowThankYou(form, response);
      }
      form.dispatchEvent(
        new CustomEvent("form-submit-success", { detail: { response } })
      );
    } else {
      form.dispatchEvent(
        new CustomEvent("form-submit-error", { detail: { response } })
      );
      FormActionShowError(
        form,
        response.statusText
          ? response.status + ":" + response.statusText
          : "status:" + response.status
      );
    }
  }
}

export function ToggleSubmitButtonState(form, state) {
  let btn = form.querySelector('[type="SUBMIT"]');
  if (btn) {
    btn.disabled = !state;
  }
}

export function ToggleContinueButtonState(form, state) {
  let btn = form.querySelector('.cmp-form-button[type="BUTTON"]');
  if (btn) {
    btn.disabled = !state;
  }
}

function sanitizeHTML(text) {
  let element = document.createElement("div");
  element.innerText = text;
  return element.innerHTML;
}

function FormActionRedirect(value) {
  window.location = value;
}

/**
 * @param {HTMLFormElement} form form element
 * @param {string|Error} err
 */
function FormActionShowError(form, err) {
  console.warn(err);
  document.querySelector("body").classList.remove("loading");
  let ElementToFocusforForm;

  if (typeof err == "string" && err.includes("File Upload error")) {
    //focus file upload button on close of error overlay.
    ElementToFocusforForm = form.querySelector(
      ".fileupload #file-upload-container"
    );
  } else {
    //focus "continue button" if available or "submit  button", on close of error overlay.
    ElementToFocusforForm =
      form.querySelector('.cmp-form-button[type="button"]') ??
      form.querySelector('.cmp-form-button[type="submit"]');
  }
  ShowFormErrorOverlay(ElementToFocusforForm);
}

function FormActionShowThankYou(form) {
  const formFieldMapping = DataMapping(form);
  let thankYouMsgContainer = form.parentElement.querySelector(
    ".form-result-container"
  );
  let fName, lName;
  try {
    (fName = sanitizeHTML(form[formFieldMapping.first_name].value)),
      (lName = sanitizeHTML(form[formFieldMapping.last_name].value));
  } catch (error) {
    console.error(error);
  }

  thankYouMsgContainer.querySelectorAll(".cmp-title__text").forEach((h2) => {
    MessageUpdater.updateWithName(h2, fName, lName);
  });
  form.classList.add("hide");
  thankYouMsgContainer.classList.add("show");
  window.scroll({
    top: 0,
    left: 0,
    behavior: "smooth",
  });
}

async function PostTheForm(form, handleError) {
  let response;
  let formData = new FormData(form);

  // Convert form data to JSON object
  let data = {};
  formData.forEach(function (value, key) {
    data[key] = sanitizeHTML(value);
  });

  // Convert data object to JSON string
  let jsonData = JSON.stringify(data);
  try {
    response = await fetch(form.dataset.action, {
      method: form.dataset.method || "POST",
      headers: { "Content-Type": "application/json" },
      body: jsonData,
    });
  } catch (error) {
    console.error(error);
    handleError && handleError(error);
  }
  return response;
}

export {
  MessageUpdater,
  RFPService,
  FormSubmitHandler,
  FormActionShowThankYou,
};

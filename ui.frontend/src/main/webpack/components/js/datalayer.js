// window.adobeDataLayer = window.adobeDataLayer || [];
const FORM_STEP_START = 1,
  FORM_STEP_SUBMIT = 2,
  RFP_FORM_STEP_FILEUPLOADED = 2,
  RFP_FORM_STEP_SECTION2 = 2,
  RFP_FORM_STEP_SUBMIT = 3;
const ComponentEventInfo = {
  rfp: {
    path: "rfp-form-",
    title: "rfp",
  },
  contact: {
    path: "generic-contact-form-",
    title: "generic contact",
  },
  peopleContact: {
    path: "people-contact-form-",
    title: "people contact",
  },
  searchInput: {
    path: "site-search_search-input-",
    title: "site search_search",
  },
  searchResult: {
    path: "site-search_search-result-",
    title: "site search_search result",
  },
  searchResultList: {
    path: "site-search_search-result-list-",
  },
};
export function HaveDataLayerEvent(obj) {
  if (!window.adobeDataLayer) {
    return false;
  }
  window.adobeDataLayer.push(obj);
  return true;
}

//#region Form Events

/**
 * @param {HTMLFormElement} form
 */
export function HandleDatalayerEventsFormStart(form, formType, details) {
  //HaveDataLayerEvent
  let resourceType = form.dataset.resourceType || "";
  let parentCmp = form.parentElement.closest(
    '[id][class^="cmp-"]:not([id=""])'
  );
  let pid = parentCmp ? parentCmp.id : "";
  let _info = ComponentEventInfo[formType];
  let formInfo = {
    formStep: FORM_STEP_START,
  };

  if (formType == "peopleContact") {
    formInfo.Contact = details && details.contact ? details.contact : "";
  } else if (formType == "rfp") {
    formInfo.referral = encodeURI(document.referrer) || "";
  }
  if (details && details.step2) {
    formInfo.formStep = RFP_FORM_STEP_SECTION2;
  }

  HaveDataLayerEvent({
    event: "cmp:formStart",
    eventInfo: {
      path: `component.${_info.path}`,
    },
    component: {
      [_info.path]: {
        "dc:title": `${_info.title} form`,
        "@type": resourceType,
        parentId: pid,
        formInfo,
      },
    },
  });
}
/**
 * @param {HTMLFormElement} form
 * @param {string} type
 */
export function HandleEventFormSubmit(form, formType, details) {
  let resourceType = form.dataset.resourceType || "";
  let parentCmp = form.parentElement.closest(
    '[id][class^="cmp-"]:not([id=""])'
  );
  let pid = parentCmp ? parentCmp.id : "";

  let _info = ComponentEventInfo[formType];
  if (formType == "rfp") {
    HaveDataLayerEvent({
      event: "cmp:formComplete",
      eventInfo: {
        path: `component.${_info.path}`,
      },
      component: {
        [_info.path]: {
          "dc:title": `${_info.title} form`,
          "@type": resourceType,
          parentId: pid,
          formInfo: {
            formStep: RFP_FORM_STEP_SUBMIT,
            referral: encodeURI(document.referrer) || "",
            rfpId: details && details.rfpid ? details.rfpid : "",
          },
        },
      },
    });
    return;
  }

  form.addEventListener("form-submit-success", () => {
    _info = ComponentEventInfo[form.dataset.formType];
    HaveDataLayerEvent({
      event: "cmp:formComplete",
      eventInfo: {
        path: `component.${_info.path}`,
      },
      component: {
        [_info.path]: {
          "dc:title": `${_info.title} form`,
          formInfo: {
            formStep: FORM_STEP_SUBMIT,
          },
        },
      },
    });
  });
}

export function HandleRFPPrintEvnt() {
  let _info = ComponentEventInfo["rfp"];
  HaveDataLayerEvent({
    event: "cmp:click",
    eventInfo: {
      path: `component.${_info.path}`,
    },
    component: {
      [_info.path]: {
        "dc:title": `${_info.title} form form_print rfp`,
      },
    },
  });
}
/**
 *
 * @param {Array<File>} files
 */
export function HandleRFPFileUploaded(files) {
  let _info = ComponentEventInfo["rfp"];
  HaveDataLayerEvent({
    event: "cmp:formFileUpdate",
    eventInfo: {
      path: `component.${_info.path}`,
    },
    component: {
      [_info.path]: {
        "dc:title": `${_info.title} form_upload file`,
        formInfo: {
          formStep: RFP_FORM_STEP_FILEUPLOADED,
          fileUploaded: {
            format: Object.keys(
              files.reduce((value, file) => {
                value[file.name.split(".").pop()] = true;
                return value;
              }, {})
            ),
            numFiles: files.length,
          },
        },
      },
    },
  });
}

/**
 * @param {Array<File>} files
 */
export function HandleRFPFileRemove(files) {
  let _info = ComponentEventInfo["rfp"];
  HaveDataLayerEvent({
    event: "cmp:formFileUpdate",
    eventInfo: {
      path: `component.${_info.path}`,
    },
    component: {
      [_info.path]: {
        "dc:title": `${_info.title} form_delete file`,
        formInfo: {
          formStep: RFP_FORM_STEP_FILEUPLOADED,
          fileUploaded: {
            format: Object.keys(
              files.reduce((value, file) => {
                value[file.name.split(".").pop()] = true;
                return value;
              }, {})
            ),
            numFiles: files.length,
          },
        },
      },
    },
  });
}

export function HandleRFPFileSelectionError(errorInfo) {
  let _info = ComponentEventInfo["rfp"];
  HaveDataLayerEvent({
    event: "cmp:formError",
    eventInfo: {
      path: `component.${_info.path}`,
    },
    component: {
      [_info.path]: {
        "dc:title": `${_info.title} form`,
        formInfo: {
          formStep: RFP_FORM_STEP_FILEUPLOADED,
          formError: errorInfo,
        },
      },
    },
  });
}

/**
 * @param {HTMLFormEleent} form
 * @param {string} type
 */
export function HandleEventFormSubmitError(form, restrictFieldNames) {
  let _info = ComponentEventInfo[form.dataset.formType];
  //path: `component.${_info.path}`
  //dc:title': `${_info.title} form`,
  //[_info.path]
  // form.addEventListener('form-submit-error', () => {
  let fieldNames = GetErrorFieldNames(form);

  if (Array.isArray(restrictFieldNames)) {
    fieldNames.error = fieldNames.error.filter(
      (strName) => restrictFieldNames.indexOf(strName) == -1
    );
    fieldNames.empty = fieldNames.empty.filter(
      (strName) => restrictFieldNames.indexOf(strName) > -1
    );
  }

  if (!(fieldNames.error.length + fieldNames.empty.length)) {
    return;
  }
  HaveDataLayerEvent({
    event: "cmp:formError",
    eventInfo: {
      path: `component.${_info.path}`,
    },
    component: {
      [_info.path]: {
        "dc:title": `${_info.title} form`,
        formInfo: {
          formError: `${fieldNames.empty.join(",")}|${fieldNames.error.join(
            ","
          )}`,
        },
      },
    },
  });
  // });
}
/**
 * @param {HTMLFormElement} form
 */
function GetErrorFieldNames(form) {
  /**@type {NodeListOf<HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement} */
  let fields = form.querySelectorAll(":invalid:not(fieldset)");
  let emptyFieldNames = {},
    errorFieldNames = {};

  fields.forEach((field) => {
    //if (field.tagName == "INPUT" || field.tagName == "TEXTAREA") {
    if (field.validity.valueMissing) {
      emptyFieldNames[field.name] = field.name;
    } else {
      errorFieldNames[field.name] = field.name;
    }
    //} else {
    //  emptyFieldNames[field.name] = field.name;
    //}
  });
  form
    .querySelectorAll("select.invalid")
    .forEach((item) => (emptyFieldNames[item.name] = item.name));

  return {
    empty: Object.keys(emptyFieldNames),
    error: Object.keys(errorFieldNames),
  };
}

export function HandleFormWarnningOverlayAppear(formType) {
  let _info = ComponentEventInfo[formType];
  HaveDataLayerEvent({
    event: "cmp:show",
    eventInfo: {
      path: `component.${_info.path}`,
    },
    component: {
      [_info.path]: {
        "dc:title": `${_info.title} form_warning overlay_popup`,
      },
    },
  });
}
export function HandleFormWarnningOverlayClick(formType, cancelOrContinue) {
  let _info = ComponentEventInfo[formType];
  //path: `component.${_info.path}`
  //dc:title': `${_info.title} form`,
  //[_info.path]

  HaveDataLayerEvent({
    event: "cmp:click",
    eventInfo: {
      path: `component.${_info.path}`,
    },
    component: {
      [_info.path]: {
        "dc:title": `${_info.title} form_warning overlay_${cancelOrContinue}`,
      },
    },
  });
}
export function HandleFormErrorOverlayAppear(formType) {
  let _info = ComponentEventInfo[formType];

  HaveDataLayerEvent({
    event: "cmp:show",
    eventInfo: {
      path: `component.${_info.path}`,
    },
    component: {
      [_info.path]: {
        "dc:title": `${_info.title} form_system error_popup`,
        formInfo: {
          formError: "|system",
        },
      },
    },
  });
}
export function HandleFormErrorOverlayClose(formType) {
  let _info = ComponentEventInfo[formType];
  HaveDataLayerEvent({
    event: "cmp:click",
    eventInfo: {
      path: `component.${_info.path}`,
    },
    component: {
      [_info.path]: {
        "dc:title": `${_info.title} form_system error_close`,
      },
    },
  });
}

//#endregion

//#region  Search Events

/**
 * @typedef {Object} SearchResultLoadInfo
 * @property {"all"|"insights"|"events"|"contacts"} basicFilter - filter applied if available. 'all' on default load
 * @property {number} numResults - number of total search results under current search term and filter
 * @property {number} searchCount - number of site searches during the same session.
 * The counter increases only when user initiates new search and gets the new search results.
 * Not updated when updating filter or going to the next result page with carousel, etc.
 * @property {number} pageCount - index of the current page in search results i.e, 1 on landing,
 * 3 when user navigated to the 3rd page with carousel etc.
 * @property {"site search"} searchPage -  fixed value
 * @property {string} inputTerm - keyword that user typed in. i.e. "Tax"
 * @property {searchMethod} searchTerm - keyword that is used for search. If user clicked autocomplete option or
 * historical search, this can be different from inputTerm. Should not be empty. i.e. "Tax Strategy"
 * @property {"typed"|"autocomplete"|"previous-search"} searchMethod - "typed" when user typed in keyword
 * and pressed enter, "autocomplete" or "previous-search" when user clicked the link on input dropdown
 * to find the results.
 * @typedef {Object} ResultTileClickInfo
 * @property {"article"|"event"|"other"} tileType - type of list tile
 * @property {number} resultPosition - the position of the clicked item from the search results page. i.e. 3
 * if user clicked the 3rd item on the search result page.
 *
 * @typedef {SearchResultLoadInfo & ResultTileClickInfo} SearchResultClickInfo
 */

/**
 * Onclick of Search Start|Cancel|Clear CTAs
 * @param {HTMLElement} cmpElem -  search component
 * @param {HTMLButtonElement} btn -  the clicked cta
 * @param {"start"|"cancel"|"clear"} eventType -  type of event
 */
export function HaveSearchInputEvents(cmpElem, eventType) {
  let _info = ComponentEventInfo["searchInput"];
  let resourceType = cmpElem.dataset.resourceType || "";
  let pid = cmpElem ? cmpElem.id : "";
  let eventName = "";
  if (eventType == "clear") {
    eventType = "input " + eventType;
    eventName = "cmp:click";
  } else if (eventType == "cancel") {
    eventName = "cmp:hide";
    ("cmp:show");
  } else if (eventType == "start") {
    eventName = "cmp:show";
  }
  HaveDataLayerEvent({
    event: eventName,
    eventInfo: {
      path: `component.${_info.path}`,
    },
    component: {
      [_info.path]: {
        "dc:title": `${_info.title} ${eventType}`,
        "@type": resourceType,
        parentId: pid,
      },
    },
  });
}

/**
 * Load of Search Result Page
 * @param {SearchResultLoadInfo} searchInfo the search info object
 */
export function HaveSearchResultLoadEvents(root, searchInfo) {
  let _info = ComponentEventInfo["searchResult"];
  let resourceType = root.dataset.resourceType || "";
  let parentCmp = root.parentElement.closest(
    '[id][class^="cmp-"]:not([id=""])'
  );
  let pid = parentCmp.id || "";
  HaveDataLayerEvent({
    event: "cmp:searchResultshow",
    eventInfo: {
      path: `component.${_info.path}`,
    },
    component: {
      [_info.path]: {
        "dc:title": `${_info.title} load`,
        "@type": resourceType,
        parentId: pid,
        searchInfo,
      },
    },
  });
}

/**
 * Onclick of Search Result Links (for all tile types except Contact)
 * @param {string} title the title of the search result tile
 * @param {string} link the url of the search result tile
 * @param {SearchResultClickInfo} searchInfo the search info object
 */
export function HaveSearchResultListEvents(pid, title, link, searchInfo) {
  let _info = ComponentEventInfo["searchResultList"];
  let resourceType = "";
  HaveDataLayerEvent({
    event: "cmp:searchResultclick",
    eventInfo: {
      path: `component.${_info.path}`,
    },
    component: {
      [_info.path]: {
        "dc:title": title,
        "xdm:linkURL": link,
        "@type": resourceType,
        parentId: pid,
        searchInfo,
      },
    },
  });
}

let objSearchInfo = {};
const storageKeyName = "search_datalayer_info";
export function SaveSearchInfoForDataLayer(name, value) {
  try {
    let storageItem = JSON.parse(sessionStorage.getItem(storageKeyName));
    if (!storageItem) {
      storageItem = {};
    }
    objSearchInfo = storageItem;
    objSearchInfo[name] = value;
    sessionStorage.setItem(storageKeyName, JSON.stringify(objSearchInfo));
  } catch (error) {
    console.warn(error);
  }
  return value;
}
export function GetSearchInfoForDataLayer(name) {
  try {
    let storageItem = JSON.parse(sessionStorage.getItem(storageKeyName));
    return storageItem ? storageItem[name] : undefined;
  } catch (error) {
    console.warn(error);
  }
}
export function ClearSearchInfoForDataLayer(name, all) {
  try {
    if (all) {
      sessionStorage.removeItem(storageKeyName);
      return;
    }
    let storageItem = JSON.parse(sessionStorage.getItem(storageKeyName));
    objSearchInfo = storageItem;
    delete objSearchInfo[name];
    sessionStorage.setItem(storageKeyName, JSON.stringify(objSearchInfo));
  } catch (error) {
    console.warn(error);
  }
}
function InitSearchInputEvents() {
  let componentElement = document.querySelector(".cmp-search");
  let searchCta = document.querySelector(".cmp-search__cta");
  if (searchCta) {
    searchCta.addEventListener("click", () =>
      HaveSearchInputEvents(componentElement, "start")
    );
  }

  let closeCta = document.querySelector(".cmp-search__cancel-cta");
  if (closeCta) {
    closeCta.addEventListener("click", () =>
      HaveSearchInputEvents(componentElement, "cancel")
    );
  }
}

if (document.readyState !== "loading") {
  InitSearchInputEvents();
} else {
  document.addEventListener("DOMContentLoaded", InitSearchInputEvents);
}

//#endregion

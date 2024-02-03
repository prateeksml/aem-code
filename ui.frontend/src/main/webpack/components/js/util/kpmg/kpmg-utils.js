//NOTE: PLACE ONLY COMMON AND REUSABLE VARIABLES AND METHODS/FUNCTIONS THAT CAN BE USED IN ANY COMPONENT
//WARNING: ANYTHING COMPONENT SPECIFIC FOUND IN THIS FILE WILL BE REMOVED

const libName = "KPMGUtils";

const docRoot = document.documentElement;
const breakpoint = {
  tablet: 768,
  "desktop-small": 1024,
  "desktop-medium": 1440,
};

const getType = (variable) => typeof variable;

const isDefined = (variable) => {
  if (variable) {
    return getType(variable) !== "undefined" ? variable : false;
  } else {
    return false;
  }
};

//Getters
const getViewportWidth = () => window.innerWidth;
const getViewportHeight = () => window.innerHeight;

const getKeyID = (evt) => {
  const { key, code } = evt;
  return key ? key.toLowerCase() : code.toLowerCase();
};

function sanitizeInputValue(val) {
  if (val && val.length !== 0) {
    // Use RegEx to identify HTML tags in the input string and remove them from string
    //TODO see if there's a way to merge these RegExs into one
    return val
      .toString()
      .replace(/(<([^>]+)>)/gi, "")
      .replace(/[@#<>=`%'";:&~^!*+?^${}()|\/[\]\\]/g, "");
  } else {
    console.warn(
      `${libName}: sanitizeInputValue: Value passed was not defined or empty`
    );
  }
}

function getCookie(name) {
  let cookie = {};

  document.cookie.split(";").forEach(function (el) {
    let [k, v] = el.split("=");
    cookie[k.trim()] = v;
  });
  return cookie[name] ? JSON.parse(cookie[name]) : false;
}

function setCookie(name, value, days) {
  let expires;
  if (days) {
    let date = new Date();
    date.setTime(date.getTime() + days * 24 * 60 * 60 * 1000);
    expires = `expires=${date.toGMTString()};`;
  } else {
    expires = "";
  }
  const cookie = `${name}=${value};${expires}; path=/`;
  document.cookie = cookie;
}

function createElement(elementType = "div", props = {}) {
  const newElement = document.createElement(elementType);

  if (props.classList) {
    if (typeof props.classList === "string") {
      newElement.classList.add(props.classList);
    } else {
      const classArray = props.classList;
      classArray.forEach((className) => newElement.classList.add(className));
    }
  }

  if (props.textContent) {
    newElement.textContent = props.textContent;
  }

  if (props.attributes) {
    const attrsArr = props.attributes;
    Object.keys(attrsArr).forEach((attribute) => {
      const attr = attrsArr[attribute];
      for (const key in attr) {
        newElement.setAttribute(key, attr[key]);
      }
    });
  }

  return newElement;
}

function query(selector = "", options = {}) {
  const logID = `######## ${libName}: query:`;

  const selectorType = getType(selector);
  const isCorrectArgType = selectorType === "string" ? true : false;

  let $parent = document;
  //Wait param is set to true by default
  let $wait = true;

  if (!isCorrectArgType) {
    throw new Error(`${logID} param of "selector" needs be a type of string!`);
  } else if (selector.length === 0) {
    // eslint-disable-next-line max-len
    throw new Error(
      // eslint-disable-next-line max-len
      `${logID} param of "selector" was an empty string. Please provide a valid selector! Possible types: id, class, data attribute.`
    );
  } else {
    const checkParamsType = getType(options);

    if (checkParamsType !== "object") {
      throw new Error(`${logID} param of "options" needs be a type of string!`);
    }

    //Check for wait in the options
    if (options.wait) {
      $wait = options.wait;
    }

    const resultsPromise = new Promise((resolve) => {
      let queryResults = $parent.querySelectorAll(selector);
      const queryResultsLength = queryResults.length;

      if (queryResultsLength !== 0) {
        //If only one result found in NodeList
        //Return it else return full Node List
        resolve(queryResultsLength === 1 ? queryResults[0] : queryResults);
      } else {
        if ($wait) {
          console.log(
            `******${logID} wait has been set to true for ${selector}. Setting up observer.`
          );

          //If wait is set to true, we will assign an observer that will callback
          //once the query is found in the DOM
          const observer = new MutationObserver((mutations) => {
            queryResults = $parent.querySelectorAll(selector);
            if (queryResults) {
              console.log(
                `${logID} mutations detected: ${mutations} on ${$parent} results are: ${queryResults}, resolving.`
              );
              observer.disconnect();
              resolve(queryResults);
            }
          });

          observer.observe($parent, {
            childList: true,
            subtree: true,
          });
        } else {
          //No waiting, return false
          resolve(false);
        }
      }
    });
    return resultsPromise;
  }
}

export {
  docRoot,
  breakpoint,
  getViewportWidth,
  getViewportHeight,
  getKeyID,
  sanitizeInputValue,
  getCookie,
  setCookie,
  createElement,
  query,
};

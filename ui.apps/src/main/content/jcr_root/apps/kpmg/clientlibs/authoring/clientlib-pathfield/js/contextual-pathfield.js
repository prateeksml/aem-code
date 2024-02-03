(function (document, $, Granite) {
  "use strict";

  var PICKER_CLASS = ".cmp-kpmg-contextual-pathfield-picker"
  var PICKER_SRC_ATTR = "pickersrc";
  var SUGGESTION_ATTR = "data-foundation-picker-buttonlist-src";
  var PICKER_STRATEGY_ATTR = "data-kpmg-picker-strategy";
  var PICKER_STRATEGY_CHILD_PAGES = "child-pages";
  var PICKER_STRATEGY_CONTEXTUAL = "contextual";

  function getPagePath() {
    if (Granite && Granite.author && Granite.author.page && Granite.author.page.path){
      return Granite.author.page.path;
    } else {
      // when dialog is open in full screen (smaller devices)
      var expectedPathStart = '/content/kpmgpublic';
      var expectedPathEnd = 'jcr:content';
      var pathBetween = location.href.match(expectedPathStart + "(.*?)" + expectedPathEnd)[1];
      return expectedPathStart + pathBetween;
    }
  }

  /**
   * Update query parameter of a string url
   * @param {String} url 
   * @param {String} key 
   * @param {String} value 
   */
  function updateUrlSearchParam(urlString, key, value) {
    if (!urlString || !key || !value) return;
    else {
      var url = new URL(urlString, location.origin);
      var search_params = url.searchParams;
      search_params.set(key, value);
      url.search = search_params.toString();
      return decodeURI(url.toString().replace(location.origin, ''));
    }
  }

   /**
   * Add label after the supplied element.
   * https://opensource.adobe.com/coral-spectrum/documentation/file/coral-spectrum/coral-component-banner/src/scripts/Banner.js.html#lineNumber50
   */
   function addLabel(element, label) {
    const banner = new Coral.Banner();
    banner.variant = "info";
    const labelEl = new Coral.Banner.Content();
    labelEl.innerText = label;
    banner.content = labelEl;
    element.after(banner);
  }

  /**
   * 
   * @param {HTMLElement} pickerElement 
   * @returns 
   */
  function updatePickerWithContextualPath(pickerElement, contextualRootPath) {
    if (!pickerElement) return; // exit, no element
    if (pickerElement.nodeName !== "FOUNDATION-AUTOCOMPLETE") return; // exit, not a picker

    // update the picker src
    if (pickerElement.pickerSrc) {
      var newPickerSrcUrl = updateUrlSearchParam(pickerElement.pickerSrc, 'root', contextualRootPath);
      pickerElement.setAttribute(PICKER_SRC_ATTR, newPickerSrcUrl);
    }
    // update the suggestion src
    var suggesstionEl = pickerElement.querySelector(`[${SUGGESTION_ATTR}]`);
    if (suggesstionEl) {
      var suggestionUrl = suggesstionEl.getAttribute(SUGGESTION_ATTR);

      if (suggestionUrl) {
        var newSuggestionsUrl = updateUrlSearchParam(suggestionUrl, 'root', contextualRootPath);
        suggesstionEl.setAttribute(SUGGESTION_ATTR, newSuggestionsUrl);
      }
    }
  }

  function getContextualRootPath(pickerElement) {
     // get the contextual path
     var pagePath = getPagePath();
     var country = pagePath.split("/")[3];
     if (country == "language-masters") {
       country = "xx";
     }
     var contextFolderPath= $(pickerElement).attr('data-contextfolder');
     return '/content/dam/kpmgsites/' + country + contextFolderPath;
  }


  /**
   * init contextual picker
   * @param {HTMLElement} pickerElement 
   */
  function initContextualPicker(pickerElement) {

    if (pickerElement.hasAttribute('data-contextual-ready')) return; // exit, already initialized

    var strategy = pickerElement.getAttribute(PICKER_STRATEGY_ATTR);
    if (!strategy) {
      strategy = PICKER_STRATEGY_CONTEXTUAL; // default to contextual strategy
    }

    var contextualRootPath;
    if (strategy === PICKER_STRATEGY_CHILD_PAGES) { // child page strategy
      contextualRootPath = getPagePath();
    } else if (strategy === PICKER_STRATEGY_CONTEXTUAL) { // explicit contextual root path
      contextualRootPath = getContextualRootPath(pickerElement);
    }

    // update the picker path
    updatePickerWithContextualPath(pickerElement, contextualRootPath);
    // mark as ready to prevent reinitialization
    pickerElement.setAttribute('data-contextual-ready', 'true');
    addLabel(pickerElement, "Note: this pathfield is restricted to the path: " + contextualRootPath + ". if that path does not exist, this picker will not work.");
  }


  /**
   * init all contextual pickers within given scope
   * @param {HTMLElement} scope 
   */
    function initAllContextualPickers(scope) {
      console.debug("initAllContextualPickers", scope);
      var pickerEls = scope.querySelectorAll(PICKER_CLASS);
      [].forEach.call(pickerEls, function(pickerElement) {
        initContextualPicker(pickerElement)
      });
    }


  $(document).on("dialog-loaded", function (e) {

    // init all contextual pickers within the dialog
    initAllContextualPickers(document)

    // listen to coral-multifield-add event to init contextual pickers within multifield
    var multifields = document.querySelectorAll("coral-multifield");
    [].forEach.call(multifields, function(multifield) {
      // https://opensource.adobe.com/coral-spectrum/documentation/typedef/index.html#static-typedef-coral-collection:add
      multifield.addEventListener('coral-collection:add', function(event) {
        // ensure the event was specifically triggered on current element
        if (event.target !== multifield) return;
        if (event.detail && event.detail.item) {
          var item = event.detail.item;
          initAllContextualPickers(item);
        }
      });
    });
  });
})(document, jQuery, Granite);

(async function () {
  ("use strict");
  /**@type{import('jquery')}*/
  const $ = window.jQuery || $ || Granite.$;
  const masterpagePath = "/content/kpmgpublic/language-masters";
  const TagsPropertyListMap = {
    geography: {
      "sl-geography-zthesid": "geographyId",
      "sl-geography-hierarchy": "geographyPath",
      "sl-geography-hierarchy-id": "geographyidPath",
      "sl-geography-iso3166-2": "geographyIso31662",
      "sl-geography-unm49-region": "geographyUnm49Region",
      "sl-geography-unm49-subregion": "geographyUnm49SubRegion",
      "sl-geography-iso3166-3": "geographyIso31663",
      "sl-geography-iso3166": "geographyIso3166",
      "sl-geography-unm49-subsubregion": "geographyUnm49SubSubRegion",
      "sl-geography-qualified-name": "geographyQualifiedName",
      "sl-geography-qualified-name-id": "geographyidDisplayPath",
      "sl-geography-keywords": "geographyKeywords"
    },
    persona: {
      "sl-persona-zthesid": "personaID",
      "sl-persona-hierarchy": "personaPath",
      "sl-persona-hierarchy-id": "personaidPath",
      "sl-persona-qualified-name": "personaQualifiedName",
      "sl-persona-qualified-name-id": "personaidDisplayPath"
    },
    contenttype: {
      "sl-content-zthesid": "contenttypeID",
      "sl-content-hierarchy": "contenttypePath",
      "sl-content-hierarchy-id": "contenttypeidPath",
      "sl-content-qualified-name": "contenttypeQualifiedName",
      "sl-content-qualified-name-id": "contenttypeidDisplayPath"
    },
    market: {
      "sl-market-zthesid": "marketId",
      "sl-market-hierarchy": "marketPath",
      "sl-market-hierarchy-id": "marketidPath",
      "sl-market-qualified-name": "marketQualifiedName",
      "sl-market-qualified-name-id": "marketidDisplayPath",
      "sl-market-keywords": "marketKeywords"
    },
    mediaformats: {
      "sl-media-zthesid": "mediaformatsID",
      "sl-media-hierarchy": "mediaformatsPath",
      "sl-media-hierarchy-id": "mediaformatsidPath",
      "sl-media-qualified-name": "mediaformatsQualifiedName",
      "sl-media-qualified-name-id": "mediaformatsidDisplayPath"
    },
    insight: {
      "sl-insight-zthesid": "insightId",
      "sl-insight-hierarchy": "insightPath",
      "sl-insight-hierarchy-id": "insightidPath",
      "sl-insight-qualified-name": "insightQualifiedName",
      "sl-insight-qualified-name-id": "insightidDisplayPath",
      "sl-insight-keywords": "insightKeywords"
    },
    insightCommon: {
      "sl-insight-common-zthesid": "insightCommonId",
      "sl-insight-common-hierarchy": "insightCommonPath",
      "sl-insight-common-hierarchy-id": "insightCommonidPath",
      "sl-insight-common-keywords": "insightCommonKeywords"
    },
    industry: {
      "sl-industry-zthesid": "industryId",
      "sl-industry-hierarchy-id": "industryidPath",
      "sl-industry-hierarchy": "industryPath",
      "sl-industry-qualified-name": "industryQualifiedName",
      "sl-industry-qualified-name-id": "industryidDisplayPath",
      "sl-industry-keywords": "industryKeywords"
    },
    industryCommon: {
      "sl-industry-common-zthesid": "industryCommonId",
      "sl-industry-common-hierarchy": "industryCommonPath",
      "sl-industry-common-hierarchy-id": "industryCommonidPath",
      "sl-industry-common-keywords": "industryCommonKeywords"
    },
    service: {
      "sl-service-zthesid": "serviceId",
      "sl-service-hierarchy-id": "serviceidPath",
      "sl-service-hierarchy": "servicePath",
      "sl-service-qualified-name": "serviceQualifiedName",
      "sl-service-qualified-name-id": "serviceidDisplayPath",
      "sl-service-keywords": "serviceKeywords"
    },
    serviceCommon: {
      "sl-service-common-zthesid": "serviceCommonId",
      "sl-service-common-hierarchy-id": "serviceCommonidPath",
      "sl-service-common-hierarchy": "serviceCommonPath",
      "sl-service-common-keywords": "serviceCommonKeywords"
    },
    allformats: {
      "sl-tag-path": "titlePath",
      "sl-tag-ID": "tagID"
    }
  };
  const TagsUniquePropertyMap = {
    geography: 'sl-geography-zthesid',
    persona: 'sl-persona-zthesid',
    contenttype: 'sl-content-zthesid',
    market: 'sl-market-zthesid',
    mediaformats: 'sl-media-zthesid',
    insight: 'sl-insight-zthesid',
    insightCommon: 'sl-insight-common-zthesid',
    industry: 'sl-industry-zthesid',
    industryCommon: 'sl-industry-common-zthesid',
    service: 'sl-service-zthesid',
    serviceCommon: 'sl-service-common-zthesid',
    allformats: 'sl-tag-ID'
  }
  async function readAllSavedProperties(jsonPath, errorCB) {
    let resp;
    try {
      resp = await fetch(jsonPath);
    } catch (error) {
      return errorCB && errorCB(error);
    }
    if (resp.status !== 200) {
      return errorCB("failed to tags!\n" + resp.statusText);
    }
    return await resp.json();
  }
  function ReadSmartLogicTagListService(url, category, path) {
    let fullRelativeUrl = `${url}?page=${encodeURI(path)}&category=${encodeURI(
      category
    )}`;
    return async function () {
      try {
        var resp = await fetch(fullRelativeUrl);
      } catch (error) {
        console.error(error);
        return [];
      }
      if (!resp && resp.status !== 200) return [];
      try {
        let data = await resp.json();
        return data;
      } catch (error) {
        console.error(error);
        return [];
      }
    };
  }

  const PagePropertiesReader = () => {
    let result = {};

    let url = new URL(window.location);
    let pagePath = url.searchParams.get("item");
    let jsonPath = `${url.protocol}//${url.host}${pagePath}/jcr:content.infinity.json`;

    return async (errorCB, force = false) => {
      if (force || Object.keys(result).length <= 0) {
        try {
          result = await readAllSavedProperties(jsonPath, errorCB);
        } catch (error) {
          return {};
        }
      }

      return (result && Object.keys(result).length > 0) ? result : {};
    };
  };


  class CustomModelUI {
    constructor(selector, onCloseCB, uid) {
      let root = $(selector);
      this.ui = {
        root,
        buttonX: root.find(".close-icon-btn"),
        buttonClose: root.find(".close-btn"),
      };
      this.CloseCallback = onCloseCB;
      this._attachEvents();
      root.attr('data-uid', uid);
    }

    showModal(clear) {
      if (clear) this._clearContent();
      this.ui.root.addClass("show");
      this.ui.root.attr("aria-hidden", false);
      this._adjustModelHeight();
      $('body').addClass('body-no-scroll');
      window.parent && window.parent.document && window.parent.document.documentElement.querySelector('body').classList.add('body-no-scroll');
    }
    hideModal() {
      this.ui.root.removeClass("show");
      this.ui.root.attr("aria-hidden", true);
      $('body').removeClass('body-no-scroll');
      window.parent && window.parent.document && window.parent.document.documentElement.querySelector('body').classList.remove('body-no-scroll');
    }
    _escapeKeyHideModal() {
      this.CloseCallback && this.CloseCallback(); //blocking callback,model wont close unless it completes
      this.hideModal();
    }
    _attachEvents() {
      this.ui.buttonX.length &&
        (this.ui.buttonX[0].onclick = (e) => {
          this.CloseCallback && this.CloseCallback(); //blocking callback,model wont close unless it completes
          this.hideModal();
        });
      this.ui.buttonClose[0].onclick = (e) => {
        this.CloseCallback && this.CloseCallback(true); //blocking callback,model wont close unless it completes
        this.hideModal();
      };

      window &&
        window.addEventListener("resize", (e) => {
          this._adjustModelHeight();
        });
    }
    _adjustModelHeight() {
      let modal_height = this.ui.root.height();
      let other_modal_element_height = 0;
      this.ui.root
        .find(
          ".dialog-container-title,.search-control,.dialog-footer,.dialog-container-content .header-area"
        )
        .each(function () {
          other_modal_element_height += $(this).height();
        });
      let targetElement = this.ui.root.find(
        ".dialog-container-content .tag-list"
      );
      targetElement.height(
        (modal_height - other_modal_element_height - 95).toFixed(0)
      );
    }
  }

  function sanitizeHTML(text) {
    var element = document.createElement("div");
    element.innerText = text;
    return element.innerHTML;
  }

  class clsJCRProperties {
    constructor() {
    }

    createTypeHintElement(name, dataType) {
      return this.createHiddenElement(name + "@TypeHint", dataType);
    }
    createDeleteHintElement(name, disabled) {
      return this.createHiddenElement(name + "@Delete", undefined, disabled);
    }
    createPatchHintElement(name, dataType) {
      return this.createHiddenElement(name + "@Patch", dataType);
    }

    createHiddenElement(name, value, disabled) {
      let input = document.createElement("input");
      (input.type = "hidden"), (input.name = "./" + name);
      if (value != undefined) {
        input.value = sanitizeHTML(value);
      }
      disabled && (input.disabled = true);
      return input;
    }
  }

  class TagPickerProperties extends clsJCRProperties {
    constructor(category, rootElement, disabled, masterCheck) {
      super();
      this.ui = { root: rootElement };
      this.category = category;
      this.masterCheck = !!masterCheck;
      this.disabled = disabled;
      this.prop_setting = this.getAllPropNames();
      this.savedProps = {};
    }
    init(savedPageProps) {
      // let count = 0;
      let props = this.prop_setting.propertyMap;
      // for (const propName in props) {
      //   if (
      //     Object.hasOwnProperty.call(props, propName) &&
      //     Object.hasOwnProperty.call(savedPageProps, propName)
      //   ) {
      //     count++;
      //     this.savedProps[propName] = savedPageProps[propName];
      //   }
      // }
      this._createTypeHint(props);
      this._createDeleteHint(props);
      // return count;
    }

    createElementForCategory(name, value, disabled = undefined, cls = "") {
      let input = this.createHiddenElement(name, value, disabled); //todo:sanitize attr value?
      cls && input.classList.add(cls);
      return input;
    }
    _createTypeHint(props) {
      let input = this.createTypeHintElement(this.category, "String[]"); //type is fixed for now
      this.ui.root.append(input);
      for (const propName in props) {
        if (Object.hasOwnProperty.call(props, propName)) {
          input = this.createTypeHintElement(propName, "String[]"); //type is fixed for now
          this.ui.root.append(input);
        }
      }
    }
    _createDeleteHint(props) {
      let input = this.createDeleteHintElement(this.category, this.disabled);
      this.ui.root.append(input);
      for (const propName in props) {
        if (Object.hasOwnProperty.call(props, propName)) {
          input = this.createDeleteHintElement(propName, this.disabled);
          this.ui.root.append(input);
        }
      }
    }
    getPropertyNameWithMasterCheck(category) {
      if (!category) category = this.category;
      if (!this.masterCheck) return category;
      switch (category) {
        case 'insight':
        case 'industry':
        case 'service':
          return this.category + "Common";
        default: return category;
      }
    }

    getDisplayFieldName(needJsonName = false) {
      if (!needJsonName) return DisplayFieldName[this.category];
    }
    getAllPropNames() {
      let qualifiedName, idDisplayPath, propertyMap;
      switch (String(this.category).toLowerCase()) {
        case "contenttype":
          qualifiedName = "contenttypeQualifiedName";
          idDisplayPath = "contenttypeID";
          propertyMap = TagsPropertyListMap["contenttype"];
          break;
        case "persona":
          qualifiedName = "personaQualifiedName";
          idDisplayPath = "personaID";
          propertyMap = TagsPropertyListMap["persona"];
          break;
        case "market":
          qualifiedName = "marketQualifiedName";
          idDisplayPath = "marketId";
          propertyMap = TagsPropertyListMap["market"];
          break;
        case "geography":
          qualifiedName = "geographyQualifiedName";
          idDisplayPath = "geographyId";
          propertyMap = TagsPropertyListMap["geography"];
          break;
        case "insight":
          qualifiedName = "insightQualifiedName";
          idDisplayPath = "insightId";
          if (this.masterCheck) {
            propertyMap = TagsPropertyListMap["insightCommon"];
          } else {
            propertyMap = TagsPropertyListMap["insight"];
          }
          break;
        case "service":
          qualifiedName = "serviceQualifiedName";
          idDisplayPath = "serviceId";
          if (this.masterCheck) {
            propertyMap = TagsPropertyListMap["serviceCommon"];
          } else {
            propertyMap = TagsPropertyListMap["service"];
          }
          break;
        case "industry":
          qualifiedName = "industryQualifiedName";
          idDisplayPath = "industryId";
          if (this.masterCheck) {
            propertyMap = TagsPropertyListMap["industryCommon"];
          } else {
            propertyMap = TagsPropertyListMap["industry"];
          }
          break;
        case "mediaformats":
          qualifiedName = "mediaformatsQualifiedName";
          idDisplayPath = "mediaformatsID";
          propertyMap = TagsPropertyListMap["mediaformats"];
          break;

        case "all":
          qualifiedName = "titlePath";
          idDisplayPath = "tagID";
          propertyMap = TagsPropertyListMap.get("allformats");
          break;
      }
      return { qualifiedName, idDisplayPath, propertyMap };
    }
  }

  class TagsElementUI {
    constructor(category, root, list, jcrElementHandler, parent) {
      this.category = category;
      this.ui = { root, list };
      /**@type {TagPickerProperties} */
      this.TagPropertyHandler = jcrElementHandler;
      this.tagPicker = parent;
    }

    addTagItem(value, displayText, cls) {
      let item = document.createElement("li");
      item.setAttribute("data-id", value);

      if (cls) item.className = "tag-item " + cls;
      else item.className = "tag-item ";
      item.appendChild(document.createTextNode(displayText));

      let button = document.createElement("button");
      button.className = "close-btn";
      button.setAttribute('is', 'coral-button');
      button.append("x");
      button.onclick = (e) => {
        this._removeClick(e);
      };

      item.appendChild(button);
      this.ui.list.append(item);
      this.ui.list.find("p").removeClass("show");
      return item;
    }
    _removeClick(e) {
      $(e.target).closest("li.tag-item").remove();
      if (!this.ui.list.find("li.tag-item").length) {
        this.ui.list.find("p").addClass("show");
        this.tagPicker.RequiredValidation.clearValue();
      }
    }
    removeAll() {
      this.ui.list.find('li.tag-item').each(function () {
        $(this).remove();
      });
    }
  }

  class TagListMultiSelect {
    constructor(root) {
      this.ui = {
        root,
        list: root.find("ul.tag-list"),
        itemCount: root.find(".item-count"),
      };
      this._attachEvents();
      this.handlers = { selected: [], unselected: [] };
      this.selectedCount = 0;
      this.itemCount = 0;
    }

    /**
     * Represents a callback function with three arguments.
     * @callback TagListMultiSelectEventhandler
     * @param {string} value -the value passed durring addAnItem call
     * @param {any} displayText - the displayText value passed durring addAnItem call
     * @param {jQuery} checkbox - The jquery checkbox elemenet
     * @returns {void}
     */

    /**
     * @param {'selected'|'unselected'} name event name
     * @param {TagListMultiSelectEventhandler} handler handler function
     */
    on(name, handler) {
      if (this.handlers[name])
        //if supported
        this.handlers[name].push(handler); //add
    }
    off(name, handler) {
      let handlers = this.handlers[name];
      if (handler && handlers && handlers.length) {
        //remove one handler
        for (let id = handlers.length - 1; id >= 0; id--) {
          const callback = handlers[id];
          if (callback == handler) {
            handlers.splice(id, 1);
          }
        }
      } else if (handlers && handlers.length) {
        //clear all
        this.handlers[name] = [];
      }
    }

    _itemSelected(e) {
      let checkbox = $(e.target);
      let displayText = checkbox.siblings("span").text();
      let value = checkbox.attr("value");
      if (e.target.checked) {
        this.selectedCount++;
        requestAnimationFrame(() => {
          this.handlers["selected"].forEach((callback) => {
            callback(value, displayText, checkbox);
          });
        });
      } else {
        this.selectedCount--;
        requestAnimationFrame(() => {
          this.handlers["unselected"].forEach((callback) => {
            callback(value, displayText, checkbox);
          });
        });
      }
      this._applySelectedUI(checkbox);
    }
    selectItem(value) {
      let ret = false;
      let listitem = this.ui.list.find(
        `li[data-id="${value}"]`
      );
      if (listitem.length) {
        let checkbox = listitem.find('input[type="checkbox"]');
        checkbox.prop("checked", true);
        this.selectedCount++;
        this._applySelectedUI(checkbox);
        ret = true;
      }
      return ret;
    }
    getSelectedItems() {
      return this.ui.list.find("li.selected input");
    }
    _applySelectedUI(checkbox) {
      if (checkbox.prop("checked")) {
        checkbox.closest("li").addClass("selected");
      } else checkbox.closest("li").removeClass("selected");
      this.ui.itemCount.html(this.selectedCount + " " +
        (this.selectedCount == 1 ? " Selected Item" : 'Selected Items'));
    }
    clearSelection() {
      this.selectedCount = 0;
      this.ui.list.find("li.tag-list-item.selected").each(function (item) {
        $(this)
          .removeClass("selected")
          .find('input[type="checkbox"]')
          .prop("checked", false);
      });
      this.ui.itemCount.html(this.selectedCount + " " +
        (this.selectedCount == 1 ? " Selected Item" : 'Selected Items'));
    }
    addAnItem(dataid, displayText, attributes, selected) {
      let listItem = document.createElement("li");
      listItem.classList.add("tag-list-item");
      listItem.setAttribute('data-id', dataid);

      let input = document.createElement("input");
      input.type = "checkbox";
      input.onchange = (e) => {
        this._itemSelected(e);
      };

      input.setAttribute("data-source-attributes", JSON.stringify(attributes));
      let label = document.createElement("label");
      let text = document.createElement("span");
      text.className = "tag-list-item-text";
      text.appendChild(document.createTextNode(displayText));

      label.appendChild(input);
      label.appendChild(text);
      listItem.appendChild(label);

      this.ui.list.append(listItem);
      this.itemCount++;
      if (selected) {
        this.selectedCount++;
        input.checked = true;
        listItem.classList.add('selected');
        this.ui.itemCount.html(this.selectedCount + " " +
          (this.selectedCount == 1 ? " Selected Item" : 'Selected Items'));
      }
      return listItem;
    }
    filter(textToMatch) {
      textToMatch = String(textToMatch).toLowerCase();
      let listItems = this.ui.list.find("li.tag-list-item");
      // if (textToMatch.replace(/\s/gi, "").length == 0) {
      //   return listItems.removeClass("hide");
      // }
      listItems.each(function () {
        var listItem = $(this);
        var listItemText = listItem.text().toLowerCase();
        if (listItemText.includes(textToMatch)) {
          listItem.removeClass("hide");
        } else {
          listItem.addClass("hide");
        }
      });
    }
    clear() {
      this.selectedCount = 0;
      this.ui.list.empty();
    }
    _attachEvents() {
      // this._itemSelected = this._itemSelected.bind(this);
    }
  }

  class MandatorySelectionValidation {
    constructor(categoryName, root) {
      this.Category = categoryName || '';
      /**@type {HTMLInputElement} */
      this.Input = null;
      this.$field = null;
      this.fieldAPI = null;

      /**@type {HTMLElement} */
      this.ErrorMessageElement = null;

      this._init(root);
    }

    _init(root) {
      this.Input = root.querySelector('.required-field');
      this.$field = $(this.Input);
      this.fieldAPI = this.$field.adaptTo("foundation-field");
      this.validation = this.$field.adaptTo("foundation-validation");
    }
    haveValue() {
      this.Input.value = ' ';
      this.upateErrorMessage();
    }
    clearValue() {
      this.Input.value = '';
      this.upateErrorMessage();
    }
    upateErrorMessage() {
      // this.fieldAPI.setRequired(this.required);
      this.validation.checkValidity();
      this.validation.updateUI();
    }
    // hideErrorMessage() {
    //   this.Input.reportValidity();
    //   // this.ErrorMessageElement.style.display = 'none';
    // }
    // addErrorMessageUI() {
    //   let p = this.ErrorMessageElement = document.createElement('label');
    //   p.appendChild(document.createTextNode(this.Message));
    //   p.style.display = 'none';
    //   p.classList.add('coral-Form-errorlabel');
    //   this.RootDiv.appendChild(p);
    // }
  }

  class TagPicker {
    constructor(root) {
      this.config = root[0].dataset;
      this.currentCategory = this.config.category || "contenttype";
      this.disabled = !!this.config.disabled;
      this.IsMasterPage = this.InheritanceLocked = false;
      this.modal = new CustomModelUI(
        root.find(".dialog-container"),
        isSaveClick => (isSaveClick ? this._onSave() : this._onClose()),
        this.currentCategory
      );

      this.RequiredValidation = new MandatorySelectionValidation(this.currentCategory,root[0]);

      this.ui = {
        root,
        clearSelectedBtn: root.find(".dialog-container .header-area button"),
      };
      this.tagListUIModel = new TagListMultiSelect(
        root.find(".dialog-container .dialog-container-content")
      );
      let currentPagePath = new URL(window.location).searchParams.get("item");
      let masterpage = this.IsMasterPage =
        currentPagePath.toLowerCase().indexOf(masterpagePath.toLowerCase()) >
        -1;
      this.getTagList = ReadSmartLogicTagListService(
        this.config.slUrl,
        this.currentCategory,
        currentPagePath
      );
      this.savedItems = new Map();

      let xs_Tag_list = root.find(".selected-tags-container ul.tags-list");
      this.properties = new TagPickerProperties(
        this.currentCategory,
        root.find(".selected-tags-container"),
        this.disabled,
        masterpage
      );
      this.TagsElementUI = new TagsElementUI(
        this.currentCategory,
        root,
        xs_Tag_list,
        this.properties,
        this
      );

      let searchcontroldiv = this.ui.root.find('.search-control');
      this.ui.search = new Coral.Search().set({
        placeholder: searchcontroldiv.data('placeholder'),
        name: "search"
      })
      searchcontroldiv.append(this.ui.search);
      this._attachEvents();
    }
    _loadSavedTags(json) {
      let current = [], c = 0,
        otherHiddenValues = {};
      if (json[this.currentCategory]) {
        current = json[this.currentCategory];
      } else return c;

      let propsToTake = this.properties.getAllPropNames().propertyMap;

      for (const jcrName in propsToTake) {
        if (Object.hasOwnProperty.call(propsToTake, jcrName)) {
          otherHiddenValues[jcrName] = json[jcrName];
        }
      }
      let jcrUniqueFieldName = TagsUniquePropertyMap[this.properties.getPropertyNameWithMasterCheck()];
      if (!otherHiddenValues[jcrUniqueFieldName]) return this;
      c += this._AddSavedProperties(current, otherHiddenValues, jcrUniqueFieldName);
      return c;
    }
    init(allsavedProperties) {
      this.properties.init();
      this.ui.root.find('.loading-message .spinner').append(new Coral.Wait().set({
        size: "L",
        variant: "dots"
      }));

      this._loadSavedTags(allsavedProperties);
      return this;

    }
    _addASelectedTag(idField, propertyMap, displayValue, objServiceTag, cls = 'new') {
      let listItem = this.TagsElementUI.addTagItem(
        idField,
        displayValue,
        cls
      );
      this.RequiredValidation.haveValue();
      if (this.InheritanceLocked == true) {
        listItem.querySelector('button').disabled = true;
      }
      listItem.appendChild(
        this.properties.createHiddenElement(
          this.currentCategory,
          displayValue,
          this.InheritanceLocked
        )
      );
      for (const jcrKey in propertyMap) {
        let jsonKeys = propertyMap[jcrKey];
        let v = objServiceTag[jsonKeys];
        if (v == undefined || String(v).replace(/\s/gi, '').length == 0) continue;//skip blank values;
        listItem.appendChild(
          this.properties.createHiddenElement(jcrKey, v, this.InheritanceLocked)
        );
      }
    }
    UpdateTags(tags) {
      const that = this;
      let prop_settings = this.properties.getAllPropNames(),
        propertyMap = prop_settings.propertyMap, count = 0,
        displayFieldjcrName = prop_settings.qualifiedName;

      let link = this.ui.root.find('.cq-msm-property-toggle-inheritance');
      if (link.length) {
        that.InheritanceLocked = !!link.data('inheritance-locked-status');
      }
      if(that.InheritanceLocked) return;
      this.TagsElementUI.removeAll();
      this.RequiredValidation.clearValue();
      tags.forEach(objServiceTag => {
        let displayValue = objServiceTag[displayFieldjcrName];
        if (!displayValue) return;
        let idField = objServiceTag[prop_settings.idDisplayPath];
        if (!idField) return;
        that._addASelectedTag(idField, propertyMap, displayValue, objServiceTag, 'updated');
        count++;
      });

      if (!tags.length || !count || !this.TagsElementUI.ui.list.find('li.tag-item').length) {
        //show : no tag selected message
        this.TagsElementUI.ui.list.find('p').addClass('show');
      }
      return count;
    }
    _addSelectedTags(items, cls) {
      const that = this;
      let prop_settings = this.properties.getAllPropNames(),
        propertyMap = prop_settings.propertyMap, count = 0,
        displayFieldjcrName = prop_settings.qualifiedName;
      items.each(function () {
        let checkbox = $(this);
        let attrV = checkbox.attr("data-source-attributes"),
          objServiceTag;
        if (attrV) objServiceTag = JSON.parse(attrV);
        let displayValue = objServiceTag[displayFieldjcrName];
        if (!displayValue) return;
        let idField = objServiceTag[prop_settings.idDisplayPath];
        that._addASelectedTag(idField, propertyMap, displayValue, objServiceTag, cls);
        count++;
      });
      return count;
    }
    _onSave() {
      const items = this.tagListUIModel.getSelectedItems();

      this.TagsElementUI.removeAll();
      this.RequiredValidation.clearValue();
      let link = this.ui.root.find('.cq-msm-property-toggle-inheritance');
      if (link.length) {
        this.InheritanceLocked = !!link.data('inheritance-locked-status');
      }
      let count = this._addSelectedTags(items, 'new');

      if (!items.length || !count) {
        //show no tag selected message
        this.TagsElementUI.ui.list.find('p').addClass('show');
      }
    }
    _onClose() { }
    _attachEvents() {
      let that = this;
      this.ui.root.find(".dialog-opener-container button").on("click", () => {
        that._prpareOpenDialog();
        that.modal.showModal();
      });
      this.ui.search.on('input', function (e) {
        that.tagListUIModel.filter(e.target.value);
      })
      this.ui.search.addEventListener('keydown', (e) => {
        if (e.key == 'Enter') {
          that.tagListUIModel.filter(e.target.value);
        }
      });
      this.ui.search.off('coral-search:clear').on('coral-search:clear', (e) => {
        that.tagListUIModel.filter('');
      })
      if (this.ui.clearSelectedBtn.length) {
        this.ui.clearSelectedBtn.on("click", function (e) {
          that.tagListUIModel.clearSelection();
        });
      }

      this.ui.root.on('click', '.cq-msm-property-toggle-inheritance', function (e) {
        that.InheritanceLocked = $(this).data('inheritance-locked-status');
      });
      // this.tagListUIModel.on('selected', (value, text, checkbox) => {

      // });
    }
    showSpinner() {
      let loading_element = this.tagListUIModel.ui.list.find(".loading-message")
      loading_element.find('.spinner').removeClass('hide');

      loading_element.find('.message')
        .text(loading_element.data('message'))
        .addClass('coral-Form-fieldlabel')
        .removeClass("coral-Form-errorlabel");

      loading_element.removeClass('hide');
      loading_element.parents('ul').css({
        display: 'flex',
        'flex-flow': 'column',
        'align-items': 'center',
        'justify-content': 'center'
      });
    }
    hideSpinner(error) {
      let loading_element = this.tagListUIModel.ui.list.find(".loading-message");

      if (error) {
        loading_element.find('.spinner').addClass('hide');
        loading_element.find('.message')
          .text(loading_element.data('no-tags-message'))
          .addClass('coral-Form-errorlabel')
      } else {
        loading_element.addClass('hide');
        loading_element.parents('ul').css({
          display: 'block'
        });
      }

    }
    async _prpareOpenDialog() {
      let prop_settings = this.properties.getAllPropNames();
      let jsonUniqueFieldName = prop_settings.idDisplayPath;

      //clear selected list
      this.tagListUIModel.clearSelection();
      if (!this.tagListUIModel.itemCount) {//load only once or if it failed on previous load.
        this.showSpinner();
        let tagsList = await this.getTagList(); //service call
        if (tagsList && tagsList.length) {
          this.hideSpinner()
        } else {
          this.hideSpinner(true);
        }

        for (let i = 0; i < tagsList.length; i++) {
          const tag = tagsList[i];
          let displayValue = tag[prop_settings.qualifiedName];
          let idfield = tag[jsonUniqueFieldName];
          let alreadyThere = this.TagsElementUI.ui.list.find(`li[data-id="${idfield}"]`).length > 0;
          this.tagListUIModel.addAnItem(idfield, displayValue, tag, alreadyThere);
        }
      } else {
        let that = this;
        this.TagsElementUI.ui.list.find(`li.tag-item`).each(function () {
          that.tagListUIModel.selectItem(this.getAttribute('data-id'));
        });
      }

    }
    _AddSavedProperties(current, otherProps, jcrUniqueFieldName) {
      let listItem, i;
      for (i = 0; i < current.length; i++) {
        listItem = this.TagsElementUI.addTagItem(
          otherProps[jcrUniqueFieldName][i],
          current[i],
          "existing"
        );
        this.RequiredValidation.haveValue();
        listItem.appendChild(
          this.properties.createHiddenElement(
            this.currentCategory,
            current[i],
            this.disabled
          )
        );
        for (const key in otherProps) {
          if (Object.hasOwnProperty.call(otherProps, key)) {
            const value = otherProps[key];
            if (value && Array.isArray(value) && value[i] != undefined) {
              const aValue = value[i];
              listItem.appendChild(
                this.properties.createHiddenElement(key, aValue, this.disabled)
              );
            }
          }
        }
      }
      return i;
    }
  }

  /**@type {TagPicker[]} */
  let Comps = [];
  let reader = PagePropertiesReader();
  let init = true;

  $(document).one("dialog-loaded", (e) => {
    reader((e) => {
      init ? InitTagPickers({}) : (init = false);
    }).then(result => {
      init ? InitTagPickers(result) : (init = false);
    }).catch(e => { console.error(e) });
  });

  function InitTagPickers(data) {
    let components = $(".com-customtagpicker:not([is-init])");
    if (components.length) {
      components.each(function (i) {
        Comps.push(new TagPicker($(this)).init(data));
      });
      components.attr('is-init', true);//fail safe, in case the event triggers more than once.
    }
    let collapsible = components.parents('.coral-Collapsible');
    if (collapsible.length) {
      collapsible.find('h3').trigger('click');//expand all collapsible
    }

    collapsible.find('.cleartags').off("click").on("click", function () {
      var ui = $(window).adaptTo("foundation-ui");
      ui.prompt(
        "Remove All Tags",
        "<b>'Removing' terms will NOT break inheritance of these values. Please break the inheritance manually to remove inherited tags.</b><br><br>'Remove All Tags' will clear all the tags that are selected under the Assisted Classification only. Click OK to proceed.", // message
        "info",
        [
          {
            id: "ok", text: "OK", primary: true, warning: false, handler: (e) => {
              clearAssistedTags(this)
            }
          }, // Action and its callback
          { id: "cancel", text: "Cancel", primary: false, warning: false, handler: () => false } // Action and its callback
        ]
      );
    });

    $('body').on('keydown', function (e) {
      if (e.key != "Escape") return;
      let category = $('.com-customtagpicker .dialog-container.show').first().attr('data-uid');
      let comp = Comps.find(value => value.currentCategory == category);
      comp && comp.modal._escapeKeyHideModal();
    });

    _handleUpdateTags(collapsible);
  }
  function clearAssistedTags(button, checkLock = true, showMessage = true) {
    if (!Comps.length) return;
    let divs = $(button).closest('.coral-Collapsible').find('.customtagpicker').each(function () {
      let div = $(this);
      let link = div.find('.cq-msm-property-toggle-inheritance');
      if (checkLock && link.length && link.data('inheritance-locked-status') == true) return;
      div.find('.selected-tags-container ul.tags-list li.tag-item').each(function () {
        $(this).remove();
      });
      if (showMessage) {
        div.find('.selected-tags-container ul.tags-list p').addClass('show');
      }
    });
  }
  function _handleUpdateTags($root) {
    $root.find('.coral-Button.updateall').off("click").on("click", function () {
      var ui = $(window).adaptTo("foundation-ui");
      ui.prompt(
        "Updates Tags",
        "<b>'Updating' terms will NOT break inheritance of these values.</b><br><br>'Updates Tags' will update all the assisted selection tags as per the latest taxonomy. Any deleted tags will be removed from the selection. Click OK to proceed.", // message
        "info", [{
          id: "ok", text: "OK", primary: true, warning: false, handler: e => { getUpdatedTags(this) }
        }, { id: "cancel", text: "Cancel", primary: false, warning: false, handler: () => false }
      ]
      );
    });

    var getUpdatedTags = function (button) {
      let url = new URL(window.location);
      let pagePath = url.searchParams.get("item");
      $.ajax({
        url: "/bin/kpmg/updateAction?pagePath=" + (pagePath),
        success: function (json) {
          let tags = {};
          //clear everything, so that when there are no tags in any category it handles that as well
          //just for assisted selection
          clearAssistedTags(button, true);
          for (let i = 0; i < json.length; i++) {
            const tag = json[i];
            if (tag.categoryType) {
              if (!tags[tag.categoryType]) tags[tag.categoryType] = [];
              tags[tag.categoryType].push(tag);
            }
          }
          for (const category in tags) {
            if (Object.hasOwnProperty.call(tags, category)) {
              const categoryTags = tags[category];
              UpdateNewTags(category, categoryTags)
            }
          }
        },
        fail: function () {
          return false;
        },
        dataType: "json"
      });
      function UpdateNewTags(category, tags) {
        let comp = Comps.find(value => value.currentCategory == category);
        if (!comp) return;
        comp.RequiredValidation.clearValue();
        comp.UpdateTags(tags);
      }
    };
  }
})();

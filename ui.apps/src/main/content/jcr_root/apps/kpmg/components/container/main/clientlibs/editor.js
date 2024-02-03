/**
 * a utility to wrap an authored components in a section component if the authored component is not allowed.
 * This is used primarily on the Main container (/apps/kpmg/components/container/main).
 */
(function () {
  const ns = Granite.author;
  const SECTION_TYPE = "/apps/kpmg/components/container/section";
  const allowedComponents = [SECTION_TYPE, "/apps/kpmg/components/content/sectionheader", "/apps/kpmg/components/content/standardteaser", "/apps/kpmg/components/content/hero-people", "/apps/kpmg/components/content/hero-fbv", "/apps/kpmg/components/content/hero-event", "/apps/kpmg/components/content/hero-csi"];


  class HistoryStep {
    constructor() {
      this._step = ns.history.util.Utils.beginStep();
      this.config = {
        step: this._step
      };
      this.finalize = function () {
        ns.history.util.Utils.finalizeStep(this._step);
      };
    }
  }
  /**
   * Check if two resource types are the same (ignoring the /apps/ or /libs/ prefix)
   * @param {string} type1
   * @param {string} type2
   * @returns {boolean}
   */
  const isSameResourceType = function (type1, type2) {
    // remove the /apps/ or /libs/ prefix
    type1 = type1.replace(/^\/(apps|libs)\//, "");
    type2 = type2.replace(/^\/(apps|libs)\//, "");
    return type1 === type2;
  };

  /**
   * check if the editable is allowed in the container
   * @param {*} editable
   * @returns
   */
  const isAllowedComponent = function (editable) {
    if (!editable) return false;
    return allowedComponents.some((componentType) =>
      isSameResourceType(componentType, editable.type)
    );
  };

  /**
   * Find component by type
   * @returns {Granite.author.Component} Component
   */
  const findComponent = function (componentType) {
    // try with the provided type, then try with /apps/ + type, then try with /libs/ + type
    const tryTypes = [
      componentType,
      "/apps/" + componentType,
      "/libs/" + componentType,
    ];

    return tryTypes
      .map((type) => {
        const components = ns.components.find(type);
        return components.length > 0 ? components[0] : null;
      })
      .find((component) => component !== null);
  };

  /**
   * Insert component with provided type before the provided neighbor editable
   * @param {*} componentType
   * @param {*} neighborEditable
   * @returns {Promise}
   */
  const insertComponent = function (componentType, neighborEditable, historyConfig) {
    var component = findComponent(componentType);
    if (component) {
      return ns.editableHelper.actions.INSERT.execute(
        component,
        ns.persistence.PARAGRAPH_ORDER.before,
        neighborEditable,
        historyConfig
      );
    } else {
      return $.Deferred().reject();
    }
  };

  /**
   * Move editable before neighborEditable
   * @param {*} editable the editable to move 
   * @param {*} neighborEditable the editable to move before
   * @param {*} historyConfig history config
   * @returns 
   */
  const moveComponent = function (editable, neighborEditable, historyConfig) {
    historyConfig = historyConfig ||  {preventAddHistory: true};
    const before = ns.persistence.PARAGRAPH_ORDER.before;
    return ns.edit.EditableActions.MOVE.execute(editable, before, neighborEditable, historyConfig);
  };

  /**
   * Replace the provided editable with a section component that contains the editable
   * @param {*} editable
   */
  const replaceWithSection = function (editable) {
    const insertSectionHistoryStep = new HistoryStep()
    // 1. insert a new section
    insertComponent(SECTION_TYPE, editable, insertSectionHistoryStep.config).done(function (insertedResult) {
      insertSectionHistoryStep.finalize()
      const sectionEditable = insertedResult.editable;
      const sectionEditableChildren = sectionEditable.getChildren();
      if (sectionEditableChildren && sectionEditableChildren.length > 0) {
        const sectionEditableNewChild = sectionEditableChildren[0];
        // 2. insert the component the author attempted to add into the new section
        moveComponent(editable, sectionEditableNewChild).then(function () {
          // 3. refresh the section.
          ns.responsive.EditableActions.REFRESH.execute(sectionEditable);
        });
      }
    });
  };

  /**
   * Prompt author to wrap the editable in a section or accept the non-recommended action.
   */
  function promptAuthor(editable) {
    ns.ui.helpers.prompt({
      title: "Only Section component is recomended to be added in the main container",
      message:
        `<p class="coral-Body--M">
          You've added a non-recommended component to the Main Section.<br>
          <b>Would you like us to wrap your added component in a section for you (Strongly Recommended)?</b>
        </p>
        <br>
        <br>
        <coral-banner size="S" variant="warning" class="u-coral-padding">
          <coral-banner-header>WARNING</coral-banner-header>
          <coral-banner-content>If you choose NOT to wrap your component in a section, it will be added as is, but it will not be positioned appropriately in large screens</coral-banner-content>
        </coral-banner>`,
      type: ns.ui.helpers.PROMPT_TYPES.WARNING,
      actions: [
        {
          id: "cancel",
          text: "No, don't wrap my component (Not Recommended)",
          primary: false,
          warning: true,
        },
        {
          id: "accept",
          text: "Yes, wrap my component in a Section (Recommended)",
          primary: true,
          handler: function () {
            replaceWithSection(editable);
          },
        }
      ],
    });
  }


  // MAIN exposed API
  window.KPMG = window.KPMG || {};
  window.KPMG.author = window.KPMG.author || {};
  window.KPMG.author.promptAuthorIfComponentNotAllowed = function (editable) {
    if (!editable) return;
    if (isAllowedComponent(editable)) return; // component is allowed, do nothing
    else {
      promptAuthor(editable); // component is not allowed, prompt author to wrap it in a section (or not)
    }
  };
})();

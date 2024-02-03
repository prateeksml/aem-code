<%--
  ADOBE CONFIDENTIAL
  ___________________

  Copyright 2016 Adobe
  All Rights Reserved.

  NOTICE: All information contained herein is, and remains
  the property of Adobe and its suppliers, if any. The intellectual
  and technical concepts contained herein are proprietary to Adobe
  and its suppliers and are protected by all applicable intellectual
  property laws, including trade secret and copyright laws.
  Dissemination of this information or reproduction of this material
  is strictly forbidden unless prior written permission is obtained
  from Adobe.
--%><%
%>
<%@ include file="/libs/granite/ui/global.jsp" %>
<%
%>
<%@ page session="false"
         import="com.adobe.granite.ui.components.AttrBuilder,
                 com.adobe.granite.ui.components.Config,
                 com.adobe.granite.ui.components.ExpressionHelper,
                 com.adobe.granite.ui.components.Field,
                 com.adobe.granite.ui.components.Tag,
                 com.adobe.granite.xss.XSSFilter,
                 org.apache.commons.lang3.StringUtils" %><%--###
PathField
=========

.. granite:servercomponent:: /libs/granite/ui/components/coral/foundation/form/pathfield
   :supertype: /libs/granite/ui/components/coral/foundation/form/field

   A field that allows the user to enter path.

   It extends :granite:servercomponent:`Field </libs/granite/ui/components/coral/foundation/form/field>` component.

   For the purpose of selecting a Material UI icon

   It has the following content structure:

   .. gnd:gnd::

      [granite:FormTextField] > granite:FormField

      /**
       * The name that identifies the field when submitting the form.
       */
      - name (String)

      /**
       * A hint to the user of what can be entered in the field.
       */
      - emptyText (String) i18n

      /**
       * Indicates if the field is in disabled state.
       */
      - disabled (Boolean)

      /**
       * Indicates if the field is mandatory to be filled.
       */
      - required (Boolean)

      /**
       * ``true`` to generate the `SlingPostServlet @Delete <http://sling.apache.org/documentation/bundles/manipulating-content-the-slingpostservlet-servlets-post.html#delete>`_ hidden input based on the field name.
       */
      - deleteHint (Boolean) = true

      /**
       * The URI template that returns the picker markup.
       *
       * It supports the following variables:
       *
       * value
       *    The value of the first item.
       */
      - pickerSrc (StringEl)
      
      /**
      * Can only be one of the following, CASE SENSITIVE:  filled, outlined, round, sharp, two-tone
      */
     - iconVariant (StringEl)
###--%>
<%

    final XSSFilter xssFilter = sling.getService(XSSFilter.class);

    Config cfg = cmp.getConfig();

    ValueMap vm = (ValueMap) request.getAttribute(Field.class.getName());
    ExpressionHelper ex = cmp.getExpressionHelper();

    final String pickerSrc = ex.getString(cfg.get("pickerSrc", "/mnt/overlay/kpmg/components/editor/iconfield/picker.html"));

    Tag tag = cmp.consumeTag();
    AttrBuilder attrs = tag.getAttrs();
    cmp.populateCommonAttrs(attrs);

    attrs.add("name", cfg.get("name", String.class));
    attrs.add("value",  vm.get("value", ""));
    attrs.addDisabled(cfg.get("disabled", false));
    attrs.addBoolean("required", cfg.get("required", false));
    attrs.add("picker-src", pickerSrc);
    attrs.add("icon-variant", cfg.get("iconVariant", "filled"));
    String fieldLabel = cfg.get("fieldLabel", String.class);
    String fieldDesc = cfg.get("fieldDescription", String.class);


    String labelledBy = null;
    if (fieldLabel != null && fieldDesc != null) {
        labelledBy = vm.get("labelId", String.class) + " " + vm.get("descriptionId", String.class);
    } else if (fieldLabel != null) {
        labelledBy = vm.get("labelId", String.class);
    } else if (fieldDesc != null) {
        labelledBy = vm.get("descriptionId", String.class);
    }

    if (StringUtils.isNotBlank(labelledBy)) {
        attrs.add("labelledby", labelledBy);
    }

    String labelled = cfg.get("labelled", String.class);
    if (StringUtils.isNotBlank(labelled)) {
        attrs.add("labelled", i18n.getVar(labelled));
    }

%>

<icon-picker <%= attrs %>></icon-picker>



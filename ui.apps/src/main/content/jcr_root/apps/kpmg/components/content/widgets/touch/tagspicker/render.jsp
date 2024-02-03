<%--
  ADOBE CONFIDENTIAL

  Copyright 2014 Adobe Systems Incorporated
  All Rights Reserved.

  NOTICE:  All information contained herein is, and remains
  the property of Adobe Systems Incorporated and its suppliers,
  if any.  The intellectual and technical concepts contained
  herein are proprietary to Adobe Systems Incorporated and its
  suppliers and may be covered by U.S. and Foreign Patents,
  patents in process, and are protected by trade secret or copyright law.
  Dissemination of this information or reproduction of this material
  is strictly forbidden unless prior written permission is obtained
  from Adobe Systems Incorporated.
--%>
<%
	
%><%@include file="/libs/granite/ui/global.jsp"%>
<%@include
	file="/apps/kpmg/components/content/widgets/touch/tagbrowser/tagbrowsercolumn/propertymapper.jsp"%>
<%@page session="false"
	import="com.adobe.granite.ui.components.AttrBuilder,
                  com.adobe.granite.ui.components.ComponentHelper.Options,
                  com.adobe.granite.ui.components.Config,
                  com.adobe.granite.ui.components.Field,
                  com.adobe.granite.ui.components.ds.ValueMapResource,
                  com.day.cq.tagging.Tag,
                  org.apache.jackrabbit.util.Text,
                  org.apache.sling.api.resource.ValueMap,
                  org.apache.sling.api.wrappers.ValueMapDecorator,
                  java.util.HashMap,org.json.JSONArray,
                  java.util.Locale,java.util.StringTokenizer, java.util.List,java.util.ArrayList,
				  org.slf4j.Logger,org.slf4j.LoggerFactory,
                  org.apache.commons.logging.Log,
                  org.apache.commons.logging.LogFactory"%>
<%
	final Locale locale = request.getLocale();

	Config cfg = cmp.getConfig();

	String name = (String) request.getAttribute(Field.class.getName() + ".name");
	String pagePath = (String) request.getAttribute("granite.ui.form.contentpath");
	final String masterpagePath = "/content/kpmgpublic/master";
	String language = null;
	String category = cfg.get("category", "contenttype");


	//prepare jcr readable name of the category
	String jcrCategory = category;
	switch (category) {
        case "contenttype":
			jcrCategory = "sl-content-qualifiedName";
            break;
        case "persona":
			jcrCategory = "sl-persona-qualifiedName";
            break;
        case "market":
			jcrCategory = "sl-market-qualifiedName";
        	break;
        case "geography":
			jcrCategory = "sl-geography-qualifiedName";
            break;
        case "insight":
			jcrCategory = "sl-insight-qualifiedName";
            break;
        case "service":
			jcrCategory = "sl-service-qualifiedName";
            break;
        case "industry":
			jcrCategory = "sl-industry-qualifiedName";
            break;
        case "mediaformats":
			jcrCategory = "sl-media-qualifiedName";
            break;
    }

	Resource propertyResource = resourceResolver.getResource(pagePath);
	List<String> customTags = new ArrayList<String>();
	if (null != propertyResource
			&& null != propertyResource.getValueMap().get(jcrCategory,
					String[].class)) {
    	String[] tagsString = propertyResource.getValueMap().get(
				jcrCategory, String[].class);

		for (int i = 0; i < tagsString.length; i++) {
			customTags.add(tagsString[i]);
		}
	}

	if (null != propertyResource && null != propertyResource.getValueMap().get("language", String.class)) {
		language = propertyResource.getValueMap().get("language", String.class);
	}

	boolean disabled = cfg.get("disabled", false);
	String rootPath = cfg.get("rootPath", cfg.get("tagsPath", "/etc/tags"));


	boolean isMixed = Field.isMixed(cfg, cmp.getValue());

	com.adobe.granite.ui.components.Tag htmlTag = cmp.consumeTag();
	AttrBuilder attrs = htmlTag.getAttrs();
	attrs.addClass(cfg.get("class", String.class));
	attrs.addClass("coral-Form-field js-cq-TagsPickerField");
	attrs.addRel(cfg.get("rel", String.class));
	attrs.add("id", cfg.get("id", String.class));
	attrs.add("title", i18n.getVar(cfg.get("title", String.class)));
	attrs.add("data-basepath", cfg.get("tagsPath", "/etc/tags"));
	attrs.add("data-property-path", name);
	attrs.add("data-group", cfg.get("group", ""));

	attrs.add("data-browserPath", request.getContextPath() + Text.escapePath(resource.getPath()));

	attrs.addOthers(cfg.getProperties(), "id", "class", "rel", "title", "type", "name", "value", "emptyText",
			"disabled", "required", "fieldLabel", "fieldDescription", "renderReadOnly", "ignoreData", "icon",
			"propertyPath", "group", "tagsPath", "rootPath");


	AttrBuilder deleteAttrs = new AttrBuilder(request, xssAPI);
	deleteAttrs.add("type", "hidden");
	deleteAttrs.addDisabled(disabled);
	deleteAttrs.add("name", name + "@Delete");

	AttrBuilder patchAttrs = new AttrBuilder(request, xssAPI);
	patchAttrs.add("type", "hidden");
	patchAttrs.addDisabled(disabled);
	patchAttrs.add("name", name + "@Patch");
	patchAttrs.add("value", "true");

	AttrBuilder typeHintAttrs = new AttrBuilder(request, xssAPI);
	typeHintAttrs.add("type", "hidden");
	typeHintAttrs.addDisabled(disabled);
	typeHintAttrs.add("name", name + "@TypeHint");
	typeHintAttrs.add("value", "String[]");


	ValueMap pathBrowserProperties = new ValueMapDecorator(new HashMap<String, Object>());
	pathBrowserProperties.put("basepath", cfg.get("tagsPath", "/etc/tags"));
	pathBrowserProperties.put("emptyText",
			isMixed ? i18n.getVar("<Mixed Entries>") : cfg.get("emptyText", String.class));

	pathBrowserProperties.put("rootPath", rootPath);
	pathBrowserProperties.put("disabled", disabled);
	pathBrowserProperties.put("allowBulkEdit", true);
	if (null != language) {
		pathBrowserProperties.put("pickerSrc",

				"/apps/kpmg/components/content/widgets/touch-pages/tagbrowser/tagbrowsercolumn" + "." + category + "."
						+ language + ".html" + Text.escapePath(pagePath));
	} else {
		pathBrowserProperties.put("pickerSrc",
				"/apps/kpmg/components/content/widgets/touch-pages/tagbrowser/tagbrowsercolumn" + "." + category + "."
						+ "language" + ".html" + Text.escapePath(pagePath));

	}
	pathBrowserProperties.put("pickerMultiselect", true);
	pathBrowserProperties.put("pickerTitle", i18n.get("Select Tags"));
	pathBrowserProperties.put("icon", cfg.get("icon", "icon-tags"));

	pathBrowserProperties.put("optionLoader", "cq.tagbrowser.optionloader");
	pathBrowserProperties.put("optionValueReader", "cq.tagbrowser.optionValueReader");
	pathBrowserProperties.put("optionTitleReader", "cq.tagbrowser.optionTitleReader");
	pathBrowserProperties.put("optionRenderer", "cq.tagbrowser.optionRenderer");
	pathBrowserProperties.put("autocompleteCallback", "cq.tagbrowser.autocompleteCallback");
	pathBrowserProperties.put("hideBrowseBtn", cfg.get("hideBrowseBtn", false));

	ValueMapResource pathBrowser = new ValueMapResource(resourceResolver, resource.getPath(),
			"granite/ui/components/foundation/form/pathbrowser", pathBrowserProperties);

	cmp.include(pathBrowser, new Options().tag(htmlTag).rootField(false));


	Map<String, String> propertyMap = new HashMap<String, String>();
	String qualifiedName = null;
	String idDisplayPath = null;
	switch (category) {
	case "contenttype":
		qualifiedName = "contenttypeQualifiedName";
		idDisplayPath = "contenttypeidDisplayPath";
		propertyMap = propertiesMap.get("contenttype");
		break;
	case "persona":
		qualifiedName = "personaQualifiedName";
		idDisplayPath = "personaidDisplayPath";
		propertyMap = propertiesMap.get("persona");
		break;
	case "market":
		qualifiedName = "marketQualifiedName";
		idDisplayPath = "marketidDisplayPath";
		propertyMap = propertiesMap.get("market");
		break;
	case "geography":
		qualifiedName = "geographyQualifiedName";
		idDisplayPath = "geographyidDisplayPath";
		propertyMap = propertiesMap.get("geography");
		break;
	case "insight":
		qualifiedName = "insightQualifiedName";
		idDisplayPath = "insightidDisplayPath";
		if(pagePath.contains(masterpagePath))
		{
		propertyMap = propertiesMap.get("insightCommon");
		}
		else
		{
		propertyMap = propertiesMap.get("insight");
		}
		break;
	case "service":
		qualifiedName = "serviceQualifiedName";
		idDisplayPath = "serviceidDisplayPath";
		if(pagePath.contains(masterpagePath))
		{
		propertyMap = propertiesMap.get("serviceCommon");
		}
		else
		{
		propertyMap = propertiesMap.get("service");
		}
		break;
	case "industry":
		qualifiedName = "industryQualifiedName";
		idDisplayPath = "industryidDisplayPath";
		if(pagePath.contains(masterpagePath))
		{
		propertyMap = propertiesMap.get("industryCommon");
		}
		else
		{
		propertyMap = propertiesMap.get("industry");
		}
		break;
	case "mediaformats":
		qualifiedName = "mediaformatsQualifiedName";
		idDisplayPath = "mediaformatsidDisplayPath";
		propertyMap = propertiesMap.get("mediaformats");
		break;

	case "all":
		qualifiedName = "title";
		idDisplayPath = "tagID";
		propertyMap = propertiesMap.get("allformats");
		break;

	}

	AttrBuilder valuesAttrs = new AttrBuilder(request, xssAPI);
	valuesAttrs.addClass("coral-TagList js-TagsPickerField-tagList");
	valuesAttrs.add("data-fieldname", name);
	valuesAttrs.add("data-allowcreate", cfg.get("allowCreate", false));
	valuesAttrs.add("data-init", "taglist");
%><ul <%=valuesAttrs.build()%>>
	<%
		for (int index = 0; index < customTags.size(); index++) {
			String tag = customTags.get(index);
			AttrBuilder hiddenAttrs = new AttrBuilder(request, xssAPI);
			hiddenAttrs.add("type", "hidden");
			hiddenAttrs.add("name", name);
			hiddenAttrs.add("value", tag); // cf. Sling POST Servlet @Patch
            hiddenAttrs.addClass("cq-TagList-tag--existing");
	%><li class="coral-TagList-tag coral-TagList-tag--multiline"
		title="<%=xssAPI.encodeForHTMLAttr(tag)%>">
		<button class="coral-TagList-tag-removeButton coral-MinimalButton"
			title="<%=i18n.get("Remove")%>">
			<i class="coral-Icon coral-Icon--sizeXS coral-Icon--close"></i>
		</button> <span class="coral-TagList-tag-label"><%=xssAPI.encodeForHTML(tag)%></span>
		<input <%=hiddenAttrs.build()%>> <%
         Log logger = LogFactory.getLog( this.getClass());
       

 			for (String key : propertyMap.keySet()) {
	 			if (null != propertyResource.getValueMap().get(key, String[].class)) {
 					AttrBuilder customHiddenAttrs = new AttrBuilder(request, xssAPI);
 					customHiddenAttrs.add("type", "hidden");
 					customHiddenAttrs.add("name", "./" + key);
                    try{
 						customHiddenAttrs.add("value", propertyResource.getValueMap().get(key, String[].class)[index]); // cf. Sling POST Servlet @Patch
                    }catch(Exception ex){
                         logger.error("ArrayIndexOutOfBoundsException : SmartLogic tags are missing. Exception is : ", ex);
                    } 
                    customHiddenAttrs.addClass("cq-TagList-tag--existing");
%> 	
					<input <%=customHiddenAttrs.build()%>> 
<%
 				}
 			}  

 %>
	</li>
	<%
		}

		List<AttrBuilder> deleteAttrsList = new ArrayList<AttrBuilder>();
		List<AttrBuilder> hintAttrsList = new ArrayList<AttrBuilder>();

		deleteAttrsList.add(deleteAttrs);
		hintAttrsList.add(typeHintAttrs);

		for (String key : propertyMap.keySet()) {
			//Adding the delete attributes for all the hidden properties.
			AttrBuilder customDeleteAttrs = new AttrBuilder(request, xssAPI);
			customDeleteAttrs.add("type", "hidden");
			customDeleteAttrs.addDisabled(disabled);
			customDeleteAttrs.add("name", "./" + key + "@Delete");
			deleteAttrsList.add(customDeleteAttrs);

			// Adding the Hint attributes for all the hidden properties
			AttrBuilder customHintAttrs = new AttrBuilder(request, xssAPI);
			customHintAttrs.add("type", "hidden");
			customHintAttrs.addDisabled(disabled);
			customHintAttrs.add("name", "./" + key + "@TypeHint");
			customHintAttrs.add("value", "String[]");
			hintAttrsList.add(customHintAttrs);

		}

	%>
</ul>
<%
	for (AttrBuilder hintAttribute : hintAttrsList) {
%>
<input <%=hintAttribute.build()%>>
<%
	}
%>
<%
	if (isMixed) {
%><input <%=patchAttrs.build()%>>
<%
	} else {
		for (AttrBuilder deleteAttribute : deleteAttrsList) {
%><input <%=deleteAttribute.build()%>>
<%
	}
	}

%>


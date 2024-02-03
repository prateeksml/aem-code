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
<%@ page import="com.adobe.granite.ui.components.AttrBuilder,
                 com.day.cq.tagging.Tag,
                 org.apache.commons.collections.Predicate,
                 org.apache.jackrabbit.util.Text,
                 org.apache.sling.api.request.RequestPathInfo,
                 org.apache.sling.api.resource.Resource,
                 org.apache.sling.api.resource.ResourceUtil,
                 org.apache.sling.api.resource.ValueMap,
                 com.google.gson.JsonArray,com.google.gson.JsonObject,java.io.PrintWriter,
		         java.io.IOException,com.kpmg.integration.services.SmartlogicService,
				 java.util.Collections,java.util.List,java.util.ArrayList,java.util.Comparator,org.json.JSONException,
				 java.util.Map,java.util.HashMap,org.slf4j.Logger,org.slf4j.LoggerFactory,org.apache.commons.lang3.StringUtils" %>

<%
%><%@page session="false" %><%
%><%@include file="/libs/granite/ui/global.jsp" %>
<%@include file="propertymapper.jsp" %>


<%
	RequestPathInfo pathInfo = slingRequest.getRequestPathInfo();
    String path = pathInfo.getSuffix();
	String[] selectorsArray =  pathInfo.getSelectors();
	String dotComSiteHomePagePath="";
	List<JsonObject> tagsList = null;
	if(path.equalsIgnoreCase("/noTags")){
		tagsList =  new ArrayList<JsonObject>();
    }
	else{
        dotComSiteHomePagePath=getDotComHomePage(path,selectorsArray);

		tagsList = sling.getService(SmartlogicService.class).getAllTagsByCategory(dotComSiteHomePagePath,selectorsArray[0]); 


        String qualifiedName = null;
		String idDisplayPath = null;
        String masterName = StringUtils.isNoneEmpty(path) && path.split("/").length > 3
					? path.split("/")[3] : StringUtils.EMPTY;
		boolean masterCheck = masterName.equalsIgnoreCase("master");

        Map<String,String> propertyMap = new HashMap<String,String>();
		switch (selectorsArray[0]) {
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
			if(masterCheck)
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
            if(masterCheck){
            propertyMap = propertiesMap.get("serviceCommon");
            }else{
			propertyMap = propertiesMap.get("service");
            }    
			break;
		case "industry":
			qualifiedName = "industryQualifiedName";
			idDisplayPath = "industryidDisplayPath";
            if(masterCheck){
            propertyMap = propertiesMap.get("industryCommon");
            }else{
			propertyMap = propertiesMap.get("industry");
            }    
			break;
		case "mediaformats":
			qualifiedName = "mediaformatsQualifiedName";
			idDisplayPath = "mediaformatsidDisplayPath";
			propertyMap = propertiesMap.get("mediaformats");
			break;

         case "all":
			qualifiedName = "titlePath";
			idDisplayPath = "tagID";
			propertyMap = propertiesMap.get("allformats");
			break;

		}
		AttrBuilder itemAttrs = new AttrBuilder(request, xssAPI);
		itemAttrs.addClass("coral-ColumnView-column");

	%><nav <%= itemAttrs.build() %> style="max-width:100%;">

	<% // Start .coral-ColumnView-column

	%><div class="coral-ColumnView-column-content kpmg-filter-container">


	<input type="text" class="coral-FixedColumn-column coral-Form-field coral3-Textfield" id="kpmg-filter-input" style="width:98%;box-sizing: border-box;
padding-left: 10px;" placeholder="Search tags.." title="Type in a tag">

	<%

			String href = resource.getPath() + ".html" + "/noTags";
	for(JsonObject displayTag : tagsList){
		 AttrBuilder childAttrs = new AttrBuilder(request, xssAPI);
		  childAttrs.addClass("coral-ColumnView-item");
		  childAttrs.add("title", displayTag.get(qualifiedName).toString().replace("\"",""));
		  childAttrs.add("data-value", displayTag.get(qualifiedName).toString().replace("\"",""));
		  childAttrs.add("data-id", displayTag.get(idDisplayPath).toString().replace("\"",""));
		  childAttrs.add("data-href", href);
		  childAttrs.add("data-titlePath", displayTag.get(qualifiedName).toString().replace("\"",""));
		  childAttrs.add("data-timeline", true);
		  childAttrs.add("data-category", selectorsArray[0]);

	      for(String key : propertyMap.keySet()){

			  String value = propertyMap.get(key).toString();
			  childAttrs.add("data-" + key.replaceAll("-",""), displayTag.get(value).toString().replace("\"",""));  
		  }


	%><a <%= childAttrs.build() %>>
	 <div class="coral-ColumnView-icon">
			<i class="coral-Icon coral-Icon--tags coral-Icon--sizeS"></i>
		</div>
		<div class="coral-ColumnView-label"><%= xssAPI.filterHTML(displayTag.get(qualifiedName).toString().replace("\"","")) %></div>
	</a><% } %>

	</div>
</nav><% } // Close .coral-ColumnView-column
%>
<%!
    private String getDotComHomePage(String path,String [] selectorsArray) {
    String dotComSiteHomePagePath = "";
    String[] pathArray=path.split("/");
	if(pathArray.length>3 && pathArray[3].equalsIgnoreCase("master")){
       dotComSiteHomePagePath=path.replace("/jcr:content","");
    }
    else if(selectorsArray.length>1 && selectorsArray[1].length()!=2){
       dotComSiteHomePagePath=path.replace("/jcr:content","");
     }
    else{
       dotComSiteHomePagePath=path.substring(0, 23)+selectorsArray[1];
    }
        return dotComSiteHomePagePath;
    }
    
%>


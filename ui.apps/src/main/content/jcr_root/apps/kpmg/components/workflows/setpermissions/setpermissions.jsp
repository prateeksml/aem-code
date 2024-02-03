<%--

    Settings Custom Permissions: Enables admin to set custom permissions from the dialog.
    @author: Manjunath Sajjan

--%><%@page import="java.util.Map"%>
<%@page import="org.apache.sling.commons.json.JSONArray"%>
<%@page import="org.apache.sling.commons.json.JSONObject"%>
<%
	
%>
<%@ page session="false"%>
<%@ include file="/apps/kpmgpublic/components/global/global.jsp"%>
<script type="text/javascript" src="/apps/kpmgpublic/components/workflows/setpermissions/clientlib.js"></script>

<c:set var="permissions"
	value="${sling2:adaptTo(slingRequest,
   'com.kpmg.cms.core.component.models.ChangePermissionModel')}"
	scope="request" />

<c:set var="result" value="${permissions.permissionsResult}" />
<h1>Execution completed</h1>
<c:forEach var="entry" items="${result}">

    <h1>User: ${entry.key} | Status: ${entry.value}</h1>

</c:forEach>
<c:set var="xpath" value="${permissions.xpathPermission}" />



<%--
  ADOBE CONFIDENTIAL

  Copyright 2013 Adobe Systems Incorporated
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
--%><%
%><%@include file="/libs/granite/ui/global.jsp" %><%
%><%@page session="false"
          import="java.util.HashMap,
                  org.apache.sling.api.resource.ValueMap,
                  org.apache.sling.api.wrappers.ValueMapDecorator,
                  com.adobe.granite.ui.components.Config,
                  com.adobe.granite.ui.components.Field,
                  com.day.cq.tagging.Tag,
                  com.day.cq.tagging.TagManager,
                  java.util.Set,
                  java.util.HashSet,
                  java.util.Map,
                  java.util.HashMap,
                  java.util.Iterator" %><%

    TagManager tm = resourceResolver.adaptTo(TagManager.class);

    Config cfg = cmp.getConfig();

    String name = cfg.get("name", "cq:tags");
    Map<String,String> templateProperties = (Map<String,String>) request.getAttribute("cq-template-properties");
    if (templateProperties != null) {
        Iterator it = templateProperties.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String,String> pair = (Map.Entry)it.next();
            if (name.contains(pair.getKey())) {
                out.print(name);
                name = name.replace(pair.getKey(), pair.getValue());
            }
        }
    }
    request.setAttribute(Field.class.getName() + ".name", name);

    String[] values = cmp.getValue().getContentValue(name, new String[0]);

    ValueMap vm = new ValueMapDecorator(new HashMap<String, Object>());
    vm.put("value", values);
    request.setAttribute(Field.class.getName(), vm);

    Set<Tag> tags = new HashSet<Tag>(values.length);
    for (String value : values) {
        Tag tag = tm.resolve(value);
        if (tag != null) {
            tags.add(tag);
        }
    }
    request.setAttribute(Field.class.getName() + ".tags", tags);
%><ui:includeClientLib categories="cq.gui.common.tagspicker" />

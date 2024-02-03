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
--%><%
%><%@include file="/libs/granite/ui/global.jsp" %><%
%><%@page session="false"
          import="com.adobe.granite.ui.components.AttrBuilder,
                  com.adobe.granite.ui.components.Config,
                  com.adobe.granite.ui.components.ComponentHelper,
                  com.adobe.granite.ui.components.Field,
                  java.util.Locale,
                  java.util.Set,
                  com.day.cq.tagging.Tag" %><%

    final Locale locale = request.getLocale();

    Config cfg = cmp.getConfig();

    AttrBuilder attrs = cmp.consumeTag().getAttrs();

    Set<Tag> tags = (Set<Tag>) request.getAttribute(Field.class.getName() + ".tags");

    if (cmp.getOptions().rootField()) {
        attrs.addClass("coral-Form-fieldwrapper");

        %><div <%= attrs.build() %>><%
            String fieldLabel = cfg.get("fieldLabel", String.class);
            if (fieldLabel != null) {
                %><label class="coral-Form-fieldlabel"><%= xssAPI.encodeForHTML(i18n.get(fieldLabel)) %></label><%
            }

            printTags(out, tags, locale, cmp);
        %></div><%
    } else {
        printTags(out, tags, locale, cmp);
    }
%><%!
    private void printTags(JspWriter out, Set<Tag> tags, Locale locale, ComponentHelper cmp) throws Exception {
        XSSAPI xss = cmp.getXss();

        out.println("<ul class='coral-TagList'>");
        for (Tag tag : tags) {
            out.print("<li class='coral-TagList-tag coral-TagList-tag--multiline'><span class='coral-TagList-tag-label'>");
            out.print(xss.encodeForHTML(tag.getTitlePath(locale)));
            out.println("</span></li>");
        }
        out.println("</ul>");
    }
%>

package com.kpmg.integration.util;

import com.adobe.cq.wcm.core.components.models.form.Field;
import com.kpmg.integration.datamap.FormFieldDefinitionVo;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FormUtil {

  private static final Logger LOGGER = LoggerFactory.getLogger(FormUtil.class);

  private static final String DATEFORMAT = "yyyy-MMM-dd/HH:mm:ss a/z";

  private static final Set<String> INTERNAL_PARAMETER =
      new HashSet<>(
          Arrays.asList(
              ":formstart",
              "_charset_",
              ":redirect",
              ":cq_csrf_token",
              "g-recaptcha-response",
              ":formstartpage"));

  private FormUtil() {}

  public static Map<FormFieldDefinitionVo, String> getFormDefinition(
      @NonNull Resource resource, @NonNull Map<String, String> receivedFormData) {
    Map<String, FormFieldDefinitionVo> formFieldNames = getFormFieldNames(resource);
    Map<FormFieldDefinitionVo, String> formFieldDefinitionVoMap = new HashMap<>();
    receivedFormData
        .keySet()
        .forEach(
            formFieldName -> {
              if (!INTERNAL_PARAMETER.contains(formFieldName)) {
                if (null != formFieldNames.get(formFieldName)) {
                  LOGGER.debug(
                      "FORM NAME PARAM : {} Value {}",
                      formFieldNames.get(formFieldName).getName(),
                      receivedFormData.get(formFieldName));
                  formFieldDefinitionVoMap.put(
                      formFieldNames.get(formFieldName), receivedFormData.get(formFieldName));
                }
              }
            });
    return formFieldDefinitionVoMap;
  }

  /**
   * Returns a set of form field names for the form specified in the request.
   *
   * @param resource - the current {@link SlingHttpServletRequest}
   * @return Set of form field names
   */
  public static Map<String, FormFieldDefinitionVo> getFormFieldNames(@NonNull Resource resource) {
    Map<String, FormFieldDefinitionVo> formFieldNames = new HashMap<>();
    collectFieldNames(resource, formFieldNames);
    return formFieldNames;
  }

  public static void collectFieldNames(
      Resource resource, Map<String, FormFieldDefinitionVo> fieldMap) {
    String regex;
    if (resource != null) {
      for (Resource child : resource.getChildren()) {
        String name =
            Optional.ofNullable(child.getValueMap())
                .map(vm -> vm.get(Field.PN_NAME, String.class))
                .orElse(StringUtils.EMPTY);
        boolean isCustomRegex =
            Optional.ofNullable(child.getValueMap())
                .map(vm -> vm.get("isCustomRegex", Boolean.class))
                .orElse(false);
        regex =
            Optional.ofNullable(child.getValueMap())
                .map(vm -> vm.get("validationRegex", String.class))
                .orElse(StringUtils.EMPTY);
        if (isCustomRegex) {
          regex =
              Optional.ofNullable(child.getValueMap())
                  .map(vm -> vm.get("customRegex", String.class))
                  .orElse(StringUtils.EMPTY);
        }
        LOGGER.debug("REGEX IS : {} FOR FIELD {}", regex, name);

        if (StringUtils.isNotEmpty(name)) {
          FormFieldDefinitionVo formFieldDefinitionVo = new FormFieldDefinitionVo();
          formFieldDefinitionVo.setName(name);
          formFieldDefinitionVo.setRegex(regex);
          fieldMap.put(name, formFieldDefinitionVo);
        }
        collectFieldNames(child, fieldMap);
      }
    }
  }

  public static boolean patternMatches(@NonNull String field, @NonNull String regexPattern) {
    return Pattern.compile(regexPattern).matcher(field).matches();
  }

  public static boolean isFormValid(Map<FormFieldDefinitionVo, String> formDefinitionMap) {
    for (Map.Entry<FormFieldDefinitionVo, String> entry : formDefinitionMap.entrySet()) {
      String regex = entry.getKey().getRegex();
      if (regex != null && StringUtils.isNotEmpty(regex.toString())) {
        if (entry.getValue() != null && StringUtils.isNotEmpty(entry.getValue().toString())) {
          Boolean flag = (FormUtil.patternMatches(entry.getValue(), regex));
          if (!flag) {
            return false;
          }
        }
      }
    }
    return true;
  }

  public static String getSubmittedDate() {
    Date date = new Date();
    DateFormat df = new SimpleDateFormat(DATEFORMAT);
    // Use default time zone to format the date in
    df.setTimeZone(TimeZone.getDefault());
    return df.format(date);
  }
}

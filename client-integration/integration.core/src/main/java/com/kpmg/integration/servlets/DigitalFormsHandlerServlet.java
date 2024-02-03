package com.kpmg.integration.servlets;

import static com.kpmg.integration.constants.Constants.SITE_ROOT;
import static org.apache.commons.lang3.StringUtils.equalsIgnoreCase;

import com.adobe.cq.dam.cfm.ContentFragment;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.google.gson.Gson;
import com.kpmg.core.caconfig.SiteSettingsConfig;
import com.kpmg.core.config.KPMGGlobal;
import com.kpmg.core.email.EmailService;
import com.kpmg.integration.datamap.FormFieldDefinitionVo;
import com.kpmg.integration.models.FormContainer;
import com.kpmg.integration.services.EnquiryTypeEmailMappingService;
import com.kpmg.integration.services.GetHTMLService;
import com.kpmg.integration.services.ReCaptchaV3Service;
import com.kpmg.integration.util.FormUtil;
import com.kpmg.integration.util.KPMGUtilities;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.jcr.Session;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.mail.EmailException;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = Servlet.class)
@SlingServletPaths(value = "/bin/kpmg/formhandler")
public class DigitalFormsHandlerServlet extends SlingAllMethodsServlet {

  @Reference transient ReCaptchaV3Service reCaptchaV3Service;

  @Reference transient EmailService emailService;

  @Reference transient GetHTMLService getHTMLService;

  @Reference transient QueryBuilder queryBuilder;

  @Reference transient KPMGGlobal kpmgGlobal;

  @Reference transient EnquiryTypeEmailMappingService enquiryTypeEmailMappingService;

  private static final Logger LOG = LoggerFactory.getLogger(DigitalFormsHandlerServlet.class);

  @Override
  protected void doPost(
      @NonNull SlingHttpServletRequest request, @NonNull SlingHttpServletResponse response) {
    try {

      String jsonBody =
          new BufferedReader(new InputStreamReader(request.getInputStream()))
              .lines()
              .collect(Collectors.joining("\n"));
      if (jsonBody == null || jsonBody.trim().length() == 0) {
        LOG.debug("NULL POINTER IN JSON BODY");
      }
      Map<String, String> receiveFormData = new Gson().fromJson(jsonBody, Map.class);

      String formStart = receiveFormData.get(":formstart");

      String peopleContactPath =
          Optional.ofNullable(receiveFormData.get("contactPath")).orElse(StringUtils.EMPTY);
      LOG.debug("People ContactPath:::{}", peopleContactPath);
      String formPagePath = receiveFormData.get(":formstartpage");
      LOG.debug("FORM PAGE path{} before appending peoplecontact path", formPagePath);
      if (StringUtils.isNotEmpty(peopleContactPath)) {
        formPagePath =
            new StringBuilder(SITE_ROOT)
                .append(new URL(peopleContactPath).getPath().substring(1))
                .toString()
                .replace(".html", StringUtils.EMPTY);
      }
      LOG.debug("FORM PAGE Path{} after appending people path", formPagePath);
      String captchaToken =
          Optional.ofNullable(receiveFormData.get("g-recaptcha-response"))
              .orElse(StringUtils.EMPTY);

      ResourceResolver resourceResolver = request.getResourceResolver();

      PageManager pageManager = resourceResolver.adaptTo(PageManager.class);
      Resource formResource =
          Optional.ofNullable(formStart).map(resourceResolver::getResource).orElse(null);

      String finalFormPagePath = formPagePath;
      LOG.debug("FORM PAGE {}", finalFormPagePath);
      Page formPage =
          Optional.ofNullable(pageManager).map(pm -> pm.getPage(finalFormPagePath)).orElse(null);

      FormContainer formContainer =
          Optional.ofNullable(formResource)
              .map(resource -> resource.adaptTo(FormContainer.class))
              .orElse(null);

      if (null != formResource && null != formContainer) {
        Map<FormFieldDefinitionVo, String> formDefinitionMap =
            FormUtil.getFormDefinition(formResource, receiveFormData);
        boolean isFormValid = FormUtil.isFormValid(formDefinitionMap);
        boolean isCaptchaVerified = reCaptchaV3Service.isCaptchaVerified(captchaToken);

        LOG.debug("IS FORM VALID {} IS CAPTCHA VERIFIED {}", isFormValid, isCaptchaVerified);
        if (isFormValid && isCaptchaVerified) {
          LOG.debug("Form Page before handleformsuccess", formPage);
          handleFormSuccess(resourceResolver, response, formDefinitionMap, formContainer, formPage);
        } else {
          handleFormFailure(response, formContainer);
        }
      } else {
        LOG.error("ERROR IN FORM SUBMISSION ");
        String[] sysError = {"error", "INTERNAL SYSTEM FAILURE"};
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        response.getWriter().write(new Gson().toJson(sysError));
      }
    } catch (IOException ex) {
      LOG.error(" AN ERROR OCCURRED IN FORM SUBMISSION {}", ex.getMessage());
    }
  }

  private void handleFormSuccess(
      @NonNull ResourceResolver resourceResolver,
      @NonNull SlingHttpServletResponse response,
      @NonNull Map<FormFieldDefinitionVo, String> formDefinitionMap,
      @NonNull FormContainer formContainer,
      @NonNull Page formPage)
      throws IOException {
    Map<String, String> formData = new HashMap<>();
    for (Map.Entry<FormFieldDefinitionVo, String> entry : formDefinitionMap.entrySet()) {
      formData.put(entry.getKey().getName(), entry.getValue());
    }
    if (equalsIgnoreCase("contact", formContainer.getFormType())
        || equalsIgnoreCase("peopleContact", formContainer.getFormType())) {
      handleContactForm(formData, formPage, resourceResolver, formContainer);
    }
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    response.getWriter().write(new Gson().toJson(formData));
  }

  private void handleFormFailure(
      @NonNull SlingHttpServletResponse response, @NonNull FormContainer formContainer)
      throws IOException {
    String[] sysError = {"error", "INVALID FORM"};
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    response.getWriter().write(new Gson().toJson(sysError));
  }

  private void handleContactForm(
      Map<String, String> formData,
      Page formPage,
      ResourceResolver resourceResolver,
      FormContainer formContainer) {
    SiteSettingsConfig formfieldMappingConfig = KPMGUtilities.getSiteSettings(formPage);

    String endUserEmail = getEndUserEmail(formData, formfieldMappingConfig);
    final String endUserEmailTemplatePath =
        equalsIgnoreCase("peopleContact", formContainer.getFormType())
            ? getPeopleContactEmailTemplate(formPage, resourceResolver)
            : getContactEmailTemplate(formPage, resourceResolver);
    final String internalUserEmailTemplatePath =
        equalsIgnoreCase("peopleContact", formContainer.getFormType())
            ? getInternalPeopleEmailTemplate(formPage, resourceResolver)
            : getInternalEmailTemplate(formPage, resourceResolver);

    String inquiryName = getInqueryName(formData, formfieldMappingConfig);
    String[] internalEmail =
        equalsIgnoreCase("peopleContact", formContainer.getFormType())
            ? Stream.concat(
                    Arrays.stream(getPeopleContactEmail(formPage, resourceResolver)),
                    Arrays.stream(getConfiguredPeopleEmail(formfieldMappingConfig)))
                .toArray(String[]::new)
            : Stream.concat(
                    Arrays.stream(getInternalEmail(formContainer, inquiryName, resourceResolver)),
                    Arrays.stream(getConfiguredContactEmail(formfieldMappingConfig)))
                .toArray(String[]::new);

    String fromAddress =
        equalsIgnoreCase("peopleContact", formContainer.getFormType())
            ? Optional.ofNullable(kpmgGlobal)
                .map(kg -> kg.getPeopleContactFormEmailFromAddress())
                .orElse(StringUtils.EMPTY)
            : Optional.ofNullable(kpmgGlobal)
                .map(kg -> kg.getGenericContactFormEmailFromAddress())
                .orElse(StringUtils.EMPTY);

    String fromAddressLabel = StringUtils.EMPTY;
    LOG.debug(
        "INTERNAL EMAIL {} AND EMAIL TEMPLATE PATH {}",
        (Arrays.toString(internalEmail)),
        internalUserEmailTemplatePath);
    if (ArrayUtils.isNotEmpty(internalEmail)
        && StringUtils.isNotEmpty(internalUserEmailTemplatePath)) {
      try {
        String internalMessage =
            getHTMLService.getHTML(internalUserEmailTemplatePath, resourceResolver);

        if (null != internalMessage && StringUtils.isNotEmpty(internalMessage)) {
          String subject = getSubject(internalMessage, "email-subject");
          LOG.debug("INTERNAL USER EMAIL SUBJECT {}", subject);
          internalMessage =
              createMessage(formData, formfieldMappingConfig, endUserEmail, internalMessage);
          sendEmailtoUser(subject, internalMessage, fromAddress, fromAddressLabel, internalEmail);
        }
      } catch (ServletException | IOException e) {
        LOG.error("AN ERROR OCCURRED WHILE HANDLING FORM {}", e.getMessage());
      }
    }
    LOG.debug(
        "END USER EMAIL {} AND END USER EMAIL TEMPLATE PATH {}",
        endUserEmail,
        endUserEmailTemplatePath);
    if (StringUtils.isNotEmpty(endUserEmail) && StringUtils.isNotEmpty(endUserEmailTemplatePath)) {
      String[] toAddressArray = new String[] {endUserEmail};
      try {
        String endUserMessage = getHTMLService.getHTML(endUserEmailTemplatePath, resourceResolver);
        if (ArrayUtils.isNotEmpty(toAddressArray) && StringUtils.isNotEmpty(endUserMessage)) {
          String subject = getSubject(endUserMessage, "email-subject");
          LOG.debug("END USER EMAIL SUBJECT {}", subject);
          endUserMessage =
              createMessage(formData, formfieldMappingConfig, endUserEmail, endUserMessage);
          sendEmailtoUser(subject, endUserMessage, fromAddress, fromAddressLabel, toAddressArray);
        }
      } catch (ServletException | IOException e) {
        LOG.error("AN ERROR OCCURRED WHILE HANDLING FORM {}", e.getMessage());
      }
    }
  }

  private String[] getInternalEmail(
      FormContainer formContainer, String inquiryName, ResourceResolver resourceResolver) {
    Map<String, String> params = new HashMap<>();
    params.put("path", formContainer.getResourcePath());
    params.put("type", "nt:base");
    params.put("1_property", "name");
    params.put("1_property.value", "inquiry");
    params.put("p.offset", "0");
    params.put("p.limit", "1");
    Query query =
        queryBuilder.createQuery(
            PredicateGroup.create(params), resourceResolver.adaptTo(Session.class));
    final Iterator<Resource> pageContentResources = query.getResult().getResources();

    String[] email = null;
    while (pageContentResources.hasNext()) {
      final Resource fragmentResource = pageContentResources.next();
      LOG.debug("Inquiry CF Resource {}", fragmentResource.getPath());
      email =
          Optional.ofNullable(fragmentResource.getValueMap())
              .map(vm -> vm.get("fragmentpath", String.class))
              .map(
                  fragmentPath ->
                      enquiryTypeEmailMappingService.getEmailBasedOnEnquiryType(
                          inquiryName, fragmentPath))
              .orElse(ArrayUtils.EMPTY_STRING_ARRAY);
    }
    return email;
  }

  private void sendEmailtoUser(
      String subject,
      String msg,
      String fromAddress,
      String fromAddressLabel,
      String[] toAddressArray) {
    try {
      emailService.sendHtmlEmail(subject, msg, fromAddress, fromAddressLabel, toAddressArray);
    } catch (EmailException e) {
      LOG.error("AN ERROR OCCURRED WHILE SENDING  EMAIL {}", e.getMessage());
    }
  }

  private String getContactEmailTemplate(Page formPage, ResourceResolver resourceResolver) {
    return Optional.ofNullable(KPMGUtilities.getSiteSettings(formPage))
        .map(SiteSettingsConfig::contactForm)
        .map(contactForm -> contactForm.contactFormEmailTemplatePath())
        .map(str -> resourceResolver.adaptTo(PageManager.class).getPage(str))
        .map(Page::getContentResource)
        .map(this::getEmailPath)
        .map(emailPath -> emailPath.concat(".html"))
        .orElse(StringUtils.EMPTY);
  }

  private String getInternalEmailTemplate(Page formPage, ResourceResolver resourceResolver) {
    return Optional.ofNullable(KPMGUtilities.getSiteSettings(formPage))
        .map(SiteSettingsConfig::contactForm)
        .map(contactForm -> contactForm.internalContactEmailTemplate())
        .map(str -> resourceResolver.adaptTo(PageManager.class).getPage(str))
        .map(Page::getContentResource)
        .map(this::getEmailPath)
        .map(emailPath -> emailPath.concat(".html"))
        .orElse(StringUtils.EMPTY);
  }

  private String getInternalPeopleEmailTemplate(Page formPage, ResourceResolver resourceResolver) {
    return Optional.ofNullable(KPMGUtilities.getSiteSettings(formPage))
        .map(SiteSettingsConfig::peopleForm)
        .map(contactForm -> contactForm.internalPeopleContactEmailTemplate())
        .map(str -> resourceResolver.adaptTo(PageManager.class).getPage(str))
        .map(Page::getContentResource)
        .map(this::getEmailPath)
        .map(emailPath -> emailPath.concat(".html"))
        .orElse(StringUtils.EMPTY);
  }

  private String getPeopleContactEmailTemplate(Page formPage, ResourceResolver resourceResolver) {
    return Optional.ofNullable(KPMGUtilities.getSiteSettings(formPage))
        .map(SiteSettingsConfig::peopleForm)
        .map(contactForm -> contactForm.peopleContactFormEmailTemplatePath())
        .map(str -> resourceResolver.adaptTo(PageManager.class).getPage(str))
        .map(Page::getContentResource)
        .map(this::getEmailPath)
        .map(emailPath -> emailPath.concat(".html"))
        .orElse(StringUtils.EMPTY);
  }

  private String getEndUserEmail(
      Map<String, String> formData, SiteSettingsConfig formfieldMappingConfig) {
    return Optional.ofNullable(formfieldMappingConfig)
        .map(SiteSettingsConfig::formMapping)
        .map(formMapping -> formMapping.email())
        .map(formData::get)
        .orElse(StringUtils.EMPTY);
  }

  private String getInqueryName(
      Map<String, String> formData, SiteSettingsConfig formfieldMappingConfig) {
    return Optional.ofNullable(formfieldMappingConfig)
        .map(SiteSettingsConfig::formMapping)
        .map(formMapping -> formMapping.inquiry())
        .map(formData::get)
        .orElse(StringUtils.EMPTY);
  }

  private String[] getConfiguredContactEmail(SiteSettingsConfig siteSettingsConfig) {
    return Optional.ofNullable(siteSettingsConfig)
        .map(SiteSettingsConfig::contactForm)
        .map(contactemail -> contactemail.contactFormEmailAddresses())
        .orElse(ArrayUtils.EMPTY_STRING_ARRAY);
  }

  private String[] getConfiguredPeopleEmail(SiteSettingsConfig siteSettingsConfig) {
    return Optional.ofNullable(siteSettingsConfig)
        .map(SiteSettingsConfig::peopleForm)
        .map(peoplemail -> peoplemail.peoplecontactFormEmailAddresses())
        .orElse(ArrayUtils.EMPTY_STRING_ARRAY);
  }

  private String[] getPeopleContactEmail(Page formPage, ResourceResolver resourceResolver) {
    return Optional.ofNullable(formPage)
        .map(Page::getContentResource)
        .map(Resource::getValueMap)
        .map(vm -> vm.get("fragmentPath", String.class))
        .map(resourceResolver::getResource)
        .map(res -> res.adaptTo(ContentFragment.class))
        .map(cf -> cf.getElement("email").getContent())
        .map(str -> new String[] {str})
        .orElse(ArrayUtils.EMPTY_STRING_ARRAY);
  }

  private String createMessage(
      Map<String, String> formData,
      SiteSettingsConfig formfieldMappingConfig,
      String endUserEmail,
      String message) {
    return message
        .replace(
            "{userName}",
            Optional.ofNullable(formData)
                    .map(fD -> fD.get(formfieldMappingConfig.formMapping().firstName()))
                    .orElse(StringUtils.EMPTY)
                + " "
                + Optional.ofNullable(formData)
                    .map(fD -> fD.get(formfieldMappingConfig.formMapping().lastName()))
                    .orElse(StringUtils.EMPTY))
        .replace("{email}", endUserEmail)
        .replace(
            "{state}",
            Optional.ofNullable(formData)
                .map(fD -> fD.get(formfieldMappingConfig.formMapping().state()))
                .orElse(StringUtils.EMPTY))
        .replace(
            "{phone}",
            Optional.ofNullable(formData)
                .map(fD -> fD.get(formfieldMappingConfig.formMapping().phone()))
                .orElse(StringUtils.EMPTY))
        .replace(
            "{company}",
            Optional.ofNullable(formData)
                .map(fD -> fD.get(formfieldMappingConfig.formMapping().company()))
                .orElse(StringUtils.EMPTY))
        .replace(
            "{role}",
            Optional.ofNullable(formData)
                .map(fD -> fD.get(formfieldMappingConfig.formMapping().role()))
                .orElse(StringUtils.EMPTY))
        .replace(
            "{message}",
            Optional.ofNullable(formData)
                .map(fD -> fD.get(formfieldMappingConfig.formMapping().message()))
                .orElse(StringUtils.EMPTY))
        .replace(
            "{reason}",
            Optional.ofNullable(formData)
                .map(fD -> fD.get(formfieldMappingConfig.formMapping().inquiry()))
                .orElse(StringUtils.EMPTY))
        .replace(
            "{country}",
            Optional.ofNullable(formData)
                .map(fD -> fD.get(formfieldMappingConfig.formMapping().country()))
                .orElse(StringUtils.EMPTY))
        .replace(
            "{submittedDate}",
            Optional.ofNullable(FormUtil.getSubmittedDate()).orElse(StringUtils.EMPTY));
  }

  private String getSubject(String htmlContent, String className) {
    Document doc = Jsoup.parse(htmlContent);
    return doc.getElementsByClass(className).html();
  }

  private String getEmailPath(Resource pageResource) {
    if (pageResource.hasChildren()) {
      Iterable<Resource> resourceIterable = pageResource.getChildren();
      for (Resource res : resourceIterable) {
        if (res.isResourceType("kpmg/components/content/emailcontent")) {
          return res.getPath();
        }
        return getEmailPath(res);
      }
    }
    return null;
  }
}

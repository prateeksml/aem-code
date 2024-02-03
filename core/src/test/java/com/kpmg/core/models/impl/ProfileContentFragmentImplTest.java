package com.kpmg.core.models.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

import com.adobe.cq.dam.cfm.ContentElement;
import com.adobe.cq.dam.cfm.ContentFragment;
import com.adobe.cq.dam.cfm.ContentVariation;
import com.adobe.cq.dam.cfm.FragmentData;
import com.kpmg.core.models.SocialLink;
import java.util.List;
import java.util.Optional;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProfileContentFragmentImplTest {

  ProfileContentFragmentImpl profile = spy(new ProfileContentFragmentImpl());

  @Mock ContentFragment contentFragment;
  @Mock ContentElement contentElement;

  @Mock ContentVariation contentVariation;
  @Mock ResourceResolver resourceResolver;
  @Mock Resource resource;
  @Mock FragmentData fragmentData;

  String CONTENT = "test";

  @BeforeEach
  void setup() {
    doReturn(resource).when(resourceResolver).getResource(anyString());
    doReturn(contentFragment).when(resource).adaptTo(ContentFragment.class);
    profile.contentFragment = Optional.ofNullable(contentFragment);
    profile.resourceResolver = resourceResolver;
    profile.fragmentPath = "/some/path";
    profile.init();
  }

  @Test
  void testWithMasterVariation() {
    doReturn(contentElement).when(contentFragment).getElement(any());
    doReturn(fragmentData).when(contentElement).getValue();
    doReturn(CONTENT).when(fragmentData).getValue();
    assertions();
  }

  @Test
  void testWithOtherVariation() {
    profile.variationName = "french";
    doReturn(contentElement).when(contentFragment).getElement(any());
    doReturn(contentVariation).when(contentElement).getVariation(any());
    doReturn(fragmentData).when(contentVariation).getValue();
    doReturn(CONTENT).when(fragmentData).getValue();
    assertions();
  }

  @Test
  void testGetSocialLinks() {
    String FACEBOOK = "https://facebook.com/test";
    String TWITTER = "https://twitter.com/test";
    doReturn(contentElement).when(contentFragment).getElement(any());
    doReturn(fragmentData).when(contentElement).getValue();
    String[] socials = {FACEBOOK, TWITTER};
    doReturn(socials).when(fragmentData).getValue();
    doReturn("test").when(profile).getId();
    List<SocialLink> socialLink = profile.getSocialLinks();
    assertEquals(2, socialLink.size());
    assertEquals("facebook", socialLink.get(0).getHostName());
    assertEquals("twitter", socialLink.get(1).getHostName());
    assertEquals(FACEBOOK, socialLink.get(0).getLink());
    assertEquals(TWITTER, socialLink.get(1).getLink());
    assertEquals(
        "{\"test-sociallink-a150f3b5a2\":{\"parentId\":\"test\",\"xdm:linkURL\":\"https://facebook.com/test\"}}",
        socialLink.get(0).getData().getJson());
    assertEquals(
        "{\"test-sociallink-bcae2ec9a0\":{\"parentId\":\"test\",\"xdm:linkURL\":\"https://twitter.com/test\"}}",
        socialLink.get(1).getData().getJson());
  }

  private void assertions() {
    assertEquals(CONTENT, profile.getImage());
    assertEquals(CONTENT, profile.getImageDescription());
    assertEquals(CONTENT, profile.getImageAltText());
    assertEquals(CONTENT, profile.getContactType());
    assertEquals(CONTENT, profile.getMemberFirm());
    assertEquals(CONTENT, profile.getMemberFirm());
    assertEquals(CONTENT, profile.getSalutation());
    assertEquals(CONTENT, profile.getFirstName());
    assertEquals(CONTENT, profile.getMiddleInitial());
    assertEquals(CONTENT, profile.getLastName());
    assertEquals(CONTENT, profile.getSuffix());
    assertEquals(CONTENT, profile.getJobTitle());
    assertEquals(CONTENT, profile.getEmail());
    assertEquals(CONTENT, profile.getPhoneNumber());
    assertEquals(CONTENT, profile.getCity());
    assertEquals(CONTENT, profile.getCountry());
    assertEquals(CONTENT, profile.getBiography());
    assertEquals(CONTENT, profile.getBusinessOwnerGroup());
    assertNotNull(profile.getDatalayerExtension());
    assertNotNull(profile.getEmailData());
    assertNotNull(profile.getPhoneData());
    assertNotNull(profile.getTitle());
  }
}

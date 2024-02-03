package com.kpmg.integration.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

class TagTest {
  /** Method under test: {@link Tag#setIso3166(String)} */
  @Test
  void testSetIso3166() {
    Tag tag = new Tag();
    tag.setIso3166("Iso3166");
    assertEquals("Iso3166", tag.getIso3166());
  }

  /** Method under test: {@link Tag#setIso3166(String)} */
  @Test
  void testSetIso31662() {
    Tag tag = new Tag();
    tag.setCategoryType("Category Type");
    tag.setContenttype("text/plain");
    tag.setGeography("Geography");
    tag.setIdDisplayPath("Id Display Path");
    tag.setIdPath("Id Path");
    tag.setIndustry("Industry");
    tag.setInsight("Insight");
    tag.setIso3166("Iso3166");
    tag.setIso31662("Iso31662");
    tag.setIso31663("Iso31663");
    tag.setKeywords("Keywords");
    tag.setLocalIdDisplayPath("Local Id Display Path");
    tag.setLocalIdPath("Local Id Path");
    tag.setLocalKeywords("Local Keywords");
    tag.setLocalPath("Local Path");
    tag.setLocalTagID("Local Tag ID");
    tag.setMarket("Market");
    tag.setMediaformats("Mediaformats");
    tag.setPath("Path");
    tag.setPersona("Persona");
    tag.setQualifiedName("Qualified Name");
    tag.setRuleBaseClass("Rule Base Class");
    tag.setService("Service");
    tag.setTagID("Tag ID");
    tag.setTitle("title");
    tag.setUnm49region("testregion");
    tag.setUnm49subregion("testsubregion");
    tag.setUnm49subsubregion("testsubsubregion");
    tag.setIso3166(null);
    assertEquals(" ", tag.getIso3166());
  }

  /** Method under test: {@link Tag#setIso31662(String)} */
  @Test
  void testSetIso316622() {
    Tag tag = new Tag();
    tag.setIso31662("Iso31662");
    assertEquals("Iso31662", tag.getIso31662());
  }

  /** Method under test: {@link Tag#setIso31662(String)} */
  @Test
  void testSetIso316623() {
    Tag tag = new Tag();
    tag.setCategoryType("Category Type");
    tag.setContenttype("text/plain");
    tag.setGeography("Geography");
    tag.setIdDisplayPath("Id Display Path");
    tag.setIdPath("Id Path");
    tag.setIndustry("Industry");
    tag.setInsight("Insight");
    tag.setIso3166("Iso3166");
    tag.setIso31662("Iso31662");
    tag.setIso31663("Iso31663");
    tag.setKeywords("Keywords");
    tag.setLocalIdDisplayPath("Local Id Display Path");
    tag.setLocalIdPath("Local Id Path");
    tag.setLocalKeywords("Local Keywords");
    tag.setLocalPath("Local Path");
    tag.setLocalTagID("Local Tag ID");
    tag.setMarket("Market");
    tag.setMediaformats("Mediaformats");
    tag.setPath("Path");
    tag.setPersona("Persona");
    tag.setQualifiedName("Qualified Name");
    tag.setRuleBaseClass("Rule Base Class");
    tag.setService("Service");
    tag.setTagID("Tag ID");
    tag.setTitle("title");
    tag.setUnm49region("testregion");
    tag.setUnm49subregion("testsubregion");
    tag.setUnm49subsubregion("testsubsubregion");
    tag.setIso31662(null);
    assertEquals(" ", tag.getIso31662());
  }

  /** Method under test: {@link Tag#setIso31663(String)} */
  @Test
  void testSetIso31663() {
    Tag tag = new Tag();
    tag.setIso31663("Iso31663");
    assertEquals("Iso31663", tag.getIso31663());
  }

  /** Method under test: {@link Tag#setIso31663(String)} */
  @Test
  void testSetIso316632() {
    Tag tag = new Tag();
    tag.setCategoryType("Category Type");
    tag.setContenttype("text/plain");
    tag.setGeography("Geography");
    tag.setIdDisplayPath("Id Display Path");
    tag.setIdPath("Id Path");
    tag.setIndustry("Industry");
    tag.setInsight("Insight");
    tag.setIso3166("Iso3166");
    tag.setIso31662("Iso31662");
    tag.setIso31663("Iso31663");
    tag.setKeywords("Keywords");
    tag.setLocalIdDisplayPath("Local Id Display Path");
    tag.setLocalIdPath("Local Id Path");
    tag.setLocalKeywords("Local Keywords");
    tag.setLocalPath("Local Path");
    tag.setLocalTagID("Local Tag ID");
    tag.setMarket("Market");
    tag.setMediaformats("Mediaformats");
    tag.setPath("Path");
    tag.setPersona("Persona");
    tag.setQualifiedName("Qualified Name");
    tag.setRuleBaseClass("Rule Base Class");
    tag.setService("Service");
    tag.setTagID("Tag ID");
    tag.setTitle("title");
    tag.setUnm49region("testregion");
    tag.setUnm49subregion("testsubregion");
    tag.setUnm49subsubregion("testsubsubregion");
    tag.setIso31663(null);
    assertEquals(" ", tag.getIso31663());
  }

  /** Method under test: {@link Tag#setUnm49region(String)} */
  @Test
  void testSetUnm49region() {
    Tag tag = new Tag();
    tag.setUnm49region("us-east-2");
    assertEquals("us-east-2", tag.getUnm49region());
  }

  /** Method under test: {@link Tag#setUnm49region(String)} */
  @Test
  void testSetUnm49region2() {
    Tag tag = new Tag();
    tag.setCategoryType("Category Type");
    tag.setContenttype("text/plain");
    tag.setGeography("Geography");
    tag.setIdDisplayPath("Id Display Path");
    tag.setIdPath("Id Path");
    tag.setIndustry("Industry");
    tag.setInsight("Insight");
    tag.setIso3166("Iso3166");
    tag.setIso31662("Iso31662");
    tag.setIso31663("Iso31663");
    tag.setKeywords("Keywords");
    tag.setLocalIdDisplayPath("Local Id Display Path");
    tag.setLocalIdPath("Local Id Path");
    tag.setLocalKeywords("Local Keywords");
    tag.setLocalPath("Local Path");
    tag.setLocalTagID("Local Tag ID");
    tag.setMarket("Market");
    tag.setMediaformats("Mediaformats");
    tag.setPath("Path");
    tag.setPersona("Persona");
    tag.setQualifiedName("Qualified Name");
    tag.setRuleBaseClass("Rule Base Class");
    tag.setService("Service");
    tag.setTagID("Tag ID");
    tag.setTitle("title");
    tag.setUnm49region("testregion");
    tag.setUnm49subregion("testsubregion");
    tag.setUnm49subsubregion("testsubsubregion");
    tag.setUnm49region(null);
    assertEquals(" ", tag.getUnm49region());
  }

  /** Method under test: {@link Tag#setUnm49subregion(String)} */
  @Test
  void testSetUnm49subregion() {
    Tag tag = new Tag();
    tag.setUnm49subregion("us-east-2");
    assertEquals("us-east-2", tag.getUnm49subregion());
  }

  /** Method under test: {@link Tag#setUnm49subregion(String)} */
  @Test
  void testSetUnm49subregion2() {
    Tag tag = new Tag();
    tag.setCategoryType("Category Type");
    tag.setContenttype("text/plain");
    tag.setGeography("Geography");
    tag.setIdDisplayPath("Id Display Path");
    tag.setIdPath("Id Path");
    tag.setIndustry("Industry");
    tag.setInsight("Insight");
    tag.setIso3166("Iso3166");
    tag.setIso31662("Iso31662");
    tag.setIso31663("Iso31663");
    tag.setKeywords("Keywords");
    tag.setLocalIdDisplayPath("Local Id Display Path");
    tag.setLocalIdPath("Local Id Path");
    tag.setLocalKeywords("Local Keywords");
    tag.setLocalPath("Local Path");
    tag.setLocalTagID("Local Tag ID");
    tag.setMarket("Market");
    tag.setMediaformats("Mediaformats");
    tag.setPath("Path");
    tag.setPersona("Persona");
    tag.setQualifiedName("Qualified Name");
    tag.setRuleBaseClass("Rule Base Class");
    tag.setService("Service");
    tag.setTagID("Tag ID");
    tag.setTitle("title");
    tag.setUnm49region("testregion");
    tag.setUnm49subregion("testsubregion");
    tag.setUnm49subsubregion("testsubsubregion");
    tag.setUnm49subregion(null);
    assertEquals(" ", tag.getUnm49subregion());
  }

  /** Method under test: {@link Tag#setUnm49subsubregion(String)} */
  @Test
  void testSetUnm49subsubregion() {
    Tag tag = new Tag();
    tag.setUnm49subsubregion("us-east-2");
    assertEquals("us-east-2", tag.getUnm49subsubregion());
  }

  /** Method under test: {@link Tag#setUnm49subsubregion(String)} */
  @Test
  void testSetUnm49subsubregion2() {
    Tag tag = new Tag();
    tag.setCategoryType("Category Type");
    tag.setContenttype("text/plain");
    tag.setGeography("Geography");
    tag.setIdDisplayPath("Id Display Path");
    tag.setIdPath("Id Path");
    tag.setIndustry("Industry");
    tag.setInsight("Insight");
    tag.setIso3166("Iso3166");
    tag.setIso31662("Iso31662");
    tag.setIso31663("Iso31663");
    tag.setKeywords("Keywords");
    tag.setLocalIdDisplayPath("Local Id Display Path");
    tag.setLocalIdPath("Local Id Path");
    tag.setLocalKeywords("Local Keywords");
    tag.setLocalPath("Local Path");
    tag.setLocalTagID("Local Tag ID");
    tag.setMarket("Market");
    tag.setMediaformats("Mediaformats");
    tag.setPath("Path");
    tag.setPersona("Persona");
    tag.setQualifiedName("Qualified Name");
    tag.setRuleBaseClass("Rule Base Class");
    tag.setService("Service");
    tag.setTagID("Tag ID");
    tag.setTitle("title");
    tag.setUnm49region("testregion");
    tag.setUnm49subregion("testsubregion");
    tag.setUnm49subsubregion(null);
    assertEquals(" ", tag.getUnm49subsubregion());
  }

  @Test
  void testConstructor() {
    Tag actualTag = new Tag();
    actualTag.setCategoryType("Category Type");
    actualTag.setContenttype("text/plain");
    actualTag.setGeography("Geography");
    actualTag.setIdDisplayPath("Id Display Path");
    actualTag.setIdPath("Id Path");
    actualTag.setIndustry("Industry");
    actualTag.setInsight("Insight");
    actualTag.setKeywords("Keywords");
    actualTag.setLocalIdDisplayPath("Local Id Display Path");
    actualTag.setLocalIdPath("Local Id Path");
    actualTag.setLocalKeywords("Local Keywords");
    actualTag.setLocalPath("Local Path");
    actualTag.setLocalTagID("Local Tag ID");
    actualTag.setMarket("Market");
    actualTag.setMediaformats("Mediaformats");
    actualTag.setPath("Path");
    actualTag.setPersona("Persona");
    actualTag.setQualifiedName("Qualified Name");
    actualTag.setRuleBaseClass("Rule Base Class");
    actualTag.setService("Service");
    actualTag.setTagID("Tag ID");
    actualTag.setTitle("Dr");
    String actualToStringResult = actualTag.toString();
    assertEquals("Category Type", actualTag.getCategoryType());
    assertEquals("text/plain", actualTag.getContenttype());
    assertEquals("Geography", actualTag.getGeography());
    assertEquals("Id Display Path", actualTag.getIdDisplayPath());
    assertEquals("Id Path", actualTag.getIdPath());
    assertEquals("Industry", actualTag.getIndustry());
    assertEquals("Insight", actualTag.getInsight());
    assertNull(actualTag.getIso31662());
    assertNull(actualTag.getIso31663());
    assertNull(actualTag.getIso3166());
    assertEquals("Keywords", actualTag.getKeywords());
    assertEquals("Local Id Display Path", actualTag.getLocalIdDisplayPath());
    assertEquals("Local Id Path", actualTag.getLocalIdPath());
    assertEquals("Local Keywords", actualTag.getLocalKeywords());
    assertEquals("Local Path", actualTag.getLocalPath());
    assertEquals("Local Tag ID", actualTag.getLocalTagID());
    assertEquals("Market", actualTag.getMarket());
    assertEquals("Mediaformats", actualTag.getMediaformats());
    assertEquals("Path", actualTag.getPath());
    assertEquals("Persona", actualTag.getPersona());
    assertEquals("Qualified Name", actualTag.getQualifiedName());
    assertEquals("Rule Base Class", actualTag.getRuleBaseClass());
    assertEquals("Service", actualTag.getService());
    assertEquals("Tag ID", actualTag.getTagID());
    assertEquals("Dr", actualTag.getTitle());
    assertNull(actualTag.getUnm49region());
    assertNull(actualTag.getUnm49subregion());
    assertNull(actualTag.getUnm49subsubregion());
    assertEquals(
        "Tag [tagID=Tag ID, title=Dr, path=Path, idPath=Id Path, idDisplayPath=Id Display Path, localIdPath=Local"
            + " Id Path, keywords=Keywords, ruleBaseClass=Rule Base Class, localTagID=Local Tag ID, localPath=Local"
            + " Path, qualifiedName=Qualified Name, localKeywords=Local Keywords, iso3166=null, iso31662=null,"
            + " iso31663=null, unm49region=null, unm49subregion=null, unm49subsubregion=null, categoryType=Category"
            + " Type, contenttype=text/plain, mediaformats=Mediaformats, persona=Persona, geography=Geography,"
            + " industry=Industry, service=Service, insight=Insight, market=Market, localIdDisplayPath=Local Id"
            + " Display Path]",
        actualToStringResult);
  }

  /** Method under test: {@link Tag#equals(Object)} */
  @Test
  void testEquals() {
    Tag tag = new Tag();
    tag.setCategoryType("Category Type");
    tag.setContenttype("text/plain");
    tag.setGeography("Geography");
    tag.setIdDisplayPath("Id Display Path");
    tag.setIdPath("Id Path");
    tag.setIndustry("Industry");
    tag.setInsight("Insight");
    tag.setIso3166("Iso3166");
    tag.setIso31662("Iso31662");
    tag.setIso31663("Iso31663");
    tag.setKeywords("Keywords");
    tag.setLocalIdDisplayPath("Local Id Display Path");
    tag.setLocalIdPath("Local Id Path");
    tag.setLocalKeywords("Local Keywords");
    tag.setLocalPath("Local Path");
    tag.setLocalTagID("Local Tag ID");
    tag.setMarket("Market");
    tag.setMediaformats("Mediaformats");
    tag.setPath("Path");
    tag.setPersona("Persona");
    tag.setQualifiedName("Qualified Name");
    tag.setRuleBaseClass("Rule Base Class");
    tag.setService("Service");
    tag.setTagID("Tag ID");
    tag.setTitle("title");
    tag.setUnm49region("testregion");
    tag.setUnm49subregion("testsubregion");
    tag.setUnm49subsubregion("testsubsubregion");
    assertNotEquals(tag, null);
  }

  /** Method under test: {@link Tag#equals(Object)} */
  @Test
  void testEquals2() {
    Tag tag = new Tag();
    tag.setCategoryType("Category Type");
    tag.setContenttype("text/plain");
    tag.setGeography("Geography");
    tag.setIdDisplayPath("Id Display Path");
    tag.setIdPath("Id Path");
    tag.setIndustry("Industry");
    tag.setInsight("Insight");
    tag.setIso3166("Iso3166");
    tag.setIso31662("Iso31662");
    tag.setIso31663("Iso31663");
    tag.setKeywords("Keywords");
    tag.setLocalIdDisplayPath("Local Id Display Path");
    tag.setLocalIdPath("Local Id Path");
    tag.setLocalKeywords("Local Keywords");
    tag.setLocalPath("Local Path");
    tag.setLocalTagID("Local Tag ID");
    tag.setMarket("Market");
    tag.setMediaformats("Mediaformats");
    tag.setPath("Path");
    tag.setPersona("Persona");
    tag.setQualifiedName("Qualified Name");
    tag.setRuleBaseClass("Rule Base Class");
    tag.setService("Service");
    tag.setTagID("Tag ID");
    tag.setTitle("title");
    tag.setUnm49region("testregion");
    tag.setUnm49subregion("testsubregion");
    tag.setUnm49subsubregion("testsubsubregion");
    assertNotEquals(tag, "Different type to Tag");
  }

  /**
   * Methods under test:
   *
   * <ul>
   *   <li>{@link Tag#equals(Object)}
   *   <li>{@link Tag#hashCode()}
   * </ul>
   */
  @Test
  void testEquals3() {
    Tag tag = new Tag();
    tag.setCategoryType("Category Type");
    tag.setContenttype("text/plain");
    tag.setGeography("Geography");
    tag.setIdDisplayPath("Id Display Path");
    tag.setIdPath("Id Path");
    tag.setIndustry("Industry");
    tag.setInsight("Insight");
    tag.setIso3166("Iso3166");
    tag.setIso31662("Iso31662");
    tag.setIso31663("Iso31663");
    tag.setKeywords("Keywords");
    tag.setLocalIdDisplayPath("Local Id Display Path");
    tag.setLocalIdPath("Local Id Path");
    tag.setLocalKeywords("Local Keywords");
    tag.setLocalPath("Local Path");
    tag.setLocalTagID("Local Tag ID");
    tag.setMarket("Market");
    tag.setMediaformats("Mediaformats");
    tag.setPath("Path");
    tag.setPersona("Persona");
    tag.setQualifiedName("Qualified Name");
    tag.setRuleBaseClass("Rule Base Class");
    tag.setService("Service");
    tag.setTagID("Tag ID");
    tag.setTitle("title");
    tag.setUnm49region("testregion");
    tag.setUnm49subregion("testsubregion");
    tag.setUnm49subsubregion("testsubsubregion");
    assertEquals(tag, tag);
    int expectedHashCodeResult = tag.hashCode();
    assertEquals(expectedHashCodeResult, tag.hashCode());
  }

  /**
   * Methods under test:
   *
   * <ul>
   *   <li>{@link Tag#equals(Object)}
   *   <li>{@link Tag#hashCode()}
   * </ul>
   */
  @Test
  void testEquals4() {
    Tag tag = new Tag();
    tag.setCategoryType("Category Type");
    tag.setContenttype("text/plain");
    tag.setGeography("Geography");
    tag.setIdDisplayPath("Id Display Path");
    tag.setIdPath("Id Path");
    tag.setIndustry("Industry");
    tag.setInsight("Insight");
    tag.setIso3166("Iso3166");
    tag.setIso31662("Iso31662");
    tag.setIso31663("Iso31663");
    tag.setKeywords("Keywords");
    tag.setLocalIdDisplayPath("Local Id Display Path");
    tag.setLocalIdPath("Local Id Path");
    tag.setLocalKeywords("Local Keywords");
    tag.setLocalPath("Local Path");
    tag.setLocalTagID("Local Tag ID");
    tag.setMarket("Market");
    tag.setMediaformats("Mediaformats");
    tag.setPath("Path");
    tag.setPersona("Persona");
    tag.setQualifiedName("Qualified Name");
    tag.setRuleBaseClass("Rule Base Class");
    tag.setService("Service");
    tag.setTagID("Tag ID");
    tag.setTitle("title");
    tag.setUnm49region("testregion");
    tag.setUnm49subregion("testsubregion");
    tag.setUnm49subsubregion("testsubsubregion");

    Tag tag2 = new Tag();
    tag2.setCategoryType("Category Type");
    tag2.setContenttype("text/plain");
    tag2.setGeography("Geography");
    tag2.setIdDisplayPath("Id Display Path");
    tag2.setIdPath("Id Path");
    tag2.setIndustry("Industry");
    tag2.setInsight("Insight");
    tag2.setIso3166("Iso3166");
    tag2.setIso31662("Iso31662");
    tag2.setIso31663("Iso31663");
    tag2.setKeywords("Keywords");
    tag2.setLocalIdDisplayPath("Local Id Display Path");
    tag2.setLocalIdPath("Local Id Path");
    tag2.setLocalKeywords("Local Keywords");
    tag2.setLocalPath("Local Path");
    tag2.setLocalTagID("Local Tag ID");
    tag2.setMarket("Market");
    tag2.setMediaformats("Mediaformats");
    tag2.setPath("Path");
    tag2.setPersona("Persona");
    tag2.setQualifiedName("Qualified Name");
    tag2.setRuleBaseClass("Rule Base Class");
    tag2.setService("Service");
    tag2.setTagID("Tag ID");
    tag2.setTitle("title");
    tag2.setUnm49region("testregion");
    tag2.setUnm49subregion("testsubregion");
    tag2.setUnm49subsubregion("testsubsubregion");
    assertEquals(tag, tag2);
    int expectedHashCodeResult = tag.hashCode();
    assertEquals(expectedHashCodeResult, tag2.hashCode());
  }

  /** Method under test: {@link Tag#equals(Object)} */
  @Test
  void testEquals5() {
    Tag tag = new Tag();
    tag.setCategoryType("Category Type");
    tag.setContenttype("text/plain");
    tag.setGeography("Geography");
    tag.setIdDisplayPath("Id Display Path");
    tag.setIdPath("Id Path");
    tag.setIndustry("Industry");
    tag.setInsight("Insight");
    tag.setIso3166("Iso3166");
    tag.setIso31662("Iso31662");
    tag.setIso31663("Iso31663");
    tag.setKeywords("Keywords");
    tag.setLocalIdDisplayPath("Local Id Display Path");
    tag.setLocalIdPath("Local Id Path");
    tag.setLocalKeywords("Local Keywords");
    tag.setLocalPath("Local Path");
    tag.setLocalTagID("Tag ID");
    tag.setMarket("Market");
    tag.setMediaformats("Mediaformats");
    tag.setPath("Path");
    tag.setPersona("Persona");
    tag.setQualifiedName("Qualified Name");
    tag.setRuleBaseClass("Rule Base Class");
    tag.setService("Service");
    tag.setTagID("Tag ID");
    tag.setTitle("title");
    tag.setUnm49region("testregion");
    tag.setUnm49subregion("testsubregion");
    tag.setUnm49subsubregion("testsubsubregion");

    Tag tag2 = new Tag();
    tag2.setCategoryType("Category Type");
    tag2.setContenttype("text/plain");
    tag2.setGeography("Geography");
    tag2.setIdDisplayPath("Id Display Path");
    tag2.setIdPath("Id Path");
    tag2.setIndustry("Industry");
    tag2.setInsight("Insight");
    tag2.setIso3166("Iso3166");
    tag2.setIso31662("Iso31662");
    tag2.setIso31663("Iso31663");
    tag2.setKeywords("Keywords");
    tag2.setLocalIdDisplayPath("Local Id Display Path");
    tag2.setLocalIdPath("Local Id Path");
    tag2.setLocalKeywords("Local Keywords");
    tag2.setLocalPath("Local Path");
    tag2.setLocalTagID("Local Tag ID");
    tag2.setMarket("Market");
    tag2.setMediaformats("Mediaformats");
    tag2.setPath("Path");
    tag2.setPersona("Persona");
    tag2.setQualifiedName("Qualified Name");
    tag2.setRuleBaseClass("Rule Base Class");
    tag2.setService("Service");
    tag2.setTagID("Tag ID");
    tag2.setTitle("title");
    tag2.setUnm49region("testregion");
    tag2.setUnm49subregion("testsubregion");
    tag2.setUnm49subsubregion("testsubsubregion");
    assertNotEquals(tag, tag2);
  }

  /** Method under test: {@link Tag#equals(Object)} */
  @Test
  void testEquals6() {
    Tag tag = new Tag();
    tag.setCategoryType("Category Type");
    tag.setContenttype("text/plain");
    tag.setGeography("Geography");
    tag.setIdDisplayPath("Id Display Path");
    tag.setIdPath("Id Path");
    tag.setIndustry("Industry");
    tag.setInsight("Insight");
    tag.setIso3166("Iso3166");
    tag.setIso31662("Iso31662");
    tag.setIso31663("Iso31663");
    tag.setKeywords("Keywords");
    tag.setLocalIdDisplayPath("Local Id Display Path");
    tag.setLocalIdPath("Local Id Path");
    tag.setLocalKeywords("Local Keywords");
    tag.setLocalPath("Local Path");
    tag.setLocalTagID(null);
    tag.setMarket("Market");
    tag.setMediaformats("Mediaformats");
    tag.setPath("Path");
    tag.setPersona("Persona");
    tag.setQualifiedName("Qualified Name");
    tag.setRuleBaseClass("Rule Base Class");
    tag.setService("Service");
    tag.setTagID("Tag ID");
    tag.setTitle("title");
    tag.setUnm49region("testregion");
    tag.setUnm49subregion("testsubregion");
    tag.setUnm49subsubregion("testsubsubregion");

    Tag tag2 = new Tag();
    tag2.setCategoryType("Category Type");
    tag2.setContenttype("text/plain");
    tag2.setGeography("Geography");
    tag2.setIdDisplayPath("Id Display Path");
    tag2.setIdPath("Id Path");
    tag2.setIndustry("Industry");
    tag2.setInsight("Insight");
    tag2.setIso3166("Iso3166");
    tag2.setIso31662("Iso31662");
    tag2.setIso31663("Iso31663");
    tag2.setKeywords("Keywords");
    tag2.setLocalIdDisplayPath("Local Id Display Path");
    tag2.setLocalIdPath("Local Id Path");
    tag2.setLocalKeywords("Local Keywords");
    tag2.setLocalPath("Local Path");
    tag2.setLocalTagID("Local Tag ID");
    tag2.setMarket("Market");
    tag2.setMediaformats("Mediaformats");
    tag2.setPath("Path");
    tag2.setPersona("Persona");
    tag2.setQualifiedName("Qualified Name");
    tag2.setRuleBaseClass("Rule Base Class");
    tag2.setService("Service");
    tag2.setTagID("Tag ID");
    tag2.setTitle("title");
    tag2.setUnm49region("testregion");
    tag2.setUnm49subregion("testsubregion");
    tag2.setUnm49subsubregion("testsubsubregion");
    assertNotEquals(tag, tag2);
  }

  /** Method under test: {@link Tag#equals(Object)} */
  @Test
  void testEquals7() {
    Tag tag = new Tag();
    tag.setCategoryType("Category Type");
    tag.setContenttype("text/plain");
    tag.setGeography("Geography");
    tag.setIdDisplayPath("Id Display Path");
    tag.setIdPath("Id Path");
    tag.setIndustry("Industry");
    tag.setInsight("Insight");
    tag.setIso3166("Iso3166");
    tag.setIso31662("Iso31662");
    tag.setIso31663("Iso31663");
    tag.setKeywords("Keywords");
    tag.setLocalIdDisplayPath("Local Id Display Path");
    tag.setLocalIdPath("Local Id Path");
    tag.setLocalKeywords("Local Keywords");
    tag.setLocalPath("Local Path");
    tag.setLocalTagID("Local Tag ID");
    tag.setMarket("Market");
    tag.setMediaformats("Mediaformats");
    tag.setPath("Path");
    tag.setPersona("Persona");
    tag.setQualifiedName("Qualified Name");
    tag.setRuleBaseClass("Rule Base Class");
    tag.setService("Service");
    tag.setTagID("Local Tag ID");
    tag.setTitle("title");
    tag.setUnm49region("testregion");
    tag.setUnm49subregion("testsubregion");
    tag.setUnm49subsubregion("testsubsubregion");

    Tag tag2 = new Tag();
    tag2.setCategoryType("Category Type");
    tag2.setContenttype("text/plain");
    tag2.setGeography("Geography");
    tag2.setIdDisplayPath("Id Display Path");
    tag2.setIdPath("Id Path");
    tag2.setIndustry("Industry");
    tag2.setInsight("Insight");
    tag2.setIso3166("Iso3166");
    tag2.setIso31662("Iso31662");
    tag2.setIso31663("Iso31663");
    tag2.setKeywords("Keywords");
    tag2.setLocalIdDisplayPath("Local Id Display Path");
    tag2.setLocalIdPath("Local Id Path");
    tag2.setLocalKeywords("Local Keywords");
    tag2.setLocalPath("Local Path");
    tag2.setLocalTagID("Local Tag ID");
    tag2.setMarket("Market");
    tag2.setMediaformats("Mediaformats");
    tag2.setPath("Path");
    tag2.setPersona("Persona");
    tag2.setQualifiedName("Qualified Name");
    tag2.setRuleBaseClass("Rule Base Class");
    tag2.setService("Service");
    tag2.setTagID("Tag ID");
    tag.setTitle("title");
    tag.setUnm49region("testregion");
    tag.setUnm49subregion("testsubregion");
    tag.setUnm49subsubregion("testsubsubregion");
    assertNotEquals(tag, tag2);
  }

  /** Method under test: {@link Tag#equals(Object)} */
  @Test
  void testEquals8() {
    Tag tag = new Tag();
    tag.setCategoryType("Category Type");
    tag.setContenttype("text/plain");
    tag.setGeography("Geography");
    tag.setIdDisplayPath("Id Display Path");
    tag.setIdPath("Id Path");
    tag.setIndustry("Industry");
    tag.setInsight("Insight");
    tag.setIso3166("Iso3166");
    tag.setIso31662("Iso31662");
    tag.setIso31663("Iso31663");
    tag.setKeywords("Keywords");
    tag.setLocalIdDisplayPath("Local Id Display Path");
    tag.setLocalIdPath("Local Id Path");
    tag.setLocalKeywords("Local Keywords");
    tag.setLocalPath("Local Path");
    tag.setLocalTagID("Local Tag ID");
    tag.setMarket("Market");
    tag.setMediaformats("Mediaformats");
    tag.setPath("Path");
    tag.setPersona("Persona");
    tag.setQualifiedName("Qualified Name");
    tag.setRuleBaseClass("Rule Base Class");
    tag.setService("Service");
    tag.setTagID(null);
    tag.setTitle("title");
    tag.setUnm49region("testregion");
    tag.setUnm49subregion("testsubregion");
    tag.setUnm49subsubregion("testsubsubregion");

    Tag tag2 = new Tag();
    tag2.setCategoryType("Category Type");
    tag2.setContenttype("text/plain");
    tag2.setGeography("Geography");
    tag2.setIdDisplayPath("Id Display Path");
    tag2.setIdPath("Id Path");
    tag2.setIndustry("Industry");
    tag2.setInsight("Insight");
    tag2.setIso3166("Iso3166");
    tag2.setIso31662("Iso31662");
    tag2.setIso31663("Iso31663");
    tag2.setKeywords("Keywords");
    tag2.setLocalIdDisplayPath("Local Id Display Path");
    tag2.setLocalIdPath("Local Id Path");
    tag2.setLocalKeywords("Local Keywords");
    tag2.setLocalPath("Local Path");
    tag2.setLocalTagID("Local Tag ID");
    tag2.setMarket("Market");
    tag2.setMediaformats("Mediaformats");
    tag2.setPath("Path");
    tag2.setPersona("Persona");
    tag2.setQualifiedName("Qualified Name");
    tag2.setRuleBaseClass("Rule Base Class");
    tag2.setService("Service");
    tag2.setTagID("Tag ID");
    tag2.setTitle("title");
    tag2.setUnm49region("testregion");
    tag2.setUnm49subregion("testsubregion");
    tag2.setUnm49subsubregion("testsubsubregion");
    assertNotEquals(tag, tag2);
  }
}

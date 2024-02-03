package com.kpmg.integration.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.kpmg.integration.models.Tag;
import com.smartlogic.ses.client.Attribute;
import com.smartlogic.ses.client.Field;
import com.smartlogic.ses.client.Metadata;
import com.smartlogic.ses.client.Path;
import com.smartlogic.ses.client.RelationMetadata;
import com.smartlogic.ses.client.SESClient;
import com.smartlogic.ses.client.Synonym;
import com.smartlogic.ses.client.Synonyms;
import com.smartlogic.ses.client.Term;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;

class TermDetailsExtractionUtilTest {

  @Test
  void testGetPath() {
    assertTrue(TermDetailsExtractionUtil.getPath(new Term(), "Localsite").isEmpty());
  }

  @Test
  void testGetPath2() {
    Field field = new Field();
    field.setId("Localsite");

    ArrayList<Field> fieldList = new ArrayList<>();
    fieldList.add(field);

    Path path = new Path();
    path.setFields(fieldList);

    Term term = new Term();
    term.addPath(path);
    assertTrue(TermDetailsExtractionUtil.getPath(term, "Localsite").isEmpty());
  }

  @Test
  void testGetPath3() {
    Field field = new Field();
    field.setId("42");

    ArrayList<Field> fieldList = new ArrayList<>();
    fieldList.add(field);

    Path path = new Path();
    path.setFields(fieldList);

    Term term = new Term();
    term.addPath(path);
    assertTrue(TermDetailsExtractionUtil.getPath(term, "").isEmpty());
  }

  @Test
  void testGetSynonyms() {
    assertEquals("", TermDetailsExtractionUtil.getSynonyms(new Term()));
  }

  @Test
  void testGetSynonyms2() {
    Synonyms synonyms = new Synonyms();
    synonyms.setType("testtype");

    Term term = new Term();
    term.addSynonyms(synonyms);
    assertEquals("", TermDetailsExtractionUtil.getSynonyms(term));
  }

  @Test
  void testGetSynonyms3() {
    Synonyms synonyms = new Synonyms();
    synonyms.setType("ISO");

    Term term = new Term();
    term.addSynonyms(synonyms);
    assertEquals("", TermDetailsExtractionUtil.getSynonyms(term));
  }

  @Test
  void testGetGeoMetaData() {
    assertTrue(TermDetailsExtractionUtil.getGeoMetaData(new Term()).isEmpty());
  }

  @Test
  void testGetGeoMetaData2() {
    Synonyms synonyms = new Synonyms();
    synonyms.setType("ISO");

    Term term = new Term();
    term.addSynonyms(synonyms);
    assertTrue(TermDetailsExtractionUtil.getGeoMetaData(term).isEmpty());
  }

  @Test
  void testGetGeoMetaData3() {
    Synonyms synonyms = new Synonyms();
    synonyms.setType("UNM49");

    Term term = new Term();
    term.addSynonyms(synonyms);
    assertTrue(TermDetailsExtractionUtil.getGeoMetaData(term).isEmpty());
  }

  @Test
  void testGetTermAssociatedDetails() {
    Term term = new Term();
    assertTrue(TermDetailsExtractionUtil.getTermAssociatedDetails(term, new SESClient()).isEmpty());
  }

  @Test
  void testGetGeographyTagDetails2() {
    Synonyms synonyms = new Synonyms();
    synonyms.setType("UF - ISO-3166-1-Alpha-3");

    Term term = new Term();
    term.addSynonyms(synonyms);
    TermDetailsExtractionUtil.getGeographyTagDetails(term);
  }

  @Test
  void testGetGeographyTagDetails3() {
    RelationMetadata relationMetadata = new RelationMetadata();
    relationMetadata.setAttribute(new Attribute());
    relationMetadata.setMetadata(new Metadata());

    Synonym synonym = new Synonym();
    synonym.setId("testid");
    synonym.setRelationMetadata(relationMetadata);
    synonym.setValue("testvalue");

    Synonyms synonyms = new Synonyms();
    synonyms.addSynonym(synonym);
    synonyms.setType("UF - ISO-3166-1-Alpha-2");

    Term term = new Term();
    term.addSynonyms(synonyms);
    assertEquals("testvalue", TermDetailsExtractionUtil.getGeographyTagDetails(term).getIso31662());
  }

  @Test
  void testGetGeographyTagDetails6() {
    RelationMetadata relationMetadata = new RelationMetadata();
    relationMetadata.setAttribute(new Attribute());
    relationMetadata.setMetadata(new Metadata());

    Synonym synonym = new Synonym();
    synonym.setId("synonymid");
    synonym.setRelationMetadata(relationMetadata);
    synonym.setValue("value");

    Synonyms synonyms = new Synonyms();
    synonyms.addSynonym(synonym);
    synonyms.setType("UF - UNM49-2");

    Term term = new Term();
    term.addSynonyms(synonyms);
    assertEquals(
        "value", TermDetailsExtractionUtil.getGeographyTagDetails(term).getUnm49subregion());
  }

  @Test
  void testGenerateJsonFromTagList() {
    assertEquals(0, TermDetailsExtractionUtil.generateJsonFromTagList(new ArrayList<>()).size());
  }

  @Test
  void testGenerateJsonFromTagListInsight() {
    Tag tag = new Tag();
    tag.setCategoryType("insight");
    tag.setContenttype("123261504933570809484355");
    tag.setGeography("93812326507033629793301");
    tag.setIdDisplayPath("displaypath");
    tag.setIdPath("idPath");
    tag.setIndustry("135320640047454042275950");
    tag.setInsight("83240608170062883780427");
    tag.setIso3166("3166");
    tag.setIso31662("31662");
    tag.setIso31663("31663");
    tag.setKeywords("keywords");
    tag.setMediaformats("164000807485345111581424");
    tag.setPersona("55463311195631660891109");
    tag.setQualifiedName("insightQualifiedName");
    tag.setRuleBaseClass("insight");
    tag.setService("service");
    tag.setTagID("tagID");
    tag.setTitle("title");
    tag.setUnm49region("testregion");
    tag.setUnm49subregion("testsubregion");
    tag.setUnm49subsubregion("testsubsubregion");

    ArrayList<Tag> tagList = new ArrayList<>();
    tagList.add(tag);
    assertEquals(1, TermDetailsExtractionUtil.generateJsonFromTagList(tagList).size());
  }

  @Test
  void testGenerateJsonFromTagListIndustry() {
    Tag tag = new Tag();
    tag.setCategoryType("industry");
    tag.setContenttype("123261504933570809484355");
    tag.setGeography("93812326507033629793301");
    tag.setIdDisplayPath("displaypath");
    tag.setIdPath("idPath");
    tag.setIndustry("135320640047454042275950");
    tag.setInsight("83240608170062883780427");
    tag.setIso3166("3166");
    tag.setIso31662("31662");
    tag.setIso31663("31663");
    tag.setKeywords("keywords");
    tag.setMediaformats("164000807485345111581424");
    tag.setPersona("55463311195631660891109");
    tag.setQualifiedName("industryQualifiedName");
    tag.setRuleBaseClass("industry");
    tag.setService("service");
    tag.setTagID("tagID");
    tag.setTitle("title");
    tag.setUnm49region("testregion");
    tag.setUnm49subregion("testsubregion");
    tag.setUnm49subsubregion("testsubsubregion");

    ArrayList<Tag> tagList = new ArrayList<>();
    tagList.add(tag);
    assertEquals(1, TermDetailsExtractionUtil.generateJsonFromTagList(tagList).size());
  }

  @Test
  void testGenerateJsonFromTagListService() {
    Tag tag = new Tag();
    tag.setCategoryType("service");
    tag.setContenttype("123261504933570809484355");
    tag.setGeography("93812326507033629793301");
    tag.setIdDisplayPath("displaypath");
    tag.setIdPath("idPath");
    tag.setIndustry("135320640047454042275950");
    tag.setInsight("83240608170062883780427");
    tag.setIso3166("3166");
    tag.setIso31662("31662");
    tag.setIso31663("31663");
    tag.setKeywords("keywords");
    tag.setMediaformats("164000807485345111581424");
    tag.setPersona("55463311195631660891109");
    tag.setQualifiedName("serviceQualifiedName");
    tag.setRuleBaseClass("service");
    tag.setService("service");
    tag.setTagID("tagID");
    tag.setTitle("title");
    tag.setUnm49region("testregion");
    tag.setUnm49subregion("testsubregion");
    tag.setUnm49subsubregion("testsubsubregion");

    ArrayList<Tag> tagList = new ArrayList<>();
    tagList.add(tag);
    assertEquals(1, TermDetailsExtractionUtil.generateJsonFromTagList(tagList).size());
  }

  @Test
  void testGenerateJsonFromTagListMarket() {
    Tag tag = new Tag();
    tag.setCategoryType("market");
    tag.setContenttype("123261504933570809484355");
    tag.setGeography("93812326507033629793301");
    tag.setIdDisplayPath("displaypath");
    tag.setIdPath("idPath");
    tag.setIndustry("135320640047454042275950");
    tag.setInsight("83240608170062883780427");
    tag.setIso3166("3166");
    tag.setIso31662("31662");
    tag.setIso31663("31663");
    tag.setKeywords("keywords");
    tag.setMediaformats("164000807485345111581424");
    tag.setPersona("55463311195631660891109");
    tag.setQualifiedName("markeetQualifiedName");
    tag.setRuleBaseClass("market");
    tag.setService("service");
    tag.setTagID("tagID");
    tag.setTitle("title");
    tag.setUnm49region("testregion");
    tag.setUnm49subregion("testsubregion");
    tag.setUnm49subsubregion("testsubsubregion");

    ArrayList<Tag> tagList = new ArrayList<>();
    tagList.add(tag);
    assertEquals(1, TermDetailsExtractionUtil.generateJsonFromTagList(tagList).size());
  }

  @Test
  void testGenerateJsonFromTagListGeography() {
    Tag tag = new Tag();
    tag.setCategoryType("geography");
    tag.setContenttype("123261504933570809484355");
    tag.setGeography("93812326507033629793301");
    tag.setIdDisplayPath("displaypath");
    tag.setIdPath("idPath");
    tag.setIndustry("135320640047454042275950");
    tag.setInsight("83240608170062883780427");
    tag.setIso3166("3166");
    tag.setIso31662("31662");
    tag.setIso31663("31663");
    tag.setKeywords("keywords");
    tag.setMediaformats("164000807485345111581424");
    tag.setPersona("55463311195631660891109");
    tag.setQualifiedName("geographyQualifiedName");
    tag.setRuleBaseClass("geography");
    tag.setService("service");
    tag.setTagID("tagID");
    tag.setTitle("title");
    tag.setUnm49region("testregion");
    tag.setUnm49subregion("testsubregion");
    tag.setUnm49subsubregion("testsubsubregion");

    ArrayList<Tag> tagList = new ArrayList<>();
    tagList.add(tag);
    assertEquals(1, TermDetailsExtractionUtil.generateJsonFromTagList(tagList).size());
  }
}

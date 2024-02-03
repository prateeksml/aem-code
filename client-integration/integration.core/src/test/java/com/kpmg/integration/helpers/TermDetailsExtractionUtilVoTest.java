package com.kpmg.integration.helpers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.kpmg.integration.models.Tag;
import com.smartlogic.ses.client.Attribute;
import com.smartlogic.ses.client.Metadata;
import com.smartlogic.ses.client.RelationMetadata;
import com.smartlogic.ses.client.Synonym;
import com.smartlogic.ses.client.Synonyms;
import com.smartlogic.ses.client.Term;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class TermDetailsExtractionUtilVoTest {

  @Test
  void testGetSynonyms() {
    assertEquals("", TermDetailsExtractionUtilVo.getSynonyms(new Term()));
  }

  @Test
  void testGetSynonyms2() {
    Synonyms synonyms = new Synonyms();
    synonyms.setType("synonyms");

    Term term = new Term();
    term.addSynonyms(synonyms);
    assertEquals("", TermDetailsExtractionUtilVo.getSynonyms(term));
  }

  @Test
  void testGetGeoMetaData() {
    Synonyms synonyms = new Synonyms();
    synonyms.setType("type");

    Term term = new Term();
    term.addSynonyms(synonyms);
    assertTrue(TermDetailsExtractionUtilVo.getGeoMetaData(term).isEmpty());
  }

  @Test
  void testGetGeoMetaData2() {
    Synonyms synonyms = new Synonyms();
    synonyms.setType("ISO");

    Term term = new Term();
    term.addSynonyms(synonyms);
    assertTrue(TermDetailsExtractionUtilVo.getGeoMetaData(term).isEmpty());
  }

  @Test
  void testGetGeographyTagDetailsISO3166() {
    RelationMetadata relationMetadata = new RelationMetadata();
    relationMetadata.setAttribute(new Attribute());
    relationMetadata.setMetadata(new Metadata());

    Synonym synonym = new Synonym();
    synonym.setRelationMetadata(relationMetadata);
    Synonyms synonyms = new Synonyms();
    synonyms.addSynonym(synonym);
    synonyms.setType("UF - ISO-3166-1-Numeric");

    Term term = new Term();
    term.addSynonyms(synonyms);
    assertNotNull(
        "subregion",
        TermDetailsExtractionUtilVo.getGeographyTagDetails(term).getUnm49subsubregion());
  }

  @Test
  void testGetGeographyTagDetailsISO31662() {
    RelationMetadata relationMetadata = new RelationMetadata();
    relationMetadata.setAttribute(new Attribute());
    relationMetadata.setMetadata(new Metadata());

    Synonym synonym = new Synonym();
    synonym.setRelationMetadata(relationMetadata);
    Synonyms synonyms = new Synonyms();
    synonyms.addSynonym(synonym);
    synonyms.setType("UF - ISO-3166-1-Alpha-2");

    Term term = new Term();
    term.addSynonyms(synonyms);
    assertNotNull(
        "subsubregion2",
        TermDetailsExtractionUtilVo.getGeographyTagDetails(term).getUnm49subsubregion());
  }

  @Test
  void testGetGeographyTagDetailsISO31663() {
    RelationMetadata relationMetadata = new RelationMetadata();
    relationMetadata.setAttribute(new Attribute());
    relationMetadata.setMetadata(new Metadata());

    Synonym synonym = new Synonym();
    synonym.setRelationMetadata(relationMetadata);
    Synonyms synonyms = new Synonyms();
    synonyms.addSynonym(synonym);
    synonyms.setType("UF - ISO-3166-1-Alpha-3");

    Term term = new Term();
    term.addSynonyms(synonyms);
    assertNotNull(
        "subsubregion3",
        TermDetailsExtractionUtilVo.getGeographyTagDetails(term).getUnm49subsubregion());
  }

  @Test
  void testGetGeographyTagDetailsRegion() {
    RelationMetadata relationMetadata = new RelationMetadata();
    relationMetadata.setAttribute(new Attribute());
    relationMetadata.setMetadata(new Metadata());

    Synonym synonym = new Synonym();
    synonym.setRelationMetadata(relationMetadata);
    Synonyms synonyms = new Synonyms();
    synonyms.addSynonym(synonym);
    synonyms.setType("UF - UNM49-1");

    Term term = new Term();
    term.addSynonyms(synonyms);
    assertNotNull(
        "subsubregion4",
        TermDetailsExtractionUtilVo.getGeographyTagDetails(term).getUnm49subsubregion());
  }

  @Test
  void testGetGeographyTagDetailsSubRegion() {
    RelationMetadata relationMetadata = new RelationMetadata();
    relationMetadata.setAttribute(new Attribute());
    relationMetadata.setMetadata(new Metadata());

    Synonym synonym = new Synonym();
    synonym.setRelationMetadata(relationMetadata);
    Synonyms synonyms = new Synonyms();
    synonyms.addSynonym(synonym);
    synonyms.setType("UF - UNM49-2");

    Term term = new Term();
    term.addSynonyms(synonyms);
    assertNotNull(
        "subsubregion5",
        TermDetailsExtractionUtilVo.getGeographyTagDetails(term).getUnm49subsubregion());
  }

  @Test
  void testGetGeographyTagDetailsSubRegion3() {
    RelationMetadata relationMetadata = new RelationMetadata();
    relationMetadata.setAttribute(new Attribute());
    relationMetadata.setMetadata(new Metadata());

    Synonym synonym = new Synonym();
    synonym.setRelationMetadata(relationMetadata);
    Synonyms synonyms = new Synonyms();
    synonyms.addSynonym(synonym);
    synonyms.setType("UF - UNM49-3");

    Term term = new Term();
    term.addSynonyms(synonyms);
    assertNotNull(
        "subsubregion6",
        TermDetailsExtractionUtilVo.getGeographyTagDetails(term).getUnm49subsubregion());
  }

  @Test
  void testGenerateCategorySpecificJSONFromTagListpersona() {
    Tag tag = new Tag();
    tag.setCategoryType("persona");
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
    tag.setQualifiedName("personaQualifiedName");
    tag.setRuleBaseClass("persona");
    tag.setService("service");
    tag.setTagID("tagID");
    tag.setTitle("title");
    tag.setUnm49region("TestUnm49region");
    tag.setUnm49subregion("TestUnm49Subregion");
    tag.setUnm49subsubregion("TestUnm49Subsubregion");

    HashSet<Tag> tagList = new HashSet<>();
    tagList.add(tag);
    assertEquals(
        1, TermDetailsExtractionUtilVo.generateCategorySpecificJSONFromTagList(tagList).size());
  }

  @Test
  void testGenerateCategorySpecificJSONFromTagListgeography() {
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
    tag.setUnm49region("Unm49region");
    tag.setUnm49subregion("Unm49subregion");
    tag.setUnm49subsubregion("Unm49subsubregion");

    HashSet<Tag> tagList = new HashSet<>();
    tagList.add(tag);
    assertEquals(
        1, TermDetailsExtractionUtilVo.generateCategorySpecificJSONFromTagList(tagList).size());
  }

  @Test
  void testGenerateCategorySpecificJSONFromTagListmediaformats() {
    Tag tag = new Tag();
    tag.setCategoryType("mediaformats");
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
    tag.setQualifiedName("mediaformatsQualifiedName");
    tag.setRuleBaseClass("mediaformats");
    tag.setService("service");
    tag.setTagID("tagID");
    tag.setTitle("title");
    tag.setUnm49region("Unm49region");
    tag.setUnm49subregion("Unm49subregion");
    tag.setUnm49subsubregion("Unm49subsubregion");

    HashSet<Tag> tagList = new HashSet<>();
    tagList.add(tag);
    assertEquals(
        1, TermDetailsExtractionUtilVo.generateCategorySpecificJSONFromTagList(tagList).size());
  }

  @Test
  void testGenerateCategorySpecificJSONFromTagListindustry() {
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

    HashSet<Tag> tagList = new HashSet<>();
    tagList.add(tag);
    assertEquals(
        1, TermDetailsExtractionUtilVo.generateCategorySpecificJSONFromTagList(tagList).size());
  }

  @Test
  void testGenerateCategorySpecificJSONFromTagListinsight() {
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
    tag.setUnm49region("region");
    tag.setUnm49subregion("subregion");
    tag.setUnm49subsubregion("subsubregion");

    HashSet<Tag> tagList = new HashSet<>();
    tagList.add(tag);
    assertEquals(
        1, TermDetailsExtractionUtilVo.generateCategorySpecificJSONFromTagList(tagList).size());
  }

  @Test
  void testGenerateCategorySpecificJSONFromTagListmarket() {
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
    tag.setQualifiedName("marketQualifiedName");
    tag.setRuleBaseClass("market");
    tag.setService("service");
    tag.setTagID("tagID");
    tag.setTitle("title");
    tag.setUnm49region("testregion");
    tag.setUnm49subregion("testsubregion");
    tag.setUnm49subsubregion("testsubsubregion");

    HashSet<Tag> tagList = new HashSet<>();
    tagList.add(tag);
    assertEquals(
        1, TermDetailsExtractionUtilVo.generateCategorySpecificJSONFromTagList(tagList).size());
  }

  @Test
  void testGenerateCategorySpecificJSONFromTagListservice() {
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

    HashSet<Tag> tagList = new HashSet<>();
    tagList.add(tag);
    assertEquals(
        1, TermDetailsExtractionUtilVo.generateCategorySpecificJSONFromTagList(tagList).size());
  }

  /**
   * Method under test: {@link
   * TermDetailsExtractionUtilVo#generateCategorySpecificJSONFromTagList(Set)}
   */
  @Test
  void testGenerateCategorySpecificJSONFromTagListContent() {
    Tag tag = new Tag();
    tag.setCategoryType("contenttype");
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
    tag.setQualifiedName("contenttypeQualifiedName");
    tag.setRuleBaseClass("contenttype");
    tag.setService("service");
    tag.setTagID("tagID");
    tag.setTitle("title");
    tag.setUnm49region("region");
    tag.setUnm49subregion("subregion");
    tag.setUnm49subsubregion("subsubregion");

    HashSet<Tag> tagList = new HashSet<>();
    tagList.add(tag);
    assertEquals(
        1, TermDetailsExtractionUtilVo.generateCategorySpecificJSONFromTagList(tagList).size());
  }
}

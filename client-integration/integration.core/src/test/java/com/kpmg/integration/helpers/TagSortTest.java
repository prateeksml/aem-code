package com.kpmg.integration.helpers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.junit.jupiter.api.Test;

class TagSortTest {
  @Test
  void testSortSmartLogicTagsMarket() {
    TagSort tagSort = new TagSort();
    JsonObject json1 =
        new Gson()
            .fromJson(
                "{\"marketidDisplayPath\":\"115733510401367082745912/22125264688914272418221\",\"tagID\":\"22125264688914272418221\",\"marketidPath\":\"128865388042170613038114/115733510401367082745912/22125264688914272418221\",\"title\":\"Services/Audit/Financial Statement Audit\",\"titlePath\":\"Services/Audit/Financial Statement Audit\",\"marketLocalidPath\":\"128865388042170613038114/115733510401367082745912/22125264688914272418221\",\"marketQualifiedName\":\"Audit/Financial Statement Audit\",\"marketKeywords\":\" \",\"marketLocalKeywords\":\" \",\"marketPath\":\"Services/Audit/Financial Statement Audit\",\"marketLocalPath\":\"Services/Audit/Financial Statement Audit\",\"marketLocalId\":\"22125264688914272418221\",\"marketId\":\"22125264688914272418221\"}",
                JsonObject.class);
    JsonArray tagsArray = new JsonArray();
    tagsArray.add(json1);
    assertEquals(1, tagSort.sortSmartLogicTags("market", tagsArray).size());
  }

  @Test
  void testSortSmartLogicTagsInsight() {
    TagSort tagSort = new TagSort();
    JsonObject json1 =
        new Gson()
            .fromJson(
                "{\"insightidDisplayPath\":\"115733510401367082745912/22125264688914272418221\",\"tagID\":\"22125264688914272418221\",\"insightidPath\":\"128865388042170613038114/115733510401367082745912/22125264688914272418221\",\"title\":\"Services/Audit/Financial Statement Audit\",\"titlePath\":\"Services/Audit/Financial Statement Audit\",\"insightLocalidPath\":\"128865388042170613038114/115733510401367082745912/22125264688914272418221\",\"insightQualifiedName\":\"Audit/Financial Statement Audit\",\"insightKeywords\":\" \",\"insightLocalKeywords\":\" \",\"insightPath\":\"Services/Audit/Financial Statement Audit\",\"insightLocalPath\":\"Services/Audit/Financial Statement Audit\",\"insightLocalId\":\"22125264688914272418221\",\"insightId\":\"22125264688914272418221\"}",
                JsonObject.class);
    JsonArray tagsArray = new JsonArray();
    tagsArray.add(json1);
    assertEquals(1, tagSort.sortSmartLogicTags("insight", tagsArray).size());
  }

  @Test
  void testSortSmartLogicTagsIndustry() {
    TagSort tagSort = new TagSort();
    JsonObject json1 =
        new Gson()
            .fromJson(
                "{\"industryidDisplayPath\":\"115733510401367082745912/22125264688914272418221\",\"tagID\":\"22125264688914272418221\",\"industryidPath\":\"128865388042170613038114/115733510401367082745912/22125264688914272418221\",\"title\":\"Services/Audit/Financial Statement Audit\",\"titlePath\":\"Services/Audit/Financial Statement Audit\",\"industryLocalidPath\":\"128865388042170613038114/115733510401367082745912/22125264688914272418221\",\"industryQualifiedName\":\"Audit/Financial Statement Audit\",\"industryKeywords\":\" \",\"industryLocalKeywords\":\" \",\"industryPath\":\"Services/Audit/Financial Statement Audit\",\"industryLocalPath\":\"Services/Audit/Financial Statement Audit\",\"industryLocalId\":\"22125264688914272418221\",\"industryId\":\"22125264688914272418221\"}",
                JsonObject.class);
    JsonArray tagsArray = new JsonArray();
    tagsArray.add(json1);
    assertEquals(1, tagSort.sortSmartLogicTags("industry", tagsArray).size());
  }

  @Test
  void testSortSmartLogicTagsGeography() {
    TagSort tagSort = new TagSort();
    JsonObject json1 =
        new Gson()
            .fromJson(
                "{\"geographyidDisplayPath\":\"115733510401367082745912/22125264688914272418221\",\"tagID\":\"22125264688914272418221\",\"geographyidPath\":\"128865388042170613038114/115733510401367082745912/22125264688914272418221\",\"title\":\"Services/Audit/Financial Statement Audit\",\"titlePath\":\"Services/Audit/Financial Statement Audit\",\"geographyLocalidPath\":\"128865388042170613038114/115733510401367082745912/22125264688914272418221\",\"geographyQualifiedName\":\"Audit/Financial Statement Audit\",\"geographyKeywords\":\" \",\"geographyLocalKeywords\":\" \",\"geographyPath\":\"Services/Audit/Financial Statement Audit\",\"geographyLocalPath\":\"Services/Audit/Financial Statement Audit\",\"geographyLocalId\":\"22125264688914272418221\",\"geographyId\":\"22125264688914272418221\"}",
                JsonObject.class);
    JsonArray tagsArray = new JsonArray();
    tagsArray.add(json1);
    assertEquals(1, tagSort.sortSmartLogicTags("geography", tagsArray).size());
  }

  @Test
  void testSortSmartLogicTagsPersona() {
    TagSort tagSort = new TagSort();
    JsonObject json1 =
        new Gson()
            .fromJson(
                "{\"personaidDisplayPath\":\"115733510401367082745912/22125264688914272418221\",\"tagID\":\"22125264688914272418221\",\"personaidPath\":\"128865388042170613038114/115733510401367082745912/22125264688914272418221\",\"title\":\"Services/Audit/Financial Statement Audit\",\"titlePath\":\"Services/Audit/Financial Statement Audit\",\"personaLocalidPath\":\"128865388042170613038114/115733510401367082745912/22125264688914272418221\",\"personaQualifiedName\":\"Audit/Financial Statement Audit\",\"personaKeywords\":\" \",\"personaLocalKeywords\":\" \",\"personaPath\":\"Services/Audit/Financial Statement Audit\",\"personaLocalPath\":\"Services/Audit/Financial Statement Audit\",\"personaLocalId\":\"22125264688914272418221\",\"personaId\":\"22125264688914272418221\"}",
                JsonObject.class);
    JsonArray tagsArray = new JsonArray();
    tagsArray.add(json1);
    assertEquals(1, tagSort.sortSmartLogicTags("persona", tagsArray).size());
  }

  @Test
  void testSortSmartLogicTagsMedia() {
    TagSort tagSort = new TagSort();
    JsonObject json1 =
        new Gson()
            .fromJson(
                "{\"mediaformatsidDisplayPath\":\"115733510401367082745912/22125264688914272418221\",\"tagID\":\"22125264688914272418221\",\"mediaformatsidPath\":\"128865388042170613038114/115733510401367082745912/22125264688914272418221\",\"title\":\"Services/Audit/Financial Statement Audit\",\"titlePath\":\"Services/Audit/Financial Statement Audit\",\"mediaformatsLocalidPath\":\"128865388042170613038114/115733510401367082745912/22125264688914272418221\",\"mediaformatsQualifiedName\":\"Audit/Financial Statement Audit\",\"mediaformatsKeywords\":\" \",\"mediaformatsLocalKeywords\":\" \",\"mediaformatsPath\":\"Services/Audit/Financial Statement Audit\",\"mediaformatsLocalPath\":\"Services/Audit/Financial Statement Audit\",\"mediaformatsLocalId\":\"22125264688914272418221\",\"mediaformatsId\":\"22125264688914272418221\"}",
                JsonObject.class);
    JsonArray tagsArray = new JsonArray();
    tagsArray.add(json1);
    assertEquals(1, tagSort.sortSmartLogicTags("mediaformats", tagsArray).size());
  }

  @Test
  void testSortSmartLogicTagsService() {
    TagSort tagSort = new TagSort();
    JsonObject json1 =
        new Gson()
            .fromJson(
                "{\"serviceidDisplayPath\":\"115733510401367082745912/22125264688914272418221\",\"tagID\":\"22125264688914272418221\",\"serviceidPath\":\"128865388042170613038114/115733510401367082745912/22125264688914272418221\",\"title\":\"Services/Audit/Financial Statement Audit\",\"titlePath\":\"Services/Audit/Financial Statement Audit\",\"serviceLocalidPath\":\"128865388042170613038114/115733510401367082745912/22125264688914272418221\",\"serviceQualifiedName\":\"Audit/Financial Statement Audit\",\"serviceKeywords\":\" \",\"serviceLocalKeywords\":\" \",\"servicePath\":\"Services/Audit/Financial Statement Audit\",\"serviceLocalPath\":\"Services/Audit/Financial Statement Audit\",\"serviceLocalId\":\"22125264688914272418221\",\"serviceId\":\"22125264688914272418221\"}",
                JsonObject.class);
    JsonArray tagsArray = new JsonArray();
    tagsArray.add(json1);
    assertEquals(1, tagSort.sortSmartLogicTags("service", tagsArray).size());
  }

  @Test
  void testSortSmartLogicTagsContent() {
    TagSort tagSort = new TagSort();
    JsonObject json1 =
        new Gson()
            .fromJson(
                "{\"contenttypeidDisplayPath\":\"115733510401367082745912/22125264688914272418221\",\"tagID\":\"22125264688914272418221\",\"contenttypeidPath\":\"128865388042170613038114/115733510401367082745912/22125264688914272418221\",\"title\":\"Services/Audit/Financial Statement Audit\",\"titlePath\":\"Services/Audit/Financial Statement Audit\",\"contenttypeLocalidPath\":\"128865388042170613038114/115733510401367082745912/22125264688914272418221\",\"contenttypeQualifiedName\":\"Audit/Financial Statement Audit\",\"contenttypeKeywords\":\" \",\"contenttypeLocalKeywords\":\" \",\"contenttypePath\":\"Services/Audit/Financial Statement Audit\",\"contenttypeLocalPath\":\"Services/Audit/Financial Statement Audit\",\"contenttypeLocalId\":\"22125264688914272418221\",\"contenttypeId\":\"22125264688914272418221\"}",
                JsonObject.class);
    JsonObject json2 =
        new Gson()
            .fromJson(
                "{\"contenttypeidDisplayPath\":\"115733510401367082745912/123261504933570809484355\",\"tagID\":\"123261504933570809484355\",\"contenttypeidPath\":\"128865388042170613038114/115733510401367082745912/123261504933570809484355\",\"title\":\"Services/Audit/Attestation\",\"titlePath\":\"Services/Audit/Attestation\",\"contenttypeLocalidPath\":\"128865388042170613038114/115733510401367082745912/123261504933570809484355\",\"contenttypeQualifiedName\":\"Audit/Attestation\",\"contenttypeKeywords\":\" \",\"contenttypeLocalKeywords\":\" \",\"contenttypePath\":\"Services/Audit/Attestation\",\"contenttypeLocalPath\":\"Services/Audit/Attestation\",\"contenttypeLocalId\":\"123261504933570809484355\",\"contenttypeId\":\"123261504933570809484355\"}",
                JsonObject.class);
    JsonObject json3 =
        new Gson()
            .fromJson(
                "{\"contenttypeidDisplayPath\":\"139451683369905992286273/13644581436062504279162/192361243998238290851644/81264039084067904606613\",\"tagID\":\"81264039084067904606613\",\"contenttypeidPath\":\"128865388042170613038114/139451683369905992286273/13644581436062504279162/192361243998238290851644/81264039084067904606613\",\"title\":\"Services/Advisory/Management Consulting/Solutions Network/Transformational Program Management\",\"titlePath\":\"Services/Advisory/Management Consulting/Solutions Network/Transformational Program Management\",\"contenttypeLocalidPath\":\"128865388042170613038114/139451683369905992286273/13644581436062504279162/192361243998238290851644/81264039084067904606613\",\"contenttypeQualifiedName\":\"Advisory/Management Consulting/Solutions Network/Transformational Program Management\",\"contenttypeKeywords\":\" \",\"contenttypeLocalKeywords\":\" \",\"contenttypePath\":\"Services/Advisory/Management Consulting/Solutions Network/Transformational Program Management\",\"contenttypeLocalPath\":\"Services/Advisory/Management Consulting/Solutions Network/Transformational Program Management\",\"contenttypeLocalId\":\"81264039084067904606613\",\"contenttypeId\":\"81264039084067904606613\"}",
                JsonObject.class);
    JsonObject json4 =
        new Gson()
            .fromJson(
                "{\"contenttypeidDisplayPath\":\"139451683369905992286273/13644581436062504279162/204601847462425458250597\",\"tagID\":\"204601847462425458250597\",\"contenttypeidPath\":\"128865388042170613038114/139451683369905992286273/13644581436062504279162/204601847462425458250597\",\"title\":\"Services/Advisory/Management Consulting/Financial Management\",\"titlePath\":\"Services/Advisory/Management Consulting/Financial Management\",\"contenttypeLocalidPath\":\"128865388042170613038114/139451683369905992286273/13644581436062504279162/204601847462425458250597\",\"contenttypeQualifiedName\":\"Advisory/Management Consulting/Financial Management\",\"contenttypeKeywords\":\" \",\"contenttypeLocalKeywords\":\" \",\"contenttypePath\":\"Services/Advisory/Management Consulting/Financial Management\",\"contenttypeLocalPath\":\"Services/Advisory/Management Consulting/Financial Management\",\"contenttypeLocalId\":\"204601847462425458250597\",\"contenttypeId\":\"204601847462425458250597\"}",
                JsonObject.class);
    JsonArray tagsArray = new JsonArray();
    tagsArray.add(json1);
    tagsArray.add(json2);
    tagsArray.add(json3);
    tagsArray.add(json4);

    assertEquals(4, tagSort.sortSmartLogicTags("contenttype", tagsArray).size());
  }

  @Test
  void testSortSmartLogicTagsAll() {
    TagSort tagSort = new TagSort();
    JsonObject json1 =
        new Gson()
            .fromJson(
                "{\"serviceidDisplayPath\":\"115733510401367082745912/22125264688914272418221\",\"tagID\":\"22125264688914272418221\",\"serviceidPath\":\"128865388042170613038114/115733510401367082745912/22125264688914272418221\",\"title\":\"Services/Audit/Financial Statement Audit\",\"titlePath\":\"Services/Audit/Financial Statement Audit\",\"serviceLocalidPath\":\"128865388042170613038114/115733510401367082745912/22125264688914272418221\",\"serviceQualifiedName\":\"Audit/Financial Statement Audit\",\"serviceKeywords\":\" \",\"serviceLocalKeywords\":\" \",\"servicePath\":\"Services/Audit/Financial Statement Audit\",\"serviceLocalPath\":\"Services/Audit/Financial Statement Audit\",\"serviceLocalId\":\"22125264688914272418221\",\"serviceId\":\"22125264688914272418221\"}",
                JsonObject.class);
    JsonArray tagsArray = new JsonArray();
    tagsArray.add(json1);
    assertEquals(1, tagSort.sortSmartLogicTags("all", tagsArray).size());
  }
}

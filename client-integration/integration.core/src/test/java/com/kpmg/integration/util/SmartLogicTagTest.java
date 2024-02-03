package com.kpmg.integration.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import com.google.gson.JsonArray;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class SmartLogicTagTest {

  @Test
  void testConstructor() {
    SmartLogicTag actualSmartLogicTag =
        new SmartLogicTag(new HashSet<>(), "Name", 1, "Zthes ID", "English Name", 1);
    HashSet<SmartLogicTag> children = new HashSet<>();
    actualSmartLogicTag.setChildren(children);
    actualSmartLogicTag.setEnglishName("English Name");
    actualSmartLogicTag.setLevel(1);
    actualSmartLogicTag.setName("Name");
    actualSmartLogicTag.setOrder(1);
    actualSmartLogicTag.setZthesID("Zthes ID");
    String actualToStringResult = actualSmartLogicTag.toString();
    assertEquals(children, actualSmartLogicTag.getChildren());
    assertEquals("English Name", actualSmartLogicTag.getEnglishName());
    assertEquals(1, actualSmartLogicTag.getLevel());
    assertEquals("Name", actualSmartLogicTag.getName());
    assertEquals(1, actualSmartLogicTag.getOrder());
    assertEquals("Zthes ID", actualSmartLogicTag.getZthesID());
    assertEquals(
        "SmartLogicTag [children=[], name=Name, level=1, englishName=English Name, zthesID=Zthes ID, order=1]",
        actualToStringResult);
  }

  @Test
  void testToJSON() {
    SmartLogicTag smartLogicTag =
        new SmartLogicTag(new HashSet<>(), "Name", 1, "Zthes ID", "English Name", 1);
    HashSet<SmartLogicTag> parentSmartLogicTagSet = new HashSet<>();
    JsonArray jsonArray = new JsonArray();
    assertSame(jsonArray, smartLogicTag.toJSON(parentSmartLogicTagSet, jsonArray));
  }

  @Test
  void testToJSON2() {
    SmartLogicTag smartLogicTag =
        new SmartLogicTag(new HashSet<>(), "Name", 1, "Zthes ID", "English Name", 1);

    HashSet<SmartLogicTag> parentSmartLogicTagSet = new HashSet<>();
    parentSmartLogicTagSet.add(
        new SmartLogicTag(new HashSet<>(), "Name", 1, "Zthes ID", "English Name", 1));
    JsonArray jsonArray = new JsonArray();
    assertSame(jsonArray, smartLogicTag.toJSON(parentSmartLogicTagSet, jsonArray));
  }

  @Test
  void testToJSON3() {
    SmartLogicTag smartLogicTag =
        new SmartLogicTag(new HashSet<>(), "Name", 1, "Zthes ID", "English Name", 1);

    HashSet<SmartLogicTag> parentSmartLogicTagSet = new HashSet<>();
    parentSmartLogicTagSet.add(
        new SmartLogicTag(new HashSet<>(), "zthes-id", 1, "zthes-id", "zthes-id", 1));
    parentSmartLogicTagSet.add(
        new SmartLogicTag(new HashSet<>(), "Name", 1, "Zthes ID", "English Name", 1));
    JsonArray jsonArray = new JsonArray();
    assertSame(jsonArray, smartLogicTag.toJSON(parentSmartLogicTagSet, jsonArray));
  }

  @Test
  void testToJSON4() {
    SmartLogicTag smartLogicTag =
        new SmartLogicTag(new HashSet<>(), "Name", 1, "Zthes ID", "English Name", 1);

    HashSet<SmartLogicTag> parentSmartLogicTagSet = new HashSet<>();
    parentSmartLogicTagSet.add(
        new SmartLogicTag(new HashSet<>(), "Name", 1, "Zthes ID", "English Name", 0));
    JsonArray jsonArray = new JsonArray();
    assertSame(jsonArray, smartLogicTag.toJSON(parentSmartLogicTagSet, jsonArray));
  }

  @Test
  void testToJSON5() {
    SmartLogicTag smartLogicTag =
        new SmartLogicTag(new HashSet<>(), "Name", 1, "Zthes ID", "English Name", 1);

    Set<SmartLogicTag> parentSmartLogicTagSet = new HashSet<>();
    Set<SmartLogicTag> smartlogictags = new HashSet<>();
    parentSmartLogicTagSet.add(new SmartLogicTag(null, null, 0, null, null, 0));
    JsonArray jsonArray = new JsonArray();
    assertSame(jsonArray, smartLogicTag.toJSON(smartlogictags, jsonArray));
  }

  @Test
  void testToJSON6() {
    SmartLogicTag smartLogicTag =
        new SmartLogicTag(new HashSet<>(), "Name", 1, "Zthes ID", "English Name", 1);

    HashSet<SmartLogicTag> parentSmartLogicTagSet = new HashSet<>();
    parentSmartLogicTagSet.add(
        new SmartLogicTag(new HashSet<>(), "Name", 1, "Zthes ID", "English Name", 1));
    assertNull(smartLogicTag.toJSON(parentSmartLogicTagSet, null));
  }

  @Test
  void testEquals() {
    assertNotEquals(
        new SmartLogicTag(new HashSet<>(), "Name", 1, "Zthes ID", "English Name", 1), null);
    assertNotEquals(
        new SmartLogicTag(new HashSet<>(), "Name", 1, "Zthes ID", "English Name", 1),
        "Different type to SmartLogicTag");
  }

  @Test
  void testEquals2() {
    SmartLogicTag smartLogicTag =
        new SmartLogicTag(new HashSet<>(), "Name", 1, "Zthes ID", "English Name", 1);
    assertEquals(smartLogicTag, smartLogicTag);
    int expectedHashCodeResult = smartLogicTag.hashCode();
    assertEquals(expectedHashCodeResult, smartLogicTag.hashCode());
  }

  @Test
  void testEquals3() {
    SmartLogicTag smartLogicTag =
        new SmartLogicTag(new HashSet<>(), "Name", 1, "Zthes ID", "English Name", 1);
    SmartLogicTag smartLogicTag2 =
        new SmartLogicTag(new HashSet<>(), "Name", 1, "Zthes ID", "English Name", 1);

    assertEquals(smartLogicTag, smartLogicTag2);
    int expectedHashCodeResult = smartLogicTag.hashCode();
    assertEquals(expectedHashCodeResult, smartLogicTag2.hashCode());
  }

  @Test
  void testEquals4() {
    SmartLogicTag smartLogicTag =
        new SmartLogicTag(
            new HashSet<>(),
            "com.kpmg.integration.util.SmartLogicTag",
            1,
            "Zthes ID",
            "English Name",
            1);
    assertNotEquals(
        smartLogicTag,
        new SmartLogicTag(new HashSet<>(), "Name", 1, "Zthes ID", "English Name", 1));
  }
}

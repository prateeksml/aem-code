package com.kpmg.core.models.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.kpmg.core.models.TextList;
import com.kpmg.core.models.TextListItem;
import com.kpmg.core.testcontext.AppAemContext;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(AemContextExtension.class)
class TextListImplTest {
  private final AemContext context = AppAemContext.newAemContext();
  private TextList textList;

  @BeforeEach
  void setUp() {
    context.load().json("/TextListImpl.json", "/content/textlist");
    context.currentResource("/content/textlist");
    textList = context.request().adaptTo(TextList.class);
  }

  @Test
  void testGetListItems() {
    List<TextListItem> listItems = textList.getListItems();
    assertNotNull(listItems);
    assertEquals(2, listItems.size());
    assertEquals("item1", listItems.get(0).getText());
    assertEquals("item2", listItems.get(1).getText());
    assertEquals("item 1", listItems.get(0).getTitle());
    assertEquals("item 2", listItems.get(1).getTitle());
    assertEquals("icon1", listItems.get(0).getIcon());
    assertEquals("icon2", listItems.get(1).getIcon());
    assertEquals("textlistt-ead2eec103", textList.getId());
  }

  @Test
  void testGetListType() {
    assertEquals("decimal-leading-zero", textList.getListType());
  }
}

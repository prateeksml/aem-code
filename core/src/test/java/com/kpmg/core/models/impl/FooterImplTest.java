package com.kpmg.core.models.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import com.kpmg.core.models.Footer;
import com.kpmg.core.testcontext.AppAemContext;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import java.util.Calendar;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(AemContextExtension.class)
class FooterImplTest {
  private final AemContext context = AppAemContext.newAemContext();

  private Footer footer;

  @BeforeEach
  void setUp() {
    footer = FooterTestHelpers.setup(context, "", Footer.class);
  }

  @Test
  void test() {
    assertEquals(
        Calendar.getInstance().get(Calendar.YEAR) + " is the best year", footer.getCopyrightText());
  }
}

package com.kpmg.core.exceptions;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class KPMGRuntimeExceptionTest {

  @Test
  void testConstructor() {
    KPMGRuntimeException kpmgRuntimeException = new KPMGRuntimeException(new Throwable());
    assertNotNull(kpmgRuntimeException);
  }
}

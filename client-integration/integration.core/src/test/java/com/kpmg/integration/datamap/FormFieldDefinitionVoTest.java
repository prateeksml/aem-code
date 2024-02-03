package com.kpmg.integration.datamap;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({MockitoExtension.class})
public class FormFieldDefinitionVoTest {

  @InjectMocks FormFieldDefinitionVo vo;

  @Test
  public void testGetName() {
    FormFieldDefinitionVo vo = new FormFieldDefinitionVo();
    vo.setName("TestName");
    assertEquals("TestName", vo.getName());
  }

  @Test
  public void testGetRegex() {
    FormFieldDefinitionVo vo = new FormFieldDefinitionVo();
    vo.setRegex(".*");
    assertEquals(".*", vo.getRegex());
  }
}

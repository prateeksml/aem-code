package com.kpmg.core.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Any class/method annotated with this annotation indicates the code was migrated workflow code and
 * should be ignored from jacoco code coverage.
 *
 * <p>At the time of this writing, the team had to migrate existing on-prem workflow code; close to
 * 100 classes. Unit test could not be written for this workflow code due to time constraints, but
 * we wanted to maintain code coverage for new code.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface WorkflowCodeExcludeFromCodeCoverageReportGenerated {}

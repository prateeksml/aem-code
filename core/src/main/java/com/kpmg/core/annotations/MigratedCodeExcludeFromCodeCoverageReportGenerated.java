package com.kpmg.core.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Any class/method annotated with this annotation indicates the code was migrated code and should
 * be ignored from jacoco code coverage.
 *
 * <p>At the time of this writing, the team had to migrate existing on-prem code for dateformat. The
 * code is large and convoluted and would require a large effort from the team to refactor and write
 * unit tests for it. Hence, we decided to exclude it from code coverage.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface MigratedCodeExcludeFromCodeCoverageReportGenerated {}

/*
 *  Copyright 2021 Adobe Systems Incorporated
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.kpmg.core.testcontext;

import static com.adobe.cq.wcm.core.components.testing.mock.ContextPlugins.CORE_COMPONENTS;
import static org.apache.sling.testing.mock.caconfig.ContextPlugins.CACONFIG;

import com.adobe.acs.commons.i18n.impl.I18nProviderImpl;
import com.adobe.acs.commons.models.injectors.annotation.impl.ChildResourceFromRequestAnnotationProcessorFactory;
import com.adobe.acs.commons.models.injectors.annotation.impl.I18NAnnotationProcessorFactory;
import com.adobe.acs.commons.models.injectors.impl.ChildResourceFromRequestInjector;
import com.adobe.acs.commons.models.injectors.impl.I18nInjector;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextBuilder;
import io.wcm.testing.mock.aem.junit5.AemContextCallback;
import javax.management.NotCompliantMBeanException;
import org.apache.sling.testing.mock.caconfig.MockContextAwareConfig;
import org.apache.sling.testing.mock.sling.ResourceResolverType;

/** Sets up {@link AemContext} for unit tests in this application. */
public final class AppAemContext {

  /** Custom set up rules required in all unit tests. */
  private static final AemContextCallback SETUP_CALLBACK =
      new AemContextCallback() {
        @Override
        public void execute(AemContext context) {
          // custom project initialization code for every unit test
          MockContextAwareConfig.registerAnnotationPackages(context, "com.kpmg.core.caconfig");
          context.addModelsForPackage("com.kpmg.core.models");
          // ACS Commons Child Resource Injector
          context.registerInjectActivateService(
              new ChildResourceFromRequestAnnotationProcessorFactory());
          context.registerInjectActivateService(new ChildResourceFromRequestInjector());
          // ACS Commons I18N Injector
          try {
            context.registerInjectActivateService(new I18nProviderImpl());
          } catch (NotCompliantMBeanException e) {
            e.printStackTrace();
          }
          context.registerInjectActivateService(new I18NAnnotationProcessorFactory());
          context.registerInjectActivateService(new I18nInjector());
        }
      };

  private AppAemContext() {
    // static methods only

  }

  /**
   * @return {@link AemContext}
   */
  public static AemContext newAemContext() {
    return newAemContextBuilder().build();
  }

  /**
   * @return {@link AemContextBuilder}
   */
  public static AemContextBuilder newAemContextBuilder() {
    return newAemContextBuilder(ResourceResolverType.RESOURCERESOLVER_MOCK);
  }

  /**
   * @return {@link AemContextBuilder}
   */
  public static AemContextBuilder newAemContextBuilder(ResourceResolverType resourceResolverType) {
    return new AemContextBuilder()
        .plugin(CACONFIG)
        .plugin(CORE_COMPONENTS)
        .afterSetUp(SETUP_CALLBACK);
  }
}

/*******************************************************************************
 * Copyright 2016 Adobe
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
(function ($, channel, Coral) {
  "use strict";

  let LABEL_NAME_TO_MATCH = 'Loading Messages';
  const FORM_TYPES = ".cmp-form-container_form-type-select";
  let tabView, tabList, MatchedLabel, anotherTabListItem;
  function InitHideTabLabel(root) {
    LABEL_NAME_TO_MATCH = LABEL_NAME_TO_MATCH.toUpperCase();
    let dialog = $(root);

    tabView = dialog.find('coral-tabview')[0];
    tabList = tabView.tabList.items.getAll();
    for (let i = 0; i < tabList.length; i++) {
      const tabListItem = tabList[i];
      if (tabListItem.label.innerHTML.toUpperCase() == LABEL_NAME_TO_MATCH) {
        MatchedLabel = tabListItem.label;
        break;
      }
    }

    const component = dialog.find(FORM_TYPES)[0];
    handleShowHide(MatchedLabel, component, 'rfp');
  }

  function handleShowHide(item, dropdown, value) {
    let label = $(item);
    let v = dropdown.value;
    if (v == value) {
      label.show();
    } else {
      label.hide();
    }
    dropdown.on('change', function (e) {
      if (e.target.value == value) {
        label.show()
      } else {
        label.hide();
      }
    });
  }

  channel.on("foundation-contentloaded", function (e) {
    Coral.commons.ready(e.target, function (component) {
      InitHideTabLabel(component);
    });
  });

})(jQuery, jQuery(document), Coral);
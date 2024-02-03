import { registerComponent } from "./component";
import { fetchApi } from "./util/fetchApi";

/**
 *
 * @param {*} id the filter listing component id
 * @param {*} filterSelected list of all filter selected
 *            i.e. "Data and Analytics > Artificial Intelligence, Financial Management"
 * @param {*} numResults the number of total search results under current search term and filter
 * @param {*} pageCount the index of the current page in filter listing
 *             i.e, 1 on first load, 3 when user navigated to the 3rd page in pagination etc
 */
function pushListingAnalyticsEvent(id, filterSelected, numResults, pageCount) {
  try {
    if (window.adobeDataLayer && id) {
      const listingDatalayer = window.adobeDataLayer.find(
        (i) => i.component && i.component[id]
      );
      if (listingDatalayer) {
        window.adobeDataLayer.push({
          event: "cmp:show",
          eventInfo: {
            reference: `component.${id}`,
          },
          component: {
            [id]: {
              ...listingDatalayer.component[id],
              searchInfo: {
                filterSelected,
                numResults,
                pageCount,
              },
            },
          },
        });
      }
    }
  } catch (error) {
    console.log("Analytics error", error);
  }
}

const pushListingItemClickEvent = (item, url, title) => {
  if (!item) {
    return; // no item, no event
  }
  item.addEventListener("click", (e) => {
    if (window.adobeDataLayer) {
      window.adobeDataLayer.push({
        event: "cmp:click",
        eventInfo: {
          path: url,
          title: title,
        },
      });
    }
  });
};

function FilterList() {
  const filterListParent = document.querySelectorAll(`[data-filterlist]`)[0],
    filterListParentId = filterListParent.getAttribute("id");
  let totalCount = 0;
  let isCheckboxApiAlreadyLoaded = false;
  let loadMoreTrigger = false;
  const filterOpenButton = filterListParent.querySelectorAll(
      `[data-filterlist-filter-toggle-button]`
    ),
    filterSubmitButton = filterListParent.querySelector(
      `[data-filterlist-submit-button]`
    ),
    filterClearButton = filterListParent.querySelector(
      `[data-filterlist-clear-button]`
    ),
    filterCloseButton = filterListParent.querySelector(
      `[data-filterlist-close-button]`
    ),
    filterByCount = filterListParent.querySelector(
      `[data-filterlist-checkbox-count]`
    ),
    loadMoreBtn = filterListParent.querySelector(
      `[data-filterlist-load-more-button]`
    ),
    filterOverlay = filterListParent.querySelector(`[data-filterlist-overlay]`),
    errorMessageBox = filterListParent.querySelector(
      `[cmp-filterlist-error-message]`
    ),
    serverErrorMessage = errorMessageBox.querySelector(
      `[error-message-failure]`
    ),
    noResultErrorMessage = errorMessageBox.querySelector(
      `[error-message-noresult]`
    ),
    spinner = filterListParent.querySelector(`[cmp-filterlist-spinner]`),
    countBadgeHtml = filterListParent.querySelectorAll(".count-badge"),
    paginationContainer = filterListParent.querySelector(
      `[data-filterlist-pagination]`
    );

  let allCheckBox = filterOverlay.querySelectorAll(`input[type='checkbox']`);

  const listPaginationFrom = 0,
    countryCode = filterListParent.getAttribute(`data-country-code`),
    languageCode = filterListParent.getAttribute(`data-lang-code`),
    listDocType = filterListParent.getAttribute(`data-list-document-type`),
    listSize = parseInt(filterListParent.getAttribute(`data-list-size`)),
    filterCategoryValue = JSON.parse(
      filterListParent.getAttribute("data-filter-category")
    );

  const listCardContainer = filterListParent.querySelector(
    "[cmp_filterlist_result_container]"
  );

  let listCardApiUrl = filterListParent.getAttribute(`data-list-url`);
  listCardApiUrl = `${listCardApiUrl}/${countryCode}_${languageCode}`;

  let filterCheckboxApiUrl = filterListParent.getAttribute(`data-filter-url`);
  filterCheckboxApiUrl = `${filterCheckboxApiUrl}/${countryCode}_${languageCode}`;

  let filterRequestData = {
    categories: filterCategoryValue,
    levels: ["1", "2"],
  };

  let listCardRequestData = {
    items_from: listPaginationFrom,
    size: listSize,
    document_type: listDocType,
    sort: "asc",
  };

  // onSubmit of Filter button
  const filterSubmitHandler = async (e) => {
    e.stopPropagation();
    e.preventDefault();

    const checkedInput = filterOverlay.querySelectorAll(
      `input[type='checkbox']:checked`
    );
    loadMoreTrigger = true;
    closeFilterOverlay();
    if (checkedCheckboxes.length > 0) {
      filterCategorySetRequestData();
      showFilterCountBadge(checkedCheckboxes.length);
      // rest the items_from value due to pagination
      listCardRequestData.items_from = 0;
      // Call List Api based on filter
      loadListCardWithData(listCardApiUrl, listCardRequestData);
    } else {
      // Remove cat_set since no checkbox is checked
      // and rest the items_from value due to pagination
      const { categories_set, ...newObj } = listCardRequestData;
      newObj.items_from = 0;
      hideFilterCountBadge();
      // Call List Api when all items are unchecked.
      loadListCardWithData(listCardApiUrl, newObj);
    }
  };

  const showFilterCountBadge = (count) => {
    filterByCount
      .closest(".cmp-filterlist__header .title")
      .classList.add("active");
    filterByCount.textContent = count;
    for (const tag of countBadgeHtml) {
      tag.classList.add("active");
      tag.textContent = count;
    }
  };

  const hideFilterCountBadge = () => {
    filterByCount
      .closest(".cmp-filterlist__header .title")
      .classList.remove("active");
    for (const tag of countBadgeHtml) {
      tag.classList.remove("active");
    }
  };

  const toggleFilterOverlay = (e) => {
    e.stopPropagation();
    e.preventDefault();
    if (filterOverlay.classList.contains("active")) {
      closeFilterOverlay();
    } else {
      openFilterOverlay();
    }
  };

  const closeFilterOverlay = () => {
    document.documentElement.classList.remove(
      "no-scroll",
      "cmp-filterlist__bodypopup-active"
    );
    filterOverlay.classList.remove("visually-active");
    setTimeout(function () {
      filterOverlay.classList.remove("active");
    }, 900);
  };

  const openFilterOverlay = () => {
    filterOverlay.classList.add("active");
    document.documentElement.classList.add(
      "no-scroll",
      "cmp-filterlist__bodypopup-active"
    );
    setTimeout(function () {
      filterOverlay.classList.add("visually-active");
      filterCloseButton.focus();
    }, 100);
  };
  const hideSpinnerAndHideErrorMessage = () => {
    spinner.classList.remove("active");
    errorMessageBox.classList.remove("active");
    paginationContainer.classList.add("active");
    loadMoreBtn.classList.remove("hide");
    listCardContainer.classList.remove("hide");
  };
  const hideSpinnerAndShowErrorMessage = (hasServerError) => {
    spinner.classList.remove("active");
    errorMessageBox.classList.add("active");
    paginationContainer.classList.remove("active");
    loadMoreBtn.classList.add("hide");
    listCardContainer.classList.add("hide");
    if (hasServerError) {
      serverErrorMessage.classList.remove("hide");
      noResultErrorMessage.classList.add("hide");
    } else {
      serverErrorMessage.classList.add("hide");
      noResultErrorMessage.classList.remove("hide");
    }
  };
  const showSpinnerAndHideErrorMessage = () => {
    spinner.classList.add("active");
    errorMessageBox.classList.remove("active");
    paginationContainer.classList.remove("active");
    loadMoreBtn.classList.add("hide");
    listCardContainer.classList.add("hide");
  };

  const clearAllCheckedCheckboxes = async (e) => {
    e.preventDefault();
    allCheckBox.forEach((el) => {
      el.checked = false;
      el.indeterminate = false;
    });
    hideFilterCountBadge();
    closeFilterOverlay();
    // reset the global ListCardRequestData variable
    const { categories_set, ...newObj } = listCardRequestData;
    listCardRequestData = newObj;
    loadMoreTrigger = true;
    // Call  the ListCard Api
    loadListCardWithData(listCardApiUrl, listCardRequestData);
  };

  // Function sets the listCardRequestData global variable
  // based on the selected checkbox value in api request format.
  const filterCategorySetRequestData = () => {
    // reset the category_set if any prev value exists
    listCardRequestData = {
      ...listCardRequestData,
      categories_set: {},
    };
    const checkboxContainer = document.querySelector("#list_container");
    const checked_checkboxes = checkboxContainer.querySelectorAll(
      `input[type='checkbox']:checked`
    );
    // Map array of checked items with key-value pair
    let arrCheckedChkBox = [...checked_checkboxes].map((x) => {
      let parent = x.getAttribute("data-filter-parent");
      return { id: parent, value: x.value };
    });
    // consolidate the ID with values
    arrCheckedChkBox = Array.from(
      new Set(arrCheckedChkBox.map((s) => s.id))
    ).map((k) => {
      return {
        [k]: arrCheckedChkBox.filter((s) => s.id === k).map((e) => e.value),
      };
    });
    // Re arrange the consolidated checkbox array to match with the api request structure
    // and push the new data to global listCardRequestData variable.
    listCardRequestData.categories_set = undefined;
    for (let i = 0; i < arrCheckedChkBox.length; i++) {
      listCardRequestData = {
        ...listCardRequestData,
        categories_set: {
          ...listCardRequestData.categories_set,
          [Object.keys(arrCheckedChkBox[i])[0]]: Object.values(
            arrCheckedChkBox[i]
          )[0],
        },
      };
    }
  };

  //Handles the Checkbox intermediate state, group checks etc.Mainly UI looks only
  const allCheckBoxCheckUnCheckAction = () => {
    allCheckBox.forEach(function (checkbox) {
      checkbox.addEventListener("change", function () {
        const current_chkbox_is_a_parent =
          checkbox.classList.contains("parent-chkbox");
        if (current_chkbox_is_a_parent) {
          const getChildUL =
            checkbox.parentNode.parentNode.querySelector(".child-ul");
          if (getChildUL && checkbox.checked == false) {
            let hasAnyChildSelected = getChildUL.querySelectorAll(
              "input[type = checkbox]:checked"
            ).length;
            checkbox.indeterminate = hasAnyChildSelected > 0 ? true : false;
          }
        } else {
          let closestUL = checkbox.closest(".child-ul");
          let parentCheckbox = closestUL.parentNode.querySelector(
            "input[type = checkbox"
          );

          let hasAnySiblingsSelected = closestUL.querySelectorAll(
            "input[type = checkbox]:checked"
          ).length;

          parentCheckbox.indeterminate =
            hasAnySiblingsSelected > 0 && !parentCheckbox.checked
              ? true
              : false;
        }
      });
    });
  };

  // Load the Card Template by calling Api
  const loadListCardWithData = async (url, data) => {
    const checkboxContainer = document.querySelector("#list_container");
    const checked_checkboxes = checkboxContainer.querySelectorAll(
      `input[type='checkbox']:checked`
    );
    if (!checked_checkboxes.length) {
      data.categories_set = undefined;
    }
    try {
      let hasLoadListCardError = false;
      showSpinnerAndHideErrorMessage();
      const response = await fetchApi(url, data);
      if (!response || response?.length == 0) {
        hideSpinnerAndShowErrorMessage(true);
        hasLoadListCardError = true;
        return;
      }
      const {
        results: { total_count, results },
      } = response;
      let cardTemplate = document.querySelector(
        "[cmp_filterlist_card_template]"
      );
      let container = filterListParent.querySelector(
        "[cmp_filterlist_result_container]"
      );
      if (Object.keys(results).length > 0 && total_count > 0) {
        if (
          !window.matchMedia("(max-width: 1024px)").matches ||
          loadMoreTrigger
        ) {
          container.innerHTML = "";
        }
        for (let result of results) {
          const templateClone = cardTemplate.content.cloneNode(true);
          let title = templateClone.querySelector("#title"),
            description = templateClone.querySelector("#description"),
            cta_link = templateClone.querySelector("#cta_link"),
            img_sm = templateClone.querySelector("#img_sm"),
            img_md = templateClone.querySelector("#img_md"),
            img_xl = templateClone.querySelector("#img_xl");
          const { resource } = result;
          title.textContent = resource.title;
          description.textContent = resource.description;
          cta_link.setAttribute("href", resource.qualified_url);
          cta_link.setAttribute("alt", resource.title);
          pushListingItemClickEvent(
            cta_link,
            resource.qualified_url,
            resource.title
          );
          img_xl.setAttribute(
            "srcset",
            resource?.image_url &&
              `${resource.image_url}:cq5dam-web-1120-684?wid=560&amp;hei=342,${resource.image_url}:cq5dam-web-1120-684?wid=1120&amp;hei=684 2x`
          );
          img_md.setAttribute(
            "srcset",
            resource?.image_url &&
              `${resource.image_url}:cq5dam-web-1120-684?wid=560&amp;hei=342,${resource.image_url}:cq5dam-web-1120-684?wid=1120&amp;hei=684 2x`
          );
          img_sm.setAttribute(
            "src",
            `${resource.image_url}:cq5dam-web-1120-684?wid=304&hei=171`
          );
          img_sm.setAttribute("alt", resource.image_alt_text || resource.title);
          img_sm.setAttribute(
            "title",
            resource.image_alt_text || resource.title
          );
          container.appendChild(templateClone);
        }
        const count = Math.ceil(total_count / data.size);
        totalCount = count;
        let currentPage = Math.ceil(data.items_from / listSize) + 1;
        const maxVisiblePages = 3;
        let startPage = Math.max(
          currentPage - Math.floor(maxVisiblePages / 2),
          1
        );
        let endPage = Math.min(startPage + maxVisiblePages - 1, count);
        if (endPage - startPage + 1 < maxVisiblePages) {
          startPage = Math.max(endPage - maxVisiblePages + 1, 1);
        }
        document
          .querySelectorAll("[data-filterlist-pagination] ul li.items")
          .forEach((elem) => {
            elem.remove();
          });

        // Page numbers with ellipsis
        if (startPage > 1) {
          createPaginationItems(1, currentPage);
          if (startPage > 2) {
            createPaginationItems("...", currentPage);
          }
        }
        for (let i = startPage; i <= endPage; i++) {
          if (currentPage === 1) {
            createPaginationItems(i, currentPage);
          } else {
            createPaginationItems(i, currentPage);
          }
        }
        if (endPage < count) {
          if (endPage < count - 1) {
            createPaginationItems("...", currentPage);
          }
          createPaginationItems(count, currentPage);
        }
        const nextArrow = paginationContainer.querySelector(
          ".cmp-filterlist__pagination--next a"
        );
        const prevArrow = paginationContainer.querySelector(
          ".cmp-filterlist__pagination--prev a"
        );
        if (currentPage === 1) {
          prevArrow.classList.add("disabled");
        } else {
          if (currentPage === 1) {
            prevArrow.classList.add("disabled");
          } else {
            prevArrow.classList.remove("disabled");
          }
        }
        if (currentPage === endPage) {
          loadMoreBtn.classList.add("disabled");
          nextArrow.classList.add("disabled");
        } else {
          loadMoreBtn.classList.remove("disabled");
          nextArrow.classList.remove("disabled");
        }

        loadMoreBtn.setAttribute("data-next-page", Math.ceil(currentPage));
        hideSpinnerAndHideErrorMessage();

        // get checked checkbox values/name
        let checkedCheckboxes = filterOverlay.querySelectorAll(
          `input[type='checkbox']:checked`
        );
        let filterCheckboxSelected = "";
        checkedCheckboxes.forEach(function (checkbox) {
          filterCheckboxSelected += checkbox.name + ", ";
        });
        filterCheckboxSelected =
          filterCheckboxSelected != ""
            ? filterCheckboxSelected.slice(0, -2)
            : null;
        console.log(
          "Analytics--",
          filterListParentId,
          filterCheckboxSelected,
          total_count,
          currentPage
        );
        pushListingAnalyticsEvent(
          filterListParentId,
          filterCheckboxSelected,
          total_count,
          currentPage
        );
      } else {
        console.log("No list card datas to display");
        container.innerHTML = "";
        hasLoadListCardError = true;
        hideSpinnerAndShowErrorMessage(false);
        return;
      }
      // Load checkbox filter-api only once
      // And when the card api is success.
      if (!isCheckboxApiAlreadyLoaded && !hasLoadListCardError) {
        loadListFilterCheckboxData(filterCheckboxApiUrl, filterRequestData);
        isCheckboxApiAlreadyLoaded = true;
      }
    } catch (error) {
      console.log("error catch", error);
      hideSpinnerAndShowErrorMessage(true);
    }
  };

  loadMoreBtn.addEventListener("click", (e) => {
    e.preventDefault();
    const nextPage = parseInt(
      e.currentTarget.getAttribute("data-next-page"),
      0
    );
    if (totalCount >= nextPage) {
      loadMoreTrigger = false;
      e.currentTarget.setAttribute("data-next-page", nextPage);
      listCardRequestData.items_from = nextPage * listSize;
      loadListCardWithData(listCardApiUrl, listCardRequestData);
    }
  });

  // create Pagination dynamically
  function createPaginationItems(content, currentPage) {
    const paginationContainer = document.querySelector(
      "[data-filterlist-pagination] ul"
    );
    const pagination = document.createElement("li");
    pagination.classList.add("items");
    const anchor = document.createElement("a");
    anchor.textContent = content;
    if (content !== "...") {
      anchor.setAttribute("href", "javascript:void(0)");
    }
    if (content === currentPage) {
      anchor.classList.add("active");
    }
    pagination.appendChild(anchor);
    if (content !== "...") {
      pagination.addEventListener("click", (e) => {
        e.preventDefault();
        listCardRequestData.items_from =
          parseInt(e.currentTarget.querySelector("a").textContent, 0) *
            listSize -
          listSize;
        loadMoreTrigger = false;
        loadListCardWithData(listCardApiUrl, listCardRequestData);
      });
    }

    paginationContainer.insertBefore(
      pagination,
      paginationContainer.lastElementChild
    );
  }
  // Next and Previous button click handler
  paginationContainer.querySelectorAll(".nav-btn").forEach((element) => {
    element.addEventListener("click", (e) => {
      loadMoreTrigger = false;
      e.preventDefault();
      if (!e.currentTarget.classList.contains("disabled")) {
        if (e.currentTarget.classList.contains("next")) {
          const activeElement = parseInt(
            paginationContainer.querySelector("a.active").textContent
          );

          listCardRequestData.items_from =
            (activeElement + 1) * listSize - listSize;
          loadListCardWithData(listCardApiUrl, listCardRequestData);
        } else {
          const activeElement = parseInt(
            paginationContainer.querySelector("a.active").textContent
          );
          listCardRequestData.items_from =
            (activeElement - 1) * listSize - listSize;
          loadListCardWithData(listCardApiUrl, listCardRequestData);
        }
      }
    });
  });
  // Call Checkbox filter api and load its html
  const loadListFilterCheckboxData = async (url, data) => {
    try {
      const checkboxContainer = document.getElementById("list_container");
      let messagebox = filterListParent.querySelector(
        ".checkbox-error-message"
      );
      messagebox.classList.remove("active");
      showSpinnerAndHideErrorMessage();
      const response = await fetchApi(url, data);
      if (!response || response?.length == 0) {
        // use "ShowErrorMessage" if filter api needs to block the entire view with error message.
        // else show filter api error message in the overlay only, with out blocking
        // card list display
        // hideSpinnerAndShowErrorMessage(true);
        hideSpinnerAndHideErrorMessage();
        messagebox.classList.add("active");
        return;
      }
      const {
        results: { total_count },
        categories_set,
      } = response;

      if (total_count > 0 && Object.keys(categories_set).length !== 0) {
        let temp = "";
        // support to create Filter Api Request
        // make array of selected checkbox match with its parent.[service_id, insight_id...]
        for (const y in categories_set) {
          const cat_set = categories_set[y];
          let tag_name = y;
          switch (tag_name) {
            case "service_terms_buckets":
              tag_name = "service_ids";
              break;
            case "insight_terms_buckets":
              tag_name = "insight_ids";
              break;
            case "industry_terms_buckets":
              tag_name = "industry_ids";
              break;
            default:
              tag_name = false;
              break;
          }
          if (!tag_name) {
            console.log("Filter List Api- category set id's not matching.");
            return;
          }
          const filter_uls = createFilterCheckboxTree(
            null,
            cat_set.parent_terms,
            tag_name,
            false
          );
          temp += `<div class="list-sections">
                        <h6> ${cat_set.categories.key}</h6>
                        <div>${filter_uls}</div>
                    </div>`;
        }
        checkboxContainer.innerHTML = temp;
        // re-init the 'allCheckbox' after its loaded through api
        allCheckBox = filterOverlay.querySelectorAll(`input[type='checkbox']`);
        allCheckBoxCheckUnCheckAction();
        hideSpinnerAndHideErrorMessage();
      } else {
        hideSpinnerAndHideErrorMessage();
        messagebox.classList.add("active");
      }
    } catch (error) {
      console.log("loadListFilterCheckboxData error", error);
      hideSpinnerAndHideErrorMessage();
    }
  };

  // Function creats Filter Checkbox in a tree fashion with Api data
  // and add to the HTML page.
  function createFilterCheckboxTree(container, data, category, hasChild) {
    let ul;
    if (container != null) {
      ul = container.appendChild(document.createElement("ul"));
    } else {
      ul = document.createElement("ul");
    }
    for (const [key, val] of Object.entries(data)) {
      let li = ul.appendChild(document.createElement("li"));

      const span = li.appendChild(document.createElement("span"));
      let checkbox = span.appendChild(document.createElement("input"));
      checkbox.setAttribute("type", "checkbox");
      checkbox.setAttribute("data-filter-parent", category);

      const label = span.appendChild(document.createElement("label"));
      if (hasChild) {
        li.setAttribute("class", "child-li");
        ul.setAttribute("class", "child-ul");
        checkbox.setAttribute("class", "child-chkbox");
      } else {
        checkbox.setAttribute("class", "parent-chkbox");
        ul.setAttribute("class", "filter-ul-set");
      }

      label.textContent = val.title;
      label.setAttribute("for", val.id);
      checkbox.setAttribute("name", val.title);
      checkbox.setAttribute("id", val.id);
      checkbox.setAttribute("value", val.id);

      if (val.child) {
        createFilterCheckboxTree(li, val.child, category, true);
      }
    }
    return ul.outerHTML;
  }

  loadListCardWithData(listCardApiUrl, listCardRequestData);
  filterOpenButton.forEach((e) => {
    e.addEventListener("click", toggleFilterOverlay);
  });
  filterCloseButton.addEventListener("click", toggleFilterOverlay);
  filterSubmitButton.addEventListener("click", filterSubmitHandler);
  filterClearButton.addEventListener("click", clearAllCheckedCheckboxes);
}

registerComponent(FilterList, {
  selectors: {
    self: ".cmp-filterlist",
  },
});

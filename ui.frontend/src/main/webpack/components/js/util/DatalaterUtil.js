export const pushDatalayerEvent = (event, path, otherEventInfo = {}) => {
  const onDocumentReady = () => {
    const dataLayerEnabled = document.body.hasAttribute(
      "data-cmp-data-layer-enabled"
    );
    const dataLayer = dataLayerEnabled
      ? (window.adobeDataLayer = window.adobeDataLayer || [])
      : undefined;
    if (dataLayerEnabled) {
      dataLayer.push({
        event: event,
        eventInfo: {
          path,
          ...otherEventInfo,
        },
      });
    }
  };

  if (document.readyState !== "loading") {
    onDocumentReady();
  } else {
    document.addEventListener("DOMContentLoaded", onDocumentReady);
  }
};

export const pushDatalayerShowEvent = (datalayerElementShown) => {
  pushDatalayerEvent(
    "cmp:show",
    `component.${getDataLayerId(datalayerElementShown)}`
  );
};

export const pushDatalayerHideEvent = (datalayerElementShown) => {
  pushDatalayerEvent(
    "cmp:hide",
    `component.${getDataLayerId(datalayerElementShown)}`
  );
};

/**
 * Parses the dataLayer string and returns the ID
 *
 * @private
 * @param {HTMLElement} elemnt
 * @returns {String} dataLayerId or undefined
 */
export const getDataLayerId = (elemnt) => {
  if (elemnt) {
    if (elemnt.dataset.cmpDataLayer) {
      return Object.keys(JSON.parse(elemnt.dataset.cmpDataLayer))[0];
    } else {
      return elemnt.id;
    }
  }
  return null;
};

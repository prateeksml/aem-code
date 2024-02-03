(function (document, $) {
    async function readAllSavedProperties(jsonPath, errorCB) {
        let resp;
        try {
            resp = await fetch(jsonPath);
        } catch (error) {
            return errorCB && errorCB(error);
        }
        if (resp.status !== 200) {
            return errorCB && errorCB("failed to fetch Page Properties!\n" + resp.statusText);
        }
        return await resp.json();
    }
    const PagePropertiesReader = () => {
        let result = {};

        let url = new URL(window.location);
        let pagePath = url.searchParams.get("item");
        let jsonPath = `${url.protocol}//${url.host}${pagePath}/jcr:content.infinity.json`;

        return async (errorCB, force = false) => {
            if (force || Object.keys(result).length <= 0) {
                try {
                    result = await readAllSavedProperties(jsonPath, errorCB);
                } catch (error) {
                    return {};
                }
            }

            return (result && Object.keys(result).length > 0) ? result : {};
        };
    };
    const getPageInfo = PagePropertiesReader();
    const masterpagePath = "/content/kpmgpublic/language-masters";
    var dataVar = "kpmg-auto-update-value";
    $(document).one("dialog-loaded", (e) => {
        let elements = $('[data-kpmg-auto-update-value]');
        elements.each(function () {
            const config = $(this).data(dataVar);
            setupValueFromPageInfo(config, this);
        })
        let readonlyitems = $('[data-kpmg-auto-update-value][data-readonly]');
        readonlyitems.each(function () {
            this.readOnly = this.dataset.readonly === 'true';
        });
    });
    function setupValueFromPageInfo(config, coralField) {
        getPageInfo((e) => console.warn('system-values : could not read page properties', e, config.propertyPath)).then(pageInfo => {
            var val = pageInfo[config.propertyPath];
            coralField.value = val ? val : "";
            if (pageInfo.canonicalUrl && pageInfo.canonicalUrl.indexOf(masterpagePath) > -1) {
                if (config.propertyPath == "sl-insight-common-zthesid" && !coralField.value.length && pageInfo["sl-insight-zthesid"]) {
                    coralField.value = pageInfo["sl-insight-zthesid"];
                }
                else if (config.propertyPath == "sl-insight-common-hierarchy" && !coralField.value.length && pageInfo["sl-insight-hierarchy"]) {
                    coralField.value = pageInfo["sl-insight-hierarchy"];
                }
            }
        });
    }
})(document, jQuery);

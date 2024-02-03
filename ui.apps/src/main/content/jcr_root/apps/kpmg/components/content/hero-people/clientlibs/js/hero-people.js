(function(document, $) {
    var pagePath = `${CQ.shared.HTTP.getPath().replace('/editor.html', '')}/jcr:content.json`;
    var extPagePath = CQ.shared.HTTP.externalize(pagePath);
    var pageData = CQ.shared.HTTP.get(extPagePath);
    var pageObj = CQ.shared.Util.eval(pageData);

    function updateField(value, field) {
        var foundationField = field.adaptTo("foundation-field")
        if (value && foundationField &&  foundationField.setValue) {
            console.log(`Setting value of ${field.attr('name')} to ${value}`);
            foundationField.setValue(value);
            // must be triggered since the value changed and there is core comopoent logic that needs to run based on it.
            // see: https://developer.adobe.com/experience-manager/reference-materials/6-5/granite-ui/api/jcr_root/libs/granite/ui/components/coral/foundation/clientlibs/foundation/vocabulary/field.html
            field.trigger("foundation-field-change");
        }
    }

    document.on("foundation-contentloaded", function(e) {
        setTimeout(function() {
            var fragPath = pageObj['fragmentPath'];
            var fragField = $(document).find("[name='./fragmentPath']");
            updateField(fragPath, fragField);
        }, 0);

    });
})(jQuery(document), Granite.$);
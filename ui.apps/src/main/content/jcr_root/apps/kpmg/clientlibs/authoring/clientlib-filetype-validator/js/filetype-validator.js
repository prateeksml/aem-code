(function(document, $, ns) {
    "use strict";
    $(document).on("click", ".cq-dialog-submit", function(e) {
        e.stopPropagation();
        e.preventDefault();
        var $form = $(this).closest("form.foundation-form"),
            $cssFilePath = $form.find("[name$='./cssPath']"),
            $jsFilePath = $form.find("[name$='./jsPath']"),
            $pdfFilePath = $form.find("[name$='./fileReference']"),
            $input = null,
            authoredPath,
            isError = false,
            patterns = {};
        $cssFilePath.each(function(index, input) {
            validateFileType(input, '.css')
        });

        $jsFilePath.each(function(index, input) {
            validateFileType(input, '.js')
        });

        $pdfFilePath.each(function(index, input) {
            validateFileType(input, '.pdf')
        });

        function validateFileType(input, extension) {
                $input = $(input);
                            authoredPath = $input.val();
                            if (authoredPath != "" && !authoredPath.endsWith(extension) && (authoredPath != null)) {
                                isError = true;
                                $input.css("border", "2px solid #FF0000");
                                ns.ui.helpers.prompt({
                                    title: Granite.I18n.get("Invalid Input"),
                                    message: Granite.I18n.get("Please Enter a valid file type."),
                                    actions: [{
                                        id: "CANCEL",
                                        text: "CANCEL",
                                        className: "coral-Button"
                                    }],
                                    callback: function(actionId) {
                                        if (actionId === "CANCEL") {}
                                    }
                                });
                            } else {
                                $input.css("border", "");
                            }
                }

        if (!isError) {
            $form.submit();
        }
    });
})(document, Granite.$, Granite.author);
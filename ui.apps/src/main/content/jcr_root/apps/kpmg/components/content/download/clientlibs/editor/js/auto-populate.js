(function(window, document, $) {
    "use strict";
    var dialogContentSelector = ".cmp-download__editor";
    var titleCheckboxSelector = 'coral-checkbox[name="./titleFromAsset"]';
    var titleTextfieldSelector = 'input[name="./jcr:title"]';
    var descriptionCheckboxSelector = 'coral-checkbox[name="./descriptionFromAsset"]';
    var descriptionTextfieldSelector = '.cq-RichText-editable[name="./jcr:description"]';
    var CheckboxTextfieldTuple = window.CQ.CoreComponents.CheckboxTextfieldTuple.v1;
    var titleTuple;
    var descriptionTuple;
    var fileReference;
    var cqFileUpload;
    var filepath;

    $(document).on("dialog-loaded", function(e) {
        var $dialog = e.dialog;
        var $dialogContent = $dialog.find(dialogContentSelector);
        var dialogContent = $dialogContent.length > 0 ? $dialogContent[0] : undefined;
        if (dialogContent) {
            var descriptionOverride = document.querySelector('coral-checkbox[name="./descriptionFromAsset"]');
            var descr = document.querySelector('div[name="./jcr:description"]');
            var descrVal = descr.getAttribute('value').replace('<p>','').replace('</p>','').trim();
            if(!descriptionOverride.checked && descrVal!='') {
                descr.setAttribute('data-previous-value', descrVal);
            }
            descriptionOverride.addEventListener('change', function(){
            	if(!descriptionOverride.checked && descr.getAttribute('data-previous-value')== null) {
                    descr.setAttribute('data-previous-value', descrVal);
                }
            });
            var rteInstance = $(descriptionTextfieldSelector).data("rteinstance");
            if (rteInstance && rteInstance.isActive) {
                init($dialog, dialogContent);
            } else {
                $(descriptionTextfieldSelector).on("editing-start", function() {
                    init($dialog, dialogContent);
                });
            }
        }
    });

    // Initialize all fields once both the dialog and the description textfield RTE have loaded
    function init($dialog, dialogContent) {
        titleTuple = new CheckboxTextfieldTuple(dialogContent, titleCheckboxSelector, titleTextfieldSelector, false);
        descriptionTuple = new CheckboxTextfieldTuple(dialogContent, descriptionCheckboxSelector, descriptionTextfieldSelector, true);
        cqFileUpload     = document.querySelector('[name="./fileReference"]');
        if (cqFileUpload) {
            filepath = cqFileUpload.value;
            $(cqFileUpload).on("change", function(e) {
                fileReference = e.target.value;
                retrieveDAMInfo(fileReference).then(
                    function() {
                        titleTuple.reinitCheckbox();
                        descriptionTuple.reinitCheckbox();
                    }
                );
            });

            if(filepath!=''){
                retrieveDAMInfo(filepath).then(
                    function() {
                        titleTuple.reinitCheckbox();
                        descriptionTuple.reinitCheckbox();
                    }
                );
            }
        }
    }

    $(window).on("focus", function() {
        if (fileReference) {
            retrieveDAMInfo(fileReference);
        }
    });

    function retrieveDAMInfo(fileReference) {
        return $.ajax({
            url: fileReference + "/_jcr_content/metadata.json"
        }).done(function(data) {
            if (data) {
                if (descriptionTuple) {
                    var description = data["dc:description"];
                    if (description === undefined || description.trim() === "") {
                        description = data["dc:title"];
                    }
                    descriptionTuple.seedTextValue(description);
                    descriptionTuple.update();
                }
                if (titleTuple) {
                    var title = data["dc:title"];
                    titleTuple.seedTextValue(title);
                    titleTuple.update();
                }
            }
        });
    }
})(window, document, Granite.$);

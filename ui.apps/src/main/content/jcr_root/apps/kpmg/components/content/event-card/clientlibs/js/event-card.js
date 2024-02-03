(function (document, $) {
	$(document).on('dialog-ready', () => {
		var overrideTitle = document.querySelector('[name="./overrideTitle"]');
		var eventTitle = document.querySelector('[name="./eventTitle"]');
		var hiddenEventTitle = document.querySelector('[data-hiddentitle="eventTitle"]');
		var overrideDescription = document.querySelector('[name="./overrideDescription"]');
		var eventDescription = document.querySelector('[name="./eventDescription"]');
		var hiddenDescription = document.querySelector('[data-hiddenDescription="eventDescription"]');
		var pagePath = $(document).find("[name='./linkURL']").val() + '/jcr:content.json';
		var extPagePath = CQ.shared.HTTP.externalize(pagePath);
		var pageData = CQ.shared.HTTP.get(extPagePath);
		var pageObj = CQ.shared.Util.eval(pageData);

		updateAllFields();

		document.querySelector('[name="./linkURL"]').addEventListener('change', function () {
			updateAllFields();
		});

		function updateAllFields() {
			var pagePath = $(document).find("[name='./linkURL']").val() + '/jcr:content.json';
			var extPagePath = CQ.shared.HTTP.externalize(pagePath);
			var pageData = CQ.shared.HTTP.get(extPagePath);
			var pageObj = CQ.shared.Util.eval(pageData);

			if (pageObj != null) {
				var eventTitle = pageObj['jcr:title'];
				var titleField = $(document).find("[name='./eventTitle']");
				if (!overrideTitle.checked) {
					updateField(eventTitle, titleField);
				}

				var eventDescription = pageObj['jcr:description'];
				var descriptionField = $(document).find("[name='./eventDescription']");
				if (!overrideDescription.checked) {
					updateField(eventDescription, descriptionField);
				}

				var eventStartDate = new Date(pageObj['eventStartTimeAndDate']);
				var startDateField = $(document).find("[name='./eventStartTimeAndDate']");
				updateField(eventStartDate, startDateField);

				var eventEndDate = new Date(pageObj['eventEndTimeAndDate']);
				var endDateField = $(document).find("[name='./eventEndTimeAndDate']");
				updateField(eventEndDate, endDateField);

				var eventTimeZone = pageObj['timeZone'];
				var timeZoneField = $(document).find("[name='./eventTimeZone']");
				updateField(eventTimeZone, timeZoneField);
			}
		}

		overrideTitle.addEventListener('change', function () {
			if (overrideTitle.checked) {
				eventTitle.removeAttribute('disabled');
				hiddenEventTitle.setAttribute('disabled');
			} else {
				eventTitle.setAttribute('disabled');
				hiddenEventTitle.removeAttribute('disabled');
				var title = pageObj['jcr:title'];
				var titleField = $(document).find("[name='./eventTitle']");
				updateField(title, titleField);
			}
		});

		if (overrideTitle.checked) {
			eventTitle.removeAttribute('disabled');
			hiddenEventTitle.setAttribute('disabled');
		}


		overrideDescription.addEventListener('change', function () {
			if (overrideDescription.checked) {
				eventDescription.removeAttribute('disabled');
				hiddenDescription.setAttribute('disabled');
			} else {
				eventDescription.setAttribute('disabled');
				hiddenDescription.removeAttribute('disabled');
				var description = pageObj['jcr:description'];
				var descriptionField = $(document).find("[name='./eventDescription']");
				updateField(description, descriptionField);
			}
		});

		if (overrideDescription.checked) {
			eventDescription.removeAttribute('disabled');
			hiddenDescription.setAttribute('disabled');
		}


	});

	var registry = $(window).adaptTo("foundation-registry");
        registry.register("foundation.validation.validator", {
            selector: "[name='./linkURL']",
            validate: function (element) {
                var pagePath = $(document).find("[name='./linkURL']").val() + '/jcr:content.json';
                var extPagePath = CQ.shared.HTTP.externalize(pagePath);
                var pageData = CQ.shared.HTTP.get(extPagePath);
                var pageObj = CQ.shared.Util.eval(pageData);
                if(!pagePath.startsWith('/content/kpmgpublic') || pageObj == null){
                    return "Invalid Page."
                } else {
                    var template = pageObj['cq:template'];
                    var templatePath = '/conf/kpmg/settings/wcm/templates/page-event';
                    if(template!=templatePath) {
                        return "Please select Event Page.";
                    }
                }
            }
        });

    var registry = $(window).adaptTo("foundation-registry");
    registry.register("foundation.validation.validator", {
        selector: "[name='./eventTitle']",
        validate: function (element) {
            var title = element.value;
            if(title.trim()==''){
                return "Event title can't be empty."
            }
        }
    });

	function updateField(value, field) {
		if (value) {
			var title = field;
			title.val(value);
		}
	}


})(document, Granite.$);
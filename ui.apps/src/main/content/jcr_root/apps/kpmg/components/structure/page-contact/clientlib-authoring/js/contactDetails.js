(function (jQuery, Coral) {


    var registry = $(window).adaptTo("foundation-registry");
    registry.register("foundation.validation.validator", {

        selector: "[data-validation=kpmg-phone-validation]",
        validate: function (element) {
            var phoneField = document.querySelector("[data-validate-phno='validate-kpmg-phonenumber']");
            var regEx = new RegExp("^[0-9]{0,19}$");
            var nonDigit = /\D/g;
            var phoneNumber = phoneField.value.trim();
            var flag = regEx.test(phoneNumber);
            if (nonDigit.test(phoneNumber)) {
                return "Only Integers between 0 to 9 is allowed.";
            }
            if (!flag) {
                return "Maximum length allowed for phone number is 19.";
            }
        }

    });

    let repeater = window.setInterval(function () {

        var selected = document.querySelector("[data-select-country='country-code']");
        var country = "";

        if (selected !== null) {
            var isoCode = document.querySelector("[data-iso-code='country-iso-code']");
            selected.addEventListener("change", function () {
                country = selected.value;
                isoCode.value = country;
            });
            country = selected.value;
            if (isoCode != null) {
                isoCode.value = country;
            }
            clearInterval(repeater);
        }
    }, 1000);

})(jQuery, Coral);
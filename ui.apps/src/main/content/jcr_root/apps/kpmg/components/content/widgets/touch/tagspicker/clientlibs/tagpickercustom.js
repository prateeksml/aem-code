(function(window, document, Granite, $) {
    "use strict";
    $(document).on("foundation-contentloaded", function(){
	    $(".js-cq-TagsPickerField").find("input").attr("readonly",true)
        $(".js-cq-TagsPickerField").find("input").addClass("kpmg-readonly-input");
		$(document).on("keyup",'#kpmg-filter-input',function (event) {
                var input, filter, ul, li, a, i;
                input = $("#kpmg-filter-input");
                filter = input.val().toUpperCase();
                if(filter || filter == ""){
                	filter = this.value.toUpperCase();
                }
                ul = $(".kpmg-filter-container");
                li = ul.find("a.coral-ColumnView-item");
                for (i = 0; i < li.length; i++) {
                    a = $(li[i]).attr("title");
                    if (a.toUpperCase().indexOf(filter) > -1) {
                        li[i].style.display = "";
                    } else {
                        li[i].style.display = "none";
                    }
                }
        });
    });
})(window, document, Granite, Granite.$);
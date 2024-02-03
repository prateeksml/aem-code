(function(window, document, $) {
    "use strict";
   let repeater = window.setInterval(function (){

  var selector =document.querySelector("[data-kpmg-campaign='campaign']");
       if(selector!==null)
       {
           selector.value="website";
           clearInterval(repeater);
       }
},1000);


})(window, document, Granite.$);
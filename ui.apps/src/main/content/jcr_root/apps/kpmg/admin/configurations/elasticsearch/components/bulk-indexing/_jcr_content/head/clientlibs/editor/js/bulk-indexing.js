
$(document).on('click', '#btnDryRun',function(e) {
    if($('#pageToIndex').val().trim()!=''){
		var dryRun = true;
		indexPage(dryRun);
    }

   });

$(document).on('click', '#btnExecute',function(e) {
    if($('#pageToIndex').val().trim()!=''){
		var dryRun = false;
		indexPage(dryRun);
    }

   });

function indexPage(dryRun){
    var isDeep = false;
    var servletpath ='';

    if($("#indexType").val() == "add"){
       servletpath = "/bin/kpmg/es/bulkIndex"
       }
    else if($("#indexType").val() == "delete"){
        servletpath = "/bin/kpmg/es/deleteFromIndex"
    }

    $.ajax({
        type : 'GET',
        url : servletpath,
        data : {
            "isDryRun" : dryRun,
            "isDeep": $('#depth').val(),
            "rootPage" : $('#pageToIndex').val()
    	},
        success : function(data) {
            console.log(JSON.stringify(data));
            
        },
        error : function() {
            console.log("Error");
        }
    });
}

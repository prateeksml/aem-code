use(function(){
    var geography = properties.get("sl-geography-keywords");
    var market = properties.get("sl-market-keywords");
    var industry = properties.get("sl-industry-keywords");
    var insight = properties.get("sl-insight-keywords");
    var service = properties.get("sl-service-keywords");
    var industrycommon = properties.get("sl-industry-common-keywords");
    var insightcommon = properties.get("sl-insight-common-keywords");
    var servicecommon = properties.get("sl-service-common-keywords");
	var arr = [];
    if(geography){
		arr.push(geography);
    }
 	if(market){
		arr.push(market);
    }
     if(industry){
		arr.push(industry);
    }
     if(insight){
		arr.push(insight);
    }
     if(service){
		arr.push(service);
    }
     if(industrycommon){
		arr.push(industrycommon);
    }
     if(servicecommon){
		arr.push(servicecommon);
    }
 	if(insightcommon){
		arr.push(insightcommon);
    }

return arr;
})

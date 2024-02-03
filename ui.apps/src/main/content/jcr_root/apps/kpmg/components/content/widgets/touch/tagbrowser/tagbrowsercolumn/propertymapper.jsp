<%@ page import="java.util.Map,
				 java.util.HashMap" %>

<%
	Map<String, Map<String,String>> propertiesMap =  new HashMap<String, Map<String,String>>();
	
	Map<String,String> geographyCategoryMapping =  new HashMap<String,String>();
	geographyCategoryMapping.put("sl-geography-id","geographyId");
	geographyCategoryMapping.put("sl-geography-path","geographyPath");
	geographyCategoryMapping.put("sl-geography-id-path","geographyidPath");
	geographyCategoryMapping.put("sl-geography-keyword","geographyKeywords");
	geographyCategoryMapping.put("sl-geography-iso3166-2","geographyIso31662");
	geographyCategoryMapping.put("sl-geography-unm49-region","geographyUnm49Region");
	geographyCategoryMapping.put("sl-geography-unm49-subregion","geographyUnm49SubRegion");
	geographyCategoryMapping.put("sl-geography-iso3166-3","geographyIso31663");
	geographyCategoryMapping.put("sl-geography-iso3166","geographyIso3166");
	geographyCategoryMapping.put("sl-geography-unm49-subsubregion","geographyUnm49SubSubRegion");
	geographyCategoryMapping.put("sl-geography-qualifiedName", "geographyQualifiedName");
	geographyCategoryMapping.put("sl-geography-id-displayPath","geographyidDisplayPath");

	propertiesMap.put("geography",geographyCategoryMapping);

	Map<String,String> personaCategoryMapping =  new HashMap<String,String>();
	personaCategoryMapping.put("sl-persona-id","personaID");
	personaCategoryMapping.put("sl-persona-path","personaPath");
	personaCategoryMapping.put("sl-persona-id-path","personaidPath");
	personaCategoryMapping.put("sl-persona-qualifiedName","personaQualifiedName");
	personaCategoryMapping.put("sl-persona-id-displayPath","personaidDisplayPath");
	
	propertiesMap.put("persona",personaCategoryMapping);
	
	Map<String,String> contenttypeCategoryMapping =  new HashMap<String,String>();
	contenttypeCategoryMapping.put("sl-content-id","contenttypeID");
	contenttypeCategoryMapping.put("sl-content-path","contenttypePath");
	contenttypeCategoryMapping.put("sl-content-id-path","contenttypeidPath");
	contenttypeCategoryMapping.put("sl-content-qualifiedName","contenttypeQualifiedName");
	contenttypeCategoryMapping.put("sl-content-id-displayPath","contenttypeidDisplayPath");
	
	propertiesMap.put("contenttype",contenttypeCategoryMapping);
	
	Map<String,String> marketCategoryMapping =  new HashMap<String,String>();
	marketCategoryMapping.put("sl-market-id","marketId");
	marketCategoryMapping.put("sl-market-path","marketPath");
	marketCategoryMapping.put("sl-market-id-path","marketidPath");
	marketCategoryMapping.put("sl-market-keyword","marketKeywords");
	marketCategoryMapping.put("sl-market-qualifiedName","marketQualifiedName");
	marketCategoryMapping.put("sl-market-id-displayPath","marketidDisplayPath");
	
	propertiesMap.put("market",marketCategoryMapping);
	
	Map<String,String> mediaformatsCategoryMapping =  new HashMap<String,String>();
	mediaformatsCategoryMapping.put("sl-media-id","mediaformatsID");
	mediaformatsCategoryMapping.put("sl-media-path","mediaformatsPath");
	mediaformatsCategoryMapping.put("sl-media-id-path","mediaformatsidPath");
	mediaformatsCategoryMapping.put("sl-media-qualifiedName","mediaformatsQualifiedName");
	mediaformatsCategoryMapping.put("sl-media-id-displayPath","mediaformatsidDisplayPath"); 

	propertiesMap.put("mediaformats",mediaformatsCategoryMapping);
	
	Map<String,String> insightCategoryMapping =  new HashMap<String,String>();
	insightCategoryMapping.put("sl-insight-id","insightId");
	insightCategoryMapping.put("sl-insight-path","insightPath");
	insightCategoryMapping.put("sl-insight-id-path","insightidPath");
	insightCategoryMapping.put("sl-insight-keyword","insightKeywords");
	insightCategoryMapping.put("sl-insight-qualifiedName","insightQualifiedName"); 
	insightCategoryMapping.put("sl-insight-id-displayPath","insightidDisplayPath"); 

	propertiesMap.put("insight",insightCategoryMapping);
	
	Map<String,String> insightCommonCategoryMapping =  new HashMap<String,String>();
	insightCommonCategoryMapping.put("sl-insight-id-common","insightCommonId");
	insightCommonCategoryMapping.put("sl-insight-path-common","insightCommonPath");
	insightCommonCategoryMapping.put("sl-insight-id-path-common","insightCommonidPath");
	insightCommonCategoryMapping.put("sl-insight-keyword-common","insightCommonKeywords");
	insightCommonCategoryMapping.put("sl-insight-qualifiedName","insightQualifiedName"); 
	insightCommonCategoryMapping.put("sl-insight-id-displayPath","insightidDisplayPath");  

	propertiesMap.put("insightCommon",insightCommonCategoryMapping);


	Map<String,String> industryCategoryMapping =  new HashMap<String,String>();
	industryCategoryMapping.put("sl-industry-id-path","industryidPath");
	industryCategoryMapping.put("sl-industry-id-local","industryLocalId");
	industryCategoryMapping.put("sl-industry-keyword-local","industryLocalKeywords");
	industryCategoryMapping.put("sl-industry-path-local","industryLocalPath");
	industryCategoryMapping.put("sl-industry-qualifiedName","industryQualifiedName");
	industryCategoryMapping.put("sl-industry-id-displayPath","industryidDisplayPath");

	propertiesMap.put("industry",industryCategoryMapping);

    Map<String,String> industryCommonCategoryMapping =  new HashMap<String,String>();
	industryCommonCategoryMapping.put("sl-industry-id-path","industryidPath");
	industryCommonCategoryMapping.put("sl-industry-id","industryId");
	industryCommonCategoryMapping.put("sl-industry-keyword","industryKeywords");
	industryCommonCategoryMapping.put("sl-industry-path","industryPath");
	industryCommonCategoryMapping.put("sl-industry-qualifiedName","industryQualifiedName");
	industryCommonCategoryMapping.put("sl-industry-id-displayPath","industryidDisplayPath");

	propertiesMap.put("industryCommon",industryCommonCategoryMapping);
	
	Map<String,String> serviceCategoryMapping =  new HashMap<String,String>();
	serviceCategoryMapping.put("sl-service-id-path","serviceidPath");
	serviceCategoryMapping.put("sl-service-id-local","serviceLocalId");
	serviceCategoryMapping.put("sl-service-keyword-local","serviceLocalKeywords");
	serviceCategoryMapping.put("sl-service-path-local","serviceLocalPath");
	serviceCategoryMapping.put("sl-service-qualifiedName","serviceQualifiedName");
	serviceCategoryMapping.put("sl-service-id-displayPath","serviceidDisplayPath");


	propertiesMap.put("service",serviceCategoryMapping);

    Map<String,String> serviceCommonCategoryMapping =  new HashMap<String,String>();
	serviceCommonCategoryMapping.put("sl-service-id-path","serviceidPath");
	serviceCommonCategoryMapping.put("sl-service-id","serviceId");
	serviceCommonCategoryMapping.put("sl-service-keyword","serviceKeywords");
	serviceCommonCategoryMapping.put("sl-service-path","servicePath");
	serviceCommonCategoryMapping.put("sl-service-qualifiedName","serviceQualifiedName");
	serviceCommonCategoryMapping.put("sl-service-id-displayPath","serviceidDisplayPath");


	propertiesMap.put("serviceCommon",serviceCommonCategoryMapping);

	Map<String,String> allCategoryMapping =  new HashMap<String,String>();
	allCategoryMapping.put("sl-tag-path","titlePath");
	allCategoryMapping.put("sl-tag-ID","tagID");

	propertiesMap.put("allformats",allCategoryMapping);


%>				 
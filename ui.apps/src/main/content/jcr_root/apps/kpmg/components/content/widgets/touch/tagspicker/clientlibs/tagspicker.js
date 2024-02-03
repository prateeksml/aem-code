/*
 * ADOBE CONFIDENTIAL
 *
 * Copyright 2014 Adobe Systems Incorporated
 * All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains
 * the property of Adobe Systems Incorporated and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Adobe Systems Incorporated and its
 * suppliers and may be covered by U.S. and Foreign Patents,
 * patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Adobe Systems Incorporated.
 */

(function(window, document, Granite, $) {
    "use strict";

    var rel = ".js-cq-TagsPickerField",
        rel2 = ".tagspicker";


    CUI.PathBrowser.register('optionLoader', {
        name: 'cq.tagbrowser.optionloader',
        handler: function(searchFor, callback) {
            jQuery.get( this.$element.data("browserpath") + ".tags.json",
                {
                    suggestByTitle: searchFor,
                    rootPath: this.$element.data("root-path"),
                    start: 0,
                    limit: 50
                },
                function(data) {
                    var tags = data.tags;
                    var result = [];
                    for(var i = 0; i < tags.length; i++) {
                        result.push(tags[i]);
                    }
                    if (callback) callback(result);
                }, "json");
            return false;
        }
    });
    CUI.PathBrowser.register('optionValueReader', {
        name: 'cq.tagbrowser.optionValueReader',
        handler: function(value) {
            return value.path;
        }
    });
    CUI.PathBrowser.register('optionTitleReader', {
        name: 'cq.tagbrowser.optionTitleReader',
        handler: function(value) {
            return value.titlePath;
        }
    });
    CUI.PathBrowser.register('optionRenderer', {
        name: 'cq.tagbrowser.optionRenderer',
        handler: function(iterator, index) {
            var value = this.options.options[index];
            var titleMarkup = '';
            if (this.options.showTitles && this.options.optionDisplayStrings[index] && this.options.optionDisplayStrings[index].length > 0) {
                titleMarkup = this.options.optionDisplayStrings[index];
            }
            return $('<li class="coral-SelectList-item coral-SelectList-item--option" data-value="'+ value +'">'+ titleMarkup +'</li>');
        }
    });
    CUI.PathBrowser.register('autocompleteCallback', {
        name: 'cq.tagbrowser.autocompleteCallback',
        handler: function(searchFor) {
            var self = this;
            var def = $.Deferred();
            if (self.options.optionLoader) {
                var loader = {
                    loadOptions: self.optionLoader
                };
                var loaderDef = $.Deferred();
                loaderDef.promise(loader);
                loader.done(
                    function(object) {
                        if ($.isFunction(object.promise)) {
                            object.done(
                                function(object) {
                                    self._rebuildOptions(def, "", object) // do not send searchFor to rebuildOptions to avoid further filtering of tag results CQ:69492
                                }
                            );
                        } else {
                            self._rebuildOptions(def, "", object);
                        }
                    }
                );
                var results = loader.loadOptions(searchFor, function(data) {
                    loaderDef.resolve(data);
                });
                if (results) {
                    loaderDef.resolve(results);
                }
            }
            return def.promise();
        }
    });

    function sanitizeHTML(text) {
        var element = document.createElement('div');
        element.innerText = text;
        return element.innerHTML;
    }

    /*
     * Add a coral-TagList-tag to the coral-TagList.
     */
    function addTag(tag, $tagList) {
        var fieldName = $tagList.data("fieldname");
        var $existing = $tagList.find('input[name="' + fieldName + '"][value="' + decodeURIComponent(encodeURIComponent(tag.tagID)) + '"]');
        var patchMode = $tagList.hasClass("cq-TagList--patchMode");
        var value = patchMode ? "+" + tag.tagID : tag.tagID;	
        if ($existing.length == 0) {
            var $listItem = $('<li class="coral-TagList-tag coral-TagList-tag--multiline"/>').attr('title', tag.titlePath)
                .append($('<button class="coral-MinimalButton coral-TagList-tag-removeButton" type="button"/>').attr('title', Granite.I18n.get("Remove"))
                    .append($('<i class="coral-Icon coral-Icon--sizeXS coral-Icon--close"/>')))
                .append($('<span class="coral-TagList-tag-label">').text(tag.titlePath))
                .append($('<input type="hidden" name="' + sanitizeHTML(fieldName) + '" value="' + sanitizeHTML(value) + '">'));
			switch(tag.category) {
				case "contenttype":
					$listItem.append($('<input type="hidden" name="' + "./sl-content-id" + '" value="' + sanitizeHTML(tag.contenttypeID) + '">'))
					  .append($('<input type="hidden" name="' + "./sl-content-path" + '" value="' + sanitizeHTML(tag.contenttypePath) + '">'))
					  .append($('<input type="hidden" name="' + "./sl-content-id-path" + '" value="' + sanitizeHTML(tag.contenttypeidPath) + '">'))
					  .append($('<input type="hidden" name="' + "./sl-content-qualifiedName" + '" value="' + sanitizeHTML(tag.contenttypeQualifiedName) + '">'))
					  .append($('<input type="hidden" name="' + "./sl-content-id-displayPath" + '" value="' + sanitizeHTML(tag.contenttypeidDisplayPath) + '">'));
					break;
				case "persona":
					$listItem.append($('<input type="hidden" name="' + "./sl-persona-id" + '" value="' + sanitizeHTML(tag.personaID) + '">'))
					  .append($('<input type="hidden" name="' + "./sl-persona-path" + '" value="' + sanitizeHTML(tag.personaPath) + '">'))
					  .append($('<input type="hidden" name="' + "./sl-persona-id-path" + '" value="' + sanitizeHTML(tag.personaidPath) + '">'))
					  .append($('<input type="hidden" name="' + "./sl-persona-qualifiedName" + '" value="' + sanitizeHTML(tag.personaQualifiedName) + '">'))
					  .append($('<input type="hidden" name="' + "./sl-persona-id-displayPath" + '" value="' + sanitizeHTML(tag.personaidDisplayPath) + '">'));
					break;
				case "market":
					$listItem.append($('<input type="hidden" name="' + "./sl-market-id" + '" value="' + sanitizeHTML(tag.marketId) + '">'))
					  .append($('<input type="hidden" name="' + "./sl-market-path" + '" value="' + sanitizeHTML(tag.marketPath) + '">'))
					  .append($('<input type="hidden" name="' + "./sl-market-id-path" + '" value="' + sanitizeHTML(tag.marketidPath) + '">'))
					  .append($('<input type="hidden" name="' + "./sl-market-keyword" + '" value="' + sanitizeHTML(tag.marketKeywords) + '">'))
					  .append($('<input type="hidden" name="' + "./sl-market-qualifiedName" + '" value="' + sanitizeHTML(tag.marketQualifiedName) + '">'))
					  .append($('<input type="hidden" name="' + "./sl-market-id-displayPath" + '" value="' + sanitizeHTML(tag.marketidDisplayPath) + '">'));
					break;
				case "geography":
					$listItem.append($('<input type="hidden" name="' + "./sl-geography-id" + '" value="' + sanitizeHTML(tag.geographyId) + '">'))
					  .append($('<input type="hidden" name="' + "./sl-geography-path" + '" value="' + sanitizeHTML(tag.geographyPath) + '">'))
					  .append($('<input type="hidden" name="' + "./sl-geography-id-path" + '" value="' + sanitizeHTML(tag.geographyidPath) + '">'))
					  .append($('<input type="hidden" name="' + "./sl-geography-keyword" + '" value="' + sanitizeHTML(tag.geographyKeywords) + '">'))
					  .append($('<input type="hidden" name="' + "./sl-geography-iso3166-2" + '" value="' + sanitizeHTML(tag.geographyIso31662) + '">'))
					  .append($('<input type="hidden" name="' + "./sl-geography-unm49-region" + '" value="' + sanitizeHTML(tag.geographyUnm49Region) + '">'))
					  .append($('<input type="hidden" name="' + "./sl-geography-unm49-subregion" + '" value="' + sanitizeHTML(tag.geographyUnm49SubRegion) + '">'))
					  .append($('<input type="hidden" name="' + "./sl-geography-iso3166-3" + '" value="' + sanitizeHTML(tag.geographyIso31663) + '">'))
					  .append($('<input type="hidden" name="' + "./sl-geography-iso3166" + '" value="' + sanitizeHTML(tag.geographyIso3166) + '">'))
					  .append($('<input type="hidden" name="' + "./sl-geography-unm49-subsubregion" + '" value="' + sanitizeHTML(tag.geographyUnm49SubSubRegion) + '">'))
					  .append($('<input type="hidden" name="' + "./sl-geography-qualifiedName" + '" value="' + sanitizeHTML(tag.geographyQualifiedName) + '">'))
					  .append($('<input type="hidden" name="' + "./sl-geography-id-displayPath" + '" value="' + sanitizeHTML(tag.geographyidDisplayPath) + '">'));
					break;
				case "insight":
					if(tag.insightId){
					$listItem.append($('<input type="hidden" name="' + "./sl-insight-id" + '" value="' + sanitizeHTML(tag.insightId) + '">'))
					  .append($('<input type="hidden" name="' + "./sl-insight-path" + '" value="' + sanitizeHTML(tag.insightPath) + '">'))
					  .append($('<input type="hidden" name="' + "./sl-insight-id-path" + '" value="' + sanitizeHTML(tag.insightidPath) + '">'))
					  .append($('<input type="hidden" name="' + "./sl-insight-keyword" + '" value="' + sanitizeHTML(tag.insightKeywords) + '">'))
					  .append($('<input type="hidden" name="' + "./sl-insight-qualifiedName" + '" value="' + sanitizeHTML(tag.insightQualifiedName) + '">'))
					  .append($('<input type="hidden" name="' + "./sl-insight-id-displayPath" + '" value="' + sanitizeHTML(tag.insightidDisplayPath) + '">'));
                    }else{
                        $listItem.append($('<input type="hidden" name="' + "./sl-insight-id-common" + '" value="' + sanitizeHTML(tag.insightCommonId) + '">'))
					  .append($('<input type="hidden" name="' + "./sl-insight-path-common" + '" value="' + sanitizeHTML(tag.insightCommonPath) + '">'))
					  .append($('<input type="hidden" name="' + "./sl-insight-id-path" + '" value="' + sanitizeHTML(tag.insightCommonidPath) + '">'))
					  .append($('<input type="hidden" name="' + "./sl-insight-keyword-common" + '" value="' + sanitizeHTML(tag.insightCommonKeywords) + '">'))
					  .append($('<input type="hidden" name="' + "./sl-insight-qualifiedName" + '" value="' + sanitizeHTML(tag.insightQualifiedName) + '">'))
					  .append($('<input type="hidden" name="' + "./sl-insight-id-displayPath" + '" value="' + sanitizeHTML(tag.insightidDisplayPath) + '">'));
                        }
					break;
				case "industry":
                     if(tag.industryLocalId){
					$listItem.append($('<input type="hidden" name="' + "./sl-industry-id-path" + '" value="' + sanitizeHTML(tag.industryidPath) + '">'))
					  .append($('<input type="hidden" name="' + "./sl-industry-id-local" + '" value="' + sanitizeHTML(tag.industryLocalId) + '">'))
					  .append($('<input type="hidden" name="' + "./sl-industry-keyword-local" + '" value="' + sanitizeHTML(tag.industryLocalKeywords) + '">'))
					  .append($('<input type="hidden" name="' + "./sl-industry-path-local" + '" value="' + sanitizeHTML(tag.industryLocalPath) + '">'))
					  .append($('<input type="hidden" name="' + "./sl-industry-qualifiedName" + '" value="' + sanitizeHTML(tag.industryQualifiedName) + '">'))
					  .append($('<input type="hidden" name="' + "./sl-industry-id-displayPath" + '" value="' + sanitizeHTML(tag.industryidDisplayPath) + '">'));
            } else{
                $listItem.append($('<input type="hidden" name="' + "./sl-industry-id-path" + '" value="' + sanitizeHTML(tag.industryidPath) + '">'))
					  .append($('<input type="hidden" name="' + "./sl-industry-id" + '" value="' + sanitizeHTML(tag.industryId) + '">'))
					  .append($('<input type="hidden" name="' + "./sl-industry-keyword" + '" value="' + sanitizeHTML(tag.industryKeywords) + '">'))
					  .append($('<input type="hidden" name="' + "./sl-industry-path" + '" value="' + sanitizeHTML(tag.industryPath) + '">'))
					  .append($('<input type="hidden" name="' + "./sl-industry-qualifiedName" + '" value="' + sanitizeHTML(tag.industryQualifiedName) + '">'))
					  .append($('<input type="hidden" name="' + "./sl-industry-id-displayPath" + '" value="' + sanitizeHTML(tag.industryidDisplayPath) + '">'));
            }break;
                case "service":
                if(tag.serviceLocalId){                  
					$listItem.append($('<input type="hidden" name="' + "./sl-service-id-path" + '" value="' + sanitizeHTML(tag.serviceidPath) + '">'))
					  .append($('<input type="hidden" name="' + "./sl-service-id-local" + '" value="' + sanitizeHTML(tag.serviceLocalId) + '">'))
					  .append($('<input type="hidden" name="' + "./sl-service-keyword-local" + '" value="' + sanitizeHTML(tag.serviceKeywords) + '">'))
					  .append($('<input type="hidden" name="' + "./sl-service-path-local" + '" value="' + sanitizeHTML(tag.serviceLocalPath) + '">'))
					  .append($('<input type="hidden" name="' + "./sl-service-qualifiedName" + '" value="' + sanitizeHTML(tag.serviceQualifiedName) + '">'))
					  .append($('<input type="hidden" name="' + "./sl-service-id-displayPath" + '" value="' + sanitizeHTML(tag.serviceidDisplayPath) + '">'));
                }else{
                    $listItem.append($('<input type="hidden" name="' + "./sl-service-id-path" + '" value="' + sanitizeHTML(tag.serviceidPath) + '">'))
					  .append($('<input type="hidden" name="' + "./sl-service-id" + '" value="' + sanitizeHTML(tag.serviceId) + '">'))
					  .append($('<input type="hidden" name="' + "./sl-service-keyword" + '" value="' + sanitizeHTML(tag.serviceKeywords) + '">'))
					  .append($('<input type="hidden" name="' + "./sl-service-path" + '" value="' + sanitizeHTML(tag.servicePath) + '">'))
					  .append($('<input type="hidden" name="' + "./sl-service-qualifiedName" + '" value="' + sanitizeHTML(tag.serviceQualifiedName) + '">'))
					  .append($('<input type="hidden" name="' + "./sl-service-id-displayPath" + '" value="' + sanitizeHTML(tag.serviceidDisplayPath) + '">'));
                }
                break;
				case "mediaformats":
					$listItem.append($('<input type="hidden" name="' + "./sl-media-id" + '" value="' + sanitizeHTML(tag.mediaformatsID) + '">'))
					  .append($('<input type="hidden" name="' + "./sl-media-path" + '" value="' + sanitizeHTML(tag.mediaformatsPath) + '">'))
					  .append($('<input type="hidden" name="' + "./sl-media-id-path" + '" value="' + sanitizeHTML(tag.mediaformatsidPath) + '">'))
					  .append($('<input type="hidden" name="' + "./sl-media-qualifiedName" + '" value="' + sanitizeHTML(tag.mediaformatsQualifiedName) + '">'))
					  .append($('<input type="hidden" name="' + "./sl-media-id-displayPath" + '" value="' + sanitizeHTML(tag.mediaformatsidDisplayPath) + '">'));
					break;
                case "all":
					$listItem.append($('<input type="hidden" name="' + "./sl-tag-id" + '" value="' + sanitizeHTML(tag.allTagID) + '">'))
					  .append($('<input type="hidden" name="' + "./sl-tag-path" + '" value="' + sanitizeHTML(tag.titlePath) + '">'))
                      break;
				default:
				
			}   	
            $tagList.append($listItem);
        }else {
            var ui = $(window).adaptTo("foundation-ui");
            ui.alert("Error", "Record is already exist", "error");            
        }
    }

    /*
     * Handle tag deletion.
     */
    $(document).on("click", rel + " ~ .coral-TagList .coral-TagList-tag-removeButton", function(e) {
        var $tag = $(e.target).closest(".coral-TagList-tag");
        $tag.remove();
    });

    /*
    * Handle tag deletion in patch mode
     */
    $(document).on("itemremoved", ".cq-TagList--patchMode", function (event, item) {
        var $tagList = $(this);
        var name = $tagList.data("fieldname");
        var value = item.value.replace("+", "-");

        var $input = $('<input type="hidden" name="' + name + '" value="' + value + '"/>')

        $tagList.append($input);
    });

    $(document).on("foundation-contentloaded", function (event) {
        var $target = $(event.target);
        var $pathBrowser = $target.find(rel + ".coral-PathBrowser");

        $pathBrowser.each(function () {
            var tagBrowser = $(this).data("pathBrowser");

            /**
             * Handle selections from the PathBrowser picker
             */
            tagBrowser.$picker.off("coral-pathbrowser-picker-confirm" + rel2).on("coral-pathbrowser-picker-confirm" + rel2, function (e) {
                var $input = tagBrowser.inputElement,
                    selections = $(this).find(".coral-ColumnView").data("columnView").getSelectedItems();

                if (selections.length > 0) {
                    $.each(selections, function() {
                        var selectedElem = this.item;
                        var $tagList = tagBrowser.$element.siblings(".coral-TagList.js-TagsPickerField-tagList");
						switch(selectedElem.data("category")) {
							case "contenttype":
								var tag = {
                                titlePath: selectedElem.data("titlepath"),
                                tagID: selectedElem.data("titlepath"),
								category: selectedElem.data("category"),
								contenttypeID: selectedElem.data("slcontentid"),
								contenttypePath: selectedElem.data("slcontentpath"),
								contenttypeidPath: selectedElem.data("slcontentidpath"),
								contenttypeQualifiedName: selectedElem.data("slcontentqualifiedname"),
								contenttypeidDisplayPath: selectedElem.data("slcontentiddisplaypath")
								};
								break;
							case "persona":
								var tag = {
                                titlePath: selectedElem.data("titlepath"),
                                tagID: selectedElem.data("titlepath"),
								category: selectedElem.data("category"),
								personaID: selectedElem.data("slpersonaid"),
								personaPath: selectedElem.data("slpersonapath"),
								personaidPath: selectedElem.data("slpersonaidpath"),
								personaQualifiedName: selectedElem.data("slpersonaqualifiedname"),
								personaidDisplayPath: selectedElem.data("slpersonaiddisplaypath")
								};
								break;
							case "market":
							var tag = {
							titlePath: selectedElem.data("titlepath"),
							tagID: selectedElem.data("titlepath"),
							category: selectedElem.data("category"),
							marketId: selectedElem.data("slmarketid"),
							marketPath: selectedElem.data("slmarketpath"),
							marketidPath: selectedElem.data("slmarketidpath"),
							marketKeywords: selectedElem.data("slmarketkeyword"),
							marketQualifiedName: selectedElem.data("slmarketqualifiedname"),
							marketidDisplayPath: selectedElem.data("slmarketiddisplaypath")
							};
							break;
							case "geography":
								var tag = {
                                titlePath: selectedElem.data("titlepath"),
                                tagID: selectedElem.data("titlepath"),
								category: selectedElem.data("category"),
								geographyId: selectedElem.data("slgeographyid"),
								geographyPath: selectedElem.data("slgeographypath"),
								geographyidPath: selectedElem.data("slgeographyidpath"),
								geographyKeywords: selectedElem.data("slgeographykeyword"),
								geographyIso31662: selectedElem.data("slgeographyiso31662"),

								geographyUnm49Region: selectedElem.data("slgeographyunm49region"),
								geographyUnm49SubRegion: selectedElem.data("slgeographyunm49subregion"),
								geographyIso31663: selectedElem.data("slgeographyiso31663"),
								geographyIso3166: selectedElem.data("slgeographyiso3166"),
								geographyUnm49SubSubRegion: selectedElem.data("slgeographyunm49subsubregion"),
								geographyQualifiedName: selectedElem.data("slgeographyqualifiedname"),
								geographyidDisplayPath: selectedElem.data("slgeographyiddisplaypath")
								};
								break;
							case "insight":

                                 if(selectedElem.data("slinsightid") && selectedElem.data("slinsightkeyword")
                               && selectedElem.data("slinsightpath") ){
                              	var tag = {
                                titlePath: selectedElem.data("titlepath"),
                                tagID: selectedElem.data("titlepath"),
								category: selectedElem.data("category"),
								insightId: selectedElem.data("slinsightid"),
								insightPath: selectedElem.data("slinsightpath"),
								insightidPath: selectedElem.data("slinsightidpath"),
								insightKeywords: selectedElem.data("slinsightkeyword"),
								insightQualifiedName: selectedElem.data("slinsightqualifiedname"),
								insightidDisplayPath: selectedElem.data("slinsightiddisplaypath")
								};

                               }

                               else{
                              	var tag = {
                                titlePath: selectedElem.data("titlepath"),
                                tagID: selectedElem.data("titlepath"),
								category: selectedElem.data("category"),
								insightCommonId: selectedElem.data("slinsightidcommon"),
								insightCommonPath: selectedElem.data("slinsightpathcommon"),
								insightCommonidPath: selectedElem.data("slinsightidpathcommon"),
								insightCommonKeywords: selectedElem.data("slinsightkeywordcommon"),
								insightQualifiedName: selectedElem.data("slinsightqualifiedname"),
								insightidDisplayPath: selectedElem.data("slinsightiddisplaypath")
								};

                               }


								break;
                            case "industry":
                           
                               if(selectedElem.data("slindustryidlocal") && selectedElem.data("slindustrykeywordlocal")
                               && selectedElem.data("slindustrypathlocal") ){
                                var tag = {
                                    titlePath: selectedElem.data("titlepath"),
                                    tagID: selectedElem.data("titlepath"),
                                    category: selectedElem.data("category"),
                                    industryidPath: selectedElem.data("slindustryidpath"),
                                    industryLocalId: selectedElem.data("slindustryidlocal"),
                                    industryLocalKeywords: selectedElem.data("slindustrykeywordlocal"),
                                    industryLocalPath: selectedElem.data("slindustrypathlocal"),
                                    industryQualifiedName: selectedElem.data("slindustryqualifiedname"),
                                    industryidDisplayPath: selectedElem.data("slindustryiddisplaypath")
                                    };

                               }

                               else{
                                var tag = {
                                    titlePath: selectedElem.data("titlepath"),
                                    tagID: selectedElem.data("titlepath"),
                                    category: selectedElem.data("category"),
                                    industryidPath: selectedElem.data("slindustryidpath"),
                                    industryId: selectedElem.data("slindustryid"),
                                    industryKeywords: selectedElem.data("slindustrykeyword"),
                                    industryPath: selectedElem.data("slindustrypath"),
                                    industryQualifiedName: selectedElem.data("slindustryqualifiedname"),
                                    industryidDisplayPath: selectedElem.data("slindustryiddisplaypath")
                                    };

                               }
								
								break;
                            case "service":
                            if(selectedElem.data("slserviceidlocal")&& selectedElem.data("slservicekeywordlocal")
                            && selectedElem.data("slservicekeywordlocal") ){
								var tag = {
                                titlePath: selectedElem.data("titlepath"),
                                tagID: selectedElem.data("titlepath"),
								category: selectedElem.data("category"),
								serviceidPath: selectedElem.data("slserviceidpath"),
								serviceLocalId: selectedElem.data("slserviceidlocal"),
								serviceKeywords: selectedElem.data("slservicekeywordlocal"),
								serviceLocalPath: selectedElem.data("slservicepathlocal"),
								serviceQualifiedName: selectedElem.data("slservicequalifiedname"),
								serviceidDisplayPath: selectedElem.data("slserviceiddisplaypath")
                                };
                            }
                            else{
                                var tag = {
                                    titlePath: selectedElem.data("titlepath"),
                                    tagID: selectedElem.data("titlepath"),
                                    category: selectedElem.data("category"),
                                    serviceidPath: selectedElem.data("slserviceidpath"),
                                    serviceId: selectedElem.data("slserviceid"),
                                    serviceKeywords: selectedElem.data("slservicekeyword"),
                                    servicePath: selectedElem.data("slservicepath"),
                                    serviceQualifiedName: selectedElem.data("slservicequalifiedname"),
                                    serviceidDisplayPath: selectedElem.data("slserviceiddisplaypath")
                                    };                         
                            }
								break;
							case "mediaformats":
								var tag = {
                                titlePath: selectedElem.data("titlepath"),
                                tagID: selectedElem.data("titlepath"),
								category: selectedElem.data("category"),
								mediaformatsID: selectedElem.data("slmediaid"),
								mediaformatsPath: selectedElem.data("slmediapath"),
								mediaformatsidPath: selectedElem.data("slmediaidpath"),
								mediaformatsQualifiedName: selectedElem.data("slmediaqualifiedname"),
								mediaformatsidDisplayPath: selectedElem.data("slmediaiddisplaypath")
								};
								break;
							default:
								var tag = {
                                category: selectedElem.data("category"),
                                tagID: selectedElem.data("titlepath"),
                                titlePath: selectedElem.data("titlepath"),
                                allTagID:selectedElem.data("sltagid"),

								};
						}   
                        addTag(tag, $tagList);
                    });
                    $($input).val("");
                    $(".coral-ColumnView-item.is-active.is-selected", $(this)).toggleClass('is-active is-selected');
                }
            });

            /**
             * Handle type-in from the PathBrowser textfield
             */
            tagBrowser.dropdownList.off("selected" + rel2).on("selected" + rel2, function (e) {
                var $pathBrowser = $(this).closest(rel + ".coral-PathBrowser");
                jQuery.get(e.selectedValue + ".tag.json",
                    {},
                    function(data) {
                        if (data !== undefined) {
                            var $tagList = $pathBrowser.siblings(".coral-TagList.js-TagsPickerField-tagList");
                            addTag(data, $tagList);
                            $pathBrowser.find(".js-coral-pathbrowser-input").val("");
                        } else {
                            $pathBrowser.addClass("is-invalid");
                            $pathBrowser.focus();
                        }
                    },
                    "json");

            });

            /**
             * Handle autocreation of tags from the PathBrowser textfield
             */
            tagBrowser.inputElement.off("keypress" + rel2).on("keypress" + rel2, function (e) {
                var $pathBrowser = $(this).closest(rel + ".coral-PathBrowser");

                if(e.keyCode === 13) {
                    e.preventDefault();
                    var $tagList = $pathBrowser.siblings(".coral-TagList.js-TagsPickerField-tagList"),
                        tag = {
                            titlePath: $(this).val(),
                            tagID: $(this).val()
                        };
                    var allowCreate = $tagList.data("allowcreate");
                    if (allowCreate) {
	                    addTag(tag, $tagList);
                    }
                    $(this).val("");
                }

            });

        });
    });

})(window, document, Granite, Granite.$);

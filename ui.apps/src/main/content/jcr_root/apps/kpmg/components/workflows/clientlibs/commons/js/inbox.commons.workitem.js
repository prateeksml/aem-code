/*
 * ADOBE CONFIDENTIAL
 *
 * Copyright 2016 Adobe Systems Incorporated
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

(function($, Granite, undefined) {
    "use strict";

    /**
     * Path to the workitem dialog.
     *
     * @constant
     * @private
     */
    var DIALOG_PATH = "/mnt/overlay/cq/inbox/content/inbox/dialogs/workitemcompletedialog";

    /**
     * Foundation UI API object.
     */
    var ui = $(window).adaptTo("foundation-ui");

    /**
     * Completes a workitem by showing a dialog which lets the user select the next workflow step.
     *
     * If the user successfully completes the workitem, the window is redirected to <code>successURL</code> (if specified) or else reloaded.
     *
     * @param {string} workitemId - the id of the workitem
     * @param {string} [successURL] - the URL to redirect to after the workitem has successfully been completed
     */
    window.CQ.Inbox.UI.commons.completeWorkitem = function (workitemId, successURL) {
        doWorkitemAction(workitemId,
            ".routes.json",
            "routes",
            Granite.I18n.get("Next Step"),
            Granite.I18n.get("Complete Work Item"),
            Granite.I18n.get("Failed to load workitem information. The selected item may no longer be available."),
            "advance",
            "route-",
            function(selectionChoices) {
                if (selectionChoices && selectionChoices.length>1) {
                    return false;
                }
                return true;
            },
            null,
            successURL,
            true);
    };

    /**
     * Steps back a workitem by showing a dialog which lets the user select a previous step in the workflow.
     *
     * If the user successfully steps back the workitem, the window is redirected to <code>successURL</code> (if specified) or else reloaded.
     *
     * @param {string} workitemId - the id of the workitem
     * @param {string} [successURL] - the URL to redirect to after the workitem has successfully been completed
     */
    window.CQ.Inbox.UI.commons.stepBackWorkitem = function (workitemId, successURL) {
        doWorkitemAction(
            workitemId,
            ".backroutes.json",
            "backroutes",
            Granite.I18n.get("Previous Step"),
            Granite.I18n.get("Step Back Item"),
            Granite.I18n.get("Failed to load the step back information. The selected item may no longer be available."),
            "advanceBack",
            "backroute-",
            function(selectionChoices) {
                if (selectionChoices) {
                    if (selectionChoices.length>1) {
                        return false;
                    } else if (selectionChoices.length==1) {
                        return true;
                    }
                }
                return true;
            },
            function(workitemRoutes) {
                if (workitemRoutes && workitemRoutes.length > 0) {
                    //Step Back On Load
                    var selectField = $("#workitemCompletionDialog").find("coral-select");
                    selectField.prop("disabled", true);
                    var payloadTitle = $("tr:selected").find(".payload-title").text();
                    if (payloadTitle == 'Approve Deactivation Request') {                        
                        selectField.val(workitemRoutes[3].rid);
                    } else if (payloadTitle == 'Reviewer') {
                        selectField.val(workitemRoutes[4].rid);
                    } 
                    return false;
                } else {
                    ui.prompt(
                        Granite.I18n.get("Step Back"),
                        Granite.I18n.get("The selected item cannot step back."),
                        "info",
                        [{
                            text: Granite.I18n.get("OK"),
                            primary: true
                        }]);
                    // abort the submit
                    return true;
                }
            },
            successURL);
    };

    /**
     * Delegates a workitem by showing a dialog which lets the user select a user to assign the workitem to.
     *
     * If the user successfully delegates the workitem, the window is redirected to <code>successURL</code> (if specified) or else reloaded.
     *
     * @param {string} workitemId - the id of the workitem
     * @param {string} [successURL] - the URL to redirect to after the workitem has successfully been completed
     */
    window.CQ.Inbox.UI.commons.delegateWorkitem = function (workitemId, successURL) {
        doWorkitemAction(
            workitemId,
            ".delegatees.json",
            "delegatees",
            Granite.I18n.get("User"),
            Granite.I18n.get("Delegate Item"),
            Granite.I18n.get("Failed to load the delegation information. The selected item may no longer be available."),
            "delegate",
            "delegatee-",
            function(workitemRoutes) {
                if (workitemRoutes && workitemRoutes.length>1) {
                    return false;
                }
                return true;
            },
            function(workitemRoutes) {
                if (workitemRoutes && workitemRoutes.length>0) {

                    return false;
                } else {
                    ui.prompt(
                        Granite.I18n.get("Delegate Item"),
                        Granite.I18n.get("The selected item cannot be delegated."),
                        "info",
                        [{
                            text: Granite.I18n.get("OK"),
                            primary: true
                        }]);
                    // don't show the delegate dialog, just get out.
                    return true;
                }
            },
            successURL);
    };


    var workitemActionDialogNS = ".cq-inbox-workitem-action-dialog";

    function submitInjectedDialogToPayload(payloadPath) {
        var deferred = $.Deferred();

        // we have a custom dialog -> need to post it to the payloadPath
        // and extend the workitem data with the dialog data
        if (payloadPath && payloadPath.length) {

            var originalDialog = $(".external-dialog-injection");
            var dialogClone = originalDialog.clone();

            // we're going to inject the original dialog into the temporary form to serialize
            // such that we can get the form data
            var dialogForm = $("<form>");
            var originalDialog = originalDialog.replaceWith(dialogClone);

            // inject the original dialog into the form to serialize
            dialogForm.append(originalDialog);
            dialogForm.append('<input type="hidden" name=":status" value="browser"/>');

            // get the participants dialog data which will be merged with the rest of the
            // data coming in to the save(data) function and the rest of the form data
            var dialogData = dialogForm.serializeAsJSON();


            // make sure we have fields that start with './' otherwise we can't submit to the payload
            if (hasDotSlash(dialogData)) {
                // replace the dialog that once was there, in the case that the post's fail
                // we put it back to the way it was
                var decoy = $(".external-dialog-injection");
                decoy.replaceWith(originalDialog);

                $.ajax({
                    type: "POST",
                    url: Granite.HTTP.externalize(payloadPath),
                    data: dialogData
                }).done(function () {
                    var formData = originalDialog.closest("form").serializeAsJSON();
                    // pass on the formdata to include in the save.
                    deferred.resolve(formData);
                }).fail(function (jqXHR, textStatus, errorThrown) {
                    deferred.reject(jqXHR, textStatus, errorThrown);
                });
            } else {
                // continue with the custom data that we have not yet used:
                var formData = originalDialog.closest("form").serializeAsJSON();
                // pass on the formdata to include in the save.
                deferred.resolve(formData);
            }
        } else {
            deferred.resolve();
        }
        return deferred;
    }

    function hasDotSlash(dialogData) {
        if (dialogData) {
            for (var property in dialogData) {
                if (dialogData.hasOwnProperty(property)) {
                    if (property.startsWith("./")) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    function doWorkitemAction(itemId,
                              dropdownSelector,
                              dropdownJSONSelector,
                              dropdownLabel,
                              title,
                              errorMessage,
                              command,
                              postDataSelector,
                              isSelectReadonlyCallback,
                              abortActionCallback,
                              successHref,
                              injectCustomDialog) {
        CQ.Inbox.UI.commons.loadDialog(DIALOG_PATH).done(function () {
            var routeSourceURL = itemId + dropdownSelector;
            $('#workitemCompletionDialog coral-dialog-header').text(title);
            var xhr = $.get(Granite.HTTP.externalize(routeSourceURL))
                .done(function (routeInfoAsJSON) {
                    var workitemRoutes;
                    if (routeInfoAsJSON && routeInfoAsJSON[dropdownJSONSelector]) {
                        workitemRoutes = routeInfoAsJSON[dropdownJSONSelector];
                    }

                    loadCustomDialogInjection(itemId, injectCustomDialog)
                        .done(function (injectionHTML) {
                        if (injectionHTML) {
                            $(".cq-inbox-dialog-injection-anchor")
                                .html(injectionHTML);
                            //fix for CQ-4215179
                            $(".cq-inbox-dialog-injection-anchor").trigger("foundation-contentloaded");
                        }

                        var submitAction = "/bin/workflow/inbox";
                        var form = $("#updatetaskform");
                        var dialog = $("#workitemCompletionDialog");

                        var select = dialog.find("coral-select").get(0);
                        if (select && select.items) {
                            select.items.clear();
                        }
                        var commentField = $("[name=comment]", dialog);
                        if (commentField) {
                            commentField.val("");
                        }

                        if (workitemRoutes && workitemRoutes.length > 0) {
                            select.show();
                            for (var routeIndex = 0; routeIndex < workitemRoutes.length; routeIndex++) {
                                var workitemRoute = workitemRoutes[routeIndex];

                                select.items.add({
                                    content: {
                                        innerHTML: workitemRoute.label_xss
                                    },
                                    value: workitemRoute.rid
                                });
                            }
                        } else {
                            select.hide();
                        }

                        $(".workitem-dialog-select").parent().find("label").text(dropdownLabel);

                        var $select = $(".workitem-dialog-select");
                        var coralSelect = $select[0];

                        coralSelect.readOnly = isSelectReadonlyCallback(workitemRoutes);
                        if (abortActionCallback && abortActionCallback(workitemRoutes)) {
                            return;
                        }

                        var coralDialog = dialog.get(0);
                        coralDialog.show();
                        dialog.find(".workitem-complete-dialog-cancel")
                            .off('click' + workitemActionDialogNS)
                            .on('click' + workitemActionDialogNS, function () {
                                // remove the injected dialog pieces...
                                $(".cq-inbox-dialog-injection-anchor").empty();
                        });
						 // ***** Before Submit *****
                         dialog.find(".workitem-complete-dialog-submit")
                           .on('click', function () {
                                 var payloadPath =$("tr:selected").data("payload-link");
                                 var res = decodeURIComponent(payloadPath);
                               if($("coral-dialog-header").text()=="Delegate Item"){    
                                    var arr = res.split("&");
								  var pagePath = arr[1].slice(5);
                                  var delegateUrl =pagePath+"/_jcr_content.sendEmailToDelegatee.json?user="+$("coral-select-item:selected").val()+"&path="+pagePath;
                                  $.get(delegateUrl).done(function(response){

                                });

                               }
                               if($("coral-dialog-header").text()=="Step Back Item"){   
                                  var arr = res.split("&");
								  var pagePath = arr[1].slice(5);
                                  var sendStepBackEmail = false;
								 var payloadTitle = $("tr:selected").find(".payload-title").text();
                   				 if (payloadTitle == 'Reviewer' || payloadTitle == 'Approve Deactivation Request') {   
                                     sendStepBackEmail = true;
                                 }
                                   if (sendStepBackEmail && $(".coral3-Select-label").text() == "Unlock page and assign to initiator"){
                                  var UnlockMailUrl =pagePath+"/_jcr_content.sendStepBackEmailToInitiator.json?path="+pagePath;
                                   $.get(UnlockMailUrl).done(function(response){
                                });
                                }
                                  if (sendStepBackEmail && $(".coral3-Select-label").text() == "Request for Deactivation Unlock page"){
                                  var deactivationMailUrl =pagePath+"/_jcr_content.sendStepBackEmailToInitiator.json?path="+pagePath;
                                   $.get(deactivationMailUrl).done(function(response){
                                });
                                }

                           }
                           });


                        dialog.find(".workitem-complete-dialog-submit")
                            .off('click' + workitemActionDialogNS)
                            .on('click' + workitemActionDialogNS, function () {
                                var that = this;

                                if (!checkRouteSelection()) {
                                    // this should somehow indicate that the
                                    // selected action is invalid.
                                    return;
                                }

                                if(!validateFormFields(dialog)) {
                                    return;
                                }

                                ui.wait();
                                this.disabled = true;

                                var injectedSubmitDeferred = $.Deferred();
                                var payloadPath = $(".external-dialog-injection").data("payloadpath");
                                if (injectionHTML && payloadPath) {
                                    injectedSubmitDeferred = submitInjectedDialogToPayload(payloadPath);
                                } else {
                                    injectedSubmitDeferred.resolve();
                                }

                                injectedSubmitDeferred.done(function(extendedFormData) {
                                    submitForm(form, submitAction, itemId, command, postDataSelector, extendedFormData)
                                        .done(function () {
                                            $(".cq-inbox-dialog-injection-anchor").remove();

                                            if (successHref) {
                                                window.location = Granite.HTTP.getContextPath() + successHref;
                                            } else {
                                                window.location.reload(true);
												//Page Layer Switch
                                                //document.cookie="cq-editor-layer.page=Edit; path = /";
                                            }
                                        })
                                        .fail(function (xhr) {
                                            form.trigger("foundation-form-submit-callback", [xhr]);
                                            coralDialog.hide();
                                            ui.clearWait();
                                            that.disabled = false;
                                        }).done(function () {
                                        // remove the injected dialog pieces...
                                        $(".cq-inbox-dialog-injection-anchor").empty();
                                    });
                                });
                            });
                    }).fail(function() {
                        ui.prompt(
                            title,
                            errorMessage,
                            "error",
                            [{
                                text: Granite.I18n.get("OK"),
                                primary: true,
                                handler: function () {
                                    // reload the list
                                    window.location.reload(true);
                                }
                            }]);
                    });
                }).fail(function () {
                    ui.prompt(
                        title,
                        errorMessage,
                        "error",
                        [{
                            text: Granite.I18n.get("OK"),
                            primary: true,
                            handler: function () {
                                // reload the list
                                window.location.reload(true);
                            }
                        }]);
                });
        }).fail(function (error) {
            ui.alert(Granite.I18n.get("Error"), error, "error");
        });
    };


    function checkRouteSelection() {
        var actionSelect = $("#workitemCompletionDialog").find("coral-select");
        if ( actionSelect.length > 0 ) {
            // only check the actions if the action select is not hidden
            if (!actionSelect[0].hidden) {
                if (!actionSelect.val()) {
                    return false;
                }
            }
        }
        return true;
    };

    function loadCustomDialogInjection(itemId, checkForDialog) {
        // create and store a new deferred object
        var deferred = $.Deferred();

        if (itemId && checkForDialog) {
            // load the dialog
            var url = Granite.HTTP.externalize("/libs/cq/inbox/content/inbox/dialogs/dialogInjectionRender.html?item=" + encodeURIComponent(itemId) + "&ch_ck=" + Date.now());
            $.get(url).done(function (injectionHTML) {
                deferred.resolve(injectionHTML);
            }).fail(function () {
                // signal load failure
                deferred.reject(Granite.I18n.get("Unable to load " + itemId));
            });
        } else {
            deferred.resolve();
        }

        return deferred.promise();
    };

    function submitForm($form, postUrl, taskId, command, postDataSelector, extendedFormdata) {
        var $nextStepSelect = $("#workitemCompletionDialog").find("coral-select");
        var coralNextStepSelect = $nextStepSelect[0];

        var data = {
            "cmd": command,
            ":status": "browser",
            "_charset_": "utf-8",
            "item": taskId
        };

        if (extendedFormdata) {
            $.extend(data, extendedFormdata);
        }

        data[(postDataSelector + taskId)]   = coralNextStepSelect.value;
        data[("comment-" + taskId)] = $("[name=comment]", "#workitemCompletionDialog").val();

        return $.ajax({
            url: postUrl,
            type: "POST",
            data: data
        });
    };

    function validateFormFields(dialog) {
        var isFormValid = true;
        var fields = dialog.find(".coral-Form-field").toArray();

        fields.forEach(function(field) {
            var api = $(field).adaptTo("foundation-validation");
            if (api) {
                if(!api.checkValidity()) {
                    isFormValid = false;
                }
                api.updateUI();
            }
        });

        return isFormValid;
    };

})(Granite.$, Granite);

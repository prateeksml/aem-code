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
(function ($, Granite, document) {

    "use strict";

    /**
     * Maps dialog paths to their deferred objects.
     *
     * @private
     */
    var deferreds = {};

    /**
     * Loads a dialog and appends it to the DOM. Returns a promise that is resolved when the dialog is available in the DOM. Calling this function multiple times with the same dialog path doesn't result in multiple loads, but returns promises that are bound to the same load event.
     *
     * @param {string} dialogPath - path to the dialog
     * @returns {Object} a jQuery promise bound to the load event of the specified dialog
     */
    window.CQ.Inbox.UI.commons.loadDialog = function (dialogPath) {
        // get existing deferred, if present
        var deferred = deferreds[dialogPath];
        var flag = true;
        if (!deferred) {
            // create and store a new deferred object
            var deferred = $.Deferred();
            deferreds[dialogPath] = deferred;
            // load the dialog
            var url = Granite.HTTP.externalize(dialogPath + ".html?ch_ck=" + Date.now());
            var response;
            $.get(url).done(function (dialogHtml) {
                response = dialogHtml;
                // append the dialog
                var $dialog = $(Granite.UI.Foundation.Utils.processHtml(dialogHtml));
                $(document.body).append($dialog);
                // trigger 'cui-contentloaded' to initialized CoralUI 2 components
                $dialog.trigger('cui-contentloaded');
                // defer the resolve to let coral do its thing.
                setTimeout(function () {
                    // signal load success
                    deferred.resolve();
                }, 10);
                $(document).on('cui-contentloaded', dialogReady);
            }).fail(function () {
                // signal load failure
                deferred.reject(Granite.I18n.get("Unable to load " + dialogPath));
            });
        }
        return deferred.promise();
    };

    function dialogReady() {
        $(document).find('#workitemCompletionDialog').ready(function () {
            $(".betty-ActionBar.betty-ActionBar--large").parent().removeClass("foundation-mode-switcher-item-active");
            $(".granite-collection-create").removeClass("granite-collection-create-hidden").removeAttr("hidden");
            var payloadPath = $(".external-dialog-injection").data("payloadpath");
            var $reviewerGroup = $('coral-select[name="reviewerGroup"]');
            var $reviewUserField = $('coral-select[name="reviewUser"]');
            $reviewUserField.parent().hide();
            var blueprintServiceURL = payloadPath+'/_jcr_content.confirmactivation.json?path=' + payloadPath + '&blueprintcheck=true';
            var $activateOnCompletion = $('coral-checkbox[name="activateOnCompletion"]');
            if ($activateOnCompletion !== null && $activateOnCompletion !== '') {
                if (payloadPath !== null && payloadPath !== '') {
                    $.get(blueprintServiceURL).done(function (response) {
                        if (response == "true") {
                            $activateOnCompletion.hide();
                        } else {
                            $activateOnCompletion.show();
                        }

                    });
                }
            }
            $(".workitem-complete-dialog-cancel").on('click', function () {
                $(".betty-ActionBar.betty-ActionBar--large").parent().addClass("foundation-mode-switcher-item-active");
            })
            var $confirmActivationWorkflow = $('coral-checkbox[name="confirmActivation"]');
            var activationOnCompletionChecked = payloadPath+'/_jcr_content.confirmactivation.json?path=' + payloadPath + '&activationCheck=true';
            if ($confirmActivationWorkflow !== null && $confirmActivationWorkflow !== '') {
                if (payloadPath !== null && payloadPath !== '') {
                    $.get(blueprintServiceURL).done(function (response) {
                        if (response == "true") {
                            $confirmActivationWorkflow.hide();
                        } else {
                            $.get(activationOnCompletionChecked).done(function (response) {
                                if (response == "false") {
                                    $confirmActivationWorkflow.hide();
                                } else {
                                    $confirmActivationWorkflow.show();
                                    $confirmActivationWorkflow.attr("checked", true);
                                }
                            });
                        }
                    });
                }
            }
            var payloadPath = $(".external-dialog-injection").data("payloadpath");
            var payload;
            if (payloadPath.includes("/content/kpmgpublic/language-masters/")) {
                payload = payloadPath.replaceAll("/content/kpmgpublic/language-masters/", "");
            } else {
                payload = payloadPath.replaceAll("/content/kpmgpublic/", "");
            }
            var site = payload.substring(0, 2);
            var groupListUrl = payloadPath+"/_jcr_content.grouplist.json?site=" + site,
                i;
            if ($reviewerGroup.length > 0) {
                if (payloadPath !== null && payloadPath !== '') {
                    $.get(blueprintServiceURL).done(function (response) {
                        if (response == "true") {
                            $activateOnCompletion.hide();
                        } else {
                            $activateOnCompletion.show();
                        }
                    });
                }
                $.get(groupListUrl).done(function (response) {
                    var groupArr = response.split(',');
                    var groupListArray = '<coral-select-item></coral-select-item>';
                    for (i = 0; i < groupArr.length; i++) {
                        groupListArray += '<coral-select-item value=' + groupArr[i] + '>' + groupArr[i] + '</coral-select-item>';
                    }
                    $reviewerGroup.append(groupListArray);

                });
                $reviewerGroup.on('change', function () {
                    $reviewUserField.parent().show();
                    var usersURL = payloadPath+"/_jcr_content.getUserListService.json?groupID=" + $reviewerGroup.val();
                    $.get(usersURL).done(function (response) {
                        var userArr = response.split(',');
                        var userListArray = '<coral-select-item></coral-select-item>';
                        for (i = 0; i < userArr.length; i++) {
                            var userIDAndLastNameArr = userArr[i].split(':');
                            userListArray += '<coral-select-item value=' + userIDAndLastNameArr[0] + '>' + userIDAndLastNameArr[1] + '</coral-select-item>';
                        }
                        $reviewUserField.find('coral-select-item').remove();
                        $reviewUserField.append(userListArray);
                    });
                });
            }
            // getCurrentUser
            var username = "/libs/granite/security/currentuser.json";
            var $reviewedBy = $('input[name="reviewedBy"]');
            if (null != $reviewedBy && '' != $reviewedBy) {
                $.get(username).done(function (response) {
                    $reviewedBy.val(response.name);
                });
            }
            var reviewedOnBehalf = $('input[name="reviewedOnBehalf"]');
            var url = payloadPath+'/_jcr_content.delegate.json?path=' + payloadPath;
            $.get(url).done(function (response) {
                var deleagetedBy = response.valueOf();
                if (null != reviewedOnBehalf && '' != reviewedOnBehalf) {
                    reviewedOnBehalf.val(deleagetedBy);
                }
            });
            var $pageidentifier = $('textarea[name="./referencepage"]');
            var checkPageActive =  payloadPath+'/_jcr_content.pagereference.json?pageName=' + payloadPath;
            $.get(checkPageActive).done(function (response) {
                if (response == "pagenotactive") {
                    $pageidentifier.val("This page is not active");
                } else {
                    $pageidentifier.val(response);
                }
            });
            var pathfield = $('foundation-autocomplete[name="approvedAssetPath"]');
            var assetselection = $('coral-select[name="actionType"]');
            var kpmgDam = $(".kpmg-dam-check");
            var kpmgblogsDam = $(".kpmgblogs-dam-check");
            //ON DROP DOWN SELECTION CHANGE
            assetselection.on('change coral-component:attached', function () {

                if (assetselection.val() == "rejected") {
                    kpmgDam.parent().hide();
                    kpmgblogsDam.parent().hide()
                    kpmgDam.parent().prop("disabled", true);
                    kpmgblogsDam.parent().prop("disabled", true);

                } else {
                    if (payloadPath.includes("/content/dam/kpmgsites/")) {
                        kpmgDam.parent().show();
                        kpmgDam.parent().prop("disabled", false);
                        kpmgblogsDam.parent().hide();
                        kpmgblogsDam.prop("disabled", true);
                        kpmgblogsDam.parent().remove();
                    } else {
                        kpmgblogsDam.parent().show();
                        kpmgblogsDam.prop("disabled", false);
                        kpmgDam.parent().hide();
                        kpmgDam.prop("disabled", true);
                        kpmgDam.parent().remove();
                    }
                }

            });
            pathfield.on('change', function () {
                var regexStr = /content\/dam\/kpmg/;
                var isInputValid = regexStr.test(pathfield.val());
                if (isInputValid == false) {
                    pathfield.css('border', '1px solid red');
                    return;
                } else {
                    pathfield.css('border', 'none');
                }
            });
            var $note = $('textarea[name="./note"]');
            $note.hide();
            $note.parent().css('font-weight', 'bold');
        });
    }
})(Granite.$, Granite, document);
/* Reinitializes the editor overlay after slide action on author*/
(function(ns, channel){

    var NAVIGATED_EVENT = "cmp-panelcontainer-navigated"; // documented in core components

    function findEditable(id) {
        if (!id || !ns || !ns.editables) return; // exit here. cant do anything
        var editables = Granite.author.editables.find(id);
        if (editables.length > 0) {
            return editables[0];
        }
    }

    channel && channel.on(NAVIGATED_EVENT, function(event){
        var editable = findEditable(event.id);
        if (editable && ns && ns.overlayManager) {
            // https://developer.adobe.com/experience-manager/reference-materials/6-5/jsdoc/ui-touch/editor-core/Granite.author.overlayManager.html#recreate__anchor
            ns.overlayManager.recreate(editable); // recreate the overlay/s
        }
    })
})(Granite.author, $(document));
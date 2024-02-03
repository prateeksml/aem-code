$(document).on("foundation-contentloaded", function(e) {
	var selectBackground = $(document).find("[name='./selectBackground']");
	hideShowTabs(selectBackground);
	$(document).on("change", function(event) {
		var selectBackground = $(document).find("[name='./selectBackground']");
		hideShowTabs(selectBackground);
	});
});

function hideShowTabs(selectBackground) {
	var tabviewSelector = $("coral-tablist > coral-tab");
	var assetIndex = 0;
	var videoIndex = 0;
	var imageAlt = $(document).find('[name="./alt"]');
	if (typeof tabviewSelector !== undefined || tabviewSelector !== null) {
		tabviewSelector.each(function(index) {
			if (tabviewSelector[index].label.innerHTML == 'Asset') {
				assetIndex = index;
			}
			if (tabviewSelector[index].label.innerHTML == 'Media') {
				videoIndex = index;
			}
			if (selectBackground.val() == 'video') {
				tabviewSelector[assetIndex].hide();
				tabviewSelector[videoIndex].show();
				$(document).find('[name="./alt"]').removeAttr('required');
				$(document).find('[name="./altValueFromDAM"]').removeAttr('checked');
			} else {
				tabviewSelector[videoIndex].hide();
				tabviewSelector[assetIndex].show();
				$(document).find('[name="./alt"]').attr('required');
				$(document).find('[name="./altValueFromDAM"]').attr('checked');
			}
		});
	}
}
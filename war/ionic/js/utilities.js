if (!String.prototype.format) {
  String.prototype.format = function() {
    var args = arguments;
    return this.replace(/{(\d+)}/g, function(match, number) {
      return typeof args[number] != 'undefined'
        ? args[number]
        : match
      ;
    });
  };
}

var AppUtilities = AppUtilities || {};

AppUtilities.utilities = {

	showFatalError: function ($ionicPopup, $ionicLoading) {

		if ($ionicLoading) {
			$ionicLoading.hide();
		}	

		$ionicPopup.alert({
                    title: 'Fatal Error',
                    template: 'Sorry, try later'
        });
	}

};
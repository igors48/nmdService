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
	},

	showError: function ($ionicPopup, response) {
    	$ionicPopup.alert({
            title: 'Error',
            template: '<h4>{0}</h4><h6>{1}</h6>'.format(response.error.message, response.error.hints)
        });
	},

  daysDifference: function (date) {
    var now = new Date().getTime();
    var inMinutes = parseInt((date - now) / (60 * 1000));
    var inHours = parseInt((date - now) / (3600 * 1000));
    var inDays = parseInt((date - now) / (24 * 3600 * 1000));

    if (inHours === 0) {
      return inMinutes + 'm';
    }

    return inHours < -24 ? inDays + 'd' : inHours + 'h';
  },

  addTimeDifference: function (items) {
    var me = this;

    angular.forEach(items, function (value, key) {
      value.timeDifference = '( {0} ) '.format(me.daysDifference(value.date));
    })  
  },

  loadingMessage: function (text) {
    return '<ion-spinner icon="bubbles" class="spinner-energized"></ion-spinner><p>{0}</p>'.format(text);
  }

};
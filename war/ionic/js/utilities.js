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

    countDifference: function (date) {
        var now = new Date().getTime();

        var inMinutes = parseInt((date - now) / (60 * 1000));
        var inHours = parseInt((date - now) / (3600 * 1000));
        var inDays = parseInt((date - now) / (24 * 3600 * 1000));

        return {
            minutes: inMinutes,
            hours: inHours,
            days: inDays
        }
    },

  daysDifference: function (date) {
    var difference = this.countDifference(date);

    if (difference.hours === 0) {
      return difference.minutes + 'm';
    }

    return difference.hours < -24 ? difference.days + 'd' : difference.hours + 'h';
  },

  addTimeDifference: function (items) {
    var me = this;

    angular.forEach(items, function (value, key) {
      value.timeDifference = '( {0} ) '.format(me.daysDifference(value.date));
    })  
  },

  addBackColorCode: function (items, lastUsedItemId) {
    var me = this;

    angular.forEach(items, function (value, key) {

        if (value.itemId === lastUsedItemId) {
            value.backColorCode = 'last';
        } else {
            var difference = me.countDifference(value.date);
            value.backColorCode = difference.days <= -1 ? 'old' : '';
            value.backColorCode = difference.days <= -2 ? 'very-old' : value.backColorCode;
        }

    })
  },

  loadingMessage: function (text) {
    return '<ion-spinner icon="ripple" class="spinner-energized"></ion-spinner><p>{0}</p>'.format(text);
  },

  storeScrollPosition: function(id, $rootScope, $ionicScrollDelegate) {
    var scrollPosition = $ionicScrollDelegate.getScrollPosition();
    var top = scrollPosition.top;

    $rootScope.scrollPositions[id] = top;
  },

  resetScrollPosition: function(id, $rootScope) {
    $rootScope.scrollPositions[id] = 0;
  },

  restoreScrollPosition: function(id, $rootScope, $ionicScrollDelegate) {
    var top = $rootScope.scrollPositions[id] || 0;

    $ionicScrollDelegate.scrollTo(0, top, true);
  },

};
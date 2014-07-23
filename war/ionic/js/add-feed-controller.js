'use strict';

controllers.controller('addFeedController',

    function ($scope, $state, $stateParams, $ionicLoading, $ionicPopup, feeds) {
        $scope.utilities = AppUtilities.utilities;

    	$scope.feed = { url: ''};

    	$scope.backToList = function () {
    		$state.go('category', { id : $stateParams.id });
    	};

        $scope.addNewFeed = function (feed) {
            $ionicLoading.show({
                template: 'Adding new feed...'
            });

            //debugger;
            
        	feeds.save(
                {
                    feedUrl: feed.url, 
                    categoryId: $stateParams.id
                }, 
                onAddFeedSuccess, 
                onServerFault);
        };

        var onAddFeedSuccess = function (response) {
            $ionicLoading.hide();

            if (response.status === 'SUCCESS') {
                $ionicPopup.alert({
                    title: 'Information',
                    //TODO string format utility
                    template: 'Feed is added.'
                });
            } else {
                $ionicPopup.alert({
                    title: 'Error',
                    template: response.error.message
                });
            }
        	
            //debugger;
        };

        var onServerFault = function () {
            $scope.utilities.showFatalError($ionicPopup, $ionicLoading);        
        };
    }

);

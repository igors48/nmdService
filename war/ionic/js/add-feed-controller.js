'use strict';

controllers.controller('addFeedController',

    function ($scope, $state, $stateParams, $ionicLoading, $ionicPopup, feeds) {
       $scope.showUi = true;

       $scope.utilities = AppUtilities.utilities;

    	$scope.feed = { url: ''};

    	$scope.backToCategory = function () {
    		$state.go('category', { id : $stateParams.id });
    	};

        $scope.addNewFeed = function (feed) {
            $ionicLoading.show({
                template: 'Adding new feed...'
            });

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
                    template: 'Feed is added.'
                });
            } else {
                $scope.utilities.showError($ionicPopup, response);
            }
        };

        var onServerFault = function () {
            $scope.utilities.showFatalError($ionicPopup, $ionicLoading);        
        };
    }

);

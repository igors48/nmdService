'use strict';

controllers.controller('feedCardController',

    function ($scope, $rootScope, $state, $stateParams, $ionicLoading, $ionicPopup, reads) {
        $scope.showUi = false;

        $scope.utilities = AppUtilities.utilities;

        $scope.backToCategory = function () {
            $state.go('category', { id: $stateParams.categoryId });
        };

        $scope.switchToListView = function () {
           $state.go('feed', { 
                categoryId: $stateParams.categoryId, 
                feedId: $stateParams.feedId 
            });
        }

    }
);

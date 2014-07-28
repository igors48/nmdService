'use strict';

controllers.controller('editFeedController',

    function ($scope, $rootScope, $state, $stateParams, $ionicLoading, $ionicPopup) {
        $scope.showUi = false;

        $scope.utilities = AppUtilities.utilities;

        $scope.backToCategory = function () {
            $state.go('category', { id: $stateParams.categoryId });
        };

        $scope.showUi = true;
    }
);
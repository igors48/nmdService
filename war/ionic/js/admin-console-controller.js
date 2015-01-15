'use strict';

controllers.controller('adminConsoleController',

    function ($scope, $state, $ionicLoading, $ionicPopup) {
        $scope.showUi = true;

        $scope.utilities = AppUtilities.utilities;

        $scope.backToCategories = function () {
            $state.go('categories');
        };

        $scope.onReset = function () {
            $state.go('reset-service');
        };

        $scope.onImport = function () {
            $state.go('import-feeds');
        };    
    }
);

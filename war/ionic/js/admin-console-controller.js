'use strict';

controllers.controller('adminConsoleController',

    function ($scope, $state) {
        $scope.showUi = true;

        $scope.utilities = AppUtilities.utilities;

        $scope.backToCategories = function () {
            $state.go('categories');
        };

    }

);

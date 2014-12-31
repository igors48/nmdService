'use strict';

controllers.controller('resetServiceController',

    function ($scope, $state) {

        $scope.onBackToAdminConsole = function () {
            $state.go('admin-console');
        };

        $scope.onResetConfirmed = function () {
            alert('onResetConfirmed');
        };

    }
);

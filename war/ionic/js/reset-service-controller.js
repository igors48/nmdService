'use strict';

controllers.controller('resetServiceController',

    function ($scope, $state, $ionicLoading, $ionicPopup, reset) {

        $scope.utilities = AppUtilities.utilities;

        $scope.onBackToAdminConsole = function () {
            $state.go('admin-console');
        };

        $scope.onResetConfirmed = function () {
            $ionicLoading.show({
                template: $scope.utilities.loadingMessage('Resetting server...')
            });

            reset.reset(
                onResetSuccess, 
                onServerFault);        
        };

        var onResetSuccess = function (response) {
            $ionicLoading.hide();

            if (response.status === 'SUCCESS') {
                $ionicPopup.alert({
                    title: 'Information',
                    template: 'Server reset complete.'
                }).then(function (response) {
                    $state.go('admin-console');    
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

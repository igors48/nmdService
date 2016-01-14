'use strict';

controllers.controller('flushCacheController',

    function ($scope, $state, $ionicLoading, $ionicPopup, reset) {

        $scope.utilities = AppUtilities.utilities;

        $scope.onBackToAdminConsole = function () {
            $state.go('admin-console');
        };

        $scope.onFlushConfirmed = function () {
            $ionicLoading.show({
                template: $scope.utilities.loadingMessage('Flushing server cache...')
            });

            reset.reset(
                {
                    action: 'cache'
                },
                onResetSuccess, 
                onServerFault);        
        };

        var onResetSuccess = function (response) {
            $ionicLoading.hide();

            if (response.status === 'SUCCESS') {
                $ionicPopup.alert({
                    title: 'Information',
                    template: 'Server cache flushed.'
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

'use strict';

controllers.controller('importFeedsController',

    function ($scope, $state, $ionicLoading, $ionicPopup, imports) {
        $scope.showUi = false;

        $scope.utilities = AppUtilities.utilities;

        $scope.onBackToAdminConsole = function () {
            $state.go('admin-console');
        };

        $scope.onStart = function () {
            $ionicLoading.show({
                template: 'Starting...'
            });

            imports.action(
                {
                    action: 'start'
                },
                onActionCompleted,
                onServerFault);
        };

        $scope.onStop = function () {
            $ionicLoading.show({
                template: 'Stopping...'
            });

            imports.action(
                {
                    action: 'stop'
                },
                onActionCompleted,
                onServerFault);
        };

        $scope.onReject = function () {
            $ionicLoading.show({
                template: 'Rejecting...'
            });

            imports.reject(onActionCompleted, onServerFault);
        };

        $scope.onImport = function () {
            var fileInput = document.getElementById('file').files[0];
            var fileReader = new FileReader();

            fileReader.onloadend = function(event){
                var data = event.target.result;
                upload(data);
            }

            fileReader.readAsText(fileInput);
        };

        var upload = function (data) {
            $ionicLoading.show({
                template: 'Scheduling import...'
            });

            imports.schedule(
                data,
                onScheduleSuccess,
                onServerFault
            )
        };

        var onScheduleSuccess = function (response) {
            $ionicLoading.hide();

            if (response.status === 'SUCCESS') {
                $ionicPopup.alert({
                    title: 'Information',
                    template: 'Import is scheduled.'
                }).then(
                    loadImportStatus
                );
            } else {
                $scope.utilities.showError($ionicPopup, response);
            }
        };

        var loadImportStatus = function () {
            $ionicLoading.show({
                template: 'Loading...'
            });

            imports.status(onloadImportStatusCompleted, onServerFault);
        };

        var onActionCompleted = function (response) {
            $ionicLoading.hide();

            if (response.status !== 'SUCCESS') {
                $scope.utilities.showError($ionicPopup, response);

                return;
            }

            loadImportStatus();
        };

        var onloadImportStatusCompleted = function (response) {
            $ionicLoading.hide();

            if (response.status !== 'SUCCESS') {
                $scope.utilities.showError($ionicPopup, response);

                return;
            }

            $scope.showUi = true;
            $scope.report = response.imports;
        };

        var onServerFault = function () {
            $scope.utilities.showFatalError($ionicPopup, $ionicLoading);        
        };

        loadImportStatus();
    }
);

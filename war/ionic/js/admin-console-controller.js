'use strict';

controllers.controller('adminConsoleController',

    function ($scope, $state, $ionicLoading, $ionicPopup, backup) {
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
        /*    
        $scope.onRestore = function () {
            var fileInput = document.getElementById('file').files[0];
            var fileReader = new FileReader();

            fileReader.onloadend = function(event){
                var data = event.target.result;
                upload(data);
            }

            fileReader.readAsBinaryString(fileInput);
        };

        var upload = function (data) {
            $ionicLoading.show({
                template: 'Uploading file...'
            });

            backup.restore(
                data,
                onUploadSuccess,
                onServerFault
            )
        };

        var onUploadSuccess = function (response) {
            $ionicLoading.hide();

            if (response.status === 'SUCCESS') {
                $ionicPopup.alert({
                    title: 'Information',
                    template: 'File is uploaded.'
                }).then(
                    function () {
                        debugger;
                    }
                );
            } else {
                $scope.utilities.showError($ionicPopup, response);
            }
        };

        var onServerFault = function () {
            $scope.utilities.showFatalError($ionicPopup, $ionicLoading);
        };
*/
    }
);

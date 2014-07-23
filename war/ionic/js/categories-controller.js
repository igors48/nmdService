'use strict';

controllers.controller('categoriesController',

    function ($scope, $state, $ionicLoading, categories) {
        $scope.utilities = AppUtilities.utilities;

        $scope.addCategory = function () {
            $state.go('add-category');
        };

        $scope.openCategory = function (categoryId) {
            //debugger;
            $state.go('category', { id : categoryId });
        };

        //TODO open category
        var loadCategoriesReport = function () {
            $ionicLoading.show({
                template: 'Loading categories...'
            });

            categories.query(onLoadCategoriesReportCompleted, onServerFault);
        };

        //TODO handle possible error
        var onLoadCategoriesReportCompleted = function (response) {
            $ionicLoading.hide();

            $scope.categories = response.reports;
        };

        var onServerFault = function () {
            $scope.utilities.showFatalError($ionicPopup, $ionicLoading);        
        };

        loadCategoriesReport();
    }
);

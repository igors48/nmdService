'use strict';

controllers.controller('categoriesController',

    function ($scope, $rootScope, $state, $ionicLoading, categories) {
        $scope.utilities = AppUtilities.utilities;

        $scope.openCategory = function (categoryId) {
            $rootScope.lastCategoryId = categoryId;

            $state.go('category', { id : categoryId });
        };

        $scope.addCategory = function () {
            $state.go('add-category');
        };

        $scope.editCategory = function (categoryId) {
            $rootScope.lastCategoryId = categoryId;

            $state.go('edit-category', { id : categoryId });
        };

        var loadCategoriesReport = function () {
            $ionicLoading.show({
                template: 'Loading categories...'
            });

            categories.query(onLoadCategoriesReportCompleted, onServerFault);
        };

        var onLoadCategoriesReportCompleted = function (response) {
            $ionicLoading.hide();

            if (response.status !== 'SUCCESS') {
                $scope.utilities.showError($ionicPopup, response);

                return;
            }

            $scope.categories = response.reports;
        };

        var onServerFault = function () {
            $scope.utilities.showFatalError($ionicPopup, $ionicLoading);        
        };

        loadCategoriesReport();
    }
);

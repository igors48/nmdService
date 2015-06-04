'use strict';

controllers.controller('categoriesController',

    function ($scope, $rootScope, $state, $ionicLoading, $ionicPopup, $ionicScrollDelegate, categories) {
        $scope.showUi = false;

        $scope.utilities = AppUtilities.utilities;

        $scope.openCategory = function (categoryId) {
            $rootScope.lastCategoryId = categoryId;

            $scope.utilities.storeScrollPosition('categories', $rootScope, $ionicScrollDelegate);
            $scope.utilities.resetScrollPosition(categoryId, $rootScope);

            $state.go('category', { id : categoryId });
        };

        $scope.addCategory = function () {
            $scope.utilities.resetScrollPosition('categories', $rootScope);

            $state.go('add-category');
        };

        $scope.editCategory = function (categoryId) {
            $rootScope.lastCategoryId = categoryId;

            $scope.utilities.storeScrollPosition('categories', $rootScope, $ionicScrollDelegate);

            $state.go('edit-category', { id : categoryId });
        };

        $scope.openAdminConsole = function () {
            $scope.utilities.storeScrollPosition('categories', $rootScope, $ionicScrollDelegate);

            $state.go('admin-console');
        };

        var loadCategoriesReport = function () {
            $ionicLoading.show({
                template: $scope.utilities.loadingMessage('Loading categories...')
            });

            categories.query(onLoadCategoriesReportCompleted, onServerFault);
        };

        var onLoadCategoriesReportCompleted = function (response) {
            $ionicLoading.hide();

            if (response.status !== 'SUCCESS') {
                $scope.utilities.showError($ionicPopup, response);

                return;
            }

            $scope.showUi = true;
            $scope.categories = response.reports;
            $scope.version = response.version;

            $scope.utilities.restoreScrollPosition('categories', $rootScope, $ionicScrollDelegate);
        };

        var onServerFault = function () {
            $scope.utilities.showFatalError($ionicPopup, $ionicLoading);        
        };

        loadCategoriesReport();
    }
);

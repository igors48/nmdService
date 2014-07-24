'use strict';

controllers.controller('editCategoryController',

    function ($scope, $rootScope, $state, $stateParams, $ionicLoading, $ionicPopup, categories) {
        $scope.showUi = false;

        $scope.utilities = AppUtilities.utilities;

    	$scope.backToCategories = function () {
    		$state.go('categories');
    	};

        $scope.onRenameChosen = function () {
            $scope.category.newName = $scope.category.name;
            $scope.mode = 'rename';
        };

        $scope.onDeleteChosen = function () {
            $scope.mode = 'delete';
        };

        $scope.onCancelChosen = function () {
            $scope.mode = 'choose';
        };

        $scope.renameCategory = function () {
            $ionicLoading.show({
                template: 'Renaming category...'
            });

            categories.update(
                { 
                    categoryId: $stateParams.id
                },
                $scope.category.newName,
                onUpdateCategoryCompleted,
                onServerFault);
        };

        $scope.deleteCategory = function () {
            $ionicLoading.show({
                template: 'Deleting category...'
            });

            categories.delete(
                { 
                    categoryId: $stateParams.id
                },
                onDeleteCategoryCompleted,
                onServerFault);
        };

        var loadCategoryReport = function () {
            $ionicLoading.show({
                template: 'Loading category...'
            });

            categories.query(
                { 
                    categoryId: $stateParams.id
                },
                onLoadCategoryReportCompleted,
                onServerFault);
        };

        var onLoadCategoryReportCompleted = function (response) {
            $ionicLoading.hide();

            if (response.status !== 'SUCCESS') {
                $scope.utilities.showError($ionicPopup, response);

                return;
            }

            $scope.showUi = true;
            $scope.mode = 'choose';

            $scope.category = { 
                name: response.report.name,
                feeds: response.report.feedReports.length
            };
        };

        var onDeleteCategoryCompleted = function (response) {
            $ionicLoading.hide();

            if (response.status !== 'SUCCESS') {
                $scope.utilities.showError($ionicPopup, response);

                return;
            }

            $ionicPopup.alert({
                    title: 'Information',
                    template: 'Category is deleted.'
            }).then($scope.backToCategories);
        };

        var onUpdateCategoryCompleted = function (response) {
            $ionicLoading.hide();

            if (response.status !== 'SUCCESS') {
                $scope.utilities.showError($ionicPopup, response);

                return;
            }

            $ionicPopup.alert({
                    title: 'Information',
                    template: 'Category is updated.'
            }).then($scope.backToCategories);
        };

        var onServerFault = function () {
            $scope.utilities.showFatalError($ionicPopup, $ionicLoading);        
        };

        loadCategoryReport();
    }

);

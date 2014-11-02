'use strict';

controllers.controller('selectFeedCategoryController',

    function ($scope, $state, $stateParams, $ionicLoading, $ionicPopup, categories) {
        $scope.showUi = false;

        $scope.utilities = AppUtilities.utilities;

        $scope.backToEditFeed = function () {
            $state.go('edit-feed', {
                categoryId: $stateParams.categoryId,
                feedId: $stateParams.feedId
            });
        };

        $scope.onSelectCategory = function (categoryId) {
            $ionicLoading.show({
                template: 'Assigning feed...'
            });

            categories.update(
                {
                    categoryId: categoryId,
                    feedId: $stateParams.feedId
                },
                onAssignCompleted,
                onServerFault
            );
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

            $scope.showUi = true;
            $scope.categories = response.reports;
        };

        var onAssignCompleted = function (response) {
            $ionicLoading.hide();

            if (response.status !== 'SUCCESS') {
                $scope.utilities.showError($ionicPopup, response);

                return;
            }

            $ionicPopup.alert({
                    title: 'Information',
                    template: 'Feed is assigned to category.'
            }).then($scope.backToEditFeed);
        };

        var onServerFault = function () {
            $scope.utilities.showFatalError($ionicPopup, $ionicLoading);
        };

        loadCategoriesReport();
    }

);

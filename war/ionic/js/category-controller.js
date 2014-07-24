'use strict';

controllers.controller('categoryController',

    function ($scope, $state, $stateParams, $ionicLoading, categories) {
        $scope.utilities = AppUtilities.utilities;

        $scope.backToCategories = function () {
            $state.go('categories');
        };

        $scope.addFeed = function () {
            $state.go('add-feed', { id: $stateParams.id });
        };

        $scope.openFeed = function (feedId) {
            $state.go('feed', { id: feedId });
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

            $scope.category = { title: response.report.name };
            $scope.feeds = response.report.feedReports;
        };

        var onServerFault = function () {
            $scope.utilities.showFatalError($ionicPopup, $ionicLoading);        
        };

        loadCategoryReport();
    }
);

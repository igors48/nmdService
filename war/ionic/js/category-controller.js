'use strict';

controllers.controller('categoryController',

    function ($scope, $state, $stateParams, $ionicLoading, categories) {

        $scope.addFeed = function () {
            $state.go('add-feed', { id: $stateParams.id });
        };

        $scope.openFeed = function (feedId) {
            //debugger;
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

        //TODO handle possible error
        var onLoadCategoryReportCompleted = function (response) {
            $ionicLoading.hide();

            //debugger;

            $scope.category = { title: response.report.name };
            $scope.feeds = response.report.feedReports;
        };

        //TODO server fault page
        var onServerFault = function () {
            $ionicLoading.hide();

        };

        loadCategoryReport();
    }
);

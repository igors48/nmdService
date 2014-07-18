'use strict';

controllers.controller('categoryController',

    function ($scope, $state, $stateParams, $ionicLoading, feeds) {

        $scope.addFeed = function () {
            debugger;

            $state.go('add-feed', { id : $stateParams.id });
        };

        var loadCategoryReport = function () {
            $ionicLoading.show({
                template: 'Loading category...'
            });

            feeds.query(
                { 
                    categoryId: $stateParams.id
                },
                onLoadCategoryReportCompleted,
                onServerFault);
        };

        //TODO handle possible error
        var onLoadCategoryReportCompleted = function (response) {
            $ionicLoading.hide();

            $scope.feeds = response.headers;
        };

        //TODO server fault page
        var onServerFault = function () {
            $ionicLoading.hide();

        };

        loadCategoryReport();
    }
);

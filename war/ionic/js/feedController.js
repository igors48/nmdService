'use strict';

controllers.controller('feedController',

    function ($scope, $state, $stateParams, $ionicLoading, reads) {

        var loadFeedReport = function () {
            $ionicLoading.show({
                template: 'Loading feed...'
            });

            reads.query(
                { 
                    feedId: $stateParams.id
                },
                onLoadFeedReportCompleted,
                onServerFault);
        };

        //TODO handle possible error
        var onLoadFeedReportCompleted = function (response) {
            $ionicLoading.hide();

            debugger;

            $scope.items = response.reports;
        };

        //TODO server fault page
        var onServerFault = function () {
            $ionicLoading.hide();

        };

        loadFeedReport();
    }
);

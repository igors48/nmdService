'use strict';

controllers.controller('feedController',

    function ($scope, $state, $stateParams, $ionicLoading, reads) {

        $scope.markAsRead = function (feedId, itemId) {
            $ionicLoading.show({
                template: 'Loading feed...'
            });

            reads.mark(
                {
                    feedId: feedId,
                    itemId: itemId,
                    markAs: 'read'
                },
                onMarkAsReadSuccess,
                onServerFault
            );
        };

        var onMarkAsReadSuccess = function (response) {
            $ionicLoading.hide();

            loadFeedReport();
        };

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

            $scope.feed = { title: response.title };
            $scope.items = response.reports;
        };

        //TODO server fault page
        var onServerFault = function () {
            $ionicLoading.hide();

        };

        loadFeedReport();
    }
);
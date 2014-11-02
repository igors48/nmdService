'use strict';

controllers.controller('feedController',

    function ($scope, $rootScope, $state, $stateParams, $ionicLoading, $ionicPopup, reads) {
        $scope.showUi = false;

        $scope.utilities = AppUtilities.utilities;

        $scope.backToCategory = function () {
            $state.go('category', { id: $stateParams.categoryId });
        };

        var loadFeedReport = function () {
            $ionicLoading.show({
                template: 'Loading feed...'
            });

            reads.query(
                { 
                    feedId: $stateParams.feedId
                },
                onLoadFeedReportCompleted,
                onServerFault);
        };

        $scope.markAsRead = function (feedId, itemId) {
            $rootScope.lastItemId = itemId;

            $ionicLoading.show({
                template: 'Marking item...'
            });

            reads.mark(
                {
                    feedId: feedId,
                    itemId: itemId,
                    markAs: 'read'
                },
                onMarkAsReadCompleted,
                onServerFault
            );
        };

        $scope.markAllItemsRead = function () {
            $ionicLoading.show({
                template: 'Marking items...'
            });

            var feedId = $stateParams.feedId;

            $rootScope.lastFeed = feedId;

            reads.mark({
                feedId: feedId
            },
            onMarkAllItemsReadCompleted,
            onServerFault);
        };

        $scope.switchToCardView = function () {
            $state.go('feed-card', { 
                categoryId: $stateParams.categoryId, 
                feedId: $stateParams.feedId 
            });
        };

        var onMarkAllItemsReadCompleted = function (response) {
            var me = this;

            $ionicLoading.hide();

            loadFeedReport();
        };

        var onMarkAsReadCompleted = function (response) {
            $ionicLoading.hide();

            loadFeedReport();
        };

        var onLoadFeedReportCompleted = function (response) {
            $ionicLoading.hide();

            if (response.status !== 'SUCCESS') {
                $scope.utilities.showError($ionicPopup, response);

                return;
            }

            $scope.showUi = true;
 
            $scope.feed = { title: response.title };

            $scope.utilities.addTimeDifference(response.reports);

            $scope.items = response.reports;
        };

        var onServerFault = function () {
            $scope.utilities.showFatalError($ionicPopup, $ionicLoading);       
        };

        loadFeedReport();
    }
);

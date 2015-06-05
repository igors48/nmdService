'use strict';

controllers.controller('feedController',

    function ($scope, $rootScope, $state, $stateParams, $ionicLoading, $ionicPopup, $ionicScrollDelegate, reads) {
        var topItemTimestamp = 0;

        $scope.showUi = false;

        $scope.filter = $stateParams.filter;

        $scope.utilities = AppUtilities.utilities;

        $scope.backToCategory = function () {
            $state.go('category', { id: $stateParams.categoryId });
        };

        var loadFeedReport = function () {
            $ionicLoading.show({
                template: $scope.utilities.loadingMessage('Loading feed...')
            });

            reads.query(
                { 
                    feedId: $stateParams.feedId,
                    filter: $stateParams.filter
                },
                onLoadFeedReportCompleted,
                onServerFault);
        };

        $scope.goToCard = function (itemId) {
            $scope.utilities.storeScrollPosition($stateParams.feedId, $rootScope, $ionicScrollDelegate);

            $state.go('feed-card', {
                categoryId: $stateParams.categoryId,
                feedId: $stateParams.feedId,
                itemId: itemId,
                filter: $stateParams.filter
            });
        };

        $scope.markAsRead = function (feedId, itemId) {
            $rootScope.lastItemId = itemId;

            $scope.utilities.storeScrollPosition($stateParams.feedId, $rootScope, $ionicScrollDelegate);

            $ionicLoading.show({
                template: $scope.utilities.loadingMessage('Marking item...')
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

        $scope.markAsReadLater = function (feedId, itemId) {
            $rootScope.lastItemId = itemId;
            
            $scope.utilities.storeScrollPosition($stateParams.feedId, $rootScope, $ionicScrollDelegate);

            $ionicLoading.show({
                template: $scope.utilities.loadingMessage('Marking item...')
            });

            reads.mark(
                {
                    feedId: feedId,
                    itemId: itemId,
                    markAs: 'readLater'
                },
                onMarkAsReadLaterCompleted,
                onServerFault
            );
        };

        $scope.markAllItemsRead = function () {
            $ionicLoading.show({
                template: $scope.utilities.loadingMessage('Marking items...')
            });

            var feedId = $stateParams.feedId;

            $rootScope.lastFeed = feedId;

            reads.mark({
                feedId: feedId,
                topItemTimestamp: topItemTimestamp
            },
            onMarkAllItemsReadCompleted,
            onServerFault);
        };

        $scope.switchToCardView = function () {
            $scope.utilities.storeScrollPosition($stateParams.feedId, $rootScope, $ionicScrollDelegate);

            $state.go('feed-card', { 
                categoryId: $stateParams.categoryId, 
                feedId: $stateParams.feedId 
            });
        };

        $scope.showAll = function () {
            $scope.utilities.resetScrollPosition($stateParams.feedId, $rootScope);

            $state.go('feed', {
                categoryId: $stateParams.categoryId,
                feedId: $stateParams.feedId,
                filter: 'show-all'
            });
        };

        $scope.showNew = function () {
            $scope.utilities.resetScrollPosition($stateParams.feedId, $rootScope);

            $state.go('feed', {
                categoryId: $stateParams.categoryId,
                feedId: $stateParams.feedId,
                filter: 'show-added'
            });
        };

        $scope.showNotRead = function () {
            $scope.utilities.resetScrollPosition($stateParams.feedId, $rootScope);

            $state.go('feed', {
                categoryId: $stateParams.categoryId,
                feedId: $stateParams.feedId,
                filter: 'show-not-read'
            });
        };

        $scope.showReadLater = function () {
            $scope.utilities.resetScrollPosition($stateParams.feedId, $rootScope);

            $state.go('feed', {
                categoryId: $stateParams.categoryId,
                feedId: $stateParams.feedId,
                filter: 'show-read-later'
            });
        };

        $scope.scrollToTop = function () {
            $ionicScrollDelegate.scrollTo(0, 0, true);
        };
        
        $scope.scrollToBottom = function () {
            $ionicScrollDelegate.scrollTo(0, 48000000, true);
        };
        
        var onMarkAllItemsReadCompleted = function (response) {
            var me = this;

            $ionicLoading.hide();

            $state.go('category', {
                id: $stateParams.categoryId
            });
        };

        var onMarkAsReadCompleted = function (response) {
            $ionicLoading.hide();

            loadFeedReport();
        };

        var onMarkAsReadLaterCompleted = function (response) {
            $ionicLoading.hide();

            loadFeedReport();
        };

        var onLoadFeedReportCompleted = function (response) {
            $ionicLoading.hide();

            if (response.status !== 'SUCCESS') {
                $scope.utilities.showError($ionicPopup, response);

                return;
            }

            topItemTimestamp = response.topItemTimestamp;

            $scope.showUi = true;
 
            $scope.feed = { 
                title: response.title,
                total: response.read + response.notRead, 
                notRead: response.notRead,
                readLater: response.readLater,
                addedSinceLastView: response.addedSinceLastView
            };

            $scope.utilities.addTimeDifference(response.reports);

            $scope.items = response.reports;

            $scope.utilities.restoreScrollPosition($stateParams.feedId, $rootScope, $ionicScrollDelegate);
        };

        var onServerFault = function () {
            $scope.utilities.showFatalError($ionicPopup, $ionicLoading);       
        };

        loadFeedReport();
    }
);

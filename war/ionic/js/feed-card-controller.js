'use strict';

controllers.controller('feedCardController',

    function ($scope, $rootScope, $state, $stateParams, $ionicLoading, $ionicPopup, reads) {
        var pageOffset = 0;
        var pageSize = 5;

        $scope.showUi = false;

        $scope.utilities = AppUtilities.utilities;

        $scope.backToCategory = function () {
            $state.go('category', { id: $stateParams.categoryId });
        };

        $scope.switchToListView = function () {
            $state.go('feed', { 
                categoryId: $stateParams.categoryId, 
                feedId: $stateParams.feedId,
                filter: 'show-all'                 
            });
        };

        $scope.onPrev = function () {

        };
        
        $scope.onNext = function () {

        };

        var loadPreviousCards = function () {
        };
        
        var loadNextCards = function () {
        };
        
        var loadFirstCards = function () {
            $ionicLoading.show({
                template: 'Loading...'
            });

            reads.query(
                { 
                    feedId: $stateParams.feedId,
                    size: pageSize
                },
                onLoadCardsCompleted,
                onServerFault);
        };

        var loadCardsFor = function (itemId, direction) {
            $ionicLoading.show({
                template: 'Loading...'
            });

            reads.query(
                {
                    feedId: $stateParams.feedId,
                    itemId: itemId,
                    direction: direction,
                    size: pageSize
                },
                onLoadCardsForCompleted,
                onServerFault);
        };

        var onLoadCardsForCompleted = function (response) {
            $ionicLoading.hide();

            if (response.status !== 'SUCCESS') {
                $scope.utilities.showError($ionicPopup, response);

                return;
            }

            $scope.utilities.addTimeDifference(response.reports);
            $rootScope.currentPage = response;

            var notEmptyPage = response.reports.length !== 0;

            if (notEmptyPage) {
                // show this card
                if (($rootScope.shownItem === '') || (response.reports.length === 1))}) {
                    showCurrentCard();

                    return;
                }

                // redirect to next/prev
                var shownItemIndex = 1;

                if ($rootScope.currentPage.shownItem === 'prev') {
                    shownItemIndex = response.reports.length - 2;
                }

                var shownItemId = response.reports[shownItemIndex].itemId;

                $state.go('feed-card', {
                    categoryId: $stateParams.categoryId,
                    feedId: $stateParams.feedId,
                    itemId: shownItemId
                });
            }
        };

        var onLoadCardsCompleted = function (response) {
            $ionicLoading.hide();

            if (response.status !== 'SUCCESS') {
                $scope.utilities.showError($ionicPopup, response);

                return;
            }

            $scope.utilities.addTimeDifference(response.reports);
            $rootScope.currentPage = response;

            var notEmptyPage = response.reports.length !== 0;

            if (notEmptyPage) {
                var firstItemId = response.reports[0].itemId;

                $state.go('feed-card', {
                    categoryId: $stateParams.categoryId,
                    feedId: $stateParams.feedId,
                    itemId: firstItemId
                });
            }
        };

        var onServerFault = function () {
            $scope.utilities.showFatalError($ionicPopup, $ionicLoading);       
        };

        var showCurrentCard = function () {
            var currentIndex = findByItemId($rootScope.currentPage.reports, $stateParams.itemId);
            var current = $rootScope.currentPage.reports[currentIndex];

            $scope.feed = {};
            $scope.feed.title = $rootScope.currentPage.title;

            $scope.card = {};
            $scope.card.date = current.date;
            $scope.card.timeDifference = current.timeDifference;
            $scope.card.title = current.title;
            $scope.card.description = current.description;
            $scope.card.link = current.link;
            $scope.card.notRead = !current.read;

            $scope.showUi = true;
        };

        var initialize = function () {
            var firstLoad = !$stateParams.itemId;

            if (firstLoad) {
                loadFirstCards();
                $rootScope.shownItem = '';
            } else {

                if ($rootScope.currentPage) {
                    var itemIndex = findByItemId($rootScope.currentPage.reports, $stateParams.itemId);

                    if (itemIndex === -1) {
                        loadCardsFor($stateParams.itemId, 'next');
                        $rootScope.shownItem = '';
                    } else {
                        showCurrentCard();
                    }
                } else {
                    loadCardsFor($stateParams.itemId, 'next');
                    $rootScope.shownItem = '';
                }
            }
        };

        var findByItemId = function (array, itemId) {
            var length = array.length;

            for (var index = 0; index < length; index++) {
                var current = array[index];

                if (current.itemId === itemId) {
                    return index;
                }
            }

            return -1;
        };

        initialize();
    }
);

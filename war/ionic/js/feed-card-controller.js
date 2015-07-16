'use strict';

controllers.controller('feedCardController',

    function ($scope, $rootScope, $state, $stateParams, $ionicLoading, $ionicPopup, reads, content) {
        var pageSize = 5;

        $scope.showUi = false;
        $scope.showContent = false;

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

        $scope.onMarkAllAsRead = function () {
            alert('asd');
        }

        $scope.onPrev = function () {
            var currentItemIndex = findByItemId($rootScope.currentPage.reports, $stateParams.itemId);
            var firstItemOnPage = currentItemIndex === 0;

            if (firstItemOnPage) {
                $rootScope.shownItem = 'prev';
                loadCardsFor($stateParams.itemId, 'prev');
            } else {
                var prevItemId = $rootScope.currentPage.reports[currentItemIndex - 1].itemId;
                goToItem(prevItemId);
            }
        };
        
        $scope.onNext = function () {
            var currentItemIndex = findByItemId($rootScope.currentPage.reports, $stateParams.itemId);
            var lastItemOnPage = currentItemIndex === $rootScope.currentPage.reports.length - 1;

            if (lastItemOnPage) {
                $rootScope.shownItem = 'next';
                loadCardsFor($stateParams.itemId, 'next');
            } else {
                var nextItemId = $rootScope.currentPage.reports[currentItemIndex + 1].itemId;
                goToItem(nextItemId);
            }
        };

        $scope.onViewFiltered = function () {
            $ionicLoading.show({
                template: $scope.utilities.loadingMessage('Loading...')
            });

            content.filter(
                {
                    link: $scope.card.link
                },
                onContentFilterCompleted,
                onServerFault
            );
        };

        var loadFirstCards = function () {
            $ionicLoading.show({
                template: $scope.utilities.loadingMessage('Loading...')
            });

            reads.query(
                { 
                    feedId: $stateParams.feedId,
                    size: pageSize
                },
                onLoadCardsCompleted,
                onServerFault
            );
        };

        var loadCardsFor = function (itemId, direction) {
            $ionicLoading.show({
                template: $scope.utilities.loadingMessage('Loading...')
            });

            reads.query(
                {
                    feedId: $stateParams.feedId,
                    itemId: itemId,
                    direction: direction,
                    size: pageSize
                },
                onLoadCardsCompleted,
                onServerFault
            );
        };

        var onContentFilterCompleted = function (response) {
            $ionicLoading.hide();

            if (response.status !== 'SUCCESS') {
                $scope.utilities.showError($ionicPopup, response);

                return;
            }
            
            $scope.showContent = true;
            $scope.card.content = response.content;
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

                // show this card
                if (($rootScope.shownItem === 'this') || (response.reports.length === 1)) {
                    showCurrentCard();

                    return;
                }

                // show first card
                var shownItemIndex = 0;

                // show next card
                if (($rootScope.shownItem === 'next')) {
                    shownItemIndex = 1;
                }

                // show prev card
                if ($rootScope.shownItem === 'prev') {
                    shownItemIndex = response.reports.length - 2;
                }

                var shownItemId = response.reports[shownItemIndex].itemId;
                goToItem(shownItemId);
            }
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
            $scope.card.index = current.index + 1;
            $scope.card.total = current.total;

            $scope.showUi = true;
        };

        var goToItem = function (itemId) {
            $state.go('feed-card', {
                categoryId: $stateParams.categoryId,
                feedId: $stateParams.feedId,
                itemId: itemId
            });
        };

        var initialize = function () {
            var firstLoad = !$stateParams.itemId;

            if (firstLoad) {
                loadFirstCards();
                $rootScope.shownItem = 'first';
            } else {
                var itemLoaded = ($rootScope.currentPage) && (findByItemId($rootScope.currentPage.reports, $stateParams.itemId) !== -1);

                if (itemLoaded) {
                    showCurrentCard();
                } else {
                    $rootScope.shownItem = 'this';
                    loadCardsFor($stateParams.itemId, 'next');
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

        var onServerFault = function () {
            $scope.utilities.showFatalError($ionicPopup, $ionicLoading);
        };

        initialize();
    }
);

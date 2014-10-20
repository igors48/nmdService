'use strict';

controllers.controller('feedCardController',

    function ($scope, $rootScope, $state, $stateParams, $ionicLoading, $ionicPopup, reads) {
        var pageOffset = 0;
        var pageSize = 5;

        var currentItem = 0;
        var firstIsCurrent = true;
        var currentPage = {};

        $scope.showUi = false;

        $scope.utilities = AppUtilities.utilities;

        $scope.backToCategory = function () {
            $state.go('category', { id: $stateParams.categoryId });
        };

        $scope.switchToListView = function () {
            $state.go('feed', { 
                categoryId: $stateParams.categoryId, 
                feedId: $stateParams.feedId 
            });
        };

        $scope.onPrev = function () {

            if (currentItem === 0) {
                loadPreviousCards(); 

                return;       
            }

            currentItem--;

            showCurrentCard();
        };
        
        $scope.onNext = function () {

            if (currentItem === currentPage.reports.length - 1) {
                loadNextCards();        

                return;
            }

            currentItem++;
            
            showCurrentCard();
        };
        
        $scope.onMarkAndNext = function() {
            var current = currentPage.reports[currentItem];

            var feedId = current.feedId;
            var itemId = current.itemId;

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
                onMarkItemCompleted,
                onServerFault
            );
        };

        var loadPreviousCards = function () {

            if (currentPage.first) {
                return;
            }

            pageOffset = pageOffset - pageSize;
            firstIsCurrent = false;

            loadCards();
        };
        
        var loadNextCards = function () {

            if (currentPage.last) {
                return;
            }

            pageOffset = pageOffset + pageSize;
            firstIsCurrent = true;

            loadCards();
        };
        
        var loadCards = function () {
            $ionicLoading.show({
                template: 'Loading...'
            });

            reads.query(
                { 
                    feedId: $stateParams.feedId,
                    offset: pageOffset,
                    size: pageSize
                },
                onLoadCardsCompleted,
                onServerFault);
        };

        var onMarkItemCompleted = function (response) {
            $ionicLoading.hide();

            if (response.status !== 'SUCCESS') {
                $scope.utilities.showError($ionicPopup, response);

                return;
            }

            var current = currentPage.reports[currentItem];
            current.read = true;

            $scope.onNext();
        };

        var onLoadCardsCompleted = function (response) {
            $ionicLoading.hide();

            if (response.status !== 'SUCCESS') {
                $scope.utilities.showError($ionicPopup, response);

                return;
            }

            $scope.showUi = true; 

            $scope.utilities.addDaysDifference(response.reports);
            currentPage = response;

            currentItem = firstIsCurrent ? 0 : currentPage.reports.length - 1;

            showCurrentCard();
        };

        var onServerFault = function () {
            $scope.utilities.showFatalError($ionicPopup, $ionicLoading);       
        };

        var showCurrentCard = function () {
            $scope.feed = {};
            $scope.feed.title = currentPage.title;

            var current = currentPage.reports[currentItem];

            $scope.card = {};
            $scope.card.date = current.date;
            $scope.card.daysDifference = current.daysDifference;
            $scope.card.title = current.title;
            $scope.card.description = current.description;
            $scope.card.link = current.link;
            $scope.card.notRead = !current.read;
        };

        loadCards();
    }
);

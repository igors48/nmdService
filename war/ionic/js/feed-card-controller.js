'use strict';

controllers.controller('feedCardController',

    function ($scope, $rootScope, $state, $stateParams, $ionicLoading, $ionicPopup, reads) {
        var pageOffset = 0;
        var pageSize = 5;

        var currentItem = 0;
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
        
        var loadPreviousCards = function () {

            if (currentPage.first) {
                return;
            }

            pageOffset = pageOffset - pageSize;

            loadCards();
        };
        
        var loadNextCards = function () {

            if (currentPage.last) {
                return;
            }

            pageOffset = pageOffset + pageSize;

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
        }

        var onLoadCardsCompleted = function (response) {
            $ionicLoading.hide();

            if (response.status !== 'SUCCESS') {
                $scope.utilities.showError($ionicPopup, response);

                return;
            }

            $scope.showUi = true; 

            currentPage = response;
            currentItem = 0;

            showCurrentCard();
        };

        var onServerFault = function () {
            $scope.utilities.showFatalError($ionicPopup, $ionicLoading);       
        };

        var showCurrentCard = function () {
            $scope.card = {};

            $scope.card.description = currentPage.reports[currentItem].description;
        };

        loadCards();
    }
);

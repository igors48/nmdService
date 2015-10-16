'use strict';

controllers.controller('categoryController',

    function ($scope, $rootScope, $state, $stateParams, $ionicLoading, $ionicPopup, $ionicScrollDelegate, categories, reads) {
        var sort = 'rating';

        $scope.showUi = false;

        $scope.utilities = AppUtilities.utilities;

        $scope.backToCategories = function () {
            $state.go('categories');
        };

        $scope.addFeed = function () {
            $scope.utilities.resetScrollPosition($stateParams.id, $rootScope);

            $state.go('add-feed', { id: $stateParams.id });
        };

        $scope.openFeed = function (feedId, filter) {
            $rootScope.lastFeedId = feedId;

            $scope.utilities.resetScrollPosition(feedId, $rootScope);
            $scope.utilities.storeScrollPosition($stateParams.id, $rootScope, $ionicScrollDelegate);

            $state.go('feed', { 
                categoryId: $stateParams.id, 
                feedId: feedId,
                filter: filter
            });
        };

        $scope.editFeed = function (feedId) {
            $rootScope.lastFeedId = feedId;

            $scope.utilities.storeScrollPosition($stateParams.id, $rootScope, $ionicScrollDelegate);

            $state.go('edit-feed', {                 
                categoryId: $stateParams.id, 
                feedId: feedId 
            });
        };

        $scope.openTopItem = function (feedId, topItemId) {
            
            if (topItemId.length === 0) {
                return;
            }
        
            $rootScope.lastFeedId = feedId;
            $rootScope.lastItemId = topItemId;

            $scope.utilities.storeScrollPosition($stateParams.id, $rootScope, $ionicScrollDelegate);

            $ionicLoading.show({
                template: $scope.utilities.loadingMessage('Marking item...')
            });

            reads.mark(
                {
                    feedId: feedId,
                    itemId: topItemId,
                    markAs: 'read'
                },
                onMarkAsReadSuccess,
                onServerFault
            );
        };

        $scope.toggleSort = function () {

            if (sort === 'name') {
                sort = 'rating';
            } else {
                sort = 'name';
            }

            applySort();
            updateSortButtonTitle();
        };

        var updateSortButtonTitle = function () {
            
            if (sort === 'name') {
                $scope.nextSortMode = 'by rating';
            } else {
                $scope.nextSortMode = 'by name';
            }
        };

        var onMarkAsReadSuccess = function (response) {
            $ionicLoading.hide();

            loadCategoryReport();
        };

        var loadCategoryReport = function () {
            $ionicLoading.show({
                template: $scope.utilities.loadingMessage('Loading category...')
            });

            categories.query(
                { 
                    categoryId: $stateParams.id
                },
                onLoadCategoryReportCompleted,
                onServerFault);
        };

        var applySort = function () {

            if (sort === 'name') {
                $scope.sortPredicate = 'feedTitle';
                $scope.sortReverse = false;
            } else {
                $scope.sortPredicate = 'lastUpdate';
                $scope.sortReverse = true;
            }    
   
        };

        var onLoadCategoryReportCompleted = function (response) {
            $ionicLoading.hide();

            if (response.status !== 'SUCCESS') {
                $scope.utilities.showError($ionicPopup, response);

                return;
            }

            $scope.showUi = true;

            $scope.category = { title: response.report.name };

            $scope.feeds = response.report.feedReports;

            applySort();
            updateSortButtonTitle();

            $scope.utilities.restoreScrollPosition($stateParams.id, $rootScope, $ionicScrollDelegate);
        };

        var onServerFault = function () {
            $scope.utilities.showFatalError($ionicPopup, $ionicLoading);        
        };

        loadCategoryReport();
    }
);

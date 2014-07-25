'use strict';

controllers.controller('categoryController',

    function ($scope, $rootScope, $state, $stateParams, $ionicLoading, $ionicPopup, categories, reads) {
        $scope.showUi = false;

        $scope.utilities = AppUtilities.utilities;

        $scope.backToCategories = function () {
            $state.go('categories');
        };

        $scope.addFeed = function () {
            $state.go('add-feed', { id: $stateParams.id });
        };

        $scope.openFeed = function (feedId) {
            $rootScope.lastFeedId = feedId;

            $state.go('feed', { 
                categoryId: $stateParams.id, 
                feedId: feedId 
            });
        };

        $scope.markAsRead = function (feedId, topItemId) {
            
            if (topItemId.length === 0) {
                return;
            }
        
            $rootScope.lastItemId = topItemId;

            $ionicLoading.show({
                template: 'Marking item...'
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

        var onMarkAsReadSuccess = function (response) {
            $ionicLoading.hide();

            loadCategoryReport();
        };

        var loadCategoryReport = function () {
            $ionicLoading.show({
                template: 'Loading category...'
            });

            categories.query(
                { 
                    categoryId: $stateParams.id
                },
                onLoadCategoryReportCompleted,
                onServerFault);
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
        };

        var onServerFault = function () {
            $scope.utilities.showFatalError($ionicPopup, $ionicLoading);        
        };

        loadCategoryReport();
    }
);

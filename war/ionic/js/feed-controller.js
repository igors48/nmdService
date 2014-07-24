'use strict';

controllers.controller('feedController',

    function ($scope, $state, $stateParams, $ionicLoading, $ionicPopup, reads) {
        $scope.showUi = false;

        $scope.utilities = AppUtilities.utilities;

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

        var onLoadFeedReportCompleted = function (response) {
            $ionicLoading.hide();

            if (response.status !== 'SUCCESS') {
                $scope.utilities.showError($ionicPopup, response);

                return;
            }

            $scope.showUi = true;
 
            $scope.feed = { title: response.title };
            $scope.items = response.reports;
        };

        var onServerFault = function () {
            $scope.utilities.showFatalError($ionicPopup, $ionicLoading);       
        };

        loadFeedReport();
    }
);

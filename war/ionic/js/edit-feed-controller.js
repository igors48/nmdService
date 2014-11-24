'use strict';

controllers.controller('editFeedController',

    function ($scope, $rootScope, $state, $stateParams, $ionicLoading, $ionicPopup, reads, feeds) {
        $scope.showUi = false;

        $scope.utilities = AppUtilities.utilities;

        $scope.backToCategory = function () {
            $state.go('category', { id: $stateParams.categoryId });
        };

        $scope.onRenameChosen = function () {
            $scope.feed.newName = $scope.feed.name;
            $scope.mode = 'rename';
        };

        $scope.onAssignChosen = function () {
            $state.go('select-feed-category',
                {
                    categoryId: $stateParams.categoryId,
                    feedId: $stateParams.feedId
                }
            );
        };

        $scope.onDeleteChosen = function () {
            $scope.mode = 'delete';
        };

        $scope.onCancelChosen = function () {
            $scope.mode = 'choose';
        };

        $scope.onRenameFeed = function () {
            $ionicLoading.show({
                template: 'Renaming feed...'
            });

            feeds.update(
                {
                    feedId: $stateParams.feedId
                },
                $scope.feed.newName,
                onRenameFeedCompleted,
                onServerFault
            );
        };

        $scope.onDeleteFeed = function () {
            $ionicLoading.show({
                template: 'Deleting feed...'
            });

            feeds.delete(
                {
                    feedId: $stateParams.feedId
                },
                onDeleteFeedCompleted,
                onServerFault
            );
        };

        var loadFeedReadReport = function () {
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

        var onRenameFeedCompleted = function (response) {
            $ionicLoading.hide();

            if (response.status !== 'SUCCESS') {
                $scope.utilities.showError($ionicPopup, response);

                return;
            }

            $ionicPopup.alert({
                    title: 'Information',
                    template: 'Feed is updated.'
            }).then($scope.backToCategory);
        };

        var onLoadFeedReportCompleted = function (response) {
            $ionicLoading.hide();

            if (response.status !== 'SUCCESS') {
                $scope.utilities.showError($ionicPopup, response);

                return;
            }

            $scope.showUi = true;
            $scope.mode = 'choose';

            $scope.feed = {
                name: response.title,
                items: response.reports.length,
                link: response.link
            };
        };

        var onDeleteFeedCompleted = function (response) {
            $ionicLoading.hide();

            if (response.status !== 'SUCCESS') {
                $scope.utilities.showError($ionicPopup, response);

                return;
            }

            $ionicPopup.alert({
                    title: 'Information',
                    template: 'Feed is deleted.'
            }).then($scope.backToCategory);
        };

        var onServerFault = function () {
            $scope.utilities.showFatalError($ionicPopup, $ionicLoading);
        };

        loadFeedReadReport();
    }
);
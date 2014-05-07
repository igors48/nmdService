'use strict';

controllers.controller('itemViewController', ['$scope', '$location', '$window', '$routeParams', 'reads', 'lastUsedIds', 'blockUi', function ($scope, $location, $window, $routeParams, reads, lastUsedIds, blockUi) {

    //TODO code duplication
    function showSuccessMessage (message) {
        $scope.statusMessage = message;
    }

    //TODO code duplication
    function showErrorMessage (message) {
        $scope.statusMessage = message;
    }

    //TODO code duplication
    var serverErrorHandler = function (onContinue) {
        showErrorMessage('Server error');

        blockUi.unblock();

        onContinue();
   }

    //TODO code duplication
    var serverResponseHandler = function (response, onSuccess) {

        if (response.status === 'SUCCESS') {
            blockUi.unblock();

            onSuccess(response);
        } else {
            blockUi.unblock();

            showErrorMessage('Error : ' + response.error.message + ' ' + response.error.hints);
        }
    }

    $scope.loadItemsReport = function (feedId) {
        blockUi.block();

        showSuccessMessage('loading...');

        var itemsReport = reads.query({
                feedId: feedId,
            },
            function () {
                serverResponseHandler(itemsReport,
                    function() {
                        $scope.feedTitle = itemsReport.title;
                        $scope.reports = itemsReport.reports;
                        $scope.read = itemsReport.read;
                        $scope.notRead = itemsReport.notRead;
                        $scope.readLater = itemsReport.readLater;

                        $scope.touchedItemId = lastUsedIds.getLastUsedItemId();
                    })
            },
            function () {
                serverErrorHandler(
                    function () {
                        $scope.reports = [];
                    }
                )
           }
        );
    };

    $scope.markAsRead = function (feedId, itemId) {
        blockUi.block();

        $scope.touchedItemId = itemId;
        lastUsedIds.store(feedId, itemId);
        
        var response = reads.mark({
            feedId: feedId,
            itemId: itemId,
            markAs: 'read'
            },
            function () {
                serverResponseHandler(response,
                    function() {
                        $scope.feedLink = '';
                        $scope.loadItemsReport(feedId);
                    }
                )
            },
            function () {
                serverErrorHandler(
                    function () {
                        $scope.loadItemsReport(feedId);
                    }
                )
            }
        );
    }

    $scope.markAllAsRead = function () {
        blockUi.block();

        $scope.touchedItemId = '';
        lastUsedIds.storeFeedId($routeParams.feedId);
        
        var response = reads.mark({
                feedId: $routeParams.feedId
            },
            function () {
                serverResponseHandler(response,
                    function() {
                        $scope.feedLink = '';
                        $scope.loadItemsReport($routeParams.feedId);
                    }
                )
            },
            function () {
                serverErrorHandler(
                    function () {
                        $scope.loadItemsReport($routeParams.feedId);
                    }
                )
            }
        );
    }

    $scope.toggleReadLater = function (feedId, itemId) {
        blockUi.block();

        $scope.touchedItemId = itemId;
        lastUsedIds.store(feedId, itemId);

        var response = reads.mark({
            feedId: feedId,
            itemId: itemId,
            markAs: 'readLater'
            },
            function () {
                serverResponseHandler(response,
                    function() {
                        $scope.feedLink = '';
                        $scope.loadItemsReport(feedId);
                    }
                )
            },
            function () {
                serverErrorHandler(
                    function () {
                        $scope.loadItemsReport(feedId);
                    }
                )
            }
        );
    }

    $scope.backToFeeds = function () {
        $location.path('/feeds');
    }

    lastUsedIds.storeFeedId($routeParams.feedId);
    $scope.loadItemsReport($routeParams.feedId);
}]);
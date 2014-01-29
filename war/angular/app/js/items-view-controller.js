'use strict';

controllers.controller('itemViewController', ['$scope', '$window', '$routeParams', 'reads', 'lastUsedIds', 'blockUi', function ($scope, $window, $routeParams, reads, lastUsedIds, blockUi) {

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

            showErrorMessage('Error : ' + response.message + ' ' + response.hints);
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

                        $scope.touchedItemId = lastUsedIds.getLastUsedItemId();    
                        showSuccessMessage('[ ' + itemsReport.read + ' / ' + itemsReport.notRead + ' ]');
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
    
    $scope.loadItemsReport($routeParams.feedId);
}]);
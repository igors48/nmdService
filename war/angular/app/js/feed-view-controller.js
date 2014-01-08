'use strict';

controllers.controller('feedViewController', ['$scope', '$location', '$routeParams', 'feeds', 'reads', 'blockUi', function ($scope, $location, $routeParams, feeds, reads, blockUi) {
    $scope.showDelete = false;
    $scope.deleteTouched = false;

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

    $scope.loadFeedReport = function (feedId) {
        blockUi.block();

        showSuccessMessage('loading...');

        var itemsReport = reads.query({
                feedId: feedId,
            },
            function () {
                serverResponseHandler(itemsReport,
                    function() {
                        $scope.feedTitle = itemsReport.title;

                        showSuccessMessage('[ ' + itemsReport.read + ' / ' + itemsReport.notRead + ' ]');

                        $scope.showDelete = true;
                    })
            },
            function () {
                serverErrorHandler(
                    function () {
                    }
                )
           }
        );
    };

    $scope.deleteFeed = function () {
        $scope.deleteTouched = true;

        blockUi.block();

        showSuccessMessage('remove...');

        var response = feeds.delete({
                feedId: $routeParams.feedId,
            },
            function () {
                serverResponseHandler(response,
                    function() {
                        $location.path('/feeds');
                    })
            },
            function () {
                serverErrorHandler(
                    function () {
                    }
                )
           }
        );

    };

    $scope.loadFeedReport($routeParams.feedId);
}]);
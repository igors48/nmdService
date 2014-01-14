'use strict';

controllers.controller('updateFeedTitleViewController', ['$scope', '$routeParams', 'feeds', 'blockUi', function ($scope, $routeParams, feeds, blockUi) {

        var showStatusMessage = function (message) {
            $scope.statusMessage = message;
        };

        var serverErrorHandler = function (onContinue) {
            showStatusMessage('Server error');

            blockUi.unblock();

            onContinue();
        };

        var serverResponseHandler = function (response, onSuccess) {
            blockUi.unblock();

            response.status === 'SUCCESS' ? onSuccess() : $scope.showErrorResponseMessage(response);
        };

        $scope.showErrorResponseMessage = function (response) {
            showStatusMessage('Error : ' + response.message + ' ' + response.hints)
        };

        $scope.showLoadingMessage = function () {
            showStatusMessage('loading...');
        };

        $scope.showFeedLink = function (feedLink) {
            showStatusMessage(feedLink);
        };

        $scope.showUpdatingMessage = function () {
            showStatusMessage('updaing feed title...');
        };

        $scope.loadFeedTitle = function () {
            blockUi.block();

            $scope.showLoadingMessage();

            var feedHeader = feeds.query(
                {
                    feedId: $routeParams.feedId,
                },
                function () {
                    serverResponseHandler(feedHeader,
                        function() {
                            $scope.showFeedLink(feedHeader.feedLink);
                            $scope.feedTitle = feedHeader.feedTitle;
                            $scope.newFeedTitle = feedHeader.feedTitle;
                        })
                },
                function () {
                    serverErrorHandler( 
                        function () {
                            // empty
                        }
                    )
                }
            );
        };

        $scope.updateFeedTitle = function () {
            blockUi.block();

            $scope.showUpdatingMessage();

            var response = feeds.update(
                {
                    feedId: $routeParams.feedId,
                },
                $scope.newFeedTitle,                
                function () {
                    serverResponseHandler(response,
                        function() {
                            $scope.loadFeedTitle();                                
                        })
                },
                function () {
                    serverErrorHandler( 
                        function () {
                            // empty
                        }
                    )
                }
            );
        };

        $scope.loadFeedTitle();
    }]
);
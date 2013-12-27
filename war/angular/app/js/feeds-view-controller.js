'use strict';

controllers.controller('feedsViewController', ['$scope', '$window', '$location', 'feeds', 'reads', 'blockUi', function ($scope, $window, $location, feeds, reads, blockUi) {

        var serverErrorHandler = function (onContinue) {
            $scope.showStatusMessage('Server error');

            blockUi.unblock();

            onContinue();
        };

        var serverResponseHandler = function (response, onSuccess) {
            blockUi.unblock();

            response.status === 'SUCCESS' ? onSuccess() : $scope.showStatusMessage('Error : ' + response.message + ' ' + response.hints);
        };

        $scope.showStatusMessage = function (message) {
            $scope.statusMessage = message;
        };

        $scope.showLoadingFeedsMessage = function () {
            $scope.showStatusMessage('loading feeds...');
        };

        $scope.showAddingNewFeedMessage = function () {
            $scope.showStatusMessage('adding new feed...');
        };

        $scope.showFeedsCount = function (count) {
            $scope.showStatusMessage('found ' + count + ' feed(s)');
        };

        $scope.showLoadingTopFeedItem = function () {
            $scope.showStatusMessage('loading top feed item...');
        };

        $scope.loadReadsReport = function () {
            blockUi.block();

            $scope.showLoadingFeedsMessage();

            var readReport = reads.query(
                function () {
                    serverResponseHandler(readReport,
                        function() {
                            $scope.reports = readReport.reports;

                            $scope.showFeedsCount(readReport.reports.length);
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

        $scope.addFeed = function () {
            blockUi.block();

            $scope.showAddingNewFeedMessage();

            var response = feeds.save($scope.feedLink,
                function () {
                    serverResponseHandler(response,
                        function () {
                            $scope.feedLink = '';
                            $scope.loadReadsReport();
                        })
                },
                function () {
                    serverErrorHandler(
                        function () {
                            $scope.loadReadsReport();
                        }
                    )
                }
            );
        };

        $scope.readTopItem = function (feedId, topItemId, topItemLink) {

            if (topItemId.length === 0 || topItemLink === 0) {
                return;
            }

            $scope.touchedFeedId = feedId;   

            blockUi.block();

            $scope.showLoadingTopFeedItem();

            var response = reads.mark({
                    feedId: feedId,
                    itemId: topItemId    
                },
                function () {
                    serverResponseHandler(response,
                        function() {
                            $scope.feedLink = '';
                            $scope.loadReadsReport();
                            $scope.touchedFeedId = '';   

                            $window.open(topItemLink, '_blank');
                            $window.focus();
                        })
                },
                function () {
                    serverErrorHandler( 
                        function () {
                            $scope.loadReadsReport();
                            $scope.touchedFeedId = '';   
                        }
                    )
                }
            );
        };

        $scope.viewItems = function (feedId) {
            $scope.touchedFeedId = feedId;   
            $location.path('/items/' + feedId);
        };

        $scope.viewFeed = function (feedId) {
            $scope.touchedFeedId = feedId;   
            $location.path('/feed/' + feedId);
        };

        $scope.loadReadsReport();
    }]
);
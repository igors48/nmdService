'use strict';

controllers.controller('feedsViewController', ['$scope', '$window', '$location', 'feeds', 'reads', 'lastUsedIds', 'blockUi', function ($scope, $window, $location, feeds, reads, lastUsedIds, blockUi) {

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
            showStatusMessage('Error : ' + response.error.message + ' ' + response.error.hints)
        };

        $scope.showLoadingFeedsMessage = function () {
            showStatusMessage('loading feeds...');
        };

        $scope.showAddingNewFeedMessage = function () {
            showStatusMessage('adding new feed...');
        };

        $scope.showFeedsCount = function (count) {
            showStatusMessage('found ' + count + ' feed(s)');
        };

        $scope.showLoadingTopFeedItem = function () {
            showStatusMessage('loading top feed item...');
        };

        $scope.showTouchedFeedMark = function (feedId) {
            $scope.touchedFeedId = feedId;
        };

        $scope.hideTouchedFeedMark = function () {
            $scope.touchedFeedId = '';
        };

        $scope.clearFeedLink = function () {
            $scope.feedLink = '';
        };

        $scope.setFeedsReadReport = function (report) {
            $scope.reports = report.reports;
        };

        $scope.loadReadsReport = function () {
            blockUi.block();

            $scope.showLoadingFeedsMessage();

            var readReport = reads.query(
                function () {
                    serverResponseHandler(readReport,
                        function() {
                            $scope.setFeedsReadReport(readReport);

                            $scope.showFeedsCount(readReport.reports.length);
                            
                            $scope.showTouchedFeedMark(lastUsedIds.getLastUsedFeedId());
                        })
                },
                function () {
                    serverErrorHandler( 
                        function () {
                            $scope.setFeedsReadReport([]);
                        }
                    )
                }
            );
        };

        $scope.addFeed = function () {
            blockUi.block();

            $scope.showAddingNewFeedMessage();

            var response = feeds.save(
                {feedUrl: $scope.feedLink, categoryId: 'main'},
                function () {
                    serverResponseHandler(response,
                        function () {
                            lastUsedIds.store(response.feedId, '');
                            
                            $scope.clearFeedLink();
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

        $scope.markAsRead = function (feedId, topItemId) {

            if (topItemId.length === 0) {
                return;
            }

            blockUi.block();

            $scope.showTouchedFeedMark(feedId);   
            lastUsedIds.store(feedId, topItemId);

            $scope.showLoadingTopFeedItem();

            var response = reads.mark({
                    feedId: feedId,
                    itemId: topItemId,
                    markAs: 'read'    
                },
                function () {
                    serverResponseHandler(response,
                        function() {
                            $scope.hideTouchedFeedMark();   
                            $scope.loadReadsReport();
                        })
                },
                function () {
                    serverErrorHandler( 
                        function () {
                            $scope.hideTouchedFeedMark();   
                            $scope.loadReadsReport();
                        }
                    )
                }
            );
        };

        $scope.loadReadsReport();
    }]
);
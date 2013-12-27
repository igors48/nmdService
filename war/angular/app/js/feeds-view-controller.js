'use strict';

controllers.controller('feedsViewController', ['$scope', '$window', '$location', 'feeds', 'reads', 'blockUi', function ($scope, $window, $location, feeds, reads, blockUi) {

        var serverErrorHandler = function (onContinue) {
            $scope.showStatusMessage($scope,'Server error');

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

        $scope.loadReadsReport = function () {
            blockUi.block();

            $scope.showStatusMessage('loading...');

            var readReport = reads.query(
                function () {
                    serverResponseHandler(readReport,
                        function() {
                            $scope.reports = readReport.reports;

                            $scope.showStatusMessage(readReport.reports.length + ' feed(s)');
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

            $scope.showStatusMessage('adding...');

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
            $scope.touchedFeedId = feedId;   

            if (topItemId.length === 0 || topItemLink === 0) {
                return;
            }

            blockUi.block();

            $scope.showStatusMessage('getting top item...');

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
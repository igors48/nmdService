'use strict';

angular.module('application.controllers', [])

  .controller('feedListCtrl', ['$scope', '$window', 'feeds', 'reads', 'blockUi', function($scope, $window, feeds, reads, blockUi) {

        function showSuccessMessage (message) {
            $scope.statusType = 'success';
            $scope.statusMessage = message;
        }

        function showErrorMessage (message) {
            $scope.statusType = 'error';
            $scope.statusMessage = message;
        }

        var serverErrorHandler = function (onContinue) {
            showErrorMessage('Server error');

            blockUi.unblock();

            onContinue();
        }

        var serverResponseHandler = function (response, onSuccess) {

            if (response.status === 'SUCCESS') {
                blockUi.unblock();

                onSuccess(response);
            } else {
                blockUi.unblock();

                showErrorMessage('Error : ' + response.message + ' ' + response.hints);
            }
        }

        $scope.loadReadsReport = function () {
            blockUi.block();

            showSuccessMessage('loading...');

            var readReport = reads.query(
                function () {
                    serverResponseHandler(readReport,
                        function() {
                            $scope.reports = readReport.reports;

                            showSuccessMessage(readReport.reports.length + ' feed(s)');
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

            showSuccessMessage('adding...');

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

            if (topItemId.length == 0 || topItemLink == 0) {
                return;
            }

            blockUi.block();

            showSuccessMessage('getting top item...');

            var response = reads.mark({
                    feedId: feedId,
                    itemId: topItemId    
                },
                function () {
                    serverResponseHandler(response,
                        function() {
                            $scope.feedLink = '';
                            $scope.loadReadsReport();

                            $window.open(topItemLink, '_blank');
                            $window.focus();
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

        //TODO is it in right place?
        $scope.loadReadsReport();

}]);

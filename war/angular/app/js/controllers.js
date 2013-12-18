'use strict';

angular.module('application.controllers', [])

  .controller('feedListCtrl', ['$scope', '$window', '$location', 'feeds', 'reads', 'blockUi', function($scope, $window, $location, feeds, reads, blockUi) {

    //TODO code duplication
        function showSuccessMessage (message) {
            //TODO remove status type
            $scope.statusType = 'success';
            $scope.statusMessage = message;
        }

    //TODO code duplication
        function showErrorMessage (message) {
            //TODO remove status type
            $scope.statusType = 'error';
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

        $scope.dummy = function () {
        };
            
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

        $scope.viewItems = function (feedId) {
            $location.path('/items/' + feedId);
        };

        //TODO is it in right place?
        $scope.loadReadsReport();

}])

.controller('itemListCtrl', ['$scope', '$window', '$routeParams', 'reads', 'blockUi', function($scope, $window, $routeParams, reads, blockUi) {

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

    $scope.readItem = function (feedId, itemId, itemLink) {

        var response = reads.mark({
            feedId: feedId,
            itemId: itemId    
            },
            function () {
                serverResponseHandler(response,
                    function() {
                        $scope.feedLink = '';
                        $scope.loadItemsReport(feedId);


                        $window.open(itemLink, '_blank');
                        $window.focus();
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

'use strict';

function showStatusMessage($scope, message) {
    $scope.statusMessage = message;
};

function serverErrorHandler($scope, blockUi, onContinue) {
    showStatusMessage($scope,'Server error');

    blockUi.unblock();

    onContinue();
};

function serverResponseHandler($scope, blockUi, response, onSuccess) {
    blockUi.unblock();

    response.status === 'SUCCESS' ? onSuccess(response) : showStatusMessage($scope, 'Error : ' + response.message + ' ' + response.hints);
};

angular.module('application.controllers', [])

  .controller('feedListCtrl', ['$scope', '$window', '$location', 'feeds', 'reads', 'blockUi', function($scope, $window, $location, feeds, reads, blockUi) {

        $scope.loadReadsReport = function () {
            blockUi.block();

            showStatusMessage($scope, 'loading...');

            var readReport = reads.query(
                function () {
                    serverResponseHandler($scope, blockUi, readReport,
                        function() {
                            $scope.reports = readReport.reports;

                            showStatusMessage($scope, readReport.reports.length + ' feed(s)');
                        })
                },
                function () {
                    serverErrorHandler($scope, blockUi, 
                        function () {
                            $scope.reports = [];
                        }
                    )
                }
            );
        };

        $scope.addFeed = function () {
            blockUi.block();

            showStatusMessage($scope, 'adding...');

            var response = feeds.save($scope.feedLink,
                function () {
                    serverResponseHandler($scope, blockUi, response,
                        function () {
                            $scope.feedLink = '';
                            $scope.loadReadsReport();
                        })
                },
                function () {
                    serverErrorHandler($scope, blockUi, 
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

            showStatusMessage($scope, 'getting top item...');

            var response = reads.mark({
                    feedId: feedId,
                    itemId: topItemId    
                },
                function () {
                    serverResponseHandler($scope, blockUi, response,
                        function() {
                            $scope.feedLink = '';
                            $scope.loadReadsReport();
                            $scope.touchedFeedId = '';   

                            $window.open(topItemLink, '_blank');
                            $window.focus();
                        })
                },
                function () {
                    serverErrorHandler($scope, blockUi, 
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
        $scope.touchedItemId = itemId;

        blockUi.block();

        var response = reads.mark({
            feedId: feedId,
            itemId: itemId    
            },
            function () {
                serverResponseHandler(response,
                    function() {
                        $scope.feedLink = '';
                        $scope.loadItemsReport(feedId);
                        $scope.touchedItemId = '';

                        $window.open(itemLink, '_blank');
                        $window.focus();
                    }
                )
            },
            function () {
                serverErrorHandler(
                    function () {
                        $scope.loadItemsReport(feedId);
                        $scope.touchedItemId = '';
                    }
                )
            }
        );
    }

    $scope.loadItemsReport($routeParams.feedId);
}])

.controller('feedCtrl', ['$scope', '$location', '$routeParams', 'feeds', 'reads', 'blockUi', function($scope, $location, $routeParams, feeds, reads, blockUi) {
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

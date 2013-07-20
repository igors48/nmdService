var application = angular.module('application', ['ngResource']);

application.factory('feeds', function($resource){
    return $resource('/@security.key@/v01/feeds/', {}, {
        'query': {method:'GET', params:{}},
        'save': {method:'POST'}
    });
});

function FeedListCtrl($scope, feeds) {

    var serverErrorHandler = function (onContinue) {
        $scope.status = 'Server error';

        onContinue();
    }

    var serverResponseHandler = function (response, onSuccess) {

        if (response.status === 'SUCCESS') {
            onSuccess(response);
        } else {
            $scope.status = 'Error : ' + response.message + ' ' + response.hints;
        }

    }

    $scope.loadFeedHeaders = function () {
        $scope.status = 'loading...';

        var feedList = feeds.query(
            function() {
                serverResponseHandler(feedList,
                    function() {
                        $scope.headers = feedList.headers;
                        $scope.status = feedList.headers.length;
                    })
            },
            function () {
                serverErrorHandler(
                    function () {
                        $scope.headers = [];
                    }
                )
            }
        );
    };

    $scope.addFeed = function () {
        $scope.status = 'adding...';

        var response = feeds.save($scope.feedLink,
            function() {
                serverResponseHandler(response,
                    function() {
                        $scope.feedLink = '';
                        $scope.loadFeedHeaders();
                    })
            },
            function () {
                serverErrorHandler(
                    function () {
                        $scope.loadFeedHeaders();
                    }
                )
            }
        );
    };

    $scope.loadFeedHeaders();
}


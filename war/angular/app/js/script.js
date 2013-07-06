var application = angular.module('application', ['ngResource']);

application.factory('feeds', function($resource){
    return $resource('/@security.key@/v01/feeds/', {}, {
        'query': {method:'GET', params:{}},
        'save': {method:'POST'}
    });
});

function FeedListCtrl($scope, feeds) {

    $scope.loadFeedHeaders = function () {
        $scope.status = 'loading...';

        var feedList = feeds.query(function() {
            $scope.headers = feedList.headers;
            $scope.status = feedList.headers.length;
        } );
    };

    $scope.addFeed = function () {
        $scope.status = 'adding...';

        var response = feeds.save($scope.feedLink, function() {
            $scope.feedLink = '';
            $scope.loadFeedHeaders();
        });
    };

    $scope.loadFeedHeaders();
}


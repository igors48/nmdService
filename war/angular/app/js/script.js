var application = angular.module('application', ['ngResource']);

application.factory('feeds', function($resource){
    return $resource('/@security.key@/v01/feeds/', {}, {
        query: {method:'GET', params:{}}
    });
});

function FeedListCtrl($scope, feeds) {
    $scope.headers = 'loading...';

    var feedList = feeds.query(function() {
        $scope.headers = feedList.headers;
    } );
}


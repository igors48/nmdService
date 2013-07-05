var application = angular.module('application', ['ngResource']);

application.factory('feeds', function($resource){
    return $resource('http://localhost\\:8080/security/v01/feeds/', {}, {
        query: {method:'GET', params:{}}
    });
});

function FeedListCtrl($scope, feeds) {
    $scope.feed = 'fd';
    var feedList = feeds.query(function() {
        //alert("got it");

        var feedsItem = feedList;
        $scope.feed = feedsItem.headers.length;
    } );
}


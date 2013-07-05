'use strict';

/* Controllers */

angular.module('myApp.controllers', [])

  .controller('FeedListCtrl', [function() {

     function FeedListCtrl($scope) {
       $scope.feed = 'fd';
     }

  }])

  .controller('MyCtrl2', [function() {

  }]);
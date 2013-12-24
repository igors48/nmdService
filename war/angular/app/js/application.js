'use strict';

var application = angular.module('application', ['ngRoute', 'application.services', 'application.controllers']);

application.config(['$routeProvider',
  function($routeProvider) {
    $routeProvider.
      when('/feeds', {
        templateUrl: 'partials/feeds-view.html',
        controller: 'feedListCtrl'
      }).
      when('/items/:feedId', {
        templateUrl: 'partials/items-view.html',
        controller: 'itemListCtrl'
      }).
      when('/feed/:feedId', {
        templateUrl: 'partials/feed-view.html',
        controller: 'feedCtrl'
      }).
      otherwise({
        redirectTo: '/feeds'
      });
  }]);


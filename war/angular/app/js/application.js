'use strict';

var application = angular.module('application', ['ngRoute', 'application.services', 'application.controllers']);

'use strict';

var controllers = angular.module('application.controllers', []);

application.config(['$routeProvider',
    function ($routeProvider) {
        $routeProvider.

            when('/feeds', {
                templateUrl:'partials/feeds-view.html',
                controller:'feedsViewController'
            }).

            when('/items/:feedId', {
                templateUrl:'partials/items-view.html',
                controller:'itemViewController'
            }).

            when('/feed/:feedId', {
                templateUrl:'partials/feed-view.html',
                controller:'feedViewController'
            }).

            when('/update-feed-title/:feedId', {
                templateUrl:'partials/update-feed-title-view.html',
                controller:'updateFeedTitleViewController'
            }).

            otherwise({
                redirectTo:'/feeds'
            });

    }]);


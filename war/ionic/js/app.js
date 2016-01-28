'use strict';

var orb = angular.module('orb', ['ionic', 'orb.services', 'orb.controllers']);

var controllers = angular.module('orb.controllers', []);

orb.filter('feedOrder', function ($filter) {

   return function (feeds, sort) {

      if (sort === 'title') {
        return $filter('orderBy')(feeds, 'feedTitle', false);  
      } else {
        var notEmpties = [];
        var empties = [];

        angular.forEach(feeds, 
          function (value, key) {
            if (value.notRead === 0) {
              empties.push(value);
            } else {
              notEmpties.push(value);
            }
          }
        );

        notEmpties = $filter('orderBy')(notEmpties, 'lastUpdate', true);
        empties = $filter('orderBy')(empties, 'feedTitle', false);
 
        var result = notEmpties.concat(empties);

        return result;
      }  
    };

});

orb.run(
  function ($rootScope) {
    $rootScope.scrollPositions = [];
  }
);

orb.config(function($stateProvider, $urlRouterProvider) {

  $urlRouterProvider.otherwise("/categories");

    $stateProvider
      
      .state('categories', {
        url: '/categories',
        templateUrl: 'partials/categories.html',
        controller: 'categoriesController'
      })
      
      .state('add-category', {
        url: '/add-category',
        templateUrl: 'partials/add-category.html',
        controller: 'addCategoryController'
      })
      
      .state('edit-category', {
        url: '/edit-category/{id}',
        templateUrl: 'partials/edit-category.html',
        controller: 'editCategoryController'
      })
      
      .state('category', {
        url: '/category/{id}',
        templateUrl: 'partials/category.html',
        controller: 'categoryController'
      })

      .state('add-feed', {
        url: '/add-feed/{id}',
        templateUrl: 'partials/add-feed.html',
        controller: 'addFeedController'
      })

      .state('edit-feed', {
        url: '/edit-feed/{categoryId}/{feedId}',
        templateUrl: 'partials/edit-feed.html',
        controller: 'editFeedController'
      })

      .state('select-feed-category', {
        url: '/select-feed-category/{categoryId}/{feedId}',
        templateUrl: 'partials/select-feed-category.html',
        controller: 'selectFeedCategoryController'
      })

      .state('feed', {
        url: '/feed/{categoryId}/{feedId}/{filter}',
        templateUrl: 'partials/feed.html',
        controller: 'feedController'
      })

      .state('feed-card', {
        url: '/feed-card/{categoryId}/{feedId}/{itemId}/{filter}',
        templateUrl: 'partials/feed-card.html',
        controller: 'feedCardController'
      })

      .state('admin-console', {
        url: '/admin-console',
        templateUrl: 'partials/admin-console.html',
        controller: 'adminConsoleController'
      })

      .state('reset-service', {
        url: '/reset-service',
        templateUrl: 'partials/reset-service.html',
        controller: 'resetServiceController'
      })

      .state('flush-cache', {
        url: '/flush-cache',
        templateUrl: 'partials/flush-cache.html',
        controller: 'flushCacheController'
      })

      .state('import-feeds', {
        url: '/import-feeds',
        templateUrl: 'partials/import-feeds.html',
        controller: 'importFeedsController'
      })

});


'use strict';

/* Services */


// Demonstrate how to register services
// In this case it is a simple value service.
//angular.module('myApp.services', []).
  //value('version', '0.1');
  
angular.module('myApp.services', ['ngResource']).
    factory('Feeds', function($resource){
        return $resource('http://rss-collector.appspot.com/security/v01/feeds/', {}, {
            query: {method:'GET', params:{}, isArray:true}
        });
    });

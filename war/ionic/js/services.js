'use strict';

angular.module('orb.services', ['ngResource'])

    .factory('categories', function ($resource) {
        return $resource('/@security.key@/v01/categories/:categoryId/:feedId',
            {
                categoryId: '@categoryId',
                feedId: '@feedId'
            },
            {
                'query': {method: 'GET'},
                'save': {method: 'POST'},
                'update': {method: 'PUT'},
                'delete': {method: 'DELETE'}
            }
        );
    })

    .factory('feeds', function ($resource) {
        return $resource('/@security.key@/v01/feeds/:feedId',
            {
                feedId: '@feedId'
            },
            {
                'query': {method: 'GET'},
                'save': {method: 'POST'},
                'update': {method: 'PUT'},
                'delete': {method: 'DELETE'}
            }
        );
    })

    .factory('reads', function ($resource) {
        return $resource('/@security.key@/v01/reads/:feedId/:itemId/:direction/:size',
            {
                feedId: '@feedId',
                itemId: '@itemId',
                direction: '@direction',
                size: '@size',
                filter: '@filter',
                markAs: '@markAs',
                topItemTimestamp: '@topItemTimestamp'
            },
            {
                'query': {method: 'GET'},
                'mark': {method: 'PUT'}
            }
        );
    })

    .factory('imports', function ($resource) {
        return $resource('/@security.key@/v01/import/:action',
            {
                action: '@action'
            },
            {
                'status': {method: 'GET'},
                'schedule': {method: 'POST'},
                'reject': {method: 'DELETE'},
                'action': {method: 'PUT'}
            }
        );
    })

    .factory('reset', function ($resource) {
        return $resource('/@security.key@/v01/reset/:action',
            {
                action: '@action'
            },
            {
                'reset': {method: 'POST'}
            }
        );
    })

    .factory('content', function ($resource) {
        return $resource('/@security.key@/v01/content',
            {
                link: '@link',
            },
            {
                'filter': {method: 'GET'}
            }
        );
    })


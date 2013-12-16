'use strict';

angular.module('application.services', ['ngResource'])
    
    .factory('feeds', function ($resource) {
        return $resource('/@security.key@/v01/feeds/', {}, {
            'query': {method:'GET'},
            'save': {method:'POST'}
        });
    })
    
    .factory('reads', function ($resource) {
        return $resource('/@security.key@/v01/reads/:feedId/:itemId', 
            {
                feedId: "@feedId",
                itemId: "@itemId"
            },
            {
                'query': {method:'GET'},
                'mark': {method:'POST'}
            }
        );
    })
    
    .factory('blockUi', function ($document) {
        var body = $document.find('body');

        var overlayCss = {
            'z-index': 10001,
            border: 'none',
            margin: 0,
            padding: 0,
            width: '100%',
            height: '100%',
            top: 0,
            left: 0,
            'background-color': '#000',
            opacity: 0.3,
            cursor: 'wait',
            position: 'fixed'
        };

        var overlay = angular.element('<div>');
        overlay.css(overlayCss);

        var blocked = false;

        return {

            _addOverlay: function () {
                body.append(overlay);
            },

            _removeOverlay: function () {
                overlay.remove();
            },

            block: function () {

                if (blocked) {
                    return;
                }

                this._addOverlay();

                blocked = true;
            },

            unblock: function () {

                if (!blocked) {
                    return;
                }

                this._removeOverlay();

                blocked = false;
            }

        };
    });


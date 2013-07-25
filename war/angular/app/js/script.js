var application = angular.module('application', ['ngResource', 'ui.bootstrap']);

application.factory('feeds', function ($resource) {
    return $resource('/@security.key@/v01/feeds/', {}, {
        'query': {method:'GET', params:{}},
        'save': {method:'POST'}
    });
});

application.factory('blockUi', function ($document) {
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

function FeedListCtrl($scope, feeds, blockUi) {

    function showSuccessMessage(message) {
        $scope.statusType = 'success';
        $scope.statusMessage = message;
    }

    function showErrorMessage(message) {
        $scope.statusType = 'error';
        $scope.statusMessage = message;
    }

    var serverErrorHandler = function (onContinue) {
        showErrorMessage('Server error');

        blockUi.unblock();

        onContinue();
    }

    var serverResponseHandler = function (response, onSuccess) {

        if (response.status === 'SUCCESS') {
            blockUi.unblock();

            onSuccess(response);
        } else {
            blockUi.unblock();

            showErrorMessage('Error : ' + response.message + ' ' + response.hints);
        }

    }

    $scope.loadFeedHeaders = function () {
        blockUi.block();

        showSuccessMessage('loading...');

        var feedList = feeds.query(
            function() {
                serverResponseHandler(feedList,
                    function() {
                        $scope.headers = feedList.headers;

                        showSuccessMessage(feedList.headers.length + ' feed(s)');
                    })
            },
            function () {
                serverErrorHandler(
                    function () {
                        $scope.headers = [];
                    }
                )
            }
        );
    };

    $scope.addFeed = function () {
        blockUi.block();

        showSuccessMessage('adding...');

        var response = feeds.save($scope.feedLink,
            function() {
                serverResponseHandler(response,
                    function() {
                        $scope.feedLink = '';
                        $scope.loadFeedHeaders();
                    })
            },
            function () {
                serverErrorHandler(
                    function () {
                        $scope.loadFeedHeaders();
                    }
                )
            }
        );
    };

    $scope.loadFeedHeaders();
}


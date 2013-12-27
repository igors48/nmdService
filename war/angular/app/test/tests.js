//http://sravi-kiran.blogspot.com/2013/06/UnitTestingAngularJsControllerUsingQUnitAndSinon.html
var READS_SERVICE_URL = '/@security.key@/v01/reads';
var READS_REPORT = '{"reports":[{"feedId":"8dc35df0-0027-45a7-935f-20b54e6d9f98","feedTitle":"Bash.im","read":0,"notRead":100,"addedFromLastVisit":0,"topItemId":"9931c369-e267-4368-93ea-c89e55fbcced","topItemLink":"http://bash.im/quote/426086"}],"status":"SUCCESS"}';

var backendStub;

var windowStub;
var locationStub;

var feedsStub;
var readsStub; 
var blockUiStub;

var injector;

var controller;

var scope;

module('feed list controller', {

    setup: function () {
        var stubs = angular.module('stubs', []);

        stubs.config(function ($provide) {
            $provide.decorator('$httpBackend', angular.mock.e2e.$httpBackendDecorator);
        });

        injector = angular.injector(['ng', 'ngMock', 'application', 'stubs']);
        
        backendStub = injector.get('$httpBackend');

        windowStub = { location: { href: '' } };

        locationStub = injector.get('$location');

        feedsStub = injector.get('feeds');
 
        readsStub = injector.get('reads');

        blockUiStub = injector.get('blockUi');
        sinon.stub(blockUiStub, 'block');
        sinon.stub(blockUiStub, 'unblock');

        scope = injector.get('$rootScope').$new();

        backendStub.expectGET(READS_SERVICE_URL).respond('');

        controller = injector.get('$controller')('feedsViewController', { $scope: scope, $window: windowStub, $location: locationStub, feeds: feedsStub, reads: readsStub, blockUi: blockUiStub });

        sinon.spy(scope, 'showStatusMessage');

        backendStub.flush();
    },

    teardown: function () {
    }	
});

test('load feeds', function () {
    backendStub.expectGET(READS_SERVICE_URL).respond(READS_REPORT);

    scope.loadReadsReport();

    backendStub.flush();

    ok(blockUiStub.block.calledBefore(blockUiStub.unblock), 'UI blocked');
    ok(scope.reports.length === 1, 'reports filled');

    var callCount = scope.showStatusMessage.secondCall;

    ok(blockUiStub.unblock.calledAfter(blockUiStub.block), 'UI unblocked');
});

test('view items', function () {
    scope.viewItems('item_id');
    
    ok(locationStub.path() === '/items/item_id', 'location changed');
    ok(scope.touchedFeedId === 'item_id', 'touched feed marked');
});    

test('view feed', function () {
    scope.viewFeed('item_id');
    
    ok(locationStub.path() === '/feed/item_id', 'location changed');
    ok(scope.touchedFeedId === 'item_id', 'touched feed marked');
});    

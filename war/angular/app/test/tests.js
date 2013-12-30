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

function createTestEnvinronment() {
    angular.module('stubs', [])
        .config(function ($provide) {
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
};

module('feed list controller', {

    setup: function () {
        createTestEnvinronment();

        backendStub.expectGET(READS_SERVICE_URL).respond('');

        controller = injector.get('$controller')('feedsViewController', { $scope: scope, $window: windowStub, $location: locationStub, feeds: feedsStub, reads: readsStub, blockUi: blockUiStub });

        backendStub.flush();
    },

    teardown: function () {
        // empty
    }	
});

test('load feeds', function () {
    sinon.spy(scope, 'showLoadingFeedsMessage');
    sinon.spy(scope, 'showFeedsCount');

    backendStub.expectGET(READS_SERVICE_URL).respond(READS_REPORT);

    scope.loadReadsReport();

    backendStub.flush();

    ok(blockUiStub.block.calledBefore(scope.showLoadingFeedsMessage), 'UI blocked before loading message');
    ok(scope.showLoadingFeedsMessage.calledOnce, 'loading message displayed');
    ok(scope.reports.length === 1, 'reports model updated correctly');
    ok(scope.showFeedsCount.calledOnce, 'feeds counter updated');
    ok(scope.showFeedsCount.calledWith(1), 'feeds counter updated correctly');
    ok(blockUiStub.unblock.calledBefore(scope.showFeedsCount), 'UI unblocked');

    scope.showFeedsCount.restore();
    scope.showLoadingFeedsMessage.restore();
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

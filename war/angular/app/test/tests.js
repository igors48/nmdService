//http://sravi-kiran.blogspot.com/2013/06/UnitTestingAngularJsControllerUsingQUnitAndSinon.html

var READS_SERVICE_URL = '/@security.key@/v01/reads';
var FEEDS_SERVICE_URL = '/@security.key@/v01/feeds';

var READS_REPORT_SUCCESS = '{"reports":[{"feedId":"8dc35df0-0027-45a7-935f-20b54e6d9f98","feedTitle":"Bash.im","read":0,"notRead":100,"addedFromLastVisit":0,"topItemId":"9931c369-e267-4368-93ea-c89e55fbcced","topItemLink":"http://bash.im/quote/426086"}],"status":"SUCCESS"}';
var ADD_FEED_SUCCESS = '{"feedId":"45186d6d-d7d4-4c7a-ba2c-2c8b5e8ff751","status":"SUCCESS"}'

var SOME_ERROR = '{"code":".","message":".","hints":".","status":"ERROR"}';

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
    sinon.spy(scope, 'loadReadsReport');
    sinon.spy(scope, 'setFeedsReadReport');

    backendStub.expectGET(READS_SERVICE_URL).respond(READS_REPORT_SUCCESS);

    scope.loadReadsReport();

    backendStub.flush();

    backendStub.verifyNoOutstandingExpectation();

    ok(blockUiStub.block.calledBefore(scope.loadReadsReport), 'UI blocked');
    ok(scope.showLoadingFeedsMessage.calledOnce, 'loading message displayed');
    ok(scope.setFeedsReadReport.calledOnce, 'reports model updated correctly');
    ok(scope.showFeedsCount.calledOnce, 'feeds counter updated');
    ok(scope.showFeedsCount.calledWith(1), 'feeds counter updated correctly');
    ok(blockUiStub.unblock.calledAfter(scope.loadReadsReport), 'UI unblocked');
});

test('load feeds error', function () {
    sinon.spy(scope, 'showLoadingFeedsMessage');
    sinon.spy(scope, 'showFeedsCount');
    sinon.spy(scope, 'loadReadsReport');

    backendStub.expectGET(READS_SERVICE_URL).respond(SOME_ERROR);

    scope.loadReadsReport();

    backendStub.flush();

    backendStub.verifyNoOutstandingExpectation();

    ok(blockUiStub.block.calledBefore(scope.loadReadsReport), 'UI blocked');
    ok(scope.showLoadingFeedsMessage.calledOnce, 'loading message displayed');
    ok(!scope.reports, 'reports model cleared');
    ok(blockUiStub.unblock.calledAfter(scope.loadReadsReport), 'UI unblocked');
});

test('add feed', function () {
    sinon.spy(scope, 'showAddingNewFeedMessage');
    sinon.spy(scope, 'addFeed');
 
    sinon.stub(scope, 'loadReadsReport');

    backendStub.expectPOST(FEEDS_SERVICE_URL, 'feedLink').respond(ADD_FEED_SUCCESS);
    
    scope.feedLink = 'feedLink';
    scope.addFeed();

    backendStub.flush();

    backendStub.verifyNoOutstandingExpectation();

    ok(blockUiStub.block.calledBefore(scope.addFeed), 'UI blocked');
    ok(scope.showAddingNewFeedMessage.calledOnce, 'adding message displayed')    
    ok(scope.loadReadsReport.calledAfter(scope.addFeed), 'feed report updated');
    ok(blockUiStub.unblock.calledAfter(scope.addFeed), 'UI unblocked');
});

test('add feed error', function () {
    sinon.spy(scope, 'showAddingNewFeedMessage');
    sinon.spy(scope, 'addFeed');
 
    sinon.stub(scope, 'loadReadsReport');

    backendStub.expectPOST(FEEDS_SERVICE_URL, 'feedLink').respond(SOME_ERROR);
    
    scope.feedLink = 'feedLink';
    scope.addFeed();

    backendStub.flush();

    backendStub.verifyNoOutstandingExpectation();

    ok(blockUiStub.block.calledBefore(scope.addFeed), 'UI blocked');
    ok(scope.showAddingNewFeedMessage.calledOnce, 'adding message displayed')    
    //ok(scope.loadReadsReport.calledOnce, 'feed report updated');
    ok(blockUiStub.unblock.calledAfter(scope.addFeed), 'UI unblocked');
});

test('view items', function () {
    var itemId = 'item_id';

    scope.viewItems(itemId);
    
    ok(locationStub.path() === ('/items/' + itemId), 'location changed');
    ok(scope.touchedFeedId === itemId, 'touched feed marked');
});    

test('view feed', function () {
    var itemId = 'item_id';

    scope.viewFeed(itemId);
    
    ok(locationStub.path() === ('/feed/' + itemId), 'location changed');
    ok(scope.touchedFeedId === itemId, 'touched feed marked');
});    

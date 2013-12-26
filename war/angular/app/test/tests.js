//http://sravi-kiran.blogspot.com/2013/06/UnitTestingAngularJsControllerUsingQUnitAndSinon.html

//Mocks
var windowMock, httpBackend, _shoppingData;
var feedsMock, locationMock, readsMock, blockUiMock;
//Injector
var injector;

//Controller
var ctrl;

//Scope
var ctrlScope;

//Data
var storedItems;

module('tests', {
    setup: function () {
        
        var appMocks = angular.module("appMocks", []);

        appMocks.config(function ($provide) {
            $provide.decorator('$httpBackend', angular.mock.e2e.$httpBackendDecorator);
        });

        injector = angular.injector(['ng', 'application', 'appMocks']);
        
        httpBackend = injector.get('$httpBackend');
        httpBackend.expectGET('/@security.key@/v01/reads').respond('{"reports":[{"feedId":"8dc35df0-0027-45a7-935f-20b54e6d9f98","feedTitle":"Bash.im","read":0,"notRead":100,"addedFromLastVisit":0,"topItemId":"9931c369-e267-4368-93ea-c89e55fbcced","topItemLink":"http://bash.im/quote/426086"}],"status":"SUCCESS"}');

        windowMock = { location: { href: ""} };

        feedsMock = injector.get('feeds');
        sinon.spy(feedsMock, 'query');


        locationMock = {};//injector.get('$location');
        //sinon.spy(locationMock, 'path');

        readsMock = injector.get('reads');

        blockUiMock = injector.get('blockUi');
        sinon.stub(blockUiMock, "block");
        sinon.stub(blockUiMock, "unblock");

        ctrlScope = injector.get('$rootScope').$new();

        ctrl = injector.get('$controller')('feedListCtrl', { $scope: ctrlScope, $window: windowMock, $location: locationMock, feeds: feedsMock, reads: readsMock, blockUi: blockUiMock});
    },

    teardown: function () {
    	/*
        feedsMock.setCartItems.restore();

        _shoppingData.getAllItems.restore();
        _shoppingData.addAnItem.restore();
        _shoppingData.removeItem.restore();
        */
    }	
});

test("hello test", function () {
	httpBackend.flush();

	ctrlScope.loadReadsReport();

    httpBackend.expectGET('/@security.key@/v01/reads').respond('{"reports":[{"feedId":"8dc35df0-0027-45a7-935f-20b54e6d9f98","feedTitle":"Bash.im","read":0,"notRead":100,"addedFromLastVisit":0,"topItemId":"9931c369-e267-4368-93ea-c89e55fbcced","topItemLink":"http://bash.im/quote/426086"}],"status":"SUCCESS"}');

	//httpBackend.flush();

	ok( 1 == "1", "Passed!" );
});


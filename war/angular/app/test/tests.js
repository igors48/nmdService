//TODO it needs to disable or mock the blockUI service

var injector = angular.injector(['ngMock', 'ng', 'application']);

var init = {
    setup: function () {
        this.$scope = injector.get('$rootScope').$new();
    }
};

module('tests', init);

test("hello test", function () {
	var self = this;

	var $controller = injector.get('$controller');

	$controller('feedListCtrl', {
        $scope: self.$scope
    });

	this.$scope.dummy();

	ok( 1 == "1", "Passed!" );
});
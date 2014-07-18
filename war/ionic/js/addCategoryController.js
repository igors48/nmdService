'use strict';

controllers.controller('addCategoryController',

    ['$scope', '$state', 'categories',

    function ($scope, $state, categories) {
    	$scope.category = { name: ''};

    	$scope.backToList = function () {
    		$state.go('categories');
    	};

        $scope.addNewCategory = function (category) {
        	categories.save(category.name, onAddCategorySuccess, onServerFault);
        };

        //TODO message about operation status
        var onAddCategorySuccess = function (response) {
        	debugger;
        };

        var onServerFault = function (response) {
			debugger;       		
        };
    }

]);

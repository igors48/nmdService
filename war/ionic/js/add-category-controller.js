'use strict';

controllers.controller('addCategoryController',

    function ($scope, $state, $ionicLoading, $ionicPopup, categories) {
        $scope.utilities = AppUtilities.utilities;

    	$scope.category = { name: ''};

    	$scope.backToList = function () {
    		$state.go('categories');
    	};

        $scope.addNewCategory = function (category) {
            $ionicLoading.show({
                template: 'Creating new category...'
            });

        	categories.save(category.name, onAddCategorySuccess, onServerFault);
        };

        var onAddCategorySuccess = function (response) {
            $ionicLoading.hide();

            if (response.status === 'SUCCESS') {
                $ionicPopup.alert({
                    title: 'Information',
                    //TODO string format utility
                    template: 'Category [ ' + response.category.name + ' ] is created.'
                });
            } else {
                $ionicPopup.alert({
                    title: 'Error',
                    template: response.error.message
                });
            }
        	
            //debugger;
        };
        
        var onServerFault = function () {
            $scope.utilities.showFatalError($ionicPopup, $ionicLoading);        
        };
        
    }

);

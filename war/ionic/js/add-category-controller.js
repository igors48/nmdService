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

            if (response.status !== 'SUCCESS') {
                $scope.utilities.showError($ionicPopup, response);

                return;
            }

            $ionicPopup.alert({
                title: 'Information',
                template: 'Category [ {0} ] was created.'.format(response.category.name)
            });
        };
        
        var onServerFault = function () {
            $scope.utilities.showFatalError($ionicPopup, $ionicLoading);        
        };
        
    }

);

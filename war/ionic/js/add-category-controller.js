'use strict';

controllers.controller('addCategoryController',

    function ($scope, $rootScope, $state, $ionicLoading, $ionicPopup, categories) {
        $scope.showUi = true;

        $scope.utilities = AppUtilities.utilities;

    	$scope.category = { name: ''};

    	$scope.backToCategories = function () {
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

            $scope.category.name = '';
            $rootScope.lastCategoryId = response.category.id;

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

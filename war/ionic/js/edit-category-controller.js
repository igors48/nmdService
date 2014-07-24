'use strict';

controllers.controller('editCategoryController',

    function ($scope, $rootScope, $state, $stateParams, $ionicLoading, $ionicPopup, categories) {
        $scope.showUi = false;

        $scope.utilities = AppUtilities.utilities;

    	$scope.backToCategories = function () {
    		$state.go('categories');
    	};

        var loadCategoryReport = function () {
            $ionicLoading.show({
                template: 'Loading category...'
            });

            categories.query(
                { 
                    categoryId: $stateParams.id
                },
                onLoadCategoryReportCompleted,
                onServerFault);
        };

        var onLoadCategoryReportCompleted = function (response) {
            $ionicLoading.hide();

            if (response.status !== 'SUCCESS') {
                $scope.utilities.showError($ionicPopup, response);

                return;
            }

            $scope.showUi = true;

            $scope.category = { 
                name: response.report.name,
                feeds: response.report.feedReports.length
            };
        };

        var onServerFault = function () {
            $scope.utilities.showFatalError($ionicPopup, $ionicLoading);        
        };

        loadCategoryReport();
    }

);

'use strict';

controllers.controller('categoriesController',

    ['$scope', '$state', 'categories',

    function ($scope, $state, categories) {

        $scope.addCategory = function () {
            $state.go('add-category');
        };

        var loadCategoriesReport = function () {
            categories.query(onLoadCategoriesReportCompleted, onServerFault);
        };

        var onLoadCategoriesReportCompleted = function (response) {
                $scope.reports = response.reports;
        };

        var onServerFault = function () {
        };

        loadCategoriesReport();
    }
]);

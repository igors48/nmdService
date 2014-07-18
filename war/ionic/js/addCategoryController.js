'use strict';

controllers.controller('addCategoryController',

    ['$scope', '$state', 'categories',

    function ($scope, $state, categories) {

        $scope.addNewCategory = function (categoryName) {
            var name = this.categoryName;

            this.categoryName = categoryName + '-' + categoryName;
            debugger;
        }
    }

]);

(function() {
    'use strict';

    angular
        .module('accountsApp')
        .controller('CustomerDetailController', CustomerDetailController);

    CustomerDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Customer', 'Address', 'Agreement', 'Broker'];

    function CustomerDetailController($scope, $rootScope, $stateParams, previousState, entity, Customer, Address, Agreement, Broker) {
        var vm = this;

        vm.customer = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('accountsApp:customerUpdate', function(event, result) {
            vm.customer = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();

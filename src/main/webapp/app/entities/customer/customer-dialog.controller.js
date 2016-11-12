(function() {
    'use strict';

    angular
        .module('accountsApp')
        .controller('CustomerDialogController', CustomerDialogController);

    CustomerDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Customer', 'Address', 'Agreement', 'Broker'];

    function CustomerDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Customer, Address, Agreement, Broker) {
        var vm = this;

        vm.customer = entity;
        vm.clear = clear;
        vm.save = save;
        vm.addresses = Address.query({filter: 'customer-is-null'});
        $q.all([vm.customer.$promise, vm.addresses.$promise]).then(function() {
            if (!vm.customer.address || !vm.customer.address.id) {
                return $q.reject();
            }
            return Address.get({id : vm.customer.address.id}).$promise;
        }).then(function(address) {
            vm.addresses.push(address);
        });
        vm.agreements = Agreement.query();
        vm.brokers = Broker.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.customer.id !== null) {
                Customer.update(vm.customer, onSaveSuccess, onSaveError);
            } else {
                Customer.save(vm.customer, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('accountsApp:customerUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();

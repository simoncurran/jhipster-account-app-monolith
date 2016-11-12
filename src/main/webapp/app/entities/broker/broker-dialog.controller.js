(function() {
    'use strict';

    angular
        .module('accountsApp')
        .controller('BrokerDialogController', BrokerDialogController);

    BrokerDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Broker', 'Address'];

    function BrokerDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Broker, Address) {
        var vm = this;

        vm.broker = entity;
        vm.clear = clear;
        vm.save = save;
        vm.addresses = Address.query({filter: 'broker-is-null'});
        $q.all([vm.broker.$promise, vm.addresses.$promise]).then(function() {
            if (!vm.broker.address || !vm.broker.address.id) {
                return $q.reject();
            }
            return Address.get({id : vm.broker.address.id}).$promise;
        }).then(function(address) {
            vm.addresses.push(address);
        });

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.broker.id !== null) {
                Broker.update(vm.broker, onSaveSuccess, onSaveError);
            } else {
                Broker.save(vm.broker, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('accountsApp:brokerUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();

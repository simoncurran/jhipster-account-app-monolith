(function() {
    'use strict';

    angular
        .module('accountsApp')
        .controller('AgreementDialogController', AgreementDialogController);

    AgreementDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Agreement', 'Customer', 'Broker'];

    function AgreementDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Agreement, Customer, Broker) {
        var vm = this;

        vm.agreement = entity;
        vm.clear = clear;
        vm.save = save;
        vm.customers = Customer.query();
        vm.brokers = Broker.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.agreement.id !== null) {
                Agreement.update(vm.agreement, onSaveSuccess, onSaveError);
            } else {
                Agreement.save(vm.agreement, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('accountsApp:agreementUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();

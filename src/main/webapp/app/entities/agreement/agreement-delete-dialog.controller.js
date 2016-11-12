(function() {
    'use strict';

    angular
        .module('accountsApp')
        .controller('AgreementDeleteController',AgreementDeleteController);

    AgreementDeleteController.$inject = ['$uibModalInstance', 'entity', 'Agreement'];

    function AgreementDeleteController($uibModalInstance, entity, Agreement) {
        var vm = this;

        vm.agreement = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Agreement.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();

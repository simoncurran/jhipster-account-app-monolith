(function() {
    'use strict';

    angular
        .module('accountsApp')
        .controller('BrokerDeleteController',BrokerDeleteController);

    BrokerDeleteController.$inject = ['$uibModalInstance', 'entity', 'Broker'];

    function BrokerDeleteController($uibModalInstance, entity, Broker) {
        var vm = this;

        vm.broker = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Broker.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();

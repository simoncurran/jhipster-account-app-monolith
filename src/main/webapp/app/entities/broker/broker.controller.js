(function() {
    'use strict';

    angular
        .module('accountsApp')
        .controller('BrokerController', BrokerController);

    BrokerController.$inject = ['$scope', '$state', 'Broker'];

    function BrokerController ($scope, $state, Broker) {
        var vm = this;
        
        vm.brokers = [];

        loadAll();

        function loadAll() {
            Broker.query(function(result) {
                vm.brokers = result;
            });
        }
    }
})();

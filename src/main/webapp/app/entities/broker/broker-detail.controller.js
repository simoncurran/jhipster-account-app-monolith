(function() {
    'use strict';

    angular
        .module('accountsApp')
        .controller('BrokerDetailController', BrokerDetailController);

    BrokerDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Broker', 'Address'];

    function BrokerDetailController($scope, $rootScope, $stateParams, previousState, entity, Broker, Address) {
        var vm = this;

        vm.broker = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('accountsApp:brokerUpdate', function(event, result) {
            vm.broker = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();

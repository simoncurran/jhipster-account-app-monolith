(function() {
    'use strict';

    angular
        .module('accountsApp')
        .controller('AgreementDetailController', AgreementDetailController);

    AgreementDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Agreement', 'Customer', 'Broker'];

    function AgreementDetailController($scope, $rootScope, $stateParams, previousState, entity, Agreement, Customer, Broker) {
        var vm = this;

        vm.agreement = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('accountsApp:agreementUpdate', function(event, result) {
            vm.agreement = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();

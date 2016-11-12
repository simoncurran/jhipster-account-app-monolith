(function() {
    'use strict';

    angular
        .module('accountsApp')
        .controller('AgreementController', AgreementController);

    AgreementController.$inject = ['$scope', '$state', 'Agreement'];

    function AgreementController ($scope, $state, Agreement) {
        var vm = this;
        
        vm.agreements = [];

        loadAll();

        function loadAll() {
            Agreement.query(function(result) {
                vm.agreements = result;
            });
        }
    }
})();

'use strict';

describe('Controller Tests', function() {

    describe('Agreement Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockAgreement, MockCustomer, MockBroker;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockAgreement = jasmine.createSpy('MockAgreement');
            MockCustomer = jasmine.createSpy('MockCustomer');
            MockBroker = jasmine.createSpy('MockBroker');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Agreement': MockAgreement,
                'Customer': MockCustomer,
                'Broker': MockBroker
            };
            createController = function() {
                $injector.get('$controller')("AgreementDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'accountsApp:agreementUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});

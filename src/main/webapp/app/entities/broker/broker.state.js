(function() {
    'use strict';

    angular
        .module('accountsApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('broker', {
            parent: 'entity',
            url: '/broker',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Brokers'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/broker/brokers.html',
                    controller: 'BrokerController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('broker-detail', {
            parent: 'entity',
            url: '/broker/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Broker'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/broker/broker-detail.html',
                    controller: 'BrokerDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Broker', function($stateParams, Broker) {
                    return Broker.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'broker',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('broker-detail.edit', {
            parent: 'broker-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/broker/broker-dialog.html',
                    controller: 'BrokerDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Broker', function(Broker) {
                            return Broker.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('broker.new', {
            parent: 'broker',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/broker/broker-dialog.html',
                    controller: 'BrokerDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                brokerNumber: null,
                                brokerName: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('broker', null, { reload: 'broker' });
                }, function() {
                    $state.go('broker');
                });
            }]
        })
        .state('broker.edit', {
            parent: 'broker',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/broker/broker-dialog.html',
                    controller: 'BrokerDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Broker', function(Broker) {
                            return Broker.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('broker', null, { reload: 'broker' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('broker.delete', {
            parent: 'broker',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/broker/broker-delete-dialog.html',
                    controller: 'BrokerDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Broker', function(Broker) {
                            return Broker.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('broker', null, { reload: 'broker' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();

(function() {
    'use strict';

    angular
        .module('accountsApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('agreement', {
            parent: 'entity',
            url: '/agreement',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Agreements'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/agreement/agreements.html',
                    controller: 'AgreementController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('agreement-detail', {
            parent: 'entity',
            url: '/agreement/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Agreement'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/agreement/agreement-detail.html',
                    controller: 'AgreementDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Agreement', function($stateParams, Agreement) {
                    return Agreement.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'agreement',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('agreement-detail.edit', {
            parent: 'agreement-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/agreement/agreement-dialog.html',
                    controller: 'AgreementDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Agreement', function(Agreement) {
                            return Agreement.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('agreement.new', {
            parent: 'agreement',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/agreement/agreement-dialog.html',
                    controller: 'AgreementDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                agreementNumber: null,
                                lob: null,
                                premium: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('agreement', null, { reload: 'agreement' });
                }, function() {
                    $state.go('agreement');
                });
            }]
        })
        .state('agreement.edit', {
            parent: 'agreement',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/agreement/agreement-dialog.html',
                    controller: 'AgreementDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Agreement', function(Agreement) {
                            return Agreement.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('agreement', null, { reload: 'agreement' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('agreement.delete', {
            parent: 'agreement',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/agreement/agreement-delete-dialog.html',
                    controller: 'AgreementDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Agreement', function(Agreement) {
                            return Agreement.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('agreement', null, { reload: 'agreement' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();

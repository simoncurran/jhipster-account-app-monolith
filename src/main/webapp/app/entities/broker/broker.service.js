(function() {
    'use strict';
    angular
        .module('accountsApp')
        .factory('Broker', Broker);

    Broker.$inject = ['$resource'];

    function Broker ($resource) {
        var resourceUrl =  'api/brokers/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();

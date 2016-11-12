(function() {
    'use strict';
    angular
        .module('accountsApp')
        .factory('Agreement', Agreement);

    Agreement.$inject = ['$resource'];

    function Agreement ($resource) {
        var resourceUrl =  'api/agreements/:id';

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

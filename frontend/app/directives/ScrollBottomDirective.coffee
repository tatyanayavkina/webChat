'use strict';

CoreModule.directive 'scrollBottom', ($rootScope) ->
    restrict: 'A'
    link    : (scope, element, attributes) ->

        $rootScope.$on('message:scrollBottom', (event, data) ->
            element.scrollTop(element[0].scrollHeight);
            console.log('scrollBottom', element[0].scrollHeight);
        )

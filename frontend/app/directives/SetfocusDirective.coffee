'use strict';

CoreModule.directive 'focus', ($rootScope) ->
    restrict: 'A'
    link    : (scope, element, attributes) ->

        $rootScope.$on('message:setFocus', (event, data) ->
            element.focus();
            return;
        )

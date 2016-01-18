'use strict';

CoreModule.service 'Message', ($rootScope, $timeout) ->
    new class Message
        scrollBottom : () ->
            $timeout(
                () -> $rootScope.$broadcast('message:scrollBottom');
            )

        setFocus : () ->
            $rootScope.$broadcast('message:setFocus');
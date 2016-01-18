'use strict';

CoreModule.service 'Notification', ($rootScope, $q) ->
    new class Notification

        show: (message) ->
            $rootScope.$broadcast('message:show', {type: 'notification', message: message});

        confirm: (message, affirmative = 'Да', dismiss = 'Нет') ->
            deferred = $q.defer();

            data =
                type       : 'confirm',
                message    : message,
                duration   : 3000000
                affirmative: affirmative
                dismiss    : dismiss
                promise    : deferred

            $rootScope.$broadcast('message:show', data);
            deferred.promise;
'use strict';

CoreModule.directive 'notification', ($http, $compile, $timeout) ->
    restrict: 'E'
    link    : (scope, element, attributes) ->

        scope.loadTemplate = (type) ->
            templates =
                notification: '/app/directives/templates/notification.html'
                confirm     : '/app/directives/templates/confirm.html'

            $http.get(templates[type]);

        scope.show = (data, template) ->
            scope = angular.extend(scope, data);            
            
            notification = $compile(template)(scope);
            $('body').append(notification);
            notice = $('.notice-wrapper')
            notice.show(400);

            if data.type == 'notification'
                $timeout(
                    () -> notice.hide();
                    3000
                );

        scope.$on('message:show', (event, data) ->
            scope.loadTemplate(data.type).success((template) ->
                scope.show(data, template);
            )
        )

        # для конфирмов сделаем 2 функции, которые возвращают результат
        # положительный результат
#        scope.resolve = () ->
#            scope.promise.resolve();
#
#        # отрицательный результат
#        scope.reject = () ->
#            scope.promise.reject();




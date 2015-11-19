#NotificationDirective#

'use strict';

CoreModule.directive 'notification', ($http, $compile) ->
    restrict: 'E'
    link    : (scope, element, attributes) ->

        scope.loadTemplate = (type) ->
            templates =
                notification: '/common/directives/templates/notification.html'
                confirm     : '/common/directives/templates/confirm.html'
                modal       : '/common/directives/templates/modal.html'

            $http.get(templates[type]);

        scope.show = (data, template) ->
            scope = angular.extend(scope, data);            
            
            notification = $compile(template)(scope);
            $('body').append(notification);
            notification[0].show();


        scope.$on('message:show', (event, data) ->
            scope.loadTemplate(data.type).success((template) ->
                scope.show(data, template);
            )
        )

        # для конфирмов сделаем 2 функции, которые возвращают результат
        # положительный результат
        scope.resolve = () -> 
            scope.promise.resolve();

        # отрицательный результат
        scope.reject = () ->
            scope.promise.reject();

        # удаляем из body при нажитии на "Закрыть"
        scope.close = () ->
            $('body').find('paper-toast').remove();
            return true;



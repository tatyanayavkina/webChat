#RoomsController#

'use strict';

CoreModule.controller 'RoomsController', ($scope, $rootScope, $state, $stateParams, session, room) ->
    $scope.room = room;

    $scope.save = () ->
        if isNewRecord = $scope.room.isNewRecord
            $scope.room.users = [];
            $scope.room.users.push(session.user);
            $scope.owner = session.user;

        $scope.room.save().then(
            (room) ->
                $scope.room = angular.extend({},$scope.room, room);
                #todo: сделать $emit, что появилась новая комната if isNewRecord
#                Notification.show('Комната успешно сохранена')
            (error) ->
                console.log(error);
#                Notification.show('Произошла ошибка')

        );



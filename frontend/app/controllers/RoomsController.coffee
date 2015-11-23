#RoomsController#

'use strict';

CoreModule.controller 'RoomsController', ($scope, $rootScope, $state, $stateParams, session, room, openRooms) ->
    $scope.room = room;
    $scope.openRooms = openRooms;

    if $scope.openRooms && $scope.openRooms.length > 0
        angular.forEach($scope.$parent.rooms, (room) ->
            angular.forEach($scope.openRooms, (openRoom, index) ->
                if room.id == openRoom.id
                    $scope.openRooms.splice(index, 1);
            )
        )

    $scope.joinRoom = (room) ->
        room.join(session.user.id).then(
            (result) ->
                console.log('join result', result);
                $scope.openRooms.splice($scope.openRooms.indexOf(room), 1);
                # todo: добавить комнату в список комнат пользователя
            (error) ->
                console.log('join error', error);
        )

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



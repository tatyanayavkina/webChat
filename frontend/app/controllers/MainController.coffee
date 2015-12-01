#MainController#

'use strict';

CoreModule.controller 'MainController', ($scope, $rootScope, $state, $stateParams, Auth, MessagesModel, session, rooms) ->
    $scope.auth = Auth;
    $scope.rooms = rooms;
    $scope.roomMessages = {};

    # создает объект для сущности сообщения и привязываем его к комнате
    $scope.createMessage = () ->
        id = $scope.currentRoom.id;
        $scope.roomMessages[id] = {};
        $scope.roomMessages[id].message = new MessagesModel();
        $scope.roomMessages[id].message.user = session.user;
        $scope.roomMessages[id].message.room = $scope.currentRoom;

    # в качестве открытой комнаты берем первую из списка
    if $scope.rooms && $scope.rooms.length > 0
        $scope.currentRoom = $scope.rooms[0];
        $scope.createMessage();
        $scope.roomMessages[$scope.currentRoom.id].messages = [];


    $scope.selectRoom = (room) ->
        $scope.currentRoom = room;
        if !$scope.roomMessages[$scope.currentRoom.id]
            $scope.createMessage();

    $scope.leaveRoom = () ->
        if !$scope.currentRoom
            return;
        $scope.currentRoom.leave(session.user.id).then(
            (success) ->
                $scope.rooms.splice($scope.rooms.indexOf($scope.currentRoom), 1);
                $scope.$broadcast('user:leaveRoom',{room: $scope.currentRoom});
                if $scope.rooms && $scope.rooms.length > 0
                    $scope.currentRoom = $scope.rooms[0];
                else
                    $scope.currentRoom = null;
                console.log('success', success);
            (error) ->
                console.log('error', error);
        )

    # пользователь вступил в комнату!
    $scope.$on('user:joinRoom', (event, data) ->
        $scope.rooms.push(data.room);
    )

    # пользователь создал комнату
    $scope.$on('user:createRoom', (event, data) ->
        $scope.rooms.push(data.room);
    )

    # пользователь удалил комнату
    $scope.$on('user:deleteRoom', (event, data) ->
        removedRoom = data.room;
        angular.forEach($scope.rooms, (room, index) ->
            if removedRoom.id == room.id
                $scope.rooms.splice(index, 1);
        )

        if $scope.currentRoom.id == removedRoom.id
            if $scope.rooms.length > 0 then $scope.currentRoom = $scope.rooms[0] else null;
    )

    # отправляем сообщение
    $scope.send = () ->
        id = $scope.currentRoom.id;
        if  $scope.roomMessages[id].message.content
            $scope.roomMessages[id].message.save().then(
               (message) ->
                   console.log('message in save message', message);
                   $scope.createMessage();
                   if !$scope.roomMessages[id].messages then $scope.roomMessages[id].messages = [];
                   $scope.roomMessages[id].messages.push(message);
               (error) ->
                   console.log('error in save message', error);
            )





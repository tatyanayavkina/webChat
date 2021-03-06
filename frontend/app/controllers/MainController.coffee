#MainController#

'use strict';

CoreModule.controller 'MainController', ($scope, $rootScope, $state, $stateParams, Auth, Message, MessagesModel, session, rooms) ->
    $scope.state = $state
    $scope.auth = Auth;
    $scope.rooms = rooms;
    $scope.roomMessages = {};

    # создает объект для сущности сообщения
    $scope.createMessage = (id) ->
        room = null;
        angular.forEach($scope.rooms, (item) ->
            if item.id == id then room = item;
        )
        if room
            $scope.roomMessages[id].message = new MessagesModel();
            $scope.roomMessages[id].message.user = session.user;
            $scope.roomMessages[id].message.room = room;

    $scope.getUnreadMessages = () ->
        MessagesModel.getUnreadMessages(session.user.id).then(
            (result) ->
                angular.forEach(result, (messages, index) ->
                    $scope.roomMessages[index].messages = $scope.roomMessages[index].messages.concat(messages);
                )
                $scope.getUnreadMessages();
            (error) ->
                console.log('error', error);
#                $scope.getUnreadMessages(session.user.id);
        )

    $scope.selectRoom = (room) ->
        $scope.currentRoom = room;
        # автоскрол
        Message.scrollBottom();
        Message.setFocus();


    # в качестве открытой комнаты берем первую из списка
    if $scope.rooms && $scope.rooms.length > 0
        $scope.selectRoom($scope.rooms[0]);
        angular.forEach($scope.rooms, (room, index) ->
            $scope.roomMessages[room.id] = {};
            $scope.roomMessages[room.id].messages = [];
            $scope.createMessage(room.id);
        )
        $scope.getUnreadMessages();

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
        joinedRoom = data.room
        $scope.rooms.push(joinedRoom);
        # делаем запрос на последние сообщения в комнате
        MessagesModel.getLastMessages(joinedRoom.id).then(
            (messages) ->
                $scope.roomMessages[joinedRoom.id] = {};
                $scope.roomMessages[joinedRoom.id].messages = messages.reverse();
                $scope.createMessage(joinedRoom.id);
        )

    )

    # пользователь создал комнату
    $scope.$on('user:createRoom', (event, data) ->
        room = data.room;
        $scope.rooms.push(room);
        $scope.roomMessages[room.id] = {};
        $scope.roomMessages[room.id].messages = messages.reverse();
        $scope.createMessage(room.id);
    )

    # пользователь удалил комнату
    $scope.$on('user:deleteRoom', (event, data) ->
        removedRoom = data.room;
        # удаляем сообщения комнаты
        delete $scope.roomMessages[removedRoom.id];
        # удаляем комнату из списка комнат
        angular.forEach($scope.rooms, (room, index) ->
            if removedRoom.id == room.id
                $scope.rooms.splice(index, 1);
        )
        # проверяем, если комната была текущей
        if $scope.currentRoom.id == removedRoom.id
            if $scope.rooms.length > 0 then $scope.currentRoom = $scope.rooms[0] else null;
    )

    # отправляем сообщение
    $scope.send = () ->
        id = $scope.currentRoom.id;
        if  $scope.roomMessages[id].message.content
            $scope.roomMessages[id].message.save().then(
               (message) ->
                   $scope.createMessage(id);
                   if !$scope.roomMessages[id].messages then $scope.roomMessages[id].messages = [];
                   $scope.roomMessages[id].messages.push(message);
                   # автоскрол
                   Message.scrollBottom();
                   Message.setFocus();
               (error) ->
                   console.log('error in save message', error);
            )





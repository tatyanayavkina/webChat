#RoomsController#

'use strict';

CoreModule.controller 'RoomsController', ($scope, $rootScope, $state, $stateParams, RoomsModel, Notification, InvitationsModel, session, room, openRooms) ->
    $scope.room = room;
    $scope.roomModel = RoomsModel;
    $scope.openRooms = openRooms;
    $scope.usersToRemove = {};
    $scope.usersToInvite = [];

    if $scope.openRooms && $scope.openRooms.length > 0
        angular.forEach($scope.$parent.rooms, (room) ->
            angular.forEach($scope.openRooms, (openRoom, index) ->
                if room.id == openRoom.id
                    $scope.openRooms.splice(index, 1);
            )
        )

    $scope.joinRoom = (room) ->
        console.log('room', room);
        room.join(session.user.id).then(
            (result) ->
                $scope.openRooms.splice($scope.openRooms.indexOf(room), 1);
                # Пользователь вступил в комнату
                $scope.$emit('user:joinRoom', {room: room});
            (error) ->
                console.log('join error', error);
        )

    $scope.$on('user:leaveRoom', (event, data) ->
        if data.room.type == RoomsModel.types.OPEN
            $scope.openRooms.push(data.room);
    )

    $scope.save = () ->
        if isNewRecord = $scope.room.isNewRecord
            $scope.room.users = [];
            $scope.room.users.push(session.user);
            $scope.room.owner = session.user;

        $scope.room.save().then(
            (room) ->
                $scope.room = angular.extend({},$scope.room, room);
                if isNewRecord
                    $scope.$emit('user:createRoom', {room: $scope.room});
                    $state.transitionTo('home.rooms-update', {roomID: room.id});
                Notification.show('Комната успешно сохранена')
            (error) ->
                console.log(error);
                Notification.show('Произошла ошибка')

        );

    $scope.deleteRoom = () ->
        $scope.room.remove().then(
            (success) ->
                console.log('room deleted');
                $scope.$emit('user:deleteRoom', {room: $scope.room});
                $state.transitionTo('home.rooms');
            (error) ->
                console.log('error in room deleting');
        )

    $scope.removeUsers = () ->
        $scope.room.removeUsers($scope.usersToRemove).then(
            (success) ->
                angular.forEach($scope.usersToRemove, (userToRemove) ->
                    angular.forEach($scope.room.users, (user, index) ->
                        if userToRemove.id == user.id
                            $scope.room.users.splice(index, 1);
                    )
                )

                $scope.usersToRemove = {};
                console.log('success in removeUsers', success);
            (error) ->
                console.log('error in removeUsers', error);
                Notification.show('Произошла ошибка')
        )

    $scope.addNewUserToInvite = () ->
        $scope.usersToInvite.push({login: ''});

    $scope.inviteUsers = () ->
        InvitationsModel.send($scope.room.id, $scope.usersToInvite).then(
            (result) ->
                console.log('sendInvitations -- success-', result);
                $scope.usersToInvite = [];
                Notification.show('Приглашения отправлены: ' + result.invitedUsers.length +
                    ', уже в комнате: ' + result.alreadyInRoomUsers.length +
                    ', уже приглашены: ' + result.alreadyInvitedUsers.length);
            (error) ->
                console.log('sendInvitations=',error);
                Notification.show('Произошла ошибка')
        )




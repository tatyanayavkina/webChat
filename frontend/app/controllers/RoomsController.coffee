#RoomsController#

'use strict';

CoreModule.controller 'RoomsController', ($scope, $rootScope, $state, $stateParams, RoomsModel, InvitationsModel, session, room, openRooms) ->
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
            $scope.owner = session.user;

        $scope.room.save().then(
            (room) ->
                $scope.room = angular.extend({},$scope.room, room);
                $scope.$emit('user:createRoom', {room: $scope.room})
#                Notification.show('Комната успешно сохранена')
            (error) ->
                console.log(error);
#                Notification.show('Произошла ошибка')

        );

    $scope.removeUsers = () ->
        console.log('usersToRemove', $scope.usersToRemove);
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
        )

    $scope.addNewUserToInvite = () ->
        emptyStr = '';
        $scope.usersToInvite.push(emptyStr);

    $scope.inviteUsers = () ->
        console.log('usersToInvite', $scope.usersToInvite);
#        InvitationsModel.send($scope.room.id, $scope.usersToInvite).then(
#            (success) ->
#                console.log('sendInvitations -- success-', success);
#                $scope.usersToInvite = [];
##                Notification.show('Приглашения отправлены')
#            (error) ->
#                console.log('sendInvitations=',error);
##                Notification.show('Произошла ошибка')
#        )




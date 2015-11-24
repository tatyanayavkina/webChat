#MainController#

'use strict';

CoreModule.controller 'MainController', ($scope, $rootScope, $state, $stateParams, Auth, session, rooms) ->
    $scope.auth = Auth;
    $scope.rooms = rooms;

    # в качестве открытой комнаты берем первую из списка
    if $scope.rooms && $scope.rooms.length > 0
        $scope.currentRoom = $scope.rooms[0];

    $scope.selectRoom = (room) ->
        $scope.currentRoom = room;

    $scope.leaveRoom = () ->
        if !$scope.currentRoom
            return;
        session.user.leaveRoom($scope.currentRoom.id).then(
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





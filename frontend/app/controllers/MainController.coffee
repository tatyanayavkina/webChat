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

    # пользователь вступил в комнату!
    $scope.$on('user:joinRoom', (event, data) ->
        $scope.rooms.push(data.room);
    )





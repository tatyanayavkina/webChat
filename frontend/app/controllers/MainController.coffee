#MainController#

'use strict';

CoreModule.controller 'MainController', ($scope, $rootScope, $state, $stateParams, Auth, session, rooms) ->
    $scope.auth = Auth;
    $scope.rooms = rooms;

    # в качестве открытой комнаты берем первую из списка
    if $scope.rooms && $scope.room.length > 0
        $scope.currentRoom = $scope.rooms[0];





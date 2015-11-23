#MainController#

'use strict';

CoreModule.controller 'MainController', ($scope, $rootScope, $state, $stateParams, Auth, session, rooms) ->
    $scope.auth = Auth;
    $scope.rooms = rooms;
    console.log('rooms', rooms);




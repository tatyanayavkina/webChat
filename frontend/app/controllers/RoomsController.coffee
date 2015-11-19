#RoomsController#

'use strict';

CoreModule.controller 'RoomsController', ($scope, $rootScope, $state, $stateParams, session, room) ->
    $scope.room = room;



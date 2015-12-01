#InvitationsController#

'use strict';

CoreModule.controller 'InvitationsController', ($scope, $rootScope, $state, $stateParams, RoomsModel, InvitationsModel, session, invitations) ->
    $scope.invitations = invitations;

    $scope.accept = (invitation) ->
        invitation.accept().then(
            (room) ->
                room = RoomsModel::transform(room, [{name: 'owner'}]);
                $scope.$emit('user:joinRoom', {room: room});
                $scope.invitations.splice($scope.invitations.indexOf(invitation), 1);
                console.log("success invitation accept");
            (error) ->
                console.log('error invitation accept');
        )

    $scope.reject = (invitation) ->
        invitation.reject().then(
            (success) ->
                $scope.invitations.splice($scope.invitations.indexOf(invitation), 1);
                console.log("success invitation reject");
            (error) ->
                console.log('error invitation reject');
        )



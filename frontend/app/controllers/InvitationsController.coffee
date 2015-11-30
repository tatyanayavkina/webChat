#InvitationsController#

'use strict';

CoreModule.controller 'InvitationsController', ($scope, $rootScope, $state, $stateParams, InvitationsModel, session, invitations) ->
    $scope.invitations = invitations;



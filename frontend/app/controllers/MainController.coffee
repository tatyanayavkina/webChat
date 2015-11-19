#MainController#

'use strict';

CoreModule.controller 'MainController', ($scope, $rootScope, $state, $stateParams, Auth, session) ->
    $scope.auth = Auth;



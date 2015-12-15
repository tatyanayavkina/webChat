#AuthController#

'use strict';

CoreModule.controller 'AuthController', ($scope, $rootScope, $state, $stateParams, Auth) ->
    $scope.account =
        username : null
        password : null

    $scope.accountNick =
        nickname : null

    $scope.loginNick = () ->
        Auth.loginNick($scope.accountNick).then(
            (session) ->
                $scope.accountNick =
                    nickname : null
                $state.transitionTo($state.current, $stateParams,{reload: true});
            (error) ->
                console.log('error', error);
        )

    $scope.login = () ->
        $scope.loginErrors = {};
        Auth.login($scope.account).then(
            (session) ->
                console.log('session', session);
                $scope.account =
                    username : null
                    password : null
                $state.transitionTo($state.current, $stateParams,{reload: true});
            (error) ->
                if error.result == 401
                    $scope.loginErrors.custom = {id: 'auth', message: 'Неправильный логин или пароль.'};
                else
                    $scope.loginErrors.custom = {id: 'serverError', message: 'Ошибка сервера'};
        )

    $scope.logout = () ->
        Auth.logout();
        $state.transitionTo($state.current, $stateParams,{reload: true});

#
#    $scope.registration = () ->
#        $scope.error = false;
#        $scope.account.login = $scope.account.phone;
#        delete $scope.account.password;
#        Auth.registerSms($scope.account).then(
#            (success) ->
#                console.log(success);
#                $scope.registered = true;
#
#            (error) ->
#                $scope.error = error;
#                console.log(error);
#        )


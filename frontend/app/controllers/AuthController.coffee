#AuthController#

'use strict';

CoreModule.controller 'AuthController', ($scope, $rootScope, $state, $stateParams, Auth) ->
    $scope.account =
        username : null
        password : null

    $scope.accountNick =
        nickname : null

    $scope.regAccount =
        login   : null
        nickname: null
        password: null

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
        $scope.loginError = "";
        Auth.login($scope.account).then(
            (session) ->
                $scope.account =
                    username : null
                    password : null
                $state.transitionTo('home.rooms', $stateParams,{reload: true});
            (error) ->
                if error.status == 401
                    $scope.loginError = 'Неправильный логин или пароль';
                else
                    $scope.loginError = 'Ошибка сервера';
        )

    $scope.logout = () ->
        Auth.logout();
        $state.transitionTo('entrance', $stateParams,{reload: true});


    $scope.register = () ->
        Auth.register($scope.regAccount).then(
            (success) ->
                $scope.regAccount =
                    login   : null
                    nickname: null
                    password: null
                $state.transitionTo('home.rooms', $stateParams,{reload: true});
            (error) ->
                $scope.error = error;
                console.log(error);
        )


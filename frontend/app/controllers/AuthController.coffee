#AuthController#

'use strict';

CoreModule.controller 'AuthController', ($scope, $rootScope, $state, $stateParams, Auth) ->
    console.log('AuthController');
    $scope.account =
        username : null
        password : null

    $scope.accountNick =
        nickname : null

    $scope.loginNick = () ->
        Auth.loginNick($scope.accountNick).then(
            (session) ->
                console.log('session', session);
            (error) ->
                console.log('error', error);
        )

    $scope.login = () ->
#        $scope.loginErrors = {};
        Auth.login($scope.account).then(
            (session) ->
                console.log('session', session);
                # если данные аккаунта нужно запомнить
#                if $scope.account.remember
#                    Storage.setItem('account-portal',$scope.account);
#                else
#                    Storage.removeItem('account-portal');
#
#                # если не инлайн-авторизация
#                if $state.current && $state.current.name == 'login'
#                    if $state.previousState
#                        $state.transitionTo($state.previousState, $state.fromParams, {reload: true});
#                    else
#                        $state.transitionTo('core.profile.settings', {}, {reload: true});
#                else
#                    $state.transitionTo($state.current, $stateParams, {reload: true});
            (error) ->
                console.log('error', error);
#                if error.result == 401
#                    $scope.loginErrors.custom = {id: 'auth', message: error.message};
#                else
#                    $scope.loginErrors.custom = {id: 'serverError', message: 'Ошибка сервера'};
        )

#    if account = Storage.getItem('account-portal')
#        console.log('account-portal',account);
#        $scope.account = account
#    else
#        $scope.account =
#            login    : null
#            password : null
#
#    $scope.loginErrors = {};
#
#    $scope.login = () ->
#        $scope.loginErrors = {};
#        $scope.account.authType = 'phone';
#        Auth.login($scope.account).then(
#            (session) ->
#                # если данные аккаунта нужно запомнить
#                if $scope.account.remember
#                    Storage.setItem('account-portal',$scope.account);
#                else
#                    Storage.removeItem('account-portal');
#
#                # если не инлайн-авторизация
#                if $state.current && $state.current.name == 'login'
#                    if $state.previousState
#                        $state.transitionTo($state.previousState, $state.fromParams, {reload: true});
#                    else
#                        $state.transitionTo('core.profile.settings', {}, {reload: true});
#                else
#                    $state.transitionTo($state.current, $stateParams, {reload: true});
#            (error) ->
#                if error.result == 401
#                    $scope.loginErrors.custom = {id: 'auth', message: error.message};
#                else
#                    $scope.loginErrors.custom = {id: 'serverError', message: 'Ошибка сервера'};
#        )
#
#    $scope.logout = () ->
#        Auth.logout();
#        $state.transitionTo('core.home',{},{reload: true});
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

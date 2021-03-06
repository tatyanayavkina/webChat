'use strict';

CoreModule.factory 'Auth', ($http, $q, $state, $httpParamSerializerJQLike, Storage, config, roles, UsersModel) ->
    new class Auth
        session       : null
        refreshSession: null

        url:
            login       : config.api + 'access/login'
            nickname    : config.api + 'access/nickname'
            register    : config.api + 'access/register'
            refresh     : config.api + 'access/refresh'
            logout      : config.api + 'access/logout'

        # заголовки запросов
        headers:
            'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8';

        _setSession : (response) ->
            Storage.setItem('accessToken', response.token);
            delete $http.defaults.headers.common['AccessToken'];
            $http.defaults.headers.common['AccessToken'] = response.token;

            @session =
                user: @_transform(response.user, [{name: 'roles'}])
                accessToken: response.token

        isLoggedIn : () -> @session isnt null;

        isSuperAdmin : () ->
            isAdmin = false;
            if @isLoggedIn()
                angular.forEach(@session.user.roles, (role, index) ->
                    if role.code == roles.admin
                        isAdmin = true;
                )

            isAdmin

        hasAccess : (accessRoles) ->
            hasAccess = false;
            angular.forEach(@session.user.roles, (role) ->
                hasAccess = hasAccess || role.code in accessRoles;
            )

            hasAccess;

        _transform : (object, relations) ->
            UsersModel:: transform(object, relations);

        # проверить состояние сессии
        check : (state = $state.current) ->
            deferred = $q.defer();
            token = Storage.getItem('accessToken');

            if !$http.defaults.headers.common['AccessToken'] && token
                $http.defaults.headers.common['AccessToken'] = token;

            if @isLoggedIn()
                deferred.resolve(@session);
            else if token
                # Нажатие кнопки F5
                $http.get(@url.refresh, params: {accessToken: token})
                .success((response) =>
                    @_setSession(response);
                    deferred.resolve(@session);
                )
                .error((error) =>
                    Storage.clear();
                    deferred.reject({status: 401});
                )
            else
                deferred.reject({status: 401});

            deferred.promise;

        # регистрация
        register : (params) ->
            deferred = $q.defer();
            $http.post(@url.register, $httpParamSerializerJQLike(params), {headers: @headers})
            .success((response) =>
                @_setSession(response);
                deferred.resolve(@session);
            )
            .error((error) ->
                deferred.reject(error.items);
            );
            deferred.promise;

        # авторизация
        login : (params) ->
            deferred = $q.defer();
            $http.post(@url.login, $httpParamSerializerJQLike(params), {headers: @headers})
            .success((response) =>
                @_setSession(response);
                deferred.resolve(@session);
            )
            .error((error) ->
                deferred.reject({status: 401});
            );
            deferred.promise;

        # авторизация
        loginNick : (params) ->
            deferred = $q.defer();
            $http.post(@url.nickname, $httpParamSerializerJQLike(params), {headers: @headers})
            .success((response) =>
                @_setSession(response);
                deferred.resolve(@session);
            )
            .error((error) ->
                deferred.reject({status: 401});
            );
            deferred.promise;

        # выход
        logout : () ->
            # Отправили запрос на выход
            $http.get(@url.logout);
            # удалили token из заголовков
            delete $http.defaults.headers.common['AccessToken'];
            # чистим сессию
            @session = null;
            account = null;
            if Storage.getItem('account')
                account = Storage.getItem('account')
            Storage.clear();
            Storage.setItem('account',account);


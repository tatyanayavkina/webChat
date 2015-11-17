'use strict';

CoreModule.factory 'Auth', ($http, $q, $state, $httpParamSerializerJQLike, config, roles) ->
    new class Auth
        session       : null
        refreshSession: null

        url:
            login       : config.api + 'access/login'
            nickname    : config.api + 'access/nickname'
            register    : config.api + 'access/register'
            refresh     : config.api + 'access/refresh'

        # заголовки запросов
        headers:
            'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8';

        _setSession : (response) ->
#            Storage.setItem('accessToken', response.accessToken);
            delete $http.defaults.headers.common['AccessToken'];
            $http.defaults.headers.common['AccessToken'] = response.accessToken;

            @session =
#                user: @_transform(response.params, [{name: 'roles'}])
                user: response.user
                accessToken: response.accessToken

        isLoggedIn : () -> @session isnt null;

        isSuperAdmin : () ->
            @isLoggedIn() && roles.admin in @session.user.roles

        hasAccess : (accessPermissions) ->
            hasAccess = false;
            angular.forEach(@session.user.roles, (role) ->
                hasAccess = hasAccess || role.code in accessPermissions;
            )

            hasAccess;

        _transform : (object, relations) ->
            UsersModel:: transform(object, relations);

        # обновить сессию
        refresh : () ->
            deferred = $q.defer();

            token = Storage.getItem('accessToken');
            refreshToken = Storage.getItem('refreshToken');

            if token && refreshToken
                $http.post(@url.refresh, {accessToken: token, refreshToken: refreshToken}, {headers: @headers})
                .success((response) =>
                    @_setSession(response);
                    @refreshSession = false;
                    deferred.resolve(@session);
                )
                .error(() =>
                    @refreshSession = false;
                    deferred.reject(status : 401);
                )
            else
                @refreshSession = false;
                deferred.reject(status : 401);

            deferred.promise;




        # проверить состояние сессии
        check : (state = $state.current) ->
            deferred = $q.defer();

            expire = Storage.getItem('expire') * 1000;
            token = Storage.getItem('accessToken');
            refreshToken = Storage.getItem('refreshToken');

            if !$http.defaults.headers.common['Authorization'] && token
                $http.defaults.headers.common['Authorization'] = 'Bearer '+ token;

            if Date.now() < expire
                if @isLoggedIn()
                    deferred.resolve(@session);
                else if token
                    # Нажатие кнопки F5
                    @refreshSession = true;
                    $http.post(@url.session,{accessToken: token, refreshToken: refreshToken})
                        .success((response) =>
                            @_setSession(response);
                            @refreshSession = false;
                            deferred.resolve(@session);
                        )
                        .error((error) =>
                            # если можно неавторизованным
                            @refreshSession = false;
                            if state && (state.anonymous || state.abstract)
                                deferred.resolve(true);
                            else
                                deferred.reject(status : 401);
                        )
                else
                    # если можно неавторизованным
                    if state && (state.anonymous || state.abstract)
                        deferred.resolve(true);
                    else
                        deferred.reject(status : 401);
            else
                # флаг на то, что ушло обновление сессии
                @refreshSession = true;
                @refresh().then(
                    () => deferred.resolve(@session);
                    () ->
                        # если можно неавторизованным
                        if state && (state.anonymous || state.abstract)
                            deferred.resolve(true);
                        else
                            deferred.reject(status : 401);
                );

            deferred.promise;

        # регистрация
        register : (params) ->
            deferred = $q.defer();
            $http.post(@url.register, params, {headers: @headers})
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
                deferred.reject(error);
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
                deferred.reject(error);
            );
            deferred.promise;

        # выход
        logout : () ->
            @session = null;
            account = null;
            if Storage.getItem('account')
                account = Storage.getItem('account')
            Storage.clear();
            Storage.setItem('account',account);

        # восстановление пароля
        restore : (params) ->
            deferred = $q.defer();
            $http.post(@url.restore, params, {headers: @headers})
            .success( (response) ->
                deferred.resolve(response);
            )
            .error((error) ->
                deferred.reject(error);
            )

            deferred.promise;

        # восстановление пароля sms
        restoreSms : (params) ->
            deferred = $q.defer();
            $http.post(@url.restoreSms, params, {headers: @headers})
            .success( (response) ->
                deferred.resolve(response);
            )
            .error((error) ->
                deferred.reject(error);
            )

            deferred.promise;

        # после запроса на восстановление пароля
        send : (params) ->
            deferred = $q.defer();
            $http.post(@url.afterRestore, params)
            .success( (response) ->
                deferred.resolve(response);
            )
            .error((error) ->
                deferred.reject(error);
            )

            deferred.promise;

#        serializeData: ( data ) ->
#            # If this is not an object, defer to native stringification.
#            if  !angular.isObject( data )
#                return if data == null then "" else data.toString();
#            buffer = [];
#            # Serialize each key in the object.
#            for name in data
#                console.log('name=',name);
#                if !data.hasOwnProperty( name )
#                    continue;
#                value = data[ name ];
#                encodeValue = if value then encodeURIComponent("") else encodeURIComponent(value);
#                encodeString = encodeURIComponent( name )+"="+encodeValue;
#                buffer.push(encodeString);
#
#            # Serialize the buffer and clean it up for transportation.
#            source = buffer.join( "&" ).replace( /%20/g, "+" );
#
#            return( source );

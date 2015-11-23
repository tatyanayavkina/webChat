'use strict';

CoreModule.config [ '$stateProvider', '$locationProvider', '$urlRouterProvider', '$httpProvider',
      ($stateProvider, $locationProvider, $urlRouterProvider, $httpProvider) ->
            # включаем html5-режим работы с урлами
            $locationProvider.html5Mode(true);
            $locationProvider.hashPrefix('!');

            $stateProvider
            .state('registration',
                name       : 'registration'
                url        : '/registration'
                controller : 'AuthController'
                templateUrl: '/app/views/registration.html'
                anonymous  : true
            )
            .state('home',
                name       : 'home'
                url        : '/'
                abstract   : true
                views      :
                    ''         :
                        controller : 'MainController'
                        templateUrl: '/app/views/index.html'
                resolve    :
                    session: (Auth) ->
                        Auth.check();
                    rooms: ($q, RoomsModel, session) ->
                        if session && session.user
                            RoomsModel.findUserRooms(session.user.id);
                        else
                            $q.when(null);
            )
            .state('home.rooms',
                name       : 'home.rooms'
                url        : 'rooms'
                controller : 'RoomsController'
                templateUrl: '/app/views/rooms.html'
                anonymous  : true
                resolve    :
                    room: ($q) ->
                        $q.when(null);
            )
            .state('home.rooms-new',
                name       : 'home.rooms-new'
                url        : 'rooms/new'
                controller : 'RoomsController'
                templateUrl: '/app/views/rooms-new.html'
                resolve    :
                    room: (RoomsModel) ->
                        new RoomsModel();
            )
            .state('home.rooms-update',
                name       : 'home.rooms-update'
                url        : 'rooms/update/:roomID'
                controller : 'RoomsController'
                templateUrl: '/app/views/rooms-update.html'
                resolve    :
                    room: (RoomsModel, $stateParams) ->
                        RoomsModel.find({id: $stateParams.roomID});

            )

            $urlRouterProvider.when('/', '/rooms');

];

CoreModule.run(($rootScope, $state) ->
    $rootScope.$on('$stateChangeError', (event, toState, toParams, fromState, fromParams, error) ->
        console.log(error);
        if error.status == 401
            $state.transitionTo('login', {}, {reload: true});

        if error.status == 403
#           отправить на домашнюю страницу, сообщить через Notification, что доступ запрещен
            $state.transitionTo('home', {}, {reload: true});
    );
    # при удачном переходе на новую страницу - прокручиваем страницу наверх
    $rootScope.$on('$stateChangeSuccess', (event, toState, toParams, fromState, fromParams) ->
        window.scrollTo(0,0);
    );

)
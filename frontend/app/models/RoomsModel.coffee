#RoomsModel#

'use strict';

CoreModule.factory 'RoomsModel', (BaseModel, config, $q, $http) ->
    class RoomsModel extends BaseModel
        model: 'rooms'

        attributes:
            name: null # Название комнаты
            type: null # Тип комнаты - открытая или закрытая

        @types:
            OPEN : 0
            CLOSE: 1

        relations:
            users:
                model: 'UsersModel'
                type : 'hasMany'
            owner:
                model: 'UsersModel'
                type : 'belongsTo'

        @findUserRooms: (id) ->
            @findAll({url: config.api + @:: model + '/byUserId/' + id},[{name: 'owner'}]);

        @findOpen: () ->
            @findAll({url: config.api + @:: model + '/open'});

        join: (userId) ->
            deferred = $q.defer();

            $http.post(config.api + @model + '/join/' + @id, userId).then(
                (response) =>
                    if response && response.data
                        deferred.resolve(@transform(response.data, [{name: 'owner'}]));
                (error) ->
                    deferred.reject(error);
            )

            deferred.promise;

        leave: (userId) ->
            deferred = $q.defer();

            $http.post(config.api + @model + '/leave/' + @id, userId).then(
                (response) =>
                    deferred.resolve(null);
                (error) ->
                    deferred.reject(error);
            )

            deferred.promise;

        removeUsers: (users) ->
            deferred = $q.defer();

            $http.post(config.api + @model + '/removeUsers/' + @id, users).then(
                (response) =>
                    deferred.resolve(null);
                (error) ->
                    deferred.reject(error);
            )

            deferred.promise;




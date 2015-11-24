#UsersModel#

'use strict';

CoreModule.factory 'UsersModel', ($q, BaseModel, config) ->
    class UsersModel extends BaseModel
        model: 'users'

        relations:
            roles:
                model: 'RolesModel'
                type : 'hasMany'

        joinRoom: (roomId) ->
            deferred = $q.defer();

            $http.post(config.api + @model + '/' + @id + '/join', roomId).then(
                (response) =>
                    if response && response.data
                        deferred.resolve(@transform(response.data, [{name: 'owner'}]));
                (error) ->
                    deferred.reject(error);
            )

            deferred.promise;

        leaveRoom: (roomId) ->
            deferred = $q.defer();

            $http.post(config.api + @model + '/' + @id + '/leave', roomId).then(
                (response) =>
                    deferred.resolve(null);
                (error) ->
                    deferred.reject(error);
            )

            deferred.promise;

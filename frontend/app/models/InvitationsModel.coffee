#InvitationsModel#

'use strict';

CoreModule.factory 'InvitationsModel', ($q, BaseModel, config) ->
    class InvitationsModel extends BaseModel
        model: 'invitations'

        relations:
            user:
                model: 'UsersModel'
                type : 'belongsTo'
            room:
                model: 'RoomModel'
                type : 'belongsTo'

        @send: (roomId, users) ->
            deferred = $q.defer();

            $http.post(config.api + '/invitation/send/'+ roomId, users)
            .success(
                (response) ->
                    deferred.resolve(response);
            )
            .error(
                (error) ->
                    deferred.reject(error);
            )

            deferred.promise;

        accept: () ->
            deferred = $q.defer();

            $http.post(config.api + @model + '/' + @id + '/accept', {})
            .success(
                (response) ->
                    deferred.resolve(response);
            )
            .error(
                (error) ->
                    deferred.reject(error);
            )

            deferred.promise;

        reject: () ->
            deferred = $q.defer();

            $http.post(config.api + @model + '/' + @id + '/reject', {})
            .success(
                (response) ->
                    deferred.resolve(response);
            )
            .error(
                (error) ->
                    deferred.reject(error);
            )

            deferred.promise;
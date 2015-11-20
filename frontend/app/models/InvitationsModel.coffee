#InvitationsModel#

'use strict';

CoreModule.factory 'InvitationsModel', ($q, BaseModel, config) ->
    class InvitationsModel extends BaseModel
        model: 'invitations'

        attributes:
            status: null # Статус приглашения @statuses
            type  : null # Тип - приглашение или запрос на вступление @types

        @statuses:
            SEND     : 0
            RESOLVED : 1
            REJECTED : 2

        @types:
            INVITATION : 0
            REQUEST    : 1

        relations:
            user:
                model: 'UsersModel'
                type : 'belongsTo'
            room:
                model: 'RoomModel'
                type : 'belongsTo'

        send: (roomId, users) ->
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

        request: () ->
            deferred = $q.defer();

            $http.post(config.api + '/invitation/request', beforeSave(@))
            .success(
                (response) ->
                    deferred.resolve(response);
            )
            .error(
                (error) ->
                    deferred.reject(error);
            )

            deferred.promise;
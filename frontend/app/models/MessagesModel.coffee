#MessagesModel#

'use strict';

CoreModule.factory 'MessagesModel', (BaseModel, config, $q, $http) ->
    class MessagesModel extends BaseModel
        model: 'messages'

        attributes:
            content     : null # Содержание
            creationTime: null # Дата создания

        relations:
            user:
                model: 'UsersModel'
                type : 'belongsTo'
            room:
                model: 'RoomsModel'
                type : 'belongsTo'


        @getLastMessages: (roomId) ->
            @findAll({url: config.api + @::model + '/last/' + roomId},[{name:'user'},{name:'room'}]);

        @getUnreadMessages: () ->
            deferred = $q.defer();

            $http.get(config.api + @::model + '/unread')
            .success(
                (response) =>
                    result = {};
                    if response.length > 0
                        angular.forEach(response, (item) =>
                            transformed = ( @::transform(item,[{name:'user'},{name:'room'}]));
                            if !result[transformed.room.id] then result[transformed.room.id] = [];
                            result[transformed.room.id].push(transformed);
                        )
                    deferred.resolve(result);
            )
            .error(
                (error) -> deferred.reject(error);
            )

            deferred.promise;







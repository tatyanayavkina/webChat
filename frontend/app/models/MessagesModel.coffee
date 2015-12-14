#MessagesModel#

'use strict';

CoreModule.factory 'MessagesModel', (BaseModel, config, requestTimeout, $q, $http) ->
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

        transform: (data, relations) ->
            transformed = super(data, relations);
            if transformed.creationTime
                transformed.creationTime = new Date(transformed.creationTime);
            transformed;


        @getLastMessages: (roomId) ->
            @findAll({url: config.api + @::model + '/last/' + roomId},[{name:'user'},{name:'room'}]);

        @getUnreadMessages: (userId) ->
            deferred = $q.defer();

            $http.get(config.api + @::model + '/unread', {timeout: requestTimeout*1000})
            .success(
                (response) =>
                    result = {};
                    if response.length > 0
                        angular.forEach(response, (item) =>
                            transformed = ( @::transform(item,[{name:'user'},{name:'room'}]));
                            if !result[transformed.room.id] then result[transformed.room.id] = [];
                            if transformed.user.id != userId then result[transformed.room.id].push(transformed);
                        )
                    deferred.resolve(result);
            )
            .error(
                (error) -> deferred.reject(error);
            )

            deferred.promise;







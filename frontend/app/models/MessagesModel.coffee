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







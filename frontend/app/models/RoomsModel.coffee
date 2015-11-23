#RoomsModel#

'use strict';

CoreModule.factory 'RoomsModel', (BaseModel, config) ->
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
            @findAll({url: config.api + @:: model + '/byUserId/' + id});





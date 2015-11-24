#UsersModel#

'use strict';

CoreModule.factory 'UsersModel', ($q, BaseModel, config) ->
    class UsersModel extends BaseModel
        model: 'users'

        relations:
            roles:
                model: 'RolesModel'
                type : 'hasMany'



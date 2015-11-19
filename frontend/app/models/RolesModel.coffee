#RolesModel#

'use strict';

CoreModule.factory 'RolesModel', ($q, BaseModel, config) ->
    class RolesModel extends BaseModel
        model: 'roles'

        attributes:
            code: null # Кодовое название роли

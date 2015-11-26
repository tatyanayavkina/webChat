#CoreModule#
'use strict';

CoreModule = angular.module 'CoreModule', [
    'ui.router'
];

CoreModule.constant 'config',
    api: 'http://localhost:8009/api/'

CoreModule.constant 'roles',
    guest: 'ROLE_GUEST'
    user : 'ROLE_USER'
    admin: 'ROLE_ADMIN'


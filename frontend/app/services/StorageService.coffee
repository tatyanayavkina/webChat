###
#Storage#
Сервис-хранилище данных.
###

'use strict';

CoreModule.service 'Storage', ($window) ->
    class Storage
        @storage: null;

        # конструктор
        # -----------
        # `type` - тип хранилища ('sessionStorage', 'localStorage')
        constructor: (type) ->
            @storage = $window[type];


        # сохранение значения
        # -------------------
        setItem    : (index, value) ->
            if typeof value == 'object'
                value = angular.toJson(value);

            @storage.setItem(index, value);


        # получение значения
        # ------------------
        getItem    : (index) ->
            value = @storage.getItem(index);
            try
                value = angular.fromJson(value)
            catch error
            return value;


        # удаляем значение
        # ----------------
        removeItem : (index) ->
            @storage.removeItem(index);


        # очистка хранилища
        # -----------------
        clear      : () ->
            @storage.clear();


    storage = new Storage('localStorage');
    return storage;
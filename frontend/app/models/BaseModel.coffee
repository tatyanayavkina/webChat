# BaseModel - базовый класс для всех моделей

'use strict';

CoreModule.factory 'BaseModel', ($q, $window, $timeout, $injector, config, Relation) ->
    class BaseModel
        model: null

        isNewRecord: null

        attributes: null
        relations : null

        attributeLabels: {}
        validationRules: {}

        constructor : (params = {}) ->
            @isNewRecord = true;
            attributes = {};
            angular.forEach(params, (value, title) =>
                if !@[title] then attributes[title] = value;
            )
            angular.extend(@, @attributes, attributes);

        transform : (object, relations) ->
            self = new @constructor(object);
            self.isNewRecord = false;

            self.askedRelations = relations;
            self._transformRelations(relations);

            self;


        _transformRelations : (relations) ->
            if relations && relations.length > 0
                angular.forEach(relations, (relation) =>
                    Relation.load(@, relation);
                )

        # Получение лейбла атрибута
        #
        # @static
        # @param    [String]    attribute   Название атрибута
        #
        # @return   [String]
        @getAttributeLabel: (attribute) -> @:: attributeLabels[attribute];
        getAttributeLabel : (attribute) -> @attributeLabels[attribute];

        # Валидация модели
        #
        # @return   [Boolean]
        validate          : (scenario) ->
            isValid = Validator.check(@, scenario);
            @errors = Validator.errors;
            isValid;

        # Проверка на обязательность атрибута
        #
        # @param    [String]    attribute   Атрибут, обязательность которого надо проверить
        #
        # @return   [Boolean]
        isRequired        : (attribute) -> @validators && @validators[attribute] && @validators[attribute].isRequired;

        @find : (params, relations = []) ->
            deferred = $q.defer();

            ID = params.id;
            delete params.id;

            HttpAuth.get(config.api + @:: model + '/' + ID).then(
                (response) =>
                    deferred.resolve(@:: transform(response.data, relations));
                (response) =>
                    console.error(response);
                    deferred.reject(response);
            )

            deferred.promise;

        @findAll : (params = {}, relations = []) ->
            deferred = $q.defer();
            params.relations = angular.toJson(relations);

            params = @::expandRelations(params,relations);

            HttpAuth.get(config.api + @:: model, params: params).then(
                (response) =>
                    result = [];
                    response.data.forEach((item) =>
                        result.push(@:: transform(item, relations));
                    )
                    deferred.resolve(result);
                (response) =>
                    console.error(response);
                    deferred.reject(response);
            )

            deferred.promise;


        @search : (params, relations = []) ->
            deferred = $q.defer();
            url = @:: _getLink(params);

            params = @::expandRelations(params,relations);

            HttpAuth.get(url, params: params).then(
                (response) =>
                    result = [];
                    response.data.forEach((item) =>
                        result.push(@:: transform(item, relations));
                    )
                    deferred.resolve(result);
                (response) =>
                    console.error(response);
                    deferred.reject(response);
            )

            deferred.promise;

        # перед сохранением объекта - нужно почистить связки от связок
        beforeSave: () ->
            data = angular.copy(@);
            # бегаем по связкам объектам
            angular.forEach(data.relations, (relation, name) ->
                # если связка есть
                if data[name]
                    # бегаем по связкам связки и удаляем их, если они есть
                    angular.forEach(data[name].relations, (rel, relName) ->
                        if data[name][relName]
                            data[name][relName] = null;
                    )
            )

        save : (params) ->
            if @isNewRecord then @create(params) else @update(params);


        create : (params) ->
            deferred = $q.defer();
            HttpAuth.post(config.api + @model, @beforeSave(), params: params).then(
                (response) =>
                    deferred.resolve(@transform(response, @askedRelations));
                (response) =>
                    console.error(response);
                    deferred.reject(response);
            )

            deferred.promise;

        update : (params) ->
            deferred = $q.defer();

            HttpAuth.put(config.api + @model + '/' + @id, @beforeSave(), params: params).then(
                (response) =>
                    if response.data
                        deferred.resolve(@transform(response.data, @askedRelations));
                    else
                        deferred.resolve({});
                (response) =>
                    console.error(response);
                    deferred.reject(response);
            )

            deferred.promise;

        delete : (params) ->
            @deleted = true;
            @update(params);


        remove : (params) ->
            deferred = $q.defer();

            HttpAuth.delete(config.api + @model + '/' + @id, params: params).then(
                () =>
                    deferred.resolve(null);
                (response) =>
                    console.error(response);
                    deferred.reject(response);
            )

            deferred.promise;










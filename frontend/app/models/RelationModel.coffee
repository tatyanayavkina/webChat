#Relation#

'use strict';

CoreModule.factory 'Relation', ($injector) ->
    class Relation

        @load: (model, relationToLoad) ->
            name = relationToLoad.name;
            relation = model.relations[relationToLoad.name];
            if relation
                relation.field ?= relationToLoad.name;

                if relation.type in ['hasMany', 'manyMany']
                    model[name] = @loadMany(model, relation, relationToLoad.children)
                else
                    model[name] = @loadOne(model, relation, relationToLoad.children);


        @loadMany: (model, relation, children) ->
            relatedModel = $injector.get(relation.model);
            result = [];
            if model[relation.field]
                model[relation.field].forEach((item) ->
                    result.push(relatedModel::transform(item, children));
                )
            result;


        @loadOne: (model, relation, children) ->
            relatedModel = $injector.get(relation.model);
            result = null;
            if model[relation.field]
                result = relatedModel::transform(model[relation.field], children);
            result;

package io.github.blackfishlabs.forza.core.infra.converter;

import com.google.common.collect.Lists;
import io.github.blackfishlabs.forza.core.infra.json.Json;
import io.github.blackfishlabs.forza.core.infra.model.BasicEntity;

import java.util.List;

public abstract class Converter<J extends Json, M extends BasicEntity> {

    public abstract J convertFrom(M model);

    public abstract M convertFrom(J json);

    public List<J> convertListModelFrom(final List<M> models) {
        List<J> jsons = Lists.newArrayList();

        for (M model : models) {
            jsons.add(convertFrom(model));
        }

        return jsons;
    }

    public List<M> convertListJsonFrom(final List<J> jsons) {
        List<M> models = Lists.newArrayList();

        for (J json : jsons) {
            models.add(convertFrom(json));
        }

        return models;
    }
}
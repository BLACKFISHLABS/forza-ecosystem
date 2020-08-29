package io.github.blackfishlabs.forza.core.application.address.converter;

import io.github.blackfishlabs.forza.core.application.address.json.StateJson;
import io.github.blackfishlabs.forza.core.application.address.model.StateEntity;
import io.github.blackfishlabs.forza.core.infra.converter.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StateConverter extends Converter<StateJson, StateEntity> {

    @Autowired
    private StateConverter stateConverter;

    @Override
    public StateJson convertFrom(StateEntity entity) {
        StateJson json = new StateJson();

        json.setId(entity.getId());
        json.setAcronym(entity.getAcronym());
        json.setCode(String.valueOf(entity.getCode()));
        json.setState(entity.getState());

        return json;
    }

    @Override
    public StateEntity convertFrom(StateJson json) {
        StateEntity entity = new StateEntity();

        entity.setId(json.getId());
        entity.setAcronym(json.getAcronym());
        entity.setCode(Integer.valueOf(String.valueOf(json.getId())));
        entity.setState(json.getState());

        return entity;
    }
}

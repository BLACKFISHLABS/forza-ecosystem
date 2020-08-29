package io.github.blackfishlabs.forza.core.application.address.converter;

import io.github.blackfishlabs.forza.core.application.address.json.CityJson;
import io.github.blackfishlabs.forza.core.application.address.model.CityEntity;
import io.github.blackfishlabs.forza.core.infra.converter.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CityConverter extends Converter<CityJson, CityEntity> {

    @Autowired
    private CityConverter cityConverter;

    @Autowired
    private StateConverter stateConverter;

    @Override
    public CityJson convertFrom(CityEntity entity) {
        CityJson json = new CityJson();

        json.setId(entity.getId());
        json.setCity(entity.getCity());
        json.setCode(String.valueOf(entity.getCode()));
        json.setStateJson(stateConverter.convertFrom(entity.getState()));

        return json;
    }

    @Override
    public CityEntity convertFrom(CityJson json) {
        CityEntity entity = new CityEntity();

        entity.setId(json.getId());
        entity.setCity(json.getCity());
        entity.setCode(Integer.parseInt(json.getCode()));
        entity.setState(stateConverter.convertFrom(json.getStateJson()));

        return entity;
    }
}

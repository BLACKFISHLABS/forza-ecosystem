package io.github.blackfishlabs.forza.core.application.address.converter;

import io.github.blackfishlabs.forza.core.application.address.json.AddressJson;
import io.github.blackfishlabs.forza.core.application.address.model.AddressEntity;
import io.github.blackfishlabs.forza.core.infra.converter.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AddressConverter extends Converter<AddressJson, AddressEntity> {

    @Autowired
    private CityConverter cityConverter;

    @Override
    public AddressJson convertFrom(AddressEntity entity) {
        AddressJson json = new AddressJson();

        json.setId(entity.getId());
        json.setStreet(entity.getStreet());
        json.setLocation(entity.getLocation());
        json.setNeighborhood(entity.getNeighborhood());
        json.setComplement(entity.getComplement());
        json.setCep(entity.getCep());
        json.setNumber(entity.getNumber());
        json.setCityJson(cityConverter.convertFrom(entity.getCity()));

        return json;
    }

    @Override
    public AddressEntity convertFrom(AddressJson json) {
        AddressEntity entity = new AddressEntity();

        entity.setId(json.getId());
        entity.setCep(json.getCep());
        entity.setCity(cityConverter.convertFrom(json.getCityJson()));
        entity.setComplement(json.getComplement());
        entity.setLocation(json.getLocation());
        entity.setNeighborhood(json.getNeighborhood());
        entity.setNumber(json.getNumber());
        entity.setStreet(json.getStreet());

        return entity;
    }

}

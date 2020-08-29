package io.github.blackfishlabs.forza.charge.converter;

import io.github.blackfishlabs.forza.charge.json.ChargeCustomerJson;
import io.github.blackfishlabs.forza.charge.model.ChargeCustomerEntity;
import io.github.blackfishlabs.forza.core.infra.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ChargeCustomerConverter extends Converter<ChargeCustomerJson, ChargeCustomerEntity> {

    @Override
    public ChargeCustomerJson convertFrom(ChargeCustomerEntity model) {
        ChargeCustomerJson json = new ChargeCustomerJson();

        json.setId(model.getId());
        json.setCode(model.getCode());
        json.setName(model.getName());
        json.setFantasyName(model.getFantasyName());
        json.setDocument(model.getDocument());
        json.setCity(model.getCity());
        json.setNeighborhood(model.getNeighborhood());
        json.setPostalCode(model.getPostalCode());
        json.setComplement(model.getComplement());
        json.setContact(model.getContact());
        json.setEmail(model.getEmail());
        json.setAddress(model.getAddress());
        json.setNumber(model.getNumber());
        json.setPhone(model.getPhone());
        json.setSecondaryPhone(model.getSecondaryPhone());
        json.setTypePerson(model.getTypePerson());

        return json;
    }

    @Override
    public ChargeCustomerEntity convertFrom(ChargeCustomerJson json) {
        ChargeCustomerEntity entity = new ChargeCustomerEntity();

        entity.setId(json.getId());
        entity.setCode(json.getCode());
        entity.setName(json.getName());
        entity.setFantasyName(json.getFantasyName());
        entity.setDocument(json.getDocument());
        entity.setCity(json.getCity());
        entity.setNeighborhood(json.getNeighborhood());
        entity.setPostalCode(json.getPostalCode());
        entity.setComplement(json.getComplement());
        entity.setContact(json.getContact());
        entity.setEmail(json.getEmail());
        entity.setAddress(json.getAddress());
        entity.setNumber(json.getNumber());
        entity.setPhone(json.getPhone());
        entity.setSecondaryPhone(json.getSecondaryPhone());
        entity.setTypePerson(json.getTypePerson());

        return entity;
    }
}

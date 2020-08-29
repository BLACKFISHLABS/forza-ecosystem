package io.github.blackfishlabs.forza.core.application.phone.converter;

import io.github.blackfishlabs.forza.core.application.phone.json.PhoneJson;
import io.github.blackfishlabs.forza.core.application.phone.model.PhoneEntity;
import io.github.blackfishlabs.forza.core.infra.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class PhoneConverter extends Converter<PhoneJson, PhoneEntity> {

    @Override
    public PhoneJson convertFrom(PhoneEntity entity) {
        PhoneJson json = new PhoneJson();

        json.setId(entity.getId());
        json.setPhoneNumber(entity.getPhoneNumber());
        json.setPhoneType(entity.getPhoneType());

        return json;
    }

    @Override
    public PhoneEntity convertFrom(PhoneJson json) {
        PhoneEntity entity = new PhoneEntity();

        entity.setId(json.getId());
        entity.setPhoneNumber(json.getPhoneNumber());
        entity.setPhoneType(json.getPhoneType());

        return entity;
    }
}

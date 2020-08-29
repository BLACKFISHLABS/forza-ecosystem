package io.github.blackfishlabs.forza.charge.converter;

import io.github.blackfishlabs.forza.charge.json.ChargeProductJson;
import io.github.blackfishlabs.forza.charge.model.ChargeProductEntity;
import io.github.blackfishlabs.forza.core.infra.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ChargeProductConverter extends Converter<ChargeProductJson, ChargeProductEntity> {

    @Override
    public ChargeProductJson convertFrom(ChargeProductEntity model) {
        ChargeProductJson json = new ChargeProductJson();

        json.setId(model.getId());
        json.setCode(model.getCode());
        json.setEan(model.getEan());
        json.setDescription(model.getDescription());
        json.setGroup(model.getGroup());
        json.setNote(model.getNote());
        json.setQnt(model.getQnt());
        json.setUnit(model.getUnit());

        return json;
    }

    @Override
    public ChargeProductEntity convertFrom(ChargeProductJson json) {
        ChargeProductEntity entity = new ChargeProductEntity();

        entity.setId(json.getId());
        entity.setCode(json.getCode());
        entity.setEan(json.getEan());
        entity.setDescription(json.getDescription());
        entity.setGroup(json.getGroup());
        entity.setNote(json.getNote());
        entity.setQnt(json.getQnt());
        entity.setUnit(json.getUnit());

        return entity;
    }
}

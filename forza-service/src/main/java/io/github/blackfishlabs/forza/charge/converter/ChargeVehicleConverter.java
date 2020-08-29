package io.github.blackfishlabs.forza.charge.converter;

import io.github.blackfishlabs.forza.charge.json.ChargeVehicleJson;
import io.github.blackfishlabs.forza.charge.model.ChargeVehicleEntity;
import io.github.blackfishlabs.forza.core.infra.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ChargeVehicleConverter extends Converter<ChargeVehicleJson, ChargeVehicleEntity> {

    @Override
    public ChargeVehicleJson convertFrom(ChargeVehicleEntity model) {
        ChargeVehicleJson json = new ChargeVehicleJson();

        json.setId(model.getId());
        json.setPlate(model.getPlate());
        json.setInitKm(model.getInitKm());
        json.setEndKm(model.getEndKm());

        return json;
    }

    @Override
    public ChargeVehicleEntity convertFrom(ChargeVehicleJson json) {
        ChargeVehicleEntity entity = new ChargeVehicleEntity();

        entity.setId(json.getId());
        entity.setPlate(json.getPlate());
        entity.setEndKm(json.getEndKm());
        entity.setInitKm(json.getInitKm());

        return entity;
    }
}

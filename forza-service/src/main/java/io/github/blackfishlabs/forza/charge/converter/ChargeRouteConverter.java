package io.github.blackfishlabs.forza.charge.converter;

import io.github.blackfishlabs.forza.charge.json.ChargeRouteJson;
import io.github.blackfishlabs.forza.charge.model.ChargeRouteEntity;
import io.github.blackfishlabs.forza.core.infra.converter.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ChargeRouteConverter extends Converter<ChargeRouteJson, ChargeRouteEntity> {

    @Autowired
    private ChargeCustomerConverter chargeCustomerConverter;

    @Override
    public ChargeRouteJson convertFrom(ChargeRouteEntity model) {
        ChargeRouteJson json = new ChargeRouteJson();

        json.setId(model.getId());
        json.setCode(model.getCode());
        json.setDescription(model.getDescription());
        json.setCustomers(chargeCustomerConverter.convertListModelFrom(model.getCustomers()));

        return json;
    }

    @Override
    public ChargeRouteEntity convertFrom(ChargeRouteJson json) {
        ChargeRouteEntity entity = new ChargeRouteEntity();

        entity.setId(json.getId());
        entity.setCode(json.getCode());
        entity.setDescription(json.getDescription());

        json.getCustomers().forEach(item -> entity.addItem(chargeCustomerConverter.convertFrom(item)));

        return entity;
    }
}

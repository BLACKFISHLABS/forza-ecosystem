package io.github.blackfishlabs.forza.charge.converter;

import io.github.blackfishlabs.forza.charge.json.ChargeTablePriceJson;
import io.github.blackfishlabs.forza.charge.model.ChargeTablePriceEntity;
import io.github.blackfishlabs.forza.core.infra.converter.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ChargePriceTableConverter extends Converter<ChargeTablePriceJson, ChargeTablePriceEntity> {

    @Autowired
    private ChargeItemConverter chargeItemConverter;

    @Override
    public ChargeTablePriceJson convertFrom(ChargeTablePriceEntity model) {
        ChargeTablePriceJson json = new ChargeTablePriceJson();

        json.setId(model.getId());
        json.setCode(model.getCode());
        json.setItems(chargeItemConverter.convertListModelFrom(model.getItems()));
        json.setName(model.getDescription());

        return json;
    }

    @Override
    public ChargeTablePriceEntity convertFrom(ChargeTablePriceJson json) {
        ChargeTablePriceEntity entity = new ChargeTablePriceEntity();

        entity.setId(json.getId());
        entity.setCode(json.getCode());
        json.getItems().forEach(item -> entity.addItem(chargeItemConverter.convertFrom(item)));
        entity.setDescription(json.getName());

        return entity;
    }
}

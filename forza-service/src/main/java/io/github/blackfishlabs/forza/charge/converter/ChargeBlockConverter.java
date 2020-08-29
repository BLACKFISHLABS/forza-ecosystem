package io.github.blackfishlabs.forza.charge.converter;

import io.github.blackfishlabs.forza.charge.json.ChargeBlockJson;
import io.github.blackfishlabs.forza.charge.model.ChargeBlockEntity;
import io.github.blackfishlabs.forza.core.infra.converter.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ChargeBlockConverter extends Converter<ChargeBlockJson, ChargeBlockEntity> {

    @Autowired
    private ChargePriceTableConverter chargePriceTableConverter;

    @Override
    public ChargeBlockJson convertFrom(ChargeBlockEntity model) {
        ChargeBlockJson json = new ChargeBlockJson();

        json.setId(model.getId());
        json.setTables(chargePriceTableConverter.convertListModelFrom(model.getTables()));

        return json;
    }

    @Override
    public ChargeBlockEntity convertFrom(ChargeBlockJson json) {
        ChargeBlockEntity entity = new ChargeBlockEntity();

        entity.setId(json.getId());
        json.getTables().forEach(item -> entity.addItem(chargePriceTableConverter.convertFrom(item)));

        return entity;
    }
}

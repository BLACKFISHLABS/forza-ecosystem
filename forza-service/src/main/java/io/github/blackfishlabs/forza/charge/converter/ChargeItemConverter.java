package io.github.blackfishlabs.forza.charge.converter;

import io.github.blackfishlabs.forza.charge.json.ChargeItemJson;
import io.github.blackfishlabs.forza.charge.model.ChargeItemEntity;
import io.github.blackfishlabs.forza.core.infra.converter.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ChargeItemConverter extends Converter<ChargeItemJson, ChargeItemEntity> {

    @Autowired
    private ChargeProductConverter productConverter;

    @Override
    public ChargeItemJson convertFrom(ChargeItemEntity model) {
        ChargeItemJson json = new ChargeItemJson();

        json.setId(model.getId());
        json.setPrice(model.getPrice().doubleValue());
        json.setProduct(productConverter.convertFrom(model.getProduct()));

        return json;
    }

    @Override
    public ChargeItemEntity convertFrom(ChargeItemJson json) {
        ChargeItemEntity entity = new ChargeItemEntity();

        entity.setId(json.getId());
        entity.setPrice(BigDecimal.valueOf(json.getPrice()));
        entity.setProduct(productConverter.convertFrom(json.getProduct()));

        return entity;
    }
}

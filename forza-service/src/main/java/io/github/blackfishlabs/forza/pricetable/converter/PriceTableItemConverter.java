package io.github.blackfishlabs.forza.pricetable.converter;

import io.github.blackfishlabs.forza.core.infra.converter.Converter;
import io.github.blackfishlabs.forza.pricetable.json.PriceTableItemJson;
import io.github.blackfishlabs.forza.pricetable.model.PriceTableItemEntity;
import io.github.blackfishlabs.forza.product.converter.ProductConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PriceTableItemConverter extends Converter<PriceTableItemJson, PriceTableItemEntity> {

    @Autowired
    private ProductConverter productConverter;

    @Override
    public PriceTableItemJson convertFrom(PriceTableItemEntity model) {
        PriceTableItemJson json = new PriceTableItemJson();

        json.setItemId(model.getId());
        json.setLastChangeTime(model.getLastChangeTime());
        json.setProduct(productConverter.convertFrom(model.getProduct()));
        json.setProductId(productConverter.convertFrom(model.getProduct()).getProductId());
        json.setSalesPrice(model.getSalesPrice());
        json.setStockQuantity(model.getStockQuantity());

        return json;
    }

    @Override
    public PriceTableItemEntity convertFrom(PriceTableItemJson json) {
        PriceTableItemEntity entity = new PriceTableItemEntity();

        entity.setId(json.getItemId());
        entity.setLastChangeTime(json.getLastChangeTime());
        entity.setProduct(productConverter.convertFrom(json.getProduct()));
        entity.setSalesPrice(json.getSalesPrice());
        entity.setStockQuantity(json.getStockQuantity());

        return entity;
    }
}

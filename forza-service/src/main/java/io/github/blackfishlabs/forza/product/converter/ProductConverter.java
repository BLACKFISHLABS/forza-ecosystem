package io.github.blackfishlabs.forza.product.converter;

import io.github.blackfishlabs.forza.core.application.company.converter.CompanyConverter;
import io.github.blackfishlabs.forza.core.infra.converter.Converter;
import io.github.blackfishlabs.forza.product.json.ProductJson;
import io.github.blackfishlabs.forza.product.model.ProductEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProductConverter extends Converter<ProductJson, ProductEntity> {

    @Autowired
    private CompanyConverter companyConverter;

    @Override
    public ProductJson convertFrom(ProductEntity model) {
        ProductJson json = new ProductJson();

        json.setActive(model.getActive());
        json.setBarCode(model.getBarCode());
        json.setCode(model.getCode());
        json.setDescription(model.getDescription());
        json.setGroup(model.getGroup());
        json.setLastChangeTime(model.getLastChangeTime());
        json.setObservation(model.getObservation());
        json.setProductId(model.getId());
        json.setUnit(model.getUnit());
        json.setCompanyJson(companyConverter.convertFrom(model.getOwner()));

        return json;
    }

    @Override
    public ProductEntity convertFrom(ProductJson json) {
        ProductEntity entity = new ProductEntity();

        entity.setActive(json.getActive());
        entity.setBarCode(json.getBarCode());
        entity.setCode(json.getCode());
        entity.setDescription(json.getDescription());
        entity.setGroup(json.getGroup());
        entity.setLastChangeTime(json.getLastChangeTime());
        entity.setObservation(json.getObservation());
        entity.setId(json.getProductId());
        entity.setUnit(json.getUnit());
        entity.setOwner(companyConverter.convertFrom(json.getCompanyJson()));

        return entity;
    }
}

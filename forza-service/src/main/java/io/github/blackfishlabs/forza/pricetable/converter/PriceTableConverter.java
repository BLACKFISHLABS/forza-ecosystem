package io.github.blackfishlabs.forza.pricetable.converter;

import io.github.blackfishlabs.forza.core.application.company.service.CompanyService;
import io.github.blackfishlabs.forza.core.infra.converter.Converter;
import io.github.blackfishlabs.forza.pricetable.json.PriceTableJson;
import io.github.blackfishlabs.forza.pricetable.model.PriceTableEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PriceTableConverter extends Converter<PriceTableJson, PriceTableEntity> {

    @Autowired
    private PriceTableItemConverter priceTableItemConverter;
    @Autowired
    private CompanyService companyService;

    @Override
    public PriceTableJson convertFrom(PriceTableEntity model) {
        PriceTableJson json = new PriceTableJson();

        json.setPriceTableId(model.getId());
        json.setActive(model.getActive());
        json.setCode(model.getCode());
        json.setLastChangeTime(model.getLastChangeTime());
        json.setItems(priceTableItemConverter.convertListModelFrom(model.getItems()));
        json.setCompanyId(model.getOwner().getId());
        json.setDescription(model.getName());

        return json;
    }

    @Override
    public PriceTableEntity convertFrom(PriceTableJson json) {
        PriceTableEntity entity = new PriceTableEntity();

        entity.setId(json.getPriceTableId());
        entity.setActive(json.getActive());
        entity.setCode(json.getCode());
        entity.setLastChangeTime(json.getLastChangeTime());

        json.getItems().forEach(item -> entity.addItem(priceTableItemConverter.convertFrom(item)));

        entity.setOwner(companyService.findOne(json.getCompanyId()));
        entity.setName(json.getDescription());

        return entity;
    }
}

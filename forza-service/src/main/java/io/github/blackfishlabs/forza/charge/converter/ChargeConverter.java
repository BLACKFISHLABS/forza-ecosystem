package io.github.blackfishlabs.forza.charge.converter;

import io.github.blackfishlabs.forza.charge.json.ChargeJson;
import io.github.blackfishlabs.forza.charge.model.ChargeEntity;
import io.github.blackfishlabs.forza.charge.model.ChargeStatus;
import io.github.blackfishlabs.forza.core.application.company.converter.CompanyConverter;
import io.github.blackfishlabs.forza.core.infra.converter.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ChargeConverter extends Converter<ChargeJson, ChargeEntity> {

    @Autowired
    private ChargeBlockConverter chargeBlockConverter;
    @Autowired
    private ChargeVehicleConverter chargeVehicleConverter;
    @Autowired
    private ChargeRouteConverter chargeRouteConverter;
    @Autowired
    private CompanyConverter companyConverter;

    @Override
    public ChargeJson convertFrom(ChargeEntity model) {
        ChargeJson json = new ChargeJson();

        json.setId(model.getId());
        json.setCode(model.getCode());
        json.setIssueDate(model.getIssueDate());
        json.setSalesmanCode(model.getSalesmanCode());
        json.setEmitterCode(model.getEmitterCode());
        json.setChargeBlock(chargeBlockConverter.convertFrom(model.getChargeBlock()));
        json.setVehicle(chargeVehicleConverter.convertFrom(model.getVehicle()));
        json.setRoutes(chargeRouteConverter.convertListModelFrom(model.getRoutes()));
        json.setCompanyJson(companyConverter.convertFrom(model.getOwner()));
        json.setStatus(model.getStatus().getOrdinalType());

        return json;
    }

    @Override
    public ChargeEntity convertFrom(ChargeJson json) {
        ChargeEntity entity = new ChargeEntity();

        entity.setId(json.getId());
        entity.setCode(json.getCode());
        entity.setIssueDate(json.getIssueDate());
        entity.setSalesmanCode(json.getSalesmanCode());
        entity.setEmitterCode(json.getEmitterCode());
        entity.setChargeBlock(chargeBlockConverter.convertFrom(json.getChargeBlock()));
        entity.setVehicle(chargeVehicleConverter.convertFrom(json.getVehicle()));

        json.getRoutes().forEach(item -> entity.addItem(chargeRouteConverter.convertFrom(item)));

        entity.setOwner(companyConverter.convertFrom(json.getCompanyJson()));
        entity.setStatus(ChargeStatus.valueOf(json.getStatus()));

        return entity;
    }
}

package io.github.blackfishlabs.forza.salesman.converter;

import io.github.blackfishlabs.forza.core.application.company.converter.CompanyConverter;
import io.github.blackfishlabs.forza.core.infra.converter.Converter;
import io.github.blackfishlabs.forza.salesman.json.Salesman;
import io.github.blackfishlabs.forza.salesman.model.SalesmanEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SalesmanConverter extends Converter<Salesman, SalesmanEntity> {

    @Autowired
    private CompanyConverter companyConverter;

    @Override
    public Salesman convertFrom(SalesmanEntity model) {
        Salesman json = new Salesman();

        json.setActive(model.getActive());
        json.setCanApplyDiscount(model.getCanApplyDiscount());
        json.setCode(model.getCode());
        json.setCompanies(companyConverter.convertListModelFrom(model.getCompanies()));
        json.setCpfOrCnpj(model.getCpfOrCnpj());
        json.setEmail(model.getEmail());
        json.setLastChangeTime(model.getLastChangeTime());
        json.setName(model.getName());
        json.setPhoneNumber(model.getPhoneNumber());
        json.setSalesmanId(model.getId());
        json.setPassword(model.getPassword());

        return json;
    }

    @Override
    public SalesmanEntity convertFrom(Salesman json) {
        SalesmanEntity entity = new SalesmanEntity();

        entity.setActive(json.getActive());
        entity.setCanApplyDiscount(json.getCanApplyDiscount());
        entity.setCode(json.getCode());
        entity.setCompanies(companyConverter.convertListJsonFrom(json.getCompanies()));
        entity.setCpfOrCnpj(json.getCpfOrCnpj());
        entity.setEmail(json.getEmail());
        entity.setLastChangeTime(json.getLastChangeTime());
        entity.setName(json.getName());
        entity.setPhoneNumber(json.getPhoneNumber());
        entity.setId(json.getSalesmanId());
        entity.setPassword(json.getPassword());

        return entity;
    }
}

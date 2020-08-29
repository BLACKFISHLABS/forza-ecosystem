package io.github.blackfishlabs.forza.core.application.company.converter;

import io.github.blackfishlabs.forza.core.application.address.converter.AddressConverter;
import io.github.blackfishlabs.forza.core.application.company.json.CompanyJson;
import io.github.blackfishlabs.forza.core.application.company.model.CompanyEntity;
import io.github.blackfishlabs.forza.core.application.contact.converter.ContactConverter;
import io.github.blackfishlabs.forza.core.infra.converter.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CompanyConverter extends Converter<CompanyJson, CompanyEntity> {

    @Autowired
    private ContactConverter contactConverter;

    @Autowired
    private AddressConverter addressConverter;

    @Override
    public CompanyJson convertFrom(CompanyEntity entity) {
        CompanyJson json = new CompanyJson();

        json.setId(entity.getId());
        json.setCnpj(entity.getCnpj());
        json.setCompanyName(entity.getCompanyName());
        json.setFantasyName(entity.getFantasyName());
        json.setCompanyType(entity.getCompanyType());
        json.setAddressJson(addressConverter.convertFrom(entity.getAddress()));
        json.setContactJson(contactConverter.convertFrom(entity.getContact()));
        json.setPriceTableId(entity.getPriceTableId());

        return json;
    }

    @Override
    public CompanyEntity convertFrom(CompanyJson json) {
        CompanyEntity entity = new CompanyEntity();

        entity.setId(json.getId());
        entity.setCnpj(json.getCnpj());
        entity.setCompanyName(json.getCompanyName());
        entity.setFantasyName(json.getFantasyName());
        entity.setCompanyType(json.getCompanyType());
        entity.setAddress(addressConverter.convertFrom(json.getAddressJson()));
        entity.setContact(contactConverter.convertFrom(json.getContactJson()));
        entity.setPriceTableId(json.getPriceTableId());

        return entity;
    }
}

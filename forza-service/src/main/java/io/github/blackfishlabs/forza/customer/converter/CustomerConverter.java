package io.github.blackfishlabs.forza.customer.converter;

import io.github.blackfishlabs.forza.core.application.address.converter.CityConverter;
import io.github.blackfishlabs.forza.core.application.company.converter.CompanyConverter;
import io.github.blackfishlabs.forza.core.application.enumeration.TypeOfPerson;
import io.github.blackfishlabs.forza.core.infra.converter.Converter;
import io.github.blackfishlabs.forza.customer.json.CustomerJson;
import io.github.blackfishlabs.forza.customer.model.CustomerEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static java.util.Objects.nonNull;

@Component
public class CustomerConverter extends Converter<CustomerJson, CustomerEntity> {

    @Autowired
    private CityConverter cityConverter;
    @Autowired
    private CompanyConverter companyConverter;

    @Override
    public CustomerJson convertFrom(CustomerEntity model) {
        CustomerJson json = new CustomerJson();

        json.setId(model.getId());
        json.setCustomerId(model.getId());
        json.setCode(model.getCode());
        json.setName(model.getName());
        json.setFantasyName(model.getFantasyName());
        json.setType(model.getTypeOfPerson().getOrdinalType());
        json.setCpfOrCnpj(model.getCpfOrCnpj());
        json.setContact(model.getContact());
        json.setEmail(model.getEmail());
        json.setMainPhone(model.getMainPhone());
        json.setSecondaryPhone(model.getSecondaryPhone());
        json.setAddress(model.getAddress());
        json.setPostalCode(model.getPostalCode());
        json.setNeighborhood(model.getDistrict());
        json.setAddressNumber(model.getAddressNumber());
        json.setAddressComplement(model.getAddressComplement());
        if (nonNull(model.getPriceTableId()))
            json.setDefaultPriceTable(String.valueOf(model.getPriceTableId()));
        json.setCityJson(cityConverter.convertFrom(model.getCity()));
        json.setActive(model.getActive());
        json.setLastChangeTime(model.getLastChangeTime());
        json.setCompanyJson(companyConverter.convertFrom(model.getOwner()));
        json.setBuyer(model.getBuyer());
        json.setIe(model.getIe());

        return json;
    }

    @Override
    public CustomerEntity convertFrom(CustomerJson json) {
        CustomerEntity entity = new CustomerEntity();

        entity.setId(json.getId());
        entity.setCode(json.getCode());
        entity.setName(json.getName());
        entity.setFantasyName(json.getFantasyName());
        entity.setTypeOfPerson(TypeOfPerson.valueOf(json.getType()));
        entity.setCpfOrCnpj(json.getCpfOrCnpj());
        entity.setContact(json.getContact());
        entity.setEmail(json.getEmail());
        entity.setMainPhone(json.getMainPhone());
        entity.setSecondaryPhone(json.getSecondaryPhone());
        entity.setAddress(json.getAddress());
        entity.setPostalCode(json.getPostalCode());
        entity.setDistrict(json.getNeighborhood());
        entity.setAddressNumber(json.getAddressNumber());
        entity.setAddressComplement(json.getAddressComplement());
        if (nonNull(json.getDefaultPriceTable()))
            entity.setPriceTableId(Integer.parseInt(json.getDefaultPriceTable()));
        entity.setCity(cityConverter.convertFrom(json.getCityJson()));
        entity.setActive(json.getActive());
        entity.setLastChangeTime(json.getLastChangeTime());
        entity.setOwner(companyConverter.convertFrom(json.getCompanyJson()));
        entity.setBuyer(json.getBuyer());
        entity.setIe(json.getIe());

        return entity;
    }
}

package io.github.blackfishlabs.forza.customer.converter;

import io.github.blackfishlabs.forza.core.application.company.converter.CompanyConverter;
import io.github.blackfishlabs.forza.core.infra.converter.Converter;
import io.github.blackfishlabs.forza.customer.json.RouteJson;
import io.github.blackfishlabs.forza.customer.model.RouteEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RouteConverter extends Converter<RouteJson, RouteEntity> {

    @Autowired
    private RouteCustomerConverter routeCustomerConverter;
    @Autowired
    private CompanyConverter companyConverter;

    @Override
    public RouteJson convertFrom(RouteEntity model) {
        RouteJson json = new RouteJson();

        json.setRouteId(model.getId());
        json.setActive(model.getActive());
        json.setCode(model.getCode());
        json.setLastChangeTime(model.getLastChangeTime());
        json.setCustomers(routeCustomerConverter.convertListModelFrom(model.getCustomers()));
        json.setCompanyJson(companyConverter.convertFrom(model.getOwner()));
        json.setDescription(model.getName());

        return json;
    }

    @Override
    public RouteEntity convertFrom(RouteJson json) {
        RouteEntity entity = new RouteEntity();

        entity.setId(json.getRouteId());
        entity.setActive(json.getActive());
        entity.setCode(json.getCode());
        entity.setLastChangeTime(json.getLastChangeTime());

        json.getCustomers().forEach(customer -> entity.addItem(routeCustomerConverter.convertFrom(customer)));

        entity.setOwner(companyConverter.convertFrom(json.getCompanyJson()));
        entity.setName(json.getDescription());

        return entity;
    }
}

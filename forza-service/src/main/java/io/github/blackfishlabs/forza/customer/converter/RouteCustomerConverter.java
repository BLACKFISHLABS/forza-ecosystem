package io.github.blackfishlabs.forza.customer.converter;

import io.github.blackfishlabs.forza.core.infra.converter.Converter;
import io.github.blackfishlabs.forza.customer.json.RouteCustomerJson;
import io.github.blackfishlabs.forza.customer.model.RouteCustomerEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RouteCustomerConverter extends Converter<RouteCustomerJson, RouteCustomerEntity> {

    @Autowired
    private CustomerConverter customerConverter;

    @Override
    public RouteCustomerJson convertFrom(RouteCustomerEntity model) {
        RouteCustomerJson json = new RouteCustomerJson();

        json.setRouteCustomerId(model.getId());
        json.setLastChangeTime(model.getLastChangeTime());
        json.setCustomer(customerConverter.convertFrom(model.getCustomer()));
        json.setCustomerId(customerConverter.convertFrom(model.getCustomer()).getCustomerId());

        return json;
    }

    @Override
    public RouteCustomerEntity convertFrom(RouteCustomerJson json) {
        RouteCustomerEntity entity = new RouteCustomerEntity();

        entity.setId(json.getRouteCustomerId());
        entity.setLastChangeTime(json.getLastChangeTime());
        entity.setCustomer(customerConverter.convertFrom(json.getCustomer()));

        return entity;
    }
}

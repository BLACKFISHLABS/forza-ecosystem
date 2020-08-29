package io.github.blackfishlabs.forza.order.converter;

import io.github.blackfishlabs.forza.core.infra.converter.Converter;
import io.github.blackfishlabs.forza.order.json.OrderItemJson;
import io.github.blackfishlabs.forza.order.model.OrderItemEntity;
import io.github.blackfishlabs.forza.order.repository.OrderRepository;
import io.github.blackfishlabs.forza.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderItemConverter extends Converter<OrderItemJson, OrderItemEntity> {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductService productService;

    @Override
    public OrderItemJson convertFrom(OrderItemEntity model) {
        OrderItemJson json = new OrderItemJson();

        json.setId(model.getId());
        json.setProductId(model.getProductId());
        json.setQuantity(model.getQuantity());
        json.setSubTotal(model.getSubTotal());
        json.setSalesPrice(model.getSalesPrice());
        json.setLastChangeTime(model.getLastChangeTime());
        json.setProduct(productService.findOne(model.getProductId()).getDescription());
        json.setUnitCode(model.getUnitCode());
        json.setProductCode(model.getProductCode());
        json.setOrderCode(model.getOrderCode());

        return json;
    }

    @Override
    public OrderItemEntity convertFrom(OrderItemJson json) {
        OrderItemEntity entity = new OrderItemEntity();

        entity.setId(json.getId());
        entity.setProductId(json.getProductId());
        entity.setQuantity(json.getQuantity());
        entity.setSubTotal(json.getSubTotal());
        entity.setSalesPrice(json.getSalesPrice());
        entity.setLastChangeTime(json.getLastChangeTime());
        entity.setUnitCode(productService.findOne(json.getProductId()).getUnit());
        entity.setProductCode(productService.findOne(json.getProductId()).getCode());

        return entity;
    }
}

package io.github.blackfishlabs.forza.data.order;

import io.github.blackfishlabs.forza.data.LocalDataInjector;
import io.github.blackfishlabs.forza.data.realm.RealmMapper;
import io.github.blackfishlabs.forza.domain.entity.OrderEntity;
import io.github.blackfishlabs.forza.domain.entity.OrderItemEntity;
import io.github.blackfishlabs.forza.domain.pojo.Order;
import io.realm.RealmList;

public class OrderRealmMapper extends RealmMapper<Order, OrderEntity> {
    @Override
    public OrderEntity toEntity(final Order object) {
        return new OrderEntity()
                .withId(object.getId())
                .withOrderId(object.getOrderId())
                .withType(object.getType())
                .withIssueDate(object.getIssueDate())
                .withDiscount(object.getDiscount())
                .withDiscountPercentage(object.getDiscountPercentage())
                .withObservation(object.getObservation())
                .withCustomer(LocalDataInjector.customerMapper().toEntity(object.getCustomer()))
                .withPaymentMethod(LocalDataInjector.paymentMethodMapper().toEntity(object.getPaymentMethod()))
                .withPriceTable(LocalDataInjector.priceTableMapper().toEntity(object.getPriceTable()))
                .withItems((RealmList<OrderItemEntity>) LocalDataInjector.orderItemMapper().toEntities(object.getItems()))
                .withLastChangeTime(object.getLastChangeTime())
                .withSalesmanId(object.getSalesmanId())
                .withCompanyId(object.getCompanyId())
                .withStatus(object.getStatus());
    }

    @Override
    public Order toViewObject(final OrderEntity entity) {
        return new Order()
                .withId(entity.getId())
                .withOrderId(entity.getOrderId())
                .withType(entity.getType())
                .withIssueDate(entity.getIssueDate())
                .withDiscount(entity.getDiscount())
                .withDiscountPercentage(entity.getDiscountPercentage())
                .withObservation(entity.getObservation())
                .withCustomer(LocalDataInjector.customerMapper().toViewObject(entity.getCustomer()))
                .withPaymentMethod(LocalDataInjector.paymentMethodMapper().toViewObject(entity.getPaymentMethod()))
                .withPriceTable(LocalDataInjector.priceTableMapper().toViewObject(entity.getPriceTable()))
                .withItems(LocalDataInjector.orderItemMapper().toViewObjects(entity.getItems()))
                .withLastChangeTime(entity.getLastChangeTime())
                .withSalesmanId(entity.getSalesmanId())
                .withCompanyId(entity.getCompanyId())
                .withStatus(entity.getStatus());
    }
}

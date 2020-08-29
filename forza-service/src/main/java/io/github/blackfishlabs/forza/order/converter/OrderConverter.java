package io.github.blackfishlabs.forza.order.converter;

import io.github.blackfishlabs.forza.core.application.company.converter.CompanyConverter;
import io.github.blackfishlabs.forza.core.infra.converter.Converter;
import io.github.blackfishlabs.forza.customer.model.CustomerEntity;
import io.github.blackfishlabs.forza.customer.service.CustomerService;
import io.github.blackfishlabs.forza.order.json.OrderJson;
import io.github.blackfishlabs.forza.order.model.OrderEntity;
import io.github.blackfishlabs.forza.order.model.OrderStatus;
import io.github.blackfishlabs.forza.order.model.OrderType;
import io.github.blackfishlabs.forza.order.service.OrderService;
import io.github.blackfishlabs.forza.payment.model.PaymentEntity;
import io.github.blackfishlabs.forza.payment.service.PaymentService;
import io.github.blackfishlabs.forza.salesman.converter.SalesmanConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.google.common.base.Strings.isNullOrEmpty;

@Component
public class OrderConverter extends Converter<OrderJson, OrderEntity> {

    @Autowired
    private OrderItemConverter orderItemConverter;
    @Autowired
    private CompanyConverter companyConverter;
    @Autowired
    private SalesmanConverter salesmanConverter;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private OrderService orderService;

    @Override
    public OrderJson convertFrom(OrderEntity model) {
        OrderJson json = new OrderJson();

        CustomerEntity customer = customerService.findOne(model.getCustomerId());
        PaymentEntity payment = paymentService.findOne(model.getPaymentMethodId());

        json.setId(model.getId());
        json.setOrderId(model.getId());
        json.setType(model.getType().getOrdinalType());
        json.setIssueDate(model.getIssueDate());
        json.setDiscount(model.getDiscount());
        json.setDiscountPercentage(model.getDiscountPercentage());
        json.setObservation(model.getObservation());
        json.setCustomerId(model.getCustomerId());
        json.setPaymentMethodId(model.getPaymentMethodId());
        json.setPriceTableId(model.getPriceTableId());
        json.setLastChangeTime(model.getLastChangeTime());
        json.setStatus(model.getStatus().getOrdinalType());
        json.setItems(orderItemConverter.convertListModelFrom(model.getItems()));
        json.setCompanyJson(companyConverter.convertFrom(model.getOwner()));
        json.setSalesmanJson(salesmanConverter.convertFrom(model.getSalesman()));

        json.setCustomer(customer.getFantasyName());
        json.setCustomerCode(customer.getCode());
        json.setPayment(payment.getDescription());
        json.setTotal(orderService.calculateTotal(model.getId()));
        json.setResumeCode(model.getResumeCode());

        return json;
    }

    @Override
    public OrderEntity convertFrom(OrderJson json) {
        OrderEntity entity = new OrderEntity();

        CustomerEntity customer = customerService.findOne(json.getCustomerId());
        PaymentEntity payment = paymentService.findOne(json.getPaymentMethodId());

        entity.setId(json.getId());
        entity.setOrderId(json.getId());
        entity.setType(OrderType.valueOf(json.getType()));
        entity.setIssueDate(json.getIssueDate());
        entity.setDiscount(json.getDiscount());
        entity.setDiscountPercentage(json.getDiscountPercentage());
        entity.setObservation(json.getObservation());
        entity.setCustomerId(json.getCustomerId());
        entity.setPaymentMethodId(json.getPaymentMethodId());
        entity.setPriceTableId(json.getPriceTableId());
        entity.setLastChangeTime(json.getLastChangeTime());
        entity.setStatus(OrderStatus.valueOf(json.getStatus()));
        json.getItems().forEach(item -> entity.addItem(orderItemConverter.convertFrom(item)));
        entity.setOwner(companyConverter.convertFrom(json.getCompanyJson()));
        entity.setSalesman(salesmanConverter.convertFrom(json.getSalesmanJson()));

        entity.setCustomerCode(customer.getCode());
        entity.setPaymentCode(payment.getCode());
        entity.setResumeCode(isNullOrEmpty(json.getResumeCode()) ? "-" : json.getResumeCode());
        entity.setTotal(json.getTotal());

        return entity;
    }
}

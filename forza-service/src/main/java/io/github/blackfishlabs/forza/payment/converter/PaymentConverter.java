package io.github.blackfishlabs.forza.payment.converter;

import io.github.blackfishlabs.forza.core.application.company.service.CompanyService;
import io.github.blackfishlabs.forza.core.infra.converter.Converter;
import io.github.blackfishlabs.forza.payment.json.PaymentJson;
import io.github.blackfishlabs.forza.payment.model.PaymentEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PaymentConverter extends Converter<PaymentJson, PaymentEntity> {

    @Autowired
    private CompanyService companyService;

    @Override
    public PaymentJson convertFrom(PaymentEntity model) {
        PaymentJson json = new PaymentJson();

        json.setActive(model.getActive());
        json.setCode(model.getCode());
        json.setCompanyId(model.getOwner().getId());
        json.setDescription(model.getDescription());
        json.setDiscountPercentage(model.getDiscountPercentage());
        json.setLastChangeTime(model.getLastChangeTime());
        json.setPaymentMethodId(model.getId());

        return json;
    }

    @Override
    public PaymentEntity convertFrom(PaymentJson json) {
        PaymentEntity entity = new PaymentEntity();

        entity.setActive(json.getActive());
        entity.setCode(json.getCode());
        entity.setOwner(companyService.findOne(json.getCompanyId()));
        entity.setDescription(json.getDescription());
        entity.setDiscountPercentage(json.getDiscountPercentage());
        entity.setLastChangeTime(json.getLastChangeTime());
        entity.setId(json.getPaymentMethodId());

        return entity;
    }
}

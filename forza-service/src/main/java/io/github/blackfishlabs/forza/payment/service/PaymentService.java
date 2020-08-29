package io.github.blackfishlabs.forza.payment.service;

import io.github.blackfishlabs.forza.core.infra.service.GenericService;
import io.github.blackfishlabs.forza.payment.model.PaymentEntity;
import io.github.blackfishlabs.forza.payment.repository.PaymentRepository;
import io.github.blackfishlabs.forza.payment.repository.PaymentSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentService extends GenericService<PaymentEntity, Long> {

    @Autowired
    private PaymentRepository paymentRepository;

    public List<PaymentEntity> findPaymentByCompany(String cnpj) {
        return paymentRepository.findAll(PaymentSpecification.findByCompany(cnpj));
    }

    public List<PaymentEntity> findPaymentByCompanyAndLastUpdate(String cnpj, String lastUpdateTime) {
        return paymentRepository.findAll(PaymentSpecification.findByCompanyAndLastUpdate(cnpj, lastUpdateTime));
    }

    public PaymentEntity findOne(Long id) {
        return paymentRepository.getOne(id);
    }
}

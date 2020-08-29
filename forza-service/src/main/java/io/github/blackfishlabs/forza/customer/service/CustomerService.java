package io.github.blackfishlabs.forza.customer.service;

import com.google.common.base.Strings;
import io.github.blackfishlabs.forza.core.application.search.FilterSearchParam;
import io.github.blackfishlabs.forza.core.helper.FormattingUtils;
import io.github.blackfishlabs.forza.core.infra.service.GenericService;
import io.github.blackfishlabs.forza.customer.model.CustomerEntity;
import io.github.blackfishlabs.forza.customer.repository.CustomerRepository;
import io.github.blackfishlabs.forza.customer.repository.CustomerSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService extends GenericService<CustomerEntity, Long> {

    @Autowired
    private CustomerRepository customerRepository;

    public List<CustomerEntity> findCustomerByCompany(String cnpj) {
        return customerRepository.findAll(CustomerSpecification.findByCompany(cnpj));
    }

    public List<CustomerEntity> findCustomerByCompanyAndCharge(String cnpj, String code) {
        return customerRepository.findByCompanyAndCharge(FormattingUtils.formatCpforCnpj(cnpj), code);
    }

    public List<CustomerEntity> findCustomerByCompanyAndLastUpdate(String cnpj, String lastUpdateTime) {
        return customerRepository.findAll(CustomerSpecification.findByCompanyAndLastUpdate(cnpj, lastUpdateTime));
    }

    public CustomerEntity findOne(Long id) {
        return customerRepository.getOne(id);
    }

    public List<CustomerEntity> search(FilterSearchParam filter, String cnpj) {
        if (!Strings.isNullOrEmpty(filter.getDescription()))
            return findByParamAndCompany(filter.getDescription(), cnpj);
        else
            return customerRepository.findAll(CustomerSpecification.findByCompany(cnpj));
    }

    public List<CustomerEntity> findByParamAndCompany(String param, String cnpj) {
        return customerRepository.findAll(CustomerSpecification.findByParamAndCompany(param, cnpj));
    }
}

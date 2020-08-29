package io.github.blackfishlabs.forza.core.application.company.service;

import io.github.blackfishlabs.forza.core.application.company.model.CompanyEntity;
import io.github.blackfishlabs.forza.core.application.company.repository.CompanyRepository;
import io.github.blackfishlabs.forza.core.application.company.repository.CompanySpecification;
import io.github.blackfishlabs.forza.core.infra.service.GenericService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyService extends GenericService<CompanyEntity, Long> {

    @Autowired
    private CompanyRepository companyRepository;

    public CompanyEntity findOne(Long id) {
        return companyRepository.getOne(id);
    }

    public CompanyEntity findByCnpj(String cnpj) {
        List<CompanyEntity> all = companyRepository.findAll(CompanySpecification.findByCompany(cnpj));
        return all.isEmpty() ? null : all.iterator().next();
    }


}

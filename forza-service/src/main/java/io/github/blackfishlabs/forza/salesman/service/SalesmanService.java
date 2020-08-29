package io.github.blackfishlabs.forza.salesman.service;

import io.github.blackfishlabs.forza.core.infra.service.GenericService;
import io.github.blackfishlabs.forza.salesman.model.SalesmanEntity;
import io.github.blackfishlabs.forza.salesman.repository.SalesmanRepository;
import io.github.blackfishlabs.forza.salesman.repository.SalesmanSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SalesmanService extends GenericService<SalesmanEntity, Long> {

    @Autowired
    private SalesmanRepository salesmanRepository;

    public SalesmanEntity findSalesman(String cpfCnpj, String password) {
        List<SalesmanEntity> all = salesmanRepository.findAll(SalesmanSpecification.findByDocumentAndPassword(cpfCnpj, password));
        return all.isEmpty() ? null : all.iterator().next();
    }

    public SalesmanEntity findSalesman(String cpfCnpj) {
        List<SalesmanEntity> all = salesmanRepository.findAll(SalesmanSpecification.findByDocument(cpfCnpj));
        return all.isEmpty() ? null : all.iterator().next();
    }

    public List<SalesmanEntity> findSalesmanByCompany(String cnpj) {
        return salesmanRepository.findAll(SalesmanSpecification.findByCompany(cnpj));
    }

    public SalesmanEntity findOne(Long id) {
        return salesmanRepository.getOne(id);
    }

}

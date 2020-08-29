package io.github.blackfishlabs.forza.product.service;

import com.google.common.base.Strings;
import io.github.blackfishlabs.forza.core.application.search.FilterSearchParam;
import io.github.blackfishlabs.forza.core.infra.service.GenericService;
import io.github.blackfishlabs.forza.product.model.ProductEntity;
import io.github.blackfishlabs.forza.product.repository.ProductRepository;
import io.github.blackfishlabs.forza.product.repository.ProductSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService extends GenericService<ProductEntity, Long> {

    @Autowired
    private ProductRepository productRepository;

    public List<ProductEntity> findProductByCompany(String cnpj, String lastUpdateTime) {
        return productRepository.findAll(ProductSpecification.findByCompanyAndLastUpdate(cnpj, lastUpdateTime));
    }

    public List<ProductEntity> findProductByCompany(String cnpj) {
        return productRepository.findAll(ProductSpecification.findByCompany(cnpj));
    }

    public List<ProductEntity> search(FilterSearchParam filter, String cnpj) {
        if (!Strings.isNullOrEmpty(filter.getDescription()))
            return findByParamAndCompany(filter.getDescription(), cnpj);
        else
            return productRepository.findAll(ProductSpecification.findByCompany(cnpj));
    }

    public List<ProductEntity> findByParamAndCompany(String param, String cnpj) {
        return productRepository.findAll(ProductSpecification.findByParamAndCompany(param, cnpj));
    }

    public List<ProductEntity> findByCodeAndCompany(String code, String cnpj) {
        return productRepository.findAll(ProductSpecification.findByCodeAndCompany(code, cnpj));
    }

    public ProductEntity findOne(Long id) {
        return productRepository.getOne(id);
    }
}

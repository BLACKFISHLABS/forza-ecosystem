package io.github.blackfishlabs.forza.customer.service;

import com.google.common.base.Strings;
import io.github.blackfishlabs.forza.core.application.search.FilterSearchParam;
import io.github.blackfishlabs.forza.core.infra.service.GenericService;
import io.github.blackfishlabs.forza.customer.model.RouteEntity;
import io.github.blackfishlabs.forza.customer.repository.RouteRepository;
import io.github.blackfishlabs.forza.customer.repository.RouteSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RouteService extends GenericService<RouteEntity, Long> {

    @Autowired
    private RouteRepository routeRepository;

    public RouteEntity findOne(Long id) {
        return routeRepository.getOne(id);
    }

    public List<RouteEntity> search(FilterSearchParam filter, String cnpj) {
        if (!Strings.isNullOrEmpty(filter.getDescription()))
            return findByParamAndCompany(filter.getDescription(), cnpj);
        else
            return routeRepository.findAll(RouteSpecification.findByCompany(cnpj));
    }

    public List<RouteEntity> findByParamAndCompany(String param, String cnpj) {
        return routeRepository.findAll(RouteSpecification.findByParamAndCompany(param, cnpj));
    }
}

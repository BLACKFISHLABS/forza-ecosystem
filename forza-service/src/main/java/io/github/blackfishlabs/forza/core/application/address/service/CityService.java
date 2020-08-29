package io.github.blackfishlabs.forza.core.application.address.service;


import com.google.common.base.Strings;
import io.github.blackfishlabs.forza.core.application.address.model.CityEntity;
import io.github.blackfishlabs.forza.core.application.address.repository.CityRepository;
import io.github.blackfishlabs.forza.core.application.address.repository.CitySpecification;
import io.github.blackfishlabs.forza.core.application.search.FilterSearchParam;
import io.github.blackfishlabs.forza.core.infra.service.GenericService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CityService extends GenericService<CityEntity, Long> {

    @Autowired
    private CityRepository cityRepository;

    public List<CityEntity> search(FilterSearchParam filterSearchParam) {
        if (!Strings.isNullOrEmpty(filterSearchParam.getDescription()))
            return cityRepository.findAll(CitySpecification.findByName(filterSearchParam.getDescription()));
        else
            return cityRepository.findAll();
    }

    public CityEntity findByCode(Integer code) {
        return cityRepository.findByCode(code);
    }
}

package io.github.blackfishlabs.forza.core.application.address.service;

import com.google.common.base.Strings;
import io.github.blackfishlabs.forza.core.application.address.model.StateEntity;
import io.github.blackfishlabs.forza.core.application.address.repository.StateRepository;
import io.github.blackfishlabs.forza.core.application.address.repository.StateSpecification;
import io.github.blackfishlabs.forza.core.application.search.FilterSearchParam;
import io.github.blackfishlabs.forza.core.infra.service.GenericService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StateService extends GenericService<StateEntity, Long> {

    @Autowired
    private StateRepository stateRepository;

    public List<StateEntity> search(FilterSearchParam filterSearchParam) {
        if (!Strings.isNullOrEmpty(filterSearchParam.getDescription()))
            return stateRepository.findAll(StateSpecification.findByName(filterSearchParam.getDescription()));
        else
            return stateRepository.findAll();
    }
}

package io.github.blackfishlabs.forza.core.application.address.resource;

import io.github.blackfishlabs.forza.core.application.address.converter.StateConverter;
import io.github.blackfishlabs.forza.core.application.address.json.StateJson;
import io.github.blackfishlabs.forza.core.application.address.service.StateService;
import io.github.blackfishlabs.forza.core.application.search.FilterSearchParam;
import io.github.blackfishlabs.forza.core.helper.ResourcePaths;
import io.github.blackfishlabs.forza.core.infra.resource.APIMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(ResourcePaths.STATE_PATH)
public class StateResource implements APIMap {

    @Autowired
    private StateService stateService;

    @Autowired
    private StateConverter stateConverter;

    @RequestMapping(method = {RequestMethod.GET})
    public List<StateJson> getAll() {
        return stateConverter.convertListModelFrom(stateService.findAll());
    }

    @GetMapping("/search")
    public List<StateJson> search(FilterSearchParam filterSearchParam) {
        return stateConverter.convertListModelFrom(stateService.search(filterSearchParam));
    }

}

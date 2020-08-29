package io.github.blackfishlabs.forza.core.application.address.resource;

import io.github.blackfishlabs.forza.core.application.address.converter.CityConverter;
import io.github.blackfishlabs.forza.core.application.address.json.CityJson;
import io.github.blackfishlabs.forza.core.application.address.service.CityService;
import io.github.blackfishlabs.forza.core.application.search.FilterSearchParam;
import io.github.blackfishlabs.forza.core.helper.ResourcePaths;
import io.github.blackfishlabs.forza.core.infra.resource.APIMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(ResourcePaths.CITY_PATH)
public class CityResource implements APIMap {

    @Autowired
    private CityService cityService;

    @Autowired
    private CityConverter cityConverter;

    @GetMapping
    public List<CityJson> getAll() {
        return cityConverter.convertListModelFrom(cityService.findAll());
    }

    @GetMapping("/search")
    public List<CityJson> search(FilterSearchParam filterSearchParam) {
        return cityConverter.convertListModelFrom(cityService.search(filterSearchParam));
    }

}

package io.github.blackfishlabs.forza.customer.resource;

import io.github.blackfishlabs.forza.core.application.search.FilterSearchParam;
import io.github.blackfishlabs.forza.core.helper.ResourcePaths;
import io.github.blackfishlabs.forza.core.infra.resource.APIMap;
import io.github.blackfishlabs.forza.customer.converter.RouteConverter;
import io.github.blackfishlabs.forza.customer.json.RouteJson;
import io.github.blackfishlabs.forza.customer.service.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ResourcePaths.ROUTE_PATH)
public class RouteResource implements APIMap {

    @Autowired
    private RouteConverter routeConverter;
    @Autowired
    private RouteService routeService;

    @GetMapping("/{id}")
    public RouteJson findOne(@PathVariable Long id) {
        return routeConverter.convertFrom(routeService.findOne(id));
    }

    @PutMapping
    public void update(@RequestBody RouteJson json) {
        routeService.update(routeConverter.convertFrom(json));
    }

    @PostMapping
    public void save(@RequestBody RouteJson json) {
        routeService.insert(routeConverter.convertFrom(json));
    }

    @Deprecated
    @DeleteMapping
    public void delete(@RequestBody RouteJson json) {
        routeService.delete(routeConverter.convertFrom(json));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        routeService.delete(routeService.findOne(id));
    }

    @GetMapping("/search")
    public List<RouteJson> search(@RequestParam("cnpj") String cnpj, FilterSearchParam filterSearchParam) {
        return routeConverter.convertListModelFrom(routeService.search(filterSearchParam, cnpj));
    }
}

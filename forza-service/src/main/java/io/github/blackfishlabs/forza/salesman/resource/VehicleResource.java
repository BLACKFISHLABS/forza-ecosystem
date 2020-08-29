package io.github.blackfishlabs.forza.salesman.resource;

import io.github.blackfishlabs.forza.core.helper.ResourcePaths;
import io.github.blackfishlabs.forza.salesman.converter.VehicleConverter;
import io.github.blackfishlabs.forza.salesman.json.VehicleJson;
import io.github.blackfishlabs.forza.salesman.model.VehicleEntity;
import io.github.blackfishlabs.forza.salesman.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ResourcePaths.VEHICLE_PATH)
public class VehicleResource {

    @Autowired
    private VehicleService vehicleService;
    @Autowired
    private VehicleConverter vehicleConverter;

    @GetMapping("/search")
    public List<VehicleJson> get(@RequestParam("cnpj") String cnpj) {
        List<VehicleEntity> vehicles = vehicleService.findVehicleByCompany(cnpj);
        return vehicleConverter.convertListModelFrom(vehicles);
    }

    @GetMapping("/{id}")
    public VehicleJson findOne(@PathVariable Long id) {
        return vehicleConverter.convertFrom(vehicleService.findOne(id));
    }

    @PutMapping
    public void update(@RequestBody VehicleJson json) {
        vehicleService.update(vehicleConverter.convertFrom(json));
    }

    @PostMapping
    public void save(@RequestBody VehicleJson json) {
        vehicleService.insert(vehicleConverter.convertFrom(json));
    }

    @Deprecated
    @DeleteMapping
    public void delete(@RequestBody VehicleJson json) {
        vehicleService.delete(vehicleConverter.convertFrom(json));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        vehicleService.delete(vehicleService.findOne(id));
    }
}

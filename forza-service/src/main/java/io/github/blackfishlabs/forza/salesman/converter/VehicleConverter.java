package io.github.blackfishlabs.forza.salesman.converter;

import com.google.common.collect.Lists;
import io.github.blackfishlabs.forza.core.infra.converter.Converter;
import io.github.blackfishlabs.forza.salesman.json.VehicleJson;
import io.github.blackfishlabs.forza.salesman.model.VehicleEntity;
import io.github.blackfishlabs.forza.salesman.service.SalesmanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class VehicleConverter extends Converter<VehicleJson, VehicleEntity> {

    @Autowired
    private SalesmanConverter salesmanConverter;
    @Autowired
    private SalesmanService salesmanService;

    @Override
    public VehicleJson convertFrom(VehicleEntity model) {
        VehicleJson json = new VehicleJson();

        json.setId(model.getId());
        json.setDescription(model.getDescription());
        json.setPlate(model.getPlate());
        json.setSalesman(Lists.newArrayList(salesmanConverter.convertFrom(model.getSalesman())));

        return json;
    }

    @Override
    public VehicleEntity convertFrom(VehicleJson json) {
        VehicleEntity entity = new VehicleEntity();

        entity.setId(json.getId());
        entity.setDescription(json.getDescription());
        entity.setPlate(json.getPlate().replace("-", ""));
        Long salesmanId = json.getSalesman().iterator().next().getSalesmanId();
        entity.setSalesman(salesmanService.findOne(salesmanId));

        return entity;
    }
}

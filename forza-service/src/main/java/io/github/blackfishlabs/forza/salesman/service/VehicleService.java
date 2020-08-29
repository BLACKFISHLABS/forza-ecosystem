package io.github.blackfishlabs.forza.salesman.service;

import io.github.blackfishlabs.forza.core.infra.service.GenericService;
import io.github.blackfishlabs.forza.salesman.model.VehicleEntity;
import io.github.blackfishlabs.forza.salesman.repository.VehicleRepository;
import io.github.blackfishlabs.forza.salesman.repository.VehicleSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VehicleService extends GenericService<VehicleEntity, Long> {

    @Autowired
    private VehicleRepository vehicleRepository;

    public List<VehicleEntity> findVehicleByCompany(String cnpj) {
        return vehicleRepository.findAll(VehicleSpecification.findByCompany(cnpj));
    }

    public List<VehicleEntity> findVehicleByPlate(String plate) {
        return vehicleRepository.findAll(VehicleSpecification.findByPlate(plate));
    }

    public VehicleEntity findOne(Long id) {
        return vehicleRepository.getOne(id);
    }
}

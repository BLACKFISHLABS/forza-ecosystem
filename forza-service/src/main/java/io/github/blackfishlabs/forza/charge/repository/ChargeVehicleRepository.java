package io.github.blackfishlabs.forza.charge.repository;

import io.github.blackfishlabs.forza.charge.model.ChargeVehicleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ChargeVehicleRepository extends JpaRepository<ChargeVehicleEntity, Long>, JpaSpecificationExecutor<ChargeVehicleEntity> {
}


package io.github.blackfishlabs.forza.charge.repository;

import io.github.blackfishlabs.forza.charge.model.ChargeTablePriceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ChargeTablePriceRepository extends JpaRepository<ChargeTablePriceEntity, Long>, JpaSpecificationExecutor<ChargeTablePriceEntity> {
}


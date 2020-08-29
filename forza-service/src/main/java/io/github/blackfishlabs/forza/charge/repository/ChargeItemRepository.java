package io.github.blackfishlabs.forza.charge.repository;

import io.github.blackfishlabs.forza.charge.model.ChargeItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ChargeItemRepository extends JpaRepository<ChargeItemEntity, Long>, JpaSpecificationExecutor<ChargeItemEntity> {
}


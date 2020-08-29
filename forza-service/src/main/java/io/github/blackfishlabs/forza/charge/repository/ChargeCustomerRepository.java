package io.github.blackfishlabs.forza.charge.repository;

import io.github.blackfishlabs.forza.charge.model.ChargeCustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ChargeCustomerRepository extends JpaRepository<ChargeCustomerEntity, Long>, JpaSpecificationExecutor<ChargeCustomerEntity> {
}

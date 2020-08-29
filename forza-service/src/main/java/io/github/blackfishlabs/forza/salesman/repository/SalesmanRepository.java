package io.github.blackfishlabs.forza.salesman.repository;

import io.github.blackfishlabs.forza.salesman.model.SalesmanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SalesmanRepository extends JpaRepository<SalesmanEntity, Long>, JpaSpecificationExecutor<SalesmanEntity> {
}

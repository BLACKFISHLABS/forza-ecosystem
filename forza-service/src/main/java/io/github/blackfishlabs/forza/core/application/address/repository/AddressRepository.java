package io.github.blackfishlabs.forza.core.application.address.repository;

import io.github.blackfishlabs.forza.core.application.address.model.AddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AddressRepository extends JpaRepository<AddressEntity, Long>, JpaSpecificationExecutor<AddressEntity> {
}

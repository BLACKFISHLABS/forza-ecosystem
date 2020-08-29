package io.github.blackfishlabs.forza.core.application.phone.repository;

import io.github.blackfishlabs.forza.core.application.phone.model.PhoneEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PhoneRepository extends JpaRepository<PhoneEntity, Long>, JpaSpecificationExecutor<PhoneEntity> {
}

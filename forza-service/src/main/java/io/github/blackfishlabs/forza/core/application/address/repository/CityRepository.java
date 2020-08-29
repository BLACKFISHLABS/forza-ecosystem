package io.github.blackfishlabs.forza.core.application.address.repository;

import io.github.blackfishlabs.forza.core.application.address.model.CityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CityRepository extends JpaRepository<CityEntity, Long>, JpaSpecificationExecutor<CityEntity> {

    CityEntity findByCode(Integer code);
}

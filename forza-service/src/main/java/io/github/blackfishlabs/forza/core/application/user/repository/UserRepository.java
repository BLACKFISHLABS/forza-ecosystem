package io.github.blackfishlabs.forza.core.application.user.repository;


import io.github.blackfishlabs.forza.core.application.user.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserRepository extends JpaRepository<UserEntity, Long>, JpaSpecificationExecutor<UserEntity> {

    UserEntity findBySsoId(String ssoId);

    UserEntity findByEmail(String email);
}

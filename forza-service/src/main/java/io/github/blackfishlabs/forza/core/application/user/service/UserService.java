package io.github.blackfishlabs.forza.core.application.user.service;

import io.github.blackfishlabs.forza.core.application.user.model.UserEntity;
import io.github.blackfishlabs.forza.core.application.user.repository.UserRepository;
import io.github.blackfishlabs.forza.core.application.user.repository.UserSpecification;
import io.github.blackfishlabs.forza.core.infra.service.GenericService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService extends GenericService<UserEntity, Long> {

    @Autowired
    private UserRepository userRepository;

    public UserEntity findOne(String ssoId) {
        return userRepository.findBySsoId(ssoId);
    }

    public List<UserEntity> findUserByCompany(String cnpj) {
        return userRepository.findAll(UserSpecification.findByCompany(cnpj));
    }

}

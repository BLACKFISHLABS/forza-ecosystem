package io.github.blackfishlabs.forza.core.infra.security;

import io.github.blackfishlabs.forza.core.application.company.converter.CompanyConverter;
import io.github.blackfishlabs.forza.core.application.user.model.PermissionEntity;
import io.github.blackfishlabs.forza.core.application.user.model.UserEntity;
import io.github.blackfishlabs.forza.core.application.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import static java.util.Objects.isNull;

@Component
public class UserDetail implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CompanyConverter companyConverter;

    @Override
    public UserDetails loadUserByUsername(String param) throws UsernameNotFoundException {
        UserEntity user;
        if (param.contains("@")) {
            user = this.userRepository.findByEmail(param);
        } else {
            user = this.userRepository.findBySsoId(param);
        }

        if (isNull(user)) {
            throw new UsernameNotFoundException("Usuário com a credencial \"" + param + "\" não encontrado.");
        }

        LoginDetailBean login = new LoginDetailBean(
                user.getSsoId(),
                user.getFirstName(),
                user.getEmail(),
                user.getPassword(),
                companyConverter.convertFrom(user.getCompanyEntity()));

        for (PermissionEntity permission : user.getPermissions()) {
            login.addRole(permission.getRole());
        }

        return login;
    }
}


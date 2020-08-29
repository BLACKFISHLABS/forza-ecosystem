package io.github.blackfishlabs.forza.core.infra.security;

import com.google.common.collect.Lists;
import io.github.blackfishlabs.forza.core.application.company.json.CompanyJson;
import io.github.blackfishlabs.forza.core.infra.json.Json;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class LoginDetailBean extends Json implements UserDetails {

    private final String ssoId;
    private final String name;
    private final String email;
    private final String password;
    private final List<String> roles;
    private final CompanyJson company;

    public LoginDetailBean(String ssoId, String name, String email, String passwordHash, CompanyJson company) {
        this.ssoId = ssoId;
        this.name = name;
        this.email = email;
        this.password = passwordHash;
        this.roles = Lists.newArrayList();
        this.company = company;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void addRole(String role) {
        this.roles.add(role);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<String> roles = this.getRoles();

        if (roles == null) {
            return Collections.emptyList();
        }

        return roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
    }

    public String getSsoId() {
        return ssoId;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.name;
    }

    public String getEmail() {
        return email;
    }

    public CompanyJson getCompany() {
        return company;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}


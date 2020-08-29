package io.github.blackfishlabs.forza.core.application.user.service;


import io.github.blackfishlabs.forza.core.application.user.model.PermissionEntity;
import io.github.blackfishlabs.forza.core.infra.service.GenericService;
import org.springframework.stereotype.Service;

@Service
public class UserProfileService extends GenericService<PermissionEntity, Long> {
}

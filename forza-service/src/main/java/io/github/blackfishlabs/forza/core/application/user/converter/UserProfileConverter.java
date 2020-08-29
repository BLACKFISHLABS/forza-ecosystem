package io.github.blackfishlabs.forza.core.application.user.converter;

import io.github.blackfishlabs.forza.core.application.user.json.UserProfileJson;
import io.github.blackfishlabs.forza.core.application.user.model.PermissionEntity;
import io.github.blackfishlabs.forza.core.infra.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserProfileConverter extends Converter<UserProfileJson, PermissionEntity> {

    @Override
    public UserProfileJson convertFrom(PermissionEntity model) {
        UserProfileJson json = new UserProfileJson();

        json.setId(model.getId());
        json.setRole(model.getRole());

        return json;
    }

    @Override
    public PermissionEntity convertFrom(UserProfileJson json) {
        PermissionEntity entity = new PermissionEntity();

        entity.setId(json.getId());
        entity.setRole(json.getRole());

        return entity;
    }
}

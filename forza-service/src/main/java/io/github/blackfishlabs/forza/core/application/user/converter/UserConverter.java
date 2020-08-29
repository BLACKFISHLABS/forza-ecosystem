package io.github.blackfishlabs.forza.core.application.user.converter;

import io.github.blackfishlabs.forza.core.application.company.converter.CompanyConverter;
import io.github.blackfishlabs.forza.core.application.user.json.UserJson;
import io.github.blackfishlabs.forza.core.application.user.model.UserEntity;
import io.github.blackfishlabs.forza.core.infra.converter.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserConverter extends Converter<UserJson, UserEntity> {

    @Autowired
    private UserProfileConverter userProfileConverter;

    @Autowired
    private CompanyConverter companyConverter;

    @Override
    public UserJson convertFrom(UserEntity model) {
        UserJson json = new UserJson();

        json.setId(model.getId());
        json.setEmail(model.getEmail());
        json.setFirstName(model.getFirstName());
        json.setLastName(model.getLastName());
        json.setPassword(model.getPassword());
        json.setProfiles(userProfileConverter.convertListModelFrom(model.getPermissions()));
        json.setSsoId(model.getSsoId());
        json.setState(model.getState());
        json.setCompanyJson(companyConverter.convertFrom(model.getCompanyEntity()));
        json.setApiKey(model.getApiKey());

        return json;
    }

    @Override
    public UserEntity convertFrom(UserJson json) {
        UserEntity entity = new UserEntity();

        entity.setId(json.getId());
        entity.setEmail(json.getEmail());
        entity.setFirstName(json.getFirstName());
        entity.setLastName(json.getLastName());
        entity.setPassword(json.getPassword());
        entity.setPermissions(userProfileConverter.convertListJsonFrom(json.getProfiles()));
        entity.setSsoId(json.getSsoId());
        entity.setState(json.getState());
        entity.setCompanyEntity(companyConverter.convertFrom(json.getCompanyJson()));
        entity.setApiKey(json.getApiKey());

        return entity;
    }
}

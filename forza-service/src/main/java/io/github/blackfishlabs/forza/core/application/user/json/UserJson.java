package io.github.blackfishlabs.forza.core.application.user.json;

import com.google.common.collect.Lists;
import io.github.blackfishlabs.forza.core.application.company.json.CompanyJson;
import io.github.blackfishlabs.forza.core.application.user.model.UserState;
import io.github.blackfishlabs.forza.core.infra.json.Json;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserJson extends Json {

    private Long id;
    private String ssoId;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private String state = UserState.ACTIVE.getState();
    private CompanyJson companyJson;
    private List<UserProfileJson> profiles = Lists.newArrayList();
    private String apiKey;
}

package io.github.blackfishlabs.forza.core.application.user.json;

import io.github.blackfishlabs.forza.core.infra.json.Json;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserProfileJson extends Json {

    private Long id;
    private String role;
}

package io.github.blackfishlabs.forza.core.application.phone.json;

import io.github.blackfishlabs.forza.core.application.phone.model.PhoneType;
import io.github.blackfishlabs.forza.core.infra.json.Json;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PhoneJson extends Json {

    private Long id;

    private String phoneNumber;

    private PhoneType phoneType;
}

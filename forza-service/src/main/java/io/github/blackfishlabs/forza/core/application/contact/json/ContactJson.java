package io.github.blackfishlabs.forza.core.application.contact.json;

import io.github.blackfishlabs.forza.core.application.phone.json.PhoneJson;
import io.github.blackfishlabs.forza.core.infra.json.Json;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContactJson extends Json {

    private Long id;

    private String name;

    private PhoneJson phoneJson;
}
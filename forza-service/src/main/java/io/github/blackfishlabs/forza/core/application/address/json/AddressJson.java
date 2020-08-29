package io.github.blackfishlabs.forza.core.application.address.json;

import io.github.blackfishlabs.forza.core.infra.json.Json;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressJson extends Json {

    private Long id;

    private String street;

    private String location;

    private String neighborhood;

    private String complement;

    private String cep;

    private String number;

    private CityJson cityJson;
}

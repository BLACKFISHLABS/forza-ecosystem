package io.github.blackfishlabs.forza.core.application.address.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.blackfishlabs.forza.core.infra.json.Json;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CityJson extends Json {

    @JsonProperty("idCidade")
    private Long id;

    @JsonProperty("codMunicipio")
    private String code;

    @JsonProperty("nome")
    private String city;

    @JsonProperty("Estado")
    private StateJson stateJson;
}
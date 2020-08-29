package io.github.blackfishlabs.forza.core.application.address.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.blackfishlabs.forza.core.infra.json.Json;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StateJson extends Json {

    @JsonProperty("idEstado")
    private Long id;

    private String code;

    @JsonProperty("nome")
    private String state;

    @JsonProperty("uf")
    private String acronym;
}

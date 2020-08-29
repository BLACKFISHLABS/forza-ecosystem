package io.github.blackfishlabs.forza.core.application.server.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.blackfishlabs.forza.core.infra.json.Json;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServerJson extends Json {

    @JsonProperty("dataAtual")
    private String currentTime;
}

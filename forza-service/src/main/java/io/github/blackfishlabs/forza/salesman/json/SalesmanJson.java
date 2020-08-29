package io.github.blackfishlabs.forza.salesman.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.blackfishlabs.forza.core.infra.json.Json;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SalesmanJson extends Json {

    @JsonProperty("error")
    public boolean error;

    @JsonProperty("mensagem")
    public String message;

    @JsonProperty("Vendedor")
    public Salesman salesman;
}

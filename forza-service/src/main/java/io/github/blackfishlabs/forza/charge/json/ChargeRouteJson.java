package io.github.blackfishlabs.forza.charge.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.blackfishlabs.forza.core.infra.json.Json;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ChargeRouteJson extends Json {

    @JsonProperty
    private Long id;

    @JsonProperty("codigo")
    private String code;

    @JsonProperty("descricao")
    private String description;

    @JsonProperty("clientes")
    private List<ChargeCustomerJson> customers;
}

package io.github.blackfishlabs.forza.charge.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.blackfishlabs.forza.core.infra.json.Json;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ChargeBlockJson extends Json {

    @JsonProperty
    private Long id;

    @JsonProperty("tabelas")
    private List<ChargeTablePriceJson> tables;
}

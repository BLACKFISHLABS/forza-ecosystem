package io.github.blackfishlabs.forza.charge.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.blackfishlabs.forza.core.infra.json.Json;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ChargeTablePriceJson extends Json {

    @JsonProperty
    private Long id;

    @JsonProperty("codigo")
    private String code;

    @JsonProperty("nome")
    private String name;

    @JsonProperty("itens")
    private List<ChargeItemJson> items;
}

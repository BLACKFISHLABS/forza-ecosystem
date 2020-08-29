package io.github.blackfishlabs.forza.charge.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.blackfishlabs.forza.core.infra.json.Json;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChargeProductJson extends Json {

    @JsonProperty
    private Long id;

    @JsonProperty("codigo")
    private String code;

    @JsonProperty("codigoBarras")
    private String ean;

    @JsonProperty("descricao")
    private String description;

    @JsonProperty("grupo")
    private String group;

    @JsonProperty("obs")
    private String note;

    @JsonProperty("quantidade")
    private float qnt;

    @JsonProperty("un")
    private String unit;
}

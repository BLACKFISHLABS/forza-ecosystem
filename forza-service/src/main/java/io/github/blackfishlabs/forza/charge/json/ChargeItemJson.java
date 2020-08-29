package io.github.blackfishlabs.forza.charge.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.blackfishlabs.forza.core.infra.json.Json;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChargeItemJson extends Json {

    @JsonProperty
    private Long id;

    @JsonProperty("precoVenda")
    private double price;

    @JsonProperty("quantidade")
    private Float stockQuantity;

    @JsonProperty("produto")
    private ChargeProductJson product;

}

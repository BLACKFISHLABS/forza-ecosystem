package io.github.blackfishlabs.forza.order.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.github.blackfishlabs.forza.core.helper.DateDeserializer;
import io.github.blackfishlabs.forza.core.helper.DateSerializer;
import io.github.blackfishlabs.forza.core.infra.json.Json;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class OrderItemJson extends Json {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("idItem")
    private Long orderItemId;

    @JsonProperty("idProduto")
    private Long productId;

    @JsonProperty("produto")
    private String product;

    @JsonProperty("quantidade")
    private float quantity;

    @JsonProperty("subTotal")
    private double subTotal;

    @JsonProperty("prcVenda")
    private double salesPrice;

    @JsonProperty("ultimaAlteracao")
    @JsonDeserialize(using = DateDeserializer.class)
    @JsonSerialize(using = DateSerializer.class)
    private Date lastChangeTime;

    private String unitCode;
    private String productCode;
    private String orderCode;
}

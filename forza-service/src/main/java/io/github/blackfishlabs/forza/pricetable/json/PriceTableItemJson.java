package io.github.blackfishlabs.forza.pricetable.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.github.blackfishlabs.forza.core.helper.DateDeserializer;
import io.github.blackfishlabs.forza.core.helper.DateSerializer;
import io.github.blackfishlabs.forza.core.infra.json.Json;
import io.github.blackfishlabs.forza.product.json.ProductJson;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class PriceTableItemJson extends Json {

    @JsonProperty("idItensTabela")
    private Long itemId;

    @JsonProperty("prcVenda")
    private Double salesPrice;

    @JsonProperty("quantidade")
    private Float stockQuantity;

    @JsonProperty("idProduto")
    private Long productId;

    @JsonProperty("Produto")
    private ProductJson product;

    @JsonProperty("ultimaAlteracao")
    @JsonDeserialize(using = DateDeserializer.class)
    @JsonSerialize(using = DateSerializer.class)
    private Date lastChangeTime;
}

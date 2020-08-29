package io.github.blackfishlabs.forza.product.json;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.github.blackfishlabs.forza.core.application.company.json.CompanyJson;
import io.github.blackfishlabs.forza.core.helper.DateDeserializer;
import io.github.blackfishlabs.forza.core.helper.DateSerializer;
import io.github.blackfishlabs.forza.core.infra.json.Json;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ProductJson extends Json {

    @JsonProperty("idProduto")
    private Long productId;

    @JsonProperty("codigo")
    private String code;

    @JsonProperty("codigoBarras")
    private String barCode;

    @JsonProperty("descricao")
    private String description;

    @JsonProperty("un")
    private String unit;

    @JsonProperty("grupo")
    private String group;

    @JsonProperty("obs")
    private String observation;

    @JsonProperty("ativo")
    private Boolean active;

    @JsonProperty("ultimaAlteracao")
    @JsonDeserialize(using = DateDeserializer.class)
    @JsonSerialize(using = DateSerializer.class)
    private Date lastChangeTime;

    @JsonIgnore
    private CompanyJson companyJson;
}


package io.github.blackfishlabs.forza.pricetable.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.github.blackfishlabs.forza.core.helper.DateDeserializer;
import io.github.blackfishlabs.forza.core.helper.DateSerializer;
import io.github.blackfishlabs.forza.core.infra.json.Json;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class PriceTableJson extends Json {

    @JsonProperty("idTabela")
    private Long priceTableId;

    @JsonProperty("codigo")
    private String code;

    @JsonProperty("nome")
    private String description;

    @JsonProperty("ItensTabela")
    private List<PriceTableItemJson> items;

    @JsonProperty("ativo")
    private Boolean active;

    @JsonProperty("ultimaAlteracao")
    @JsonDeserialize(using = DateDeserializer.class)
    @JsonSerialize(using = DateSerializer.class)
    private Date lastChangeTime;

    @JsonProperty("idEmpresa")
    private Long companyId;
}

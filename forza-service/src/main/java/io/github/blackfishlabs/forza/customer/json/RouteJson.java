package io.github.blackfishlabs.forza.customer.json;

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
import java.util.List;

@Getter
@Setter
public class RouteJson extends Json {

    @JsonProperty("routeId")
    private Long routeId;

    @JsonProperty("codigo")
    private String code;

    @JsonProperty("nome")
    private String description;

    @JsonProperty("Clientes")
    private List<RouteCustomerJson> customers;

    @JsonProperty("ativo")
    private Boolean active;

    @JsonProperty("ultimaAlteracao")
    @JsonDeserialize(using = DateDeserializer.class)
    @JsonSerialize(using = DateSerializer.class)
    private Date lastChangeTime;

    @JsonProperty("empresa")
    private CompanyJson companyJson;
}

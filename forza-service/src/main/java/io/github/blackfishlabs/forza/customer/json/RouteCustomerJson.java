package io.github.blackfishlabs.forza.customer.json;

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
public class RouteCustomerJson extends Json {

    @JsonProperty("routeCustomerId")
    private Long routeCustomerId;

    @JsonProperty("idCliente")
    private Long customerId;

    @JsonProperty("Cliente")
    private CustomerJson customer;

    @JsonProperty("ultimaAlteracao")
    @JsonDeserialize(using = DateDeserializer.class)
    @JsonSerialize(using = DateSerializer.class)
    private Date lastChangeTime;
}

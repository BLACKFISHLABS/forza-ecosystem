package io.github.blackfishlabs.forza.salesman.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.blackfishlabs.forza.core.infra.json.Json;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class VehicleJson extends Json {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("plate")
    private String plate;

    @JsonProperty("description")
    private String description;

    @JsonProperty("Vendedor")
    private List<Salesman> salesman;
}

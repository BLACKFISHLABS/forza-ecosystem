package io.github.blackfishlabs.forza.charge.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.blackfishlabs.forza.core.infra.json.Json;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChargeVehicleJson extends Json {

    @JsonProperty
    private Long id;

    @JsonProperty("placa")
    private String plate;

    @JsonProperty("kmInicio")
    private Integer initKm;

    @JsonProperty("kmFim")
    private Integer endKm;
}

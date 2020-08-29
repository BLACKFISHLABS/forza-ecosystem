package io.github.blackfishlabs.forza.charge.json;

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
import java.util.List;

@Getter
@Setter
public class ChargeJson extends Json {

    @JsonProperty
    private Long id;

    @JsonProperty("codigo")
    private String code;

    @JsonProperty("emissao")
    @JsonDeserialize(using = DateDeserializer.class)
    @JsonSerialize(using = DateSerializer.class)
    private Date issueDate;

    @JsonProperty("veiculo")
    private ChargeVehicleJson vehicle;

    @JsonProperty("vendedor")
    private String salesmanCode;

    @JsonProperty("emitente")
    private String emitterCode;

    @JsonProperty("rotas")
    private List<ChargeRouteJson> routes;

    @JsonProperty("carga")
    private ChargeBlockJson chargeBlock;

    @JsonProperty("status")
    private int status;

    @JsonIgnore
    private CompanyJson companyJson;
}



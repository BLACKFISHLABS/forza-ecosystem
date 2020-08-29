package io.github.blackfishlabs.forza.payment.json;

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
public class PaymentJson extends Json {

    @JsonProperty("idFormPgto")
    private Long paymentMethodId;

    @JsonProperty("codigo")
    private String code;

    @JsonProperty("descricao")
    private String description;

    @JsonProperty("perDesc")
    private Float discountPercentage;

    @JsonProperty("ativo")
    private Boolean active;

    @JsonProperty("ultimaAlteracao")
    @JsonDeserialize(using = DateDeserializer.class)
    @JsonSerialize(using = DateSerializer.class)
    private Date lastChangeTime;

    @JsonProperty("idEmpresa")
    private Long companyId;
}

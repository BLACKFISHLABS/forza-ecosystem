package io.github.blackfishlabs.forza.salesman.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.github.blackfishlabs.forza.core.application.company.json.CompanyJson;
import io.github.blackfishlabs.forza.core.helper.DateDeserializer;
import io.github.blackfishlabs.forza.core.helper.DateSerializer;
import io.github.blackfishlabs.forza.core.infra.json.Json;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class Salesman extends Json {

    @JsonProperty("idVendedor")
    private Long salesmanId;

    @JsonProperty("codigo")
    private String code;

    @JsonProperty("nome")
    private String name;

    @JsonProperty("cpfCnpj")
    private String cpfOrCnpj;

    @JsonProperty("telefone")
    private String phoneNumber;

    @JsonProperty("email")
    private String email;

    @JsonProperty("senha")
    private String password;

    @JsonProperty("ativo")
    private Boolean active;

    @JsonProperty("ultimaAlteracao")
    @JsonDeserialize(using = DateDeserializer.class)
    @JsonSerialize(using = DateSerializer.class)
    private Date lastChangeTime;

    @JsonProperty("aplicaDesconto")
    private Boolean canApplyDiscount;

    @JsonProperty("Empresas")
    private List<CompanyJson> companies = new ArrayList<>();
}

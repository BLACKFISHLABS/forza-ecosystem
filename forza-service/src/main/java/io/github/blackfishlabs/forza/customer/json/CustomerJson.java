package io.github.blackfishlabs.forza.customer.json;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.github.blackfishlabs.forza.core.application.address.json.CityJson;
import io.github.blackfishlabs.forza.core.application.company.json.CompanyJson;
import io.github.blackfishlabs.forza.core.helper.DateDeserializer;
import io.github.blackfishlabs.forza.core.helper.DateSerializer;
import io.github.blackfishlabs.forza.core.infra.json.Json;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class CustomerJson extends Json {

    @JsonProperty("appKey")
    private Long id;

    @JsonProperty("idCliente")
    private Long customerId;

    @JsonProperty("codigo")
    private String code;

    @JsonProperty("nome")
    private String name;

    @JsonProperty("nomeFantasia")
    private String fantasyName;

    @JsonProperty("tipo")
    private Integer type;

    @JsonProperty("cpfCnpj")
    private String cpfOrCnpj;

    @JsonProperty("contato")
    private String contact;

    @JsonProperty("email")
    private String email;

    @JsonProperty("telefone")
    private String mainPhone;

    @JsonProperty("telefone2")
    private String secondaryPhone;

    @JsonProperty("endereco")
    private String address;

    @JsonProperty("cep")
    private String postalCode;

    @JsonProperty("bairro")
    private String neighborhood;

    @JsonProperty("numero")
    private String addressNumber;

    @JsonProperty("complemento")
    private String addressComplement;

    @JsonProperty("tabelaPadrao")
    private String defaultPriceTable;

    @JsonProperty("Cidade")
    private CityJson cityJson;

    @JsonProperty("ativo")
    private Boolean active;

    @JsonProperty("ultimaAlteracao")
    @JsonDeserialize(using = DateDeserializer.class)
    @JsonSerialize(using = DateSerializer.class)
    private Date lastChangeTime;

    @JsonIgnore
    private CompanyJson companyJson;

    @JsonProperty("ie")
    private String ie;

    @JsonProperty("comprador")
    private String buyer;
}

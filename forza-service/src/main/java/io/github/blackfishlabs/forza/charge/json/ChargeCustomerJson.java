package io.github.blackfishlabs.forza.charge.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.blackfishlabs.forza.core.infra.json.Json;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChargeCustomerJson extends Json {

    @JsonProperty
    private Long id;

    @JsonProperty("codigo")
    private String code;

    @JsonProperty("nome")
    private String name;

    @JsonProperty("nomeFantasia")
    private String fantasyName;

    @JsonProperty("cpfCnpj")
    private String document;

    @JsonProperty("codMunicipio")
    private String city;

    @JsonProperty("bairro")
    private String neighborhood;

    @JsonProperty("cep")
    private String postalCode;

    @JsonProperty("complemento")
    private String complement;

    @JsonProperty("contato")
    private String contact;

    @JsonProperty("email")
    private String email;

    @JsonProperty("endereco")
    private String address;

    @JsonProperty("numero")
    private String number;

    @JsonProperty("telefone")
    private String phone;

    @JsonProperty("telefone2")
    private String secondaryPhone;

    // 0 - Fisica / 1 - Juridica
    @JsonProperty("tipo")
    private Integer typePerson;

    @JsonProperty("ie")
    private String ie;

    @JsonProperty("comprador")
    private String buyer;
}

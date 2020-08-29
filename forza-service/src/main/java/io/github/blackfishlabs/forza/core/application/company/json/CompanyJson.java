package io.github.blackfishlabs.forza.core.application.company.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.blackfishlabs.forza.core.application.address.json.AddressJson;
import io.github.blackfishlabs.forza.core.application.company.model.CompanyType;
import io.github.blackfishlabs.forza.core.application.contact.json.ContactJson;
import io.github.blackfishlabs.forza.core.infra.json.Json;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanyJson extends Json {

    @JsonProperty("idEmpresa")
    private Long id;

    @JsonProperty("nome")
    private String fantasyName;

    private String companyName;

    @JsonProperty("cnpj")
    private String cnpj;

    private AddressJson addressJson;

    private ContactJson contactJson;

    private CompanyType companyType;

    @JsonProperty("idTabela")
    private Integer priceTableId;
}
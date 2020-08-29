package io.github.blackfishlabs.forza.order.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.collect.Lists;
import io.github.blackfishlabs.forza.core.application.company.json.CompanyJson;
import io.github.blackfishlabs.forza.core.helper.DateDeserializer;
import io.github.blackfishlabs.forza.core.helper.DateSerializer;
import io.github.blackfishlabs.forza.core.infra.json.Json;
import io.github.blackfishlabs.forza.salesman.json.Salesman;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class OrderJson extends Json {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("idPedido")
    private Long orderId;

    @JsonProperty("tipo")
    private int type;

    @JsonProperty("dtEmissao")
    @JsonDeserialize(using = DateDeserializer.class)
    @JsonSerialize(using = DateSerializer.class)
    private Date issueDate;

    @JsonProperty("desconto")
    private double discount;

    @JsonProperty("perDesc")
    private float discountPercentage;

    @JsonProperty("observacao")
    private String observation;

    @JsonProperty("codigoCliente")
    private String customerCode;

    @JsonProperty("idCliente")
    private Long customerId;

    @JsonProperty("customer")
    private String customer;

    @JsonProperty("idFormPgto")
    private Long paymentMethodId;

    @JsonProperty("payment")
    private String payment;

    @JsonProperty("idTabela")
    private Long priceTableId;

    @JsonProperty("Itens")
    private List<OrderItemJson> items = Lists.newArrayList();

    @JsonProperty("total")
    private double total;

    @JsonProperty("ultimaAlteracao")
    @JsonDeserialize(using = DateDeserializer.class)
    @JsonSerialize(using = DateSerializer.class)
    private Date lastChangeTime;

    @JsonProperty("status")
    private int status;

    @JsonProperty("empresa")
    private CompanyJson companyJson;

    @JsonProperty("vendedor")
    private Salesman salesmanJson;

    @JsonProperty("resumo")
    private String resumeCode;
}

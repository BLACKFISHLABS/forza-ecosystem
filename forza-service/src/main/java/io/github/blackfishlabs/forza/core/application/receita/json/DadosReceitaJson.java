package io.github.blackfishlabs.forza.core.application.receita.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.github.blackfishlabs.forza.core.infra.json.Json;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class DadosReceitaJson extends Json {

    private String status;
    private String message;
    private String cnpj;
    private String ultima_atualizacao;
    private String tipo;
    private String abertura;
    private String nome;
    private String fantasia;
    private ArrayList<AtividadePrincipalJson> atividade_principal;
    private ArrayList<AtividadeSecundariaJson> atividades_secundarias;
    private String natureza_juridica;
    private String logradouro;
    private String numero;
    private String complemento;
    private String cep;
    private String bairro;
    private String municipio;
    private String uf;
    private String email;
    private String telefone;
    private String efr;
    private String situacao;
    private String data_situacao;
    private String motivo_situacao;
    private String situacao_especial;
    private String data_situacao_especial;
    private String capital_social;
    private ArrayList<QuadroSocioAdmJson> qsa;
}

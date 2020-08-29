package io.github.blackfishlabs.forza.core.application.receita.json;

import io.github.blackfishlabs.forza.core.infra.json.Json;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuadroSocioAdmJson extends Json {

    private String nome;
    private String qual;
    private String pais_origim;
    private String nome_rep_legal;
    private String qual_rep_legal;
}

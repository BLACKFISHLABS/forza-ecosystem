package io.github.blackfishlabs.forza.core.application.receita.json;

import io.github.blackfishlabs.forza.core.infra.json.Json;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AtividadePrincipalJson extends Json {

    private String code;
    private String text;

}

package io.github.blackfishlabs.forza.core.application.receita.resource;

import io.github.blackfishlabs.forza.core.application.receita.json.DadosReceitaJson;
import io.github.blackfishlabs.forza.core.application.receita.service.ReceitaWSService;
import io.github.blackfishlabs.forza.core.infra.resource.APIMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/receita")
public class ReceitaWSResource implements APIMap {

    @Autowired
    private ReceitaWSService receitaWSService;

    @GetMapping("/cnpj")
    public DadosReceitaJson getCNPJ(@RequestParam("cnpj") String cnpj) {
        return receitaWSService.getCNPJ(cnpj);
    }
}

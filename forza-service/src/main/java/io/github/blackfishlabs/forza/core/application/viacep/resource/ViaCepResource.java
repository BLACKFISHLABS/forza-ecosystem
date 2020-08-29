package io.github.blackfishlabs.forza.core.application.viacep.resource;

import io.github.blackfishlabs.forza.core.application.viacep.json.ViaCepJson;
import io.github.blackfishlabs.forza.core.application.viacep.service.ViaCepService;
import io.github.blackfishlabs.forza.core.infra.resource.APIMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/cep/viaCep")
public class ViaCepResource implements APIMap {

    @Autowired
    private ViaCepService viaCepService;

    @RequestMapping(method = {RequestMethod.GET})
    public ViaCepJson getAddress(@RequestParam("cep") String cep) throws IOException {
        return viaCepService.getAddress(cep);
    }
}

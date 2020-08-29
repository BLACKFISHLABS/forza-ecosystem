package io.github.blackfishlabs.forza.salesman.resource;

import io.github.blackfishlabs.forza.core.helper.ResourcePaths;
import io.github.blackfishlabs.forza.core.infra.resource.APIMap;
import io.github.blackfishlabs.forza.salesman.converter.SalesmanConverter;
import io.github.blackfishlabs.forza.salesman.json.Salesman;
import io.github.blackfishlabs.forza.salesman.json.SalesmanJson;
import io.github.blackfishlabs.forza.salesman.model.SalesmanEntity;
import io.github.blackfishlabs.forza.salesman.service.SalesmanService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(ResourcePaths.SALESMAN_PATH)
public class SalesmanResource implements APIMap {

    @Autowired
    private SalesmanService salesmanService;
    @Autowired
    private SalesmanConverter salesmanConverter;

    private static final Logger LOGGER = LoggerFactory.getLogger(SalesmanResource.class);

    @GetMapping
    public SalesmanJson getSalesman(@RequestParam("cpfCnpj") String cpfCnpj,
                                    @RequestParam("senha") String password) {
        try {
            SalesmanEntity salesman = salesmanService.findSalesman(cpfCnpj, password);
            Salesman salesmanForJson = salesmanConverter.convertFrom(salesman);

            SalesmanJson json = new SalesmanJson();
            json.setError(false);
            json.setMessage("OK");
            json.setSalesman(salesmanForJson);

            LOGGER.info("Vendedor Logado: " + salesman.getName());
            return json;
        } catch (Exception e) {
            SalesmanJson json = new SalesmanJson();
            json.setError(true);
            json.setMessage("Vendedor " + cpfCnpj + " não encontrado!");
            json.setSalesman(null);

            LOGGER.error("Tentativa de login mal sucedida: " + json.getMessage());
            return json;
        }
    }

    @GetMapping("/search")
    public List<Salesman> get(@RequestParam("cnpj") String cnpj) {
        List<SalesmanEntity> salesman = salesmanService.findSalesmanByCompany(cnpj);
        return salesmanConverter.convertListModelFrom(salesman);
    }

    @GetMapping("/{id}")
    public Salesman findOne(@PathVariable Long id) {
        return salesmanConverter.convertFrom(salesmanService.findOne(id));
    }

    @PutMapping
    public void update(@RequestBody Salesman json) {
        json.setLastChangeTime(new Date());
        salesmanService.update(salesmanConverter.convertFrom(json));
    }

    @PostMapping
    public void save(@RequestBody Salesman json) {
        json.setLastChangeTime(new Date());
        salesmanService.insert(salesmanConverter.convertFrom(json));
    }

    @Deprecated
    @DeleteMapping
    public void delete(@RequestBody Salesman json) {
        salesmanService.delete(salesmanConverter.convertFrom(json));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        salesmanService.delete(salesmanService.findOne(id));
    }
}

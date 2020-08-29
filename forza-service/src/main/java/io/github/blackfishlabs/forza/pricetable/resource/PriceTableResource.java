package io.github.blackfishlabs.forza.pricetable.resource;

import io.github.blackfishlabs.forza.core.application.search.FilterSearchParam;
import io.github.blackfishlabs.forza.core.helper.ResourcePaths;
import io.github.blackfishlabs.forza.core.infra.resource.APIMap;
import io.github.blackfishlabs.forza.pricetable.converter.PriceTableConverter;
import io.github.blackfishlabs.forza.pricetable.json.PriceTableJson;
import io.github.blackfishlabs.forza.pricetable.service.PriceTableService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ResourcePaths.PRICE_TABLE_PATH)
public class PriceTableResource implements APIMap {

    @Autowired
    private PriceTableService priceTableService;
    @Autowired
    private PriceTableConverter priceTableConverter;

    private static final Logger LOGGER = LoggerFactory.getLogger(PriceTableResource.class);

    @GetMapping
    public List<PriceTableJson> get(@RequestParam("cnpj") String cnpj) {
        return priceTableConverter.convertListModelFrom(priceTableService.findByCompany(cnpj));
    }

    @GetMapping("/mobile")
    public List<PriceTableJson> getPriceTableMobile(@RequestParam("cnpj") String cnpj,
                                                    @RequestParam("code") String chargeCode) {
        LOGGER.info("call method getPriceTableMobile()");
        return priceTableConverter.convertListModelFrom(priceTableService.findByCompanyAndCharge(cnpj, chargeCode));
    }

    @GetMapping("/update")
    public List<PriceTableJson> getPriceTableUpdate(@RequestParam("cnpj") String cnpj,
                                                    @RequestParam("ultimaAtualizacao") String lastUpdateTime) {
        LOGGER.info("call method getPriceTableUpdate()");
        return priceTableConverter.convertListModelFrom(priceTableService.findByCompanyAndLastUpdate(cnpj, lastUpdateTime));
    }

    @GetMapping("/search")
    public List<PriceTableJson> search(@RequestParam("cnpj") String cnpj, FilterSearchParam filterSearchParam) {
        return priceTableConverter.convertListModelFrom(priceTableService.search(filterSearchParam, cnpj));
    }

    @GetMapping("/{id}")
    public PriceTableJson findOne(@PathVariable Long id) {
        return priceTableConverter.convertFrom(priceTableService.findOne(id));
    }

    @Deprecated
    @DeleteMapping
    public void delete(@RequestBody PriceTableJson json) {
        priceTableService.delete(priceTableConverter.convertFrom(json));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        priceTableService.delete(priceTableService.findOne(id));
    }
}

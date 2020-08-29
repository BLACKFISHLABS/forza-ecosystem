package io.github.blackfishlabs.forza.product.resource;

import io.github.blackfishlabs.forza.core.application.search.FilterSearchParam;
import io.github.blackfishlabs.forza.core.helper.ResourcePaths;
import io.github.blackfishlabs.forza.core.infra.resource.APIMap;
import io.github.blackfishlabs.forza.product.converter.ProductConverter;
import io.github.blackfishlabs.forza.product.json.ProductJson;
import io.github.blackfishlabs.forza.product.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ResourcePaths.PRODUCT_PATH)
public class ProductResource implements APIMap {

    @Autowired
    private ProductConverter productConverter;
    @Autowired
    private ProductService productService;

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductResource.class);

    @GetMapping
    public List<ProductJson> getProducts(@RequestParam("cnpj") String cnpj,
                                         @RequestParam("ultimaAtualizacao") String lastUpdateTime) {
        LOGGER.info("call method getProducts()");
        return productConverter.convertListModelFrom(productService.findProductByCompany(cnpj, lastUpdateTime));
    }

    @GetMapping("/search")
    public List<ProductJson> search(@RequestParam("cnpj") String cnpj, FilterSearchParam filterSearchParam) {
        return productConverter.convertListModelFrom(productService.search(filterSearchParam, cnpj));
    }

    @GetMapping("/{id}")
    public ProductJson findOne(@PathVariable Long id) {
        return productConverter.convertFrom(productService.findOne(id));
    }

    @Deprecated
    @DeleteMapping
    public void delete(@RequestBody ProductJson json) {
        productService.delete(productConverter.convertFrom(json));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        productService.delete(productService.findOne(id));
    }
}

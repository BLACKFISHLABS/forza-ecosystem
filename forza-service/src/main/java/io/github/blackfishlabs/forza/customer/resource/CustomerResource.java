package io.github.blackfishlabs.forza.customer.resource;

import com.google.common.collect.Lists;
import io.github.blackfishlabs.forza.core.application.company.converter.CompanyConverter;
import io.github.blackfishlabs.forza.core.application.company.model.CompanyEntity;
import io.github.blackfishlabs.forza.core.application.company.service.CompanyService;
import io.github.blackfishlabs.forza.core.application.search.FilterSearchParam;
import io.github.blackfishlabs.forza.core.helper.ResourcePaths;
import io.github.blackfishlabs.forza.core.infra.resource.APIMap;
import io.github.blackfishlabs.forza.customer.converter.CustomerConverter;
import io.github.blackfishlabs.forza.customer.json.CustomerJson;
import io.github.blackfishlabs.forza.customer.model.CustomerEntity;
import io.github.blackfishlabs.forza.customer.service.CustomerService;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(ResourcePaths.CUSTOMER_PATH)
public class CustomerResource implements APIMap {

    @Autowired
    private CustomerService customerService;
    @Autowired
    private CustomerConverter customerConverter;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private CompanyConverter companyConverter;

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerResource.class);

    @GetMapping
    public List<CustomerJson> getCustomerByCNPJ(@RequestParam("cnpj") String cnpj) {
        LOGGER.info("call method getCustomerByCNPJ()");
        return customerConverter.convertListModelFrom(customerService.findCustomerByCompany(cnpj));
    }

    @GetMapping("/mobile")
    public List<CustomerJson> getCustomerByChargeMobile(@RequestParam("cnpj") String cnpj, @RequestParam("code") String chargeCode) {
        LOGGER.info("call getCustomerByChargeMobile()");
        return customerConverter.convertListModelFrom(customerService.findCustomerByCompanyAndCharge(cnpj, chargeCode));
    }

    @GetMapping("/search")
    public List<CustomerJson> getCustomerSearch(@RequestParam("cnpj") String cnpj) {
        LOGGER.info("call getCustomerSearch()");
        List<CustomerEntity> customer = customerService.findCustomerByCompany(cnpj);
        return customerConverter.convertListModelFrom(customer);
    }

    @GetMapping("/search/filter")
    public List<CustomerJson> searchCustomerByFilter(@RequestParam("cnpj") String cnpj, FilterSearchParam filterSearchParam) {
        LOGGER.info("call searchCustomerByFilter()");
        return customerConverter.convertListModelFrom(customerService.search(filterSearchParam, cnpj));
    }

    @PostMapping
    public CustomerJson save(@RequestParam("cnpj") String cnpj, @RequestBody CustomerJson json) {
        CompanyEntity company = companyService.findByCnpj(cnpj);

        json.setCompanyJson(companyConverter.convertFrom(company));
        json.setCode("CLI-" + RandomStringUtils.randomAlphanumeric(6));
        json.setActive(true);
        json.setLastChangeTime(new Date());

        CustomerEntity entity = customerService.insert(customerConverter.convertFrom(json));
        return customerConverter.convertFrom(entity);
    }

    @PutMapping
    public List<CustomerJson> update(@RequestParam("cnpj") String cnpj, @RequestBody List<CustomerJson> json) {
        CompanyEntity company = companyService.findByCnpj(cnpj);

        List<CustomerJson> customers = Lists.newArrayList();
        for (CustomerJson customerJson : json) {
            // Change Id App To Local Database Id For Update Correct
            customerJson.setId(customerJson.getCustomerId());
            customerJson.setCompanyJson(companyConverter.convertFrom(company));
            customerJson.setLastChangeTime(new Date());

            CustomerEntity entity = customerService.update(customerConverter.convertFrom(customerJson));
            customers.add(customerConverter.convertFrom(entity));
        }

        return customers;
    }

    @GetMapping("/update")
    public List<CustomerJson> getCustomersUpdates(
            @RequestParam("cnpj") String cnpj,
            @RequestParam("ultimaAtualizacao") String lastUpdateTime) {
        LOGGER.info("call getCustomersUpdates()");
        return customerConverter.convertListModelFrom(customerService.findCustomerByCompanyAndLastUpdate(cnpj, lastUpdateTime));
    }

    @GetMapping("/{id}")
    public CustomerJson findOne(@PathVariable Long id) {
        return customerConverter.convertFrom(customerService.findOne(id));
    }

    @Deprecated
    @DeleteMapping
    public void delete(@RequestBody CustomerJson json) {
        customerService.delete(customerConverter.convertFrom(json));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        customerService.delete(customerService.findOne(id));
    }
}

package io.github.blackfishlabs.forza.charge.resource;

import com.google.common.collect.Lists;
import io.github.blackfishlabs.forza.charge.converter.ChargeConverter;
import io.github.blackfishlabs.forza.charge.json.ChargeCustomerJson;
import io.github.blackfishlabs.forza.charge.json.ChargeItemJson;
import io.github.blackfishlabs.forza.charge.json.ChargeJson;
import io.github.blackfishlabs.forza.charge.model.ChargeEntity;
import io.github.blackfishlabs.forza.charge.service.ChargeService;
import io.github.blackfishlabs.forza.core.application.address.converter.CityConverter;
import io.github.blackfishlabs.forza.core.application.address.service.CityService;
import io.github.blackfishlabs.forza.core.application.company.converter.CompanyConverter;
import io.github.blackfishlabs.forza.core.application.company.model.CompanyEntity;
import io.github.blackfishlabs.forza.core.application.company.service.CompanyService;
import io.github.blackfishlabs.forza.core.application.search.FilterSearchParam;
import io.github.blackfishlabs.forza.core.helper.ResourcePaths;
import io.github.blackfishlabs.forza.core.infra.resource.APIMap;
import io.github.blackfishlabs.forza.customer.converter.CustomerConverter;
import io.github.blackfishlabs.forza.customer.converter.RouteConverter;
import io.github.blackfishlabs.forza.customer.json.CustomerJson;
import io.github.blackfishlabs.forza.customer.json.RouteCustomerJson;
import io.github.blackfishlabs.forza.customer.json.RouteJson;
import io.github.blackfishlabs.forza.customer.model.CustomerEntity;
import io.github.blackfishlabs.forza.customer.model.RouteEntity;
import io.github.blackfishlabs.forza.customer.service.CustomerService;
import io.github.blackfishlabs.forza.customer.service.RouteService;
import io.github.blackfishlabs.forza.pricetable.converter.PriceTableConverter;
import io.github.blackfishlabs.forza.pricetable.json.PriceTableItemJson;
import io.github.blackfishlabs.forza.pricetable.json.PriceTableJson;
import io.github.blackfishlabs.forza.pricetable.model.PriceTableEntity;
import io.github.blackfishlabs.forza.pricetable.service.PriceTableService;
import io.github.blackfishlabs.forza.product.converter.ProductConverter;
import io.github.blackfishlabs.forza.product.json.ProductJson;
import io.github.blackfishlabs.forza.product.model.ProductEntity;
import io.github.blackfishlabs.forza.product.service.ProductService;
import io.github.blackfishlabs.forza.salesman.model.SalesmanEntity;
import io.github.blackfishlabs.forza.salesman.model.VehicleEntity;
import io.github.blackfishlabs.forza.salesman.service.SalesmanService;
import io.github.blackfishlabs.forza.salesman.service.VehicleService;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

import static com.google.common.base.Strings.isNullOrEmpty;
import static java.lang.String.format;
import static java.util.Objects.isNull;

@RestController
@RequestMapping(ResourcePaths.CHARGE_PATH)
public class ChargeResource implements APIMap {

    @Autowired
    private ChargeConverter chargeConverter;
    @Autowired
    private ChargeService chargeService;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private CompanyConverter companyConverter;
    @Autowired
    private SalesmanService salesmanService;
    @Autowired
    private VehicleService vehicleService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private CustomerConverter customerConverter;
    @Autowired
    private RouteService routeService;
    @Autowired
    private RouteConverter routeConverter;
    @Autowired
    private CityService cityService;
    @Autowired
    private CityConverter cityConverter;
    @Autowired
    private PriceTableService priceTableService;
    @Autowired
    private PriceTableConverter priceTableConverter;
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductConverter productConverter;

    private static final Logger LOGGER = LoggerFactory.getLogger(ChargeResource.class);

    @PutMapping
    public void update(@RequestBody ChargeJson json) {
        chargeService.update(chargeConverter.convertFrom(json));
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody ChargeJson json) {
        if (isNull(json.getCompanyJson())) {
            if (verifyCompany(json)) {
                String message = format("[ERRO]: Não foi encontrado uma empresa no CNPJ %s para integração da carga.", json.getEmitterCode());
                LOGGER.warn(message);
                return ResponseEntity.badRequest().body(message);
            }

            if (verifySalesman(json)) {
                String message = format("[ERRO]: Não foi encontrado um vendedor no CPF %s para integração da carga.", json.getSalesmanCode());
                LOGGER.warn(message);
                return ResponseEntity.badRequest().body(message);
            }

            if (verifyVehicle(json)) {
                String message = format("[ERRO]: Não foi encontrado um veiculo com a placa %s para integração da carga.", json.getVehicle().getPlate());
                LOGGER.warn(message);
                return ResponseEntity.badRequest().body(message);
            }

            if (verifyResumeCode(json)) {
                String message = format("[ERRO]: Código da Carga já existe. %s", json.getCode());
                LOGGER.warn(message);
                return ResponseEntity.badRequest().body(message);
            }

            try {
                LOGGER.info("Carregando carga ...");
                json.setCompanyJson(companyConverter.convertFrom(companyService.findByCnpj(json.getEmitterCode())));
                createOrUpdateRoute(json);
                createOrUpdatePriceTable(json);

                if (isNullOrEmpty(json.getCode()))
                    json.setCode("RES-" + RandomStringUtils.randomAlphanumeric(6));

                chargeService.insert(chargeConverter.convertFrom(json));

                String message = String.format("[OK]: Carga %s adicionada com sucesso", json.getCode());
                LOGGER.info(message);
                return ResponseEntity.ok().body(message);
            } catch (Exception e) {
                String message = "[ERRO]: Houve um erro ao tentar salvar o objeto: " + e.getMessage();
                LOGGER.error(message);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
            }
        } else {
            String message = "[ERRO]: Arquivo de carga com objeto empresa não é válido para inserção.";
            LOGGER.error(message);
            return ResponseEntity.badRequest().body(message);
        }
    }

    private void createOrUpdateRoute(ChargeJson json) {
        List<CustomerJson> myCustomers = Lists.newArrayList();
        List<RouteCustomerJson> myRouteCustomers = Lists.newArrayList();

        json.getRoutes().forEach(route -> {
            List<RouteEntity> foundRoute = routeService.findByParamAndCompany(route.getCode(), json.getEmitterCode());

            if (foundRoute.isEmpty()) {
                route.getCustomers().forEach(customer -> {
                    List<CustomerEntity> foundCustomer = customerService.findByParamAndCompany(customer.getDocument(), json.getEmitterCode());
                    if (foundCustomer.isEmpty()) {
                        CustomerEntity newCustomer = createCustomer(json, customer);
                        myCustomers.add(customerConverter.convertFrom(newCustomer));
                    } else {
                        CustomerEntity updatedCustomer = updateCustomer(customer, foundCustomer);
                        myCustomers.add(customerConverter.convertFrom(updatedCustomer));
                    }
                });

                RouteJson routeJson = new RouteJson();
                routeJson.setCode(route.getCode());
                routeJson.setDescription(route.getDescription());
                routeJson.setActive(true);
                routeJson.setLastChangeTime(new Date());

                myCustomers.forEach(myCustomer -> {
                    RouteCustomerJson routeCustomerJson = new RouteCustomerJson();
                    routeCustomerJson.setCustomerId(myCustomer.getCustomerId());
                    routeCustomerJson.setCustomer(myCustomer);
                    routeCustomerJson.setLastChangeTime(new Date());

                    myRouteCustomers.add(routeCustomerJson);
                });

                routeJson.setCustomers(myRouteCustomers);
                routeJson.setCompanyJson(json.getCompanyJson());

                routeService.insert(routeConverter.convertFrom(routeJson));
            } else {
                routeService.delete(foundRoute.get(0));
                createOrUpdateRoute(json);
            }
        });
    }

    private void createOrUpdatePriceTable(ChargeJson json) {
        List<PriceTableItemJson> myPriceTableItems = Lists.newArrayList();

        json.getChargeBlock().getTables().forEach(pricetable -> {
            List<PriceTableEntity> foundPriceTable = priceTableService.findByParamAndCompany(pricetable.getCode(), json.getEmitterCode());
            if (foundPriceTable.isEmpty()) {
                pricetable.getItems().forEach(item -> {
                    PriceTableItemJson priceTableItemJson = new PriceTableItemJson();

                    priceTableItemJson.setLastChangeTime(new Date());
                    priceTableItemJson.setSalesPrice(item.getPrice());
                    priceTableItemJson.setStockQuantity(item.getStockQuantity());

                    List<ProductEntity> foundProduct = productService.findByCodeAndCompany(item.getProduct().getCode(), json.getEmitterCode());
                    ProductEntity product;
                    if (foundProduct.isEmpty()) {
                        product = createProduct(json, item);
                    } else {
                        product = updateProduct(item, foundProduct);
                    }

                    priceTableItemJson.setProduct(productConverter.convertFrom(product));
                    priceTableItemJson.setProductId(product.getId());

                    myPriceTableItems.add(priceTableItemJson);
                });

                PriceTableJson priceTableJson = new PriceTableJson();

                priceTableJson.setActive(true);
                priceTableJson.setCompanyId(json.getCompanyJson().getId());
                priceTableJson.setLastChangeTime(new Date());

                priceTableJson.setCode(pricetable.getCode());
                priceTableJson.setDescription(pricetable.getName());

                priceTableJson.setItems(myPriceTableItems);

                priceTableService.insert(priceTableConverter.convertFrom(priceTableJson));
            } else {
                priceTableService.delete(foundPriceTable.get(0));
                createOrUpdatePriceTable(json);
            }
        });
    }

    private ProductEntity createProduct(ChargeJson json, ChargeItemJson item) {
        ProductJson productJson = new ProductJson();

        productJson.setActive(true);
        productJson.setCompanyJson(json.getCompanyJson());
        productJson.setLastChangeTime(new Date());

        productJson.setCode(item.getProduct().getCode());
        productJson.setBarCode(item.getProduct().getEan());
        productJson.setDescription(item.getProduct().getDescription());
        productJson.setGroup(item.getProduct().getGroup());
        productJson.setObservation(item.getProduct().getNote());
        productJson.setUnit(item.getProduct().getUnit());

        return productService.insert(productConverter.convertFrom(productJson));
    }

    private ProductEntity updateProduct(ChargeItemJson item, List<ProductEntity> foundProduct) {
        ProductJson productJson = productConverter.convertFrom(foundProduct.get(0));

        productJson.setActive(true);
        productJson.setLastChangeTime(new Date());

        productJson.setBarCode(item.getProduct().getEan());
        productJson.setDescription(item.getProduct().getDescription());
        productJson.setGroup(item.getProduct().getGroup());
        productJson.setObservation(item.getProduct().getNote());
        productJson.setUnit(item.getProduct().getUnit());

        return productService.update(productConverter.convertFrom(productJson));
    }

    private CustomerEntity createCustomer(ChargeJson json, ChargeCustomerJson customer) {
        CustomerJson customerJson = new CustomerJson();

        customerJson.setActive(true);
        customerJson.setCompanyJson(json.getCompanyJson());
        customerJson.setLastChangeTime(new Date());

        customerJson.setNeighborhood(customer.getNeighborhood());
        customerJson.setPostalCode(customer.getPostalCode());
        customerJson.setCityJson(cityConverter.convertFrom(cityService.findByCode(Integer.parseInt(customer.getCity()))));
        customerJson.setCode(customer.getCode());
        customerJson.setAddressComplement(customer.getComplement());
        customerJson.setContact(customer.getContact());
        customerJson.setCpfOrCnpj(customer.getDocument());
        customerJson.setEmail(customer.getEmail());
        customerJson.setAddress(customer.getAddress());
        customerJson.setName(customer.getName());
        customerJson.setFantasyName(customer.getFantasyName());
        customerJson.setAddressNumber(customer.getNumber());
        customerJson.setMainPhone(customer.getPhone());
        customerJson.setSecondaryPhone(customer.getSecondaryPhone());
        customerJson.setType(customer.getTypePerson());
        customerJson.setIe(customer.getIe());
        customerJson.setBuyer(customer.getBuyer());

        return customerService.insert(customerConverter.convertFrom(customerJson));
    }

    private CustomerEntity updateCustomer(ChargeCustomerJson customer, List<CustomerEntity> foundCustomer) {
        CustomerJson customerJson = customerConverter.convertFrom(foundCustomer.get(0));

        customerJson.setLastChangeTime(new Date());
        customerJson.setNeighborhood(customer.getNeighborhood());
        customerJson.setPostalCode(customer.getPostalCode());
        customerJson.setCityJson(cityConverter.convertFrom(cityService.findByCode(Integer.parseInt(customer.getCity()))));
        customerJson.setCode(customer.getCode());
        customerJson.setAddressComplement(customer.getComplement());
        customerJson.setContact(customer.getContact());
        customerJson.setCpfOrCnpj(customer.getDocument());
        customerJson.setEmail(customer.getEmail());
        customerJson.setAddress(customer.getAddress());
        customerJson.setName(customer.getName());
        customerJson.setFantasyName(customer.getFantasyName());
        customerJson.setAddressNumber(customer.getNumber());
        customerJson.setMainPhone(customer.getPhone());
        customerJson.setSecondaryPhone(customer.getSecondaryPhone());
        customerJson.setType(customer.getTypePerson());
        customerJson.setIe(customer.getIe());
        customerJson.setBuyer(customer.getBuyer());

        return customerService.update(customerConverter.convertFrom(customerJson));
    }

    private boolean verifyVehicle(@RequestBody ChargeJson json) {
        List<VehicleEntity> vehicle = vehicleService.findVehicleByPlate(json.getVehicle().getPlate());
        return vehicle.isEmpty();
    }

    private boolean verifySalesman(@RequestBody ChargeJson json) {
        SalesmanEntity salesman = salesmanService.findSalesman(json.getSalesmanCode());
        return isNull(salesman);
    }

    private boolean verifyCompany(@RequestBody ChargeJson json) {
        CompanyEntity company = companyService.findByCnpj(json.getEmitterCode());
        return isNull(company);
    }

    private boolean verifyResumeCode(@RequestBody ChargeJson json) {
        ChargeEntity charge = chargeService.searchByChargeCode(json.getCode(), json.getEmitterCode());
        return !isNull(charge);
    }

    @Deprecated
    @DeleteMapping
    public void delete(@RequestBody ChargeJson json) {
        chargeService.delete(chargeConverter.convertFrom(json));
    }

    @GetMapping("/{id}")
    public ChargeJson findOne(@PathVariable Long id) {
        return chargeConverter.convertFrom(chargeService.findOne(id));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        chargeService.delete(chargeService.findOne(id));
    }

    @GetMapping("/search")
    public List<ChargeJson> searchCharge(@RequestParam("cnpj") String cnpj, FilterSearchParam filterSearchParam) {
        LOGGER.info("call searchCharge()");
        return chargeConverter.convertListModelFrom(chargeService.search(filterSearchParam, cnpj));
    }

    @GetMapping("/search/mobile")
    public ChargeJson searchChargeMobile(@RequestParam("code") String code,
                                         @RequestParam("cnpj") String cnpj,
                                         @RequestParam("salesman") String salesman) {
        LOGGER.info("call searchChargeMobile()");
        return chargeConverter.convertFrom(chargeService.searchByCharge(code, cnpj, salesman));
    }
}

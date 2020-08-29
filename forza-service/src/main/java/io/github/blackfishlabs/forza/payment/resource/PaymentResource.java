package io.github.blackfishlabs.forza.payment.resource;

import io.github.blackfishlabs.forza.core.helper.ResourcePaths;
import io.github.blackfishlabs.forza.core.infra.resource.APIMap;
import io.github.blackfishlabs.forza.payment.converter.PaymentConverter;
import io.github.blackfishlabs.forza.payment.json.PaymentJson;
import io.github.blackfishlabs.forza.payment.service.PaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(ResourcePaths.PAYMENT_PATH)
public class PaymentResource implements APIMap {

    @Autowired
    private PaymentService paymentService;
    @Autowired
    private PaymentConverter paymentConverter;

    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentResource.class);

    @GetMapping
    public List<PaymentJson> getPayment(@RequestParam("cnpj") String cnpj) {
        LOGGER.info("call method getPayment()");
        return paymentConverter.convertListModelFrom(paymentService.findPaymentByCompany(cnpj));
    }

    @GetMapping("/update")
    public List<PaymentJson> getPaymentByLastUpdate(@RequestParam("cnpj") String cnpj,
                                                    @RequestParam("ultimaAtualizacao") String lastUpdateTime) {
        LOGGER.info("call method getPaymentByLastUpdate()");
        return paymentConverter.convertListModelFrom(paymentService.findPaymentByCompanyAndLastUpdate(cnpj, lastUpdateTime));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public PaymentJson findOne(@PathVariable Long id) {
        return paymentConverter.convertFrom(paymentService.findOne(id));
    }

    @PutMapping
    public void update(@RequestBody PaymentJson json) {
        json.setLastChangeTime(new Date());
        paymentService.update(paymentConverter.convertFrom(json));
    }

    @PostMapping
    public void save(@RequestBody PaymentJson json) {
        json.setLastChangeTime(new Date());
        paymentService.insert(paymentConverter.convertFrom(json));
    }

    @Deprecated
    @DeleteMapping
    public void delete(@RequestBody PaymentJson json) {
        paymentService.delete(paymentConverter.convertFrom(json));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        paymentService.delete(paymentService.findOne(id));
    }

}

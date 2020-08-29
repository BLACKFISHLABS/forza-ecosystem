package io.github.blackfishlabs.forza.order.resource;

import io.github.blackfishlabs.forza.core.application.company.converter.CompanyConverter;
import io.github.blackfishlabs.forza.core.application.company.model.CompanyEntity;
import io.github.blackfishlabs.forza.core.application.company.service.CompanyService;
import io.github.blackfishlabs.forza.core.application.search.FilterSearchParam;
import io.github.blackfishlabs.forza.core.helper.ResourcePaths;
import io.github.blackfishlabs.forza.core.infra.resource.APIMap;
import io.github.blackfishlabs.forza.customer.service.CustomerService;
import io.github.blackfishlabs.forza.order.converter.OrderConverter;
import io.github.blackfishlabs.forza.order.json.OrderJson;
import io.github.blackfishlabs.forza.order.model.OrderEntity;
import io.github.blackfishlabs.forza.order.model.OrderItemEntity;
import io.github.blackfishlabs.forza.order.model.OrderStatus;
import io.github.blackfishlabs.forza.order.service.OrderService;
import io.github.blackfishlabs.forza.payment.service.PaymentService;
import io.github.blackfishlabs.forza.salesman.converter.SalesmanConverter;
import io.github.blackfishlabs.forza.salesman.model.SalesmanEntity;
import io.github.blackfishlabs.forza.salesman.service.SalesmanService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.google.common.base.MoreObjects.firstNonNull;
import static java.util.Objects.isNull;

@RestController
@RequestMapping(ResourcePaths.ORDER_PATH)
public class OrderResource implements APIMap {

    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderConverter orderConverter;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private CompanyConverter companyConverter;
    @Autowired
    private SalesmanService salesmanService;
    @Autowired
    private SalesmanConverter salesmanConverter;
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private CustomerService customerService;

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderResource.class);

    @PostMapping
    public OrderJson save(@RequestParam("cnpj") String cnpj,
                          @RequestParam("cpfCnpj") String cpfCnpj,
                          @RequestBody OrderJson json) {
        CompanyEntity byCnpj = companyService.findByCnpj(cnpj);
        if (isNull(byCnpj)) {
            String message = "Empresa não encontrada: " + cnpj;
            LOGGER.error(message);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, message);
        }

        SalesmanEntity byCpfCnpj = salesmanService.findSalesman(cpfCnpj);
        if (isNull(byCpfCnpj)) {
            String message = "Vendedor não encontrada: " + cpfCnpj;
            LOGGER.error(message);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, message);
        }

        if (isNull(paymentService.findOne(json.getPaymentMethodId()))) {
            String message = "Forma de Pagamento não encontrada: " + json.getPaymentMethodId();
            LOGGER.error(message);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, message);
        }

        if (isNull(customerService.findOne(json.getCustomerId()))) {
            String message = "Cliente não encontrado: " + json.getCustomerId();
            LOGGER.error(message);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, message);
        }

        json.setLastChangeTime(new Date());
        json.setCompanyJson(companyConverter.convertFrom(byCnpj));
        json.setSalesmanJson(salesmanConverter.convertFrom(byCpfCnpj));

        OrderEntity entity = orderConverter.convertFrom(json);
        return orderConverter.convertFrom(orderService.insert(entity));
    }

    @PutMapping
    public void update(@RequestBody OrderJson json) {
        orderService.update(orderConverter.convertFrom(json));
    }

    @GetMapping
    public List<OrderJson> getOrder(@RequestParam("cnpj") String cnpj,
                                    @RequestParam("cpfCnpj") String cpfCnpj,
                                    @RequestParam("ultimaAtualizacao") String lastUpdateTime) {
        LOGGER.info("call method getOrder()");
        return orderConverter.convertListModelFrom(orderService.findOrderByCompanyAndSalesmanAndLastUpdate(cnpj, cpfCnpj, lastUpdateTime));
    }

    @GetMapping("/mobile")
    public ResponseEntity<?> getOrderMobile(@RequestParam("cnpj") String cnpj) {
        LOGGER.info("call method getOrderMobile()");
        CompanyEntity byCnpj = companyService.findByCnpj(cnpj);
        List<OrderEntity> orderMobile = orderService.findOrderMobile(byCnpj.getId(), OrderStatus.STATUS_CREATED.name());
        if (orderMobile.isEmpty()) {
            String message = "[ERRO] - Lista de vendas vazia!";
            LOGGER.warn(message);
            return ResponseEntity.badRequest().body(message);
        }

        String divider = " ";
        StringBuilder arq = new StringBuilder();
        StringBuilder fullBodyOrder = new StringBuilder();
        String head = "";
        String aux = "";
        String lineFeed = Character.toString((char) 13);

        for (OrderEntity order : orderMobile) {
            if (!aux.equals(firstNonNull(order.getResumeCode(), "*"))) {
                head = "1" + StringUtils.rightPad(firstNonNull(order.getResumeCode(), "*"), 239, '_')
                        .concat(lineFeed);
            }
            aux = firstNonNull(order.getResumeCode(), "*");

            String body = "3" + StringUtils.rightPad(String.valueOf(order.getId()), 10, '_') + divider +
                    StringUtils.rightPad(order.getCustomerCode(), 14, '_') + divider +
                    StringUtils.rightPad(order.getPaymentCode(), 6, '_') + divider +
                    StringUtils.rightPad(new SimpleDateFormat("dd/MM/yyyy").format(order.getCreatedAt()), 10, '_') + divider +
                    StringUtils.leftPad(String.valueOf(orderService.calculateTotal(order.getId())), 12, '0') + divider +
                    StringUtils.leftPad(String.valueOf(order.getDiscount()), 12, '0') + divider +
                    StringUtils.rightPad(order.getStatus().toString(), 20, '_') + divider +
                    StringUtils.rightPad(firstNonNull(order.getObservation(), ""), 148, '_');

            StringBuilder fullBodyOrderItem = new StringBuilder();
            List<OrderItemEntity> orderItemMobile = orderService.findOrderItemMobile(order.getId());
            for (OrderItemEntity item : orderItemMobile) {
                String bodyItem = "5" + StringUtils.rightPad(item.getProductCode(), 20, '_') + divider +
                        StringUtils.rightPad(item.getUnitCode(), 6, '_') + divider +
                        StringUtils.leftPad(String.valueOf(item.getQuantity()), 12, '0') + divider +
                        StringUtils.leftPad(String.valueOf(item.getSalesPrice()), 12, '0') + divider +
                        StringUtils.leftPad(String.valueOf(item.getSubTotal()), 12, '0') + divider +
                        StringUtils.rightPad("", 172, '_');

                fullBodyOrderItem.append(bodyItem).append(lineFeed);
            }

            fullBodyOrder.append(head).append(body).append(lineFeed).append(fullBodyOrderItem);
        }

        String message = arq.append(fullBodyOrder.toString()).toString();
        LOGGER.info(message);
        return ResponseEntity.ok().body(message);
    }

    @GetMapping("/search")
    public List<OrderJson> getSearch(@RequestParam("cnpj") String cnpj) {
        List<OrderEntity> customer = orderService.findOrderByCompany(cnpj);
        return orderConverter.convertListModelFrom(customer);
    }

    @GetMapping("/search/filter")
    public List<OrderJson> searchOrderByFilter(@RequestParam("cnpj") String cnpj, FilterSearchParam filterSearchParam) {
        LOGGER.info("call method searchOrderByFilter()");
        return orderConverter.convertListModelFrom(orderService.search(filterSearchParam, cnpj));
    }

    @GetMapping("/{id}")
    public OrderJson findOne(@PathVariable Long id) {
        return orderConverter.convertFrom(orderService.findOne(id));
    }

    @Deprecated
    @DeleteMapping
    public void delete(@RequestBody OrderJson json) {
        orderService.delete(orderConverter.convertFrom(json));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        orderService.delete(orderService.findOne(id));
    }
}

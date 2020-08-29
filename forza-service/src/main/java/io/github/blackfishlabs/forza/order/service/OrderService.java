package io.github.blackfishlabs.forza.order.service;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import io.github.blackfishlabs.forza.core.application.search.FilterSearchParam;
import io.github.blackfishlabs.forza.core.infra.service.GenericService;
import io.github.blackfishlabs.forza.order.model.OrderEntity;
import io.github.blackfishlabs.forza.order.model.OrderItemEntity;
import io.github.blackfishlabs.forza.order.model.OrderStatus;
import io.github.blackfishlabs.forza.order.repository.OrderItemRepository;
import io.github.blackfishlabs.forza.order.repository.OrderRepository;
import io.github.blackfishlabs.forza.order.repository.OrderSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
@EnableScheduling
public class OrderService extends GenericService<OrderEntity, Long> {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderService.class);

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;

    public List<OrderEntity> findOrderByCompanyAndSalesmanAndLastUpdate(String cnpj, String cpfCnpj, String lastUpdateTime) {
        return orderRepository.findAll(OrderSpecification.findByCompanyAndSalesmanAndLastUpdate(cnpj, cpfCnpj, lastUpdateTime));
    }

    public List<OrderEntity> findOrderMobile(Long code, String status) {
        List<OrderEntity> response = Lists.newArrayList();

        response.addAll(orderRepository.findOrderMobile(code, status));
        response.sort(Comparator.comparing(OrderEntity::getIssueDate));

        Collections.reverse(response);
        return response;
    }

    public List<OrderItemEntity> findOrderItemMobile(Long code) {
        return orderItemRepository.findOrderItemMobile(code);
    }

    public List<OrderEntity> findOrderByCompany(String cnpj) {
        List<OrderEntity> response = Lists.newArrayList();

        response.addAll(orderRepository.findAll(OrderSpecification.findByCompany(cnpj)));
        response.sort(Comparator.comparing(OrderEntity::getIssueDate));

        Collections.reverse(response);
        return response;
    }

    public List<OrderEntity> search(FilterSearchParam filter, String cnpj) {
        List<OrderEntity> response = Lists.newArrayList();

        if (!Strings.isNullOrEmpty(filter.getDescription()))
            response.addAll(orderRepository.findAll(OrderSpecification.findByDescription(filter.getDescription(), cnpj)));
        else
            response.addAll(orderRepository.findAll(OrderSpecification.findByCompany(cnpj)));

        response.sort(Comparator.comparing(OrderEntity::getIssueDate));
        Collections.reverse(response);
        return response;
    }

    public OrderEntity findOne(Long id) {
        return orderRepository.getOne(id);
    }

    public double calculateTotal(Long id) {
        double sumSubtotal = 0.0;

        OrderEntity order = orderRepository.getOne(id);

        for (OrderItemEntity item : order.getItems()) {
            sumSubtotal += item.getSubTotal();
        }

        return round(sumSubtotal, 2);
    }

    private double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    @Scheduled(fixedDelay = 86400000, initialDelay = 60000)
    public void getOlderOrderInvoicedAndRemove() {
        for (OrderEntity order : orderRepository.findAll(OrderSpecification.findByStatus(OrderStatus.STATUS_INVOICED))) {
            int daysDiff = getDifferenceDays(order.getLastChangeTime(), Calendar.getInstance().getTime());
            LOGGER.info("AUTO DELETE OLDER ORDER: " + daysDiff);
            if (daysDiff > 7) {
                LOGGER.warn("AUTO DELETE OLDER ORDER: " + order.getOrderId());
                orderRepository.delete(order);
            }
        }
    }

    private static int getDifferenceDays(Date date1, Date date2) {
        int daysDiff;
        long diff = date2.getTime() - date1.getTime();
        long diffDays = diff / (24 * 60 * 60 * 1000) + 1;
        daysDiff = (int) diffDays;
        return daysDiff;
    }
}

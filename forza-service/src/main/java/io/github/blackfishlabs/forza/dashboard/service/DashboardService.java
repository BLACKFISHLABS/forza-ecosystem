package io.github.blackfishlabs.forza.dashboard.service;

import io.github.blackfishlabs.forza.customer.service.CustomerService;
import io.github.blackfishlabs.forza.dashboard.json.DashboardJson;
import io.github.blackfishlabs.forza.order.service.OrderService;
import io.github.blackfishlabs.forza.product.service.ProductService;
import io.github.blackfishlabs.forza.salesman.service.SalesmanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {

    @Autowired
    private OrderService orderService;
    @Autowired
    private ProductService productService;
    @Autowired
    private SalesmanService salesmanService;
    @Autowired
    private CustomerService customerService;

    public DashboardJson calculate(String doc) {
        DashboardJson json = new DashboardJson();

        json.setSales(orderService.findOrderByCompany(doc).size());
        json.setProducts(productService.findProductByCompany(doc).size());
        json.setSalesman(salesmanService.findSalesmanByCompany(doc).size());
        json.setCustomers(customerService.findCustomerByCompany(doc).size());

        return json;
    }
}

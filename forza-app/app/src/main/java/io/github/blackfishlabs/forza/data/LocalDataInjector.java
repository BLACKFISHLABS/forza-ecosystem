package io.github.blackfishlabs.forza.data;

import io.github.blackfishlabs.forza.data.city.CityRealmMapper;
import io.github.blackfishlabs.forza.data.city.CityRealmRepository;
import io.github.blackfishlabs.forza.data.city.CityRepository;
import io.github.blackfishlabs.forza.data.city.StateRealmMapper;
import io.github.blackfishlabs.forza.data.city.StateRealmRepository;
import io.github.blackfishlabs.forza.data.city.StateRepository;
import io.github.blackfishlabs.forza.data.company.CompanyRealmMapper;
import io.github.blackfishlabs.forza.data.company.customer.CompanyCustomerRealmMapper;
import io.github.blackfishlabs.forza.data.company.customer.CompanyCustomerRealmRepository;
import io.github.blackfishlabs.forza.data.company.customer.CompanyCustomerRepository;
import io.github.blackfishlabs.forza.data.company.paymentmethod.CompanyPaymentMethodRealmMapper;
import io.github.blackfishlabs.forza.data.company.paymentmethod.CompanyPaymentMethodRealmRepository;
import io.github.blackfishlabs.forza.data.company.paymentmethod.CompanyPaymentMethodRepository;
import io.github.blackfishlabs.forza.data.company.pricetable.CompanyPriceTableRealmMapper;
import io.github.blackfishlabs.forza.data.company.pricetable.CompanyPriceTableRealmRepository;
import io.github.blackfishlabs.forza.data.company.pricetable.CompanyPriceTableRepository;
import io.github.blackfishlabs.forza.data.customer.CustomerRealmMapper;
import io.github.blackfishlabs.forza.data.customer.CustomerRealmRepository;
import io.github.blackfishlabs.forza.data.customer.CustomerRepository;
import io.github.blackfishlabs.forza.data.order.OrderItemRealmMapper;
import io.github.blackfishlabs.forza.data.order.OrderItemRealmRepository;
import io.github.blackfishlabs.forza.data.order.OrderItemRepository;
import io.github.blackfishlabs.forza.data.order.OrderRealmMapper;
import io.github.blackfishlabs.forza.data.order.OrderRealmRepository;
import io.github.blackfishlabs.forza.data.order.OrderRepository;
import io.github.blackfishlabs.forza.data.paymentmethod.PaymentMethodRealmMapper;
import io.github.blackfishlabs.forza.data.paymentmethod.PaymentMethodRealmRepository;
import io.github.blackfishlabs.forza.data.paymentmethod.PaymentMethodRepository;
import io.github.blackfishlabs.forza.data.pricetable.PriceTableItemRealmMapper;
import io.github.blackfishlabs.forza.data.pricetable.PriceTableRealmMapper;
import io.github.blackfishlabs.forza.data.pricetable.PriceTableRealmRepository;
import io.github.blackfishlabs.forza.data.pricetable.PriceTableRepository;
import io.github.blackfishlabs.forza.data.product.ProductRealmMapper;
import io.github.blackfishlabs.forza.data.product.ProductRealmRepository;
import io.github.blackfishlabs.forza.data.product.ProductRepository;
import io.github.blackfishlabs.forza.data.repository.Mapper;
import io.github.blackfishlabs.forza.domain.entity.CityEntity;
import io.github.blackfishlabs.forza.domain.entity.CompanyCustomerEntity;
import io.github.blackfishlabs.forza.domain.entity.CompanyEntity;
import io.github.blackfishlabs.forza.domain.entity.CompanyPaymentMethodEntity;
import io.github.blackfishlabs.forza.domain.entity.CompanyPriceTableEntity;
import io.github.blackfishlabs.forza.domain.entity.CustomerEntity;
import io.github.blackfishlabs.forza.domain.entity.OrderEntity;
import io.github.blackfishlabs.forza.domain.entity.OrderItemEntity;
import io.github.blackfishlabs.forza.domain.entity.PaymentMethodEntity;
import io.github.blackfishlabs.forza.domain.entity.PriceTableEntity;
import io.github.blackfishlabs.forza.domain.entity.PriceTableItemEntity;
import io.github.blackfishlabs.forza.domain.entity.ProductEntity;
import io.github.blackfishlabs.forza.domain.entity.StateEntity;
import io.github.blackfishlabs.forza.domain.pojo.City;
import io.github.blackfishlabs.forza.domain.pojo.Company;
import io.github.blackfishlabs.forza.domain.pojo.CompanyCustomer;
import io.github.blackfishlabs.forza.domain.pojo.CompanyPaymentMethod;
import io.github.blackfishlabs.forza.domain.pojo.CompanyPriceTable;
import io.github.blackfishlabs.forza.domain.pojo.Customer;
import io.github.blackfishlabs.forza.domain.pojo.Order;
import io.github.blackfishlabs.forza.domain.pojo.OrderItem;
import io.github.blackfishlabs.forza.domain.pojo.PaymentMethod;
import io.github.blackfishlabs.forza.domain.pojo.PriceTable;
import io.github.blackfishlabs.forza.domain.pojo.PriceTableItem;
import io.github.blackfishlabs.forza.domain.pojo.Product;
import io.github.blackfishlabs.forza.domain.pojo.State;

public class LocalDataInjector {

    private LocalDataInjector() {/* No instances */}

    private static Mapper<City, CityEntity> sCityMapper = null;

    public static Mapper<City, CityEntity> cityMapper() {
        if (sCityMapper == null) {
            sCityMapper = new CityRealmMapper();
        }
        return sCityMapper;
    }

    private static Mapper<State, StateEntity> sStateMapper = null;

    public static Mapper<State, StateEntity> stateMapper() {
        if (sStateMapper == null) {
            sStateMapper = new StateRealmMapper();
        }
        return sStateMapper;
    }

    private static Mapper<Company, CompanyEntity> sCompanyMapper = null;

    public static Mapper<Company, CompanyEntity> companyMapper() {
        if (sCompanyMapper == null) {
            sCompanyMapper = new CompanyRealmMapper();
        }
        return sCompanyMapper;
    }

    private static Mapper<CompanyPaymentMethod, CompanyPaymentMethodEntity> sCompanyPaymentMethodMapper = null;

    public static Mapper<CompanyPaymentMethod, CompanyPaymentMethodEntity> companyPaymentMethodMapper() {
        if (sCompanyPaymentMethodMapper == null) {
            sCompanyPaymentMethodMapper = new CompanyPaymentMethodRealmMapper();
        }
        return sCompanyPaymentMethodMapper;
    }

    private static Mapper<CompanyPriceTable, CompanyPriceTableEntity> sCompanyPriceTableMapper = null;

    public static Mapper<CompanyPriceTable, CompanyPriceTableEntity> companyPriceTableMapper() {
        if (sCompanyPriceTableMapper == null) {
            sCompanyPriceTableMapper = new CompanyPriceTableRealmMapper();
        }
        return sCompanyPriceTableMapper;
    }

    private static Mapper<Customer, CustomerEntity> sCustomerMapper = null;

    public static Mapper<Customer, CustomerEntity> customerMapper() {
        if (sCustomerMapper == null) {
            sCustomerMapper = new CustomerRealmMapper();
        }
        return sCustomerMapper;
    }

    private static Mapper<PriceTableItem, PriceTableItemEntity> sPriceTableItemMapper = null;

    public static Mapper<PriceTableItem, PriceTableItemEntity> priceTableItemMapper() {
        if (sPriceTableItemMapper == null) {
            sPriceTableItemMapper = new PriceTableItemRealmMapper();
        }
        return sPriceTableItemMapper;
    }

    private static Mapper<PaymentMethod, PaymentMethodEntity> sPaymentMethodMapper = null;

    public static Mapper<PaymentMethod, PaymentMethodEntity> paymentMethodMapper() {
        if (sPaymentMethodMapper == null) {
            sPaymentMethodMapper = new PaymentMethodRealmMapper();
        }
        return sPaymentMethodMapper;
    }


    private static Mapper<PriceTable, PriceTableEntity> sPriceTableMapper = null;

    public static Mapper<PriceTable, PriceTableEntity> priceTableMapper() {
        if (sPriceTableMapper == null) {
            sPriceTableMapper = new PriceTableRealmMapper();
        }
        return sPriceTableMapper;
    }

    private static Mapper<OrderItem, OrderItemEntity> sOrderItemMapper = null;

    public static Mapper<OrderItem, OrderItemEntity> orderItemMapper() {
        if (sOrderItemMapper == null) {
            sOrderItemMapper = new OrderItemRealmMapper();
        }
        return sOrderItemMapper;
    }

    private static Mapper<Order, OrderEntity> sOrderMapper = null;

    public static Mapper<Order, OrderEntity> orderMapper() {
        if (sOrderMapper == null) {
            sOrderMapper = new OrderRealmMapper();
        }
        return sOrderMapper;
    }


    private static Mapper<Product, ProductEntity> sProductMapper = null;

    public static Mapper<Product, ProductEntity> productMapper() {
        if (sProductMapper == null) {
            sProductMapper = new ProductRealmMapper();
        }
        return sProductMapper;
    }


    private static Mapper<CompanyCustomer, CompanyCustomerEntity> sCompanyCustomerMapper = null;

    public static Mapper<CompanyCustomer, CompanyCustomerEntity> companyCustomerMapper() {
        if (sCompanyCustomerMapper == null) {
            sCompanyCustomerMapper = new CompanyCustomerRealmMapper();
        }
        return sCompanyCustomerMapper;
    }

    public static CustomerRepository provideCustomerRepository() {
        return new CustomerRealmRepository();
    }

    public static PaymentMethodRepository providePaymentMethodRepository() {
        return new PaymentMethodRealmRepository();
    }

    public static PriceTableRepository providePriceTableRepository() {
        return new PriceTableRealmRepository();
    }

    public static CityRepository provideCityRepository() {
        return new CityRealmRepository();
    }

    public static CompanyCustomerRepository provideCompanyCustomerRepository() {
        return new CompanyCustomerRealmRepository();
    }

    public static CompanyPaymentMethodRepository provideCompanyPaymentMethodRepository() {
        return new CompanyPaymentMethodRealmRepository();
    }

    public static CompanyPriceTableRepository provideCompanyPriceTableRepository() {
        return new CompanyPriceTableRealmRepository();
    }

    public static StateRepository provideStateRepository() {
        return new StateRealmRepository();
    }

    public static OrderRepository providerOrderRepository() {
        return new OrderRealmRepository();
    }

    public static OrderItemRepository providerOrderItemRepository() {
        return new OrderItemRealmRepository();
    }

    public static ProductRepository provideProductRepository() {
        return new ProductRealmRepository();
    }
}

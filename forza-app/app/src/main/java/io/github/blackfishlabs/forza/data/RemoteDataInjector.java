package io.github.blackfishlabs.forza.data;

import io.github.blackfishlabs.forza.data.city.CityApi;
import io.github.blackfishlabs.forza.data.customer.CustomerApi;
import io.github.blackfishlabs.forza.data.order.OrderApi;
import io.github.blackfishlabs.forza.data.paymentmethod.PaymentMethodApi;
import io.github.blackfishlabs.forza.data.postalcode.PostalCodeApi;
import io.github.blackfishlabs.forza.data.pricetable.PriceTableApi;
import io.github.blackfishlabs.forza.data.product.ProductApi;
import io.github.blackfishlabs.forza.data.salesman.SalesmanApi;
import io.github.blackfishlabs.forza.data.sync.SyncApi;
import io.github.blackfishlabs.forza.ui.PresentationInjector;

public class RemoteDataInjector {

    private RemoteDataInjector() {/* No instances */}

    public static SalesmanApi provideSalesmanApi() {
        return PresentationInjector.provideRetrofit().create(SalesmanApi.class);
    }

    public static CustomerApi provideCustomerApi() {
        return PresentationInjector.provideRetrofit().create(CustomerApi.class);
    }

    public static PaymentMethodApi providePaymentMethodApi() {
        return PresentationInjector.provideRetrofit().create(PaymentMethodApi.class);
    }

    public static PriceTableApi providePriceTableApi() {
        return PresentationInjector.provideRetrofit().create(PriceTableApi.class);
    }

    public static CityApi provideCityApi() {
        return PresentationInjector.provideRetrofit().create(CityApi.class);
    }

    public static PostalCodeApi providePostalCodeApi() {
        return PresentationInjector.provideRetrofit().create(PostalCodeApi.class);
    }

    public static OrderApi provideOrderApi() {
        return PresentationInjector.provideRetrofit().create(OrderApi.class);
    }

    public static SyncApi provideSyncApi() {
        return PresentationInjector.provideRetrofit().create(SyncApi.class);
    }

    public static ProductApi provideProductApi() {
        return PresentationInjector.provideRetrofit().create(ProductApi.class);
    }
}


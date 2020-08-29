package io.github.blackfishlabs.forza.data.sync;

import android.content.Context;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;

import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.GcmTaskService;
import com.google.android.gms.gcm.PeriodicTask;
import com.google.android.gms.gcm.Task;
import com.google.android.gms.gcm.TaskParams;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.github.blackfishlabs.forza.data.LocalDataInjector;
import io.github.blackfishlabs.forza.data.RemoteDataInjector;
import io.github.blackfishlabs.forza.data.company.paymentmethod.CompanyPaymentMethodRepository;
import io.github.blackfishlabs.forza.data.customer.CustomerApi;
import io.github.blackfishlabs.forza.data.customer.CustomerRepository;
import io.github.blackfishlabs.forza.data.order.OrderApi;
import io.github.blackfishlabs.forza.data.order.OrderByIdSpecification;
import io.github.blackfishlabs.forza.data.order.OrderRepository;
import io.github.blackfishlabs.forza.data.order.OrderStatusSpecificationFilter;
import io.github.blackfishlabs.forza.data.order.OrdersByUserSpecification;
import io.github.blackfishlabs.forza.data.paymentmethod.PaymentMethodByIdSpecification;
import io.github.blackfishlabs.forza.data.paymentmethod.PaymentMethodRepository;
import io.github.blackfishlabs.forza.data.settings.SettingsRepository;
import io.github.blackfishlabs.forza.domain.dto.OrderDto;
import io.github.blackfishlabs.forza.domain.dto.OrderItemDto;
import io.github.blackfishlabs.forza.domain.dto.ServerStatus;
import io.github.blackfishlabs.forza.domain.pojo.CompanyPaymentMethod;
import io.github.blackfishlabs.forza.domain.pojo.LoggedUser;
import io.github.blackfishlabs.forza.domain.pojo.Order;
import io.github.blackfishlabs.forza.domain.pojo.OrderItem;
import io.github.blackfishlabs.forza.domain.pojo.OrderStatus;
import io.github.blackfishlabs.forza.domain.pojo.PaymentMethod;
import io.github.blackfishlabs.forza.ui.PresentationInjector;
import retrofit2.Response;
import timber.log.Timber;

public class SyncTaskService extends GcmTaskService {

    private OrderApi orderApi;
    private OrderRepository orderRepository;
    private String companyCnpj;
    private String salesmanCpfOrCnpj;

    public static boolean schedule(@NonNull Context context, @IntRange(from = 0) int periodInMinutes) {
        try {
            final long syncPeriodInSeconds = TimeUnit.MINUTES.toSeconds(periodInMinutes);

            if (PresentationInjector.provideSettingsRepository().isRunningSyncWith(syncPeriodInSeconds)) {
                Timber.v("sync service already scheduled with period of %d minutes", periodInMinutes);
                return false;
            }

            PeriodicTask periodicTask = new PeriodicTask.Builder()
                    .setService(SyncTaskService.class)
                    //repeat every 'n' minutes (default 30 minutes)
                    .setPeriod(syncPeriodInSeconds)
                    //tag that is unique to this task (can be used to cancel task)
                    .setTag(SyncTaskService.class.getSimpleName())
                    //whether the task persists after device reboot
                    .setPersisted(true)
                    //if another task with same tag is already scheduled, replace it with this task
                    .setUpdateCurrent(true)
                    //set required network state, this line is optional
                    .setRequiredNetwork(Task.NETWORK_STATE_CONNECTED)
                    //request that charging must be connected, this line is optional
                    .setRequiresCharging(false)
                    .build();
            GcmNetworkManager
                    .getInstance(context.getApplicationContext())
                    .schedule(periodicTask);

            PresentationInjector.provideSettingsRepository().setRunningSyncWith(syncPeriodInSeconds);
            Timber.v("sync service scheduled with period of %d minutes", periodInMinutes);
            return true;
        } catch (Exception e) {
            Timber.e(e, "scheduling sync service failed");
            return false;
        }
    }

    public static boolean cancelAll(@NonNull Context context) {
        try {
            GcmNetworkManager
                    .getInstance(context.getApplicationContext())
                    .cancelAllTasks(SyncTaskService.class);
            PresentationInjector.provideSettingsRepository().setRunningSyncWith(0);
            Timber.v("sync service cancelled");
            return true;
        } catch (Exception e) {
            Timber.e(e, "cancelling sync service failed");
            return false;
        }
    }

    @Override
    public void onInitializeTasks() {
        Timber.d("initializing sync service");
        SyncTaskService.cancelAll(this);
        int syncPeriodicity = PresentationInjector.provideSettingsRepository().getSettings().getSyncPeriodicity();
        SyncTaskService.schedule(this, syncPeriodicity);
    }

    @Override
    public int onRunTask(final TaskParams taskParams) {
        Timber.d("running sync service");

        final SettingsRepository settingsRepository = PresentationInjector.provideSettingsRepository();

        if (!settingsRepository.isUserLoggedIn()) {
            Timber.i("No user logged, sync will be cancelled");
            SyncTaskService.cancelAll(this);
            return GcmNetworkManager.RESULT_FAILURE;
        }

        final LoggedUser loggedUser = settingsRepository.getLoggedUser();

        final int companyId = loggedUser.getDefaultCompany().getCompanyId();
        companyCnpj = loggedUser.getDefaultCompany().getCnpj();
        salesmanCpfOrCnpj = loggedUser.getSalesman().getCpfOrCnpj();

        final CustomerRepository customerRepository = LocalDataInjector.provideCustomerRepository();
        final CustomerApi customerApi = RemoteDataInjector.provideCustomerApi();

        final String lastSyncTime = settingsRepository.getLastSyncTime();

        boolean notifyOrderUpdates = false;
//        boolean notifyCustomerUpdates = false;
//        boolean notifyProductUpdates = false;

        // TODO: Here condition charge loaded (code - status)
        // TODO: Add only necessary
        if (settingsRepository.isUserLoggedIn()) {
            //region getting payment method updates
            try {
                final Response<List<PaymentMethod>> response = RemoteDataInjector.providePaymentMethodApi()
                        .getUpdates(companyCnpj, lastSyncTime)
                        .execute();

                if (response.isSuccessful()) {
                    final List<PaymentMethod> updatedPaymentMethods = response.body();

                    if (updatedPaymentMethods != null && !updatedPaymentMethods.isEmpty()) {
                        final PaymentMethodRepository paymentMethodRepository
                                = LocalDataInjector.providePaymentMethodRepository();
                        final CompanyPaymentMethodRepository companyPaymentMethodRepository
                                = LocalDataInjector.provideCompanyPaymentMethodRepository();

                        for (final PaymentMethod paymentMethod : updatedPaymentMethods) {
                            final Integer paymentMethodId = paymentMethod.getPaymentMethodId();
                            final PaymentMethod existingPaymentMethod = paymentMethodRepository.findFirst(new PaymentMethodByIdSpecification(paymentMethodId));
                            final PaymentMethod savedPaymentMethod = paymentMethodRepository.save(paymentMethod);

                            if (existingPaymentMethod == null) {
                                final CompanyPaymentMethod newCompanyPaymentMethod = CompanyPaymentMethod.from(loggedUser.getDefaultCompany(), savedPaymentMethod);
                                companyPaymentMethodRepository.save(newCompanyPaymentMethod);
                            }
                        }
                    }
                } else {
                    Timber.i("Unsuccessful getting payment method updates. %s", response.message());
                }
            } catch (IOException e) {
                Timber.e(e, "Server failure while getting payment method updates");
                return GcmNetworkManager.RESULT_RESCHEDULE;
            } catch (RuntimeException e) {
                Timber.e(e, "Unknown error while getting payment method updates");
                return GcmNetworkManager.RESULT_RESCHEDULE;
            }
            //endregion

            //region sending orders
            if (settingsRepository.getSettings().isAutomaticallySyncOrders()) {
                final int salesmanId = loggedUser.getSalesman().getSalesmanId();

                final List<Order> createdOrModifiedOrders = getOrderRepository()
                        .query(new OrdersByUserSpecification(salesmanId, companyId)
                                .byStatus(OrderStatusSpecificationFilter.CREATED_OR_MODIFIED));

                Boolean successful = syncOrders(createdOrModifiedOrders);
                if (successful != null) {
                    if (successful) {
                        notifyOrderUpdates = true;
                    } else {
                        return GcmNetworkManager.RESULT_RESCHEDULE;
                    }
                }
            } else {
                Timber.i("Orders are not enabled to sync automatically");
            }
            //endregion

            //region getting orders updates
            try {
                final Response<List<OrderDto>> response = getOrderApi()
                        .getUpdates(companyCnpj, salesmanCpfOrCnpj, lastSyncTime)
                        .execute();

                if (response.isSuccessful()) {
                    final List<OrderDto> updatedOrders = response.body();

                    if (updatedOrders != null && !updatedOrders.isEmpty()) {
                        for (final OrderDto order : updatedOrders) {
                            final int orderId = order.orderId;
                            final Order existingOrder = getOrderRepository()
                                    .findFirst(new OrderByIdSpecification(orderId));

                            if (existingOrder != null) {
                                switch (order.status) {
                                    case OrderStatus.STATUS_CANCELLED: {
                                        existingOrder.withStatus(OrderStatus.STATUS_CANCELLED);
                                        break;
                                    }
                                    case OrderStatus.STATUS_INVOICED: {
                                        existingOrder.withStatus(OrderStatus.STATUS_INVOICED);
                                        break;
                                    }
                                }

                                existingOrder.withLastChangeTime(order.lastChangeTime);
                                getOrderRepository().save(existingOrder);
                            }
                        }
                        notifyOrderUpdates = true;
                    }
                } else {
                    Timber.i("Unsuccessful getting order updates. %s", response.message());
                }
            } catch (IOException e) {
                Timber.e(e, "Server failure while getting order updates");
                return GcmNetworkManager.RESULT_RESCHEDULE;
            } catch (RuntimeException e) {
                Timber.e(e, "Unknown error while getting order updates");
                return GcmNetworkManager.RESULT_RESCHEDULE;
            }
            //endregion

            //region updating last sync time
            try {
                final Response<ServerStatus> response = RemoteDataInjector.provideSyncApi()
                        .getServerStatus()
                        .execute();

                if (response.isSuccessful()) {
                    final ServerStatus body = response.body();
                    if (body != null) {
                        settingsRepository.setLastSyncTime(body.currentTime);
                    }
                }
            } catch (IOException e) {
                Timber.e(e, "Server failure while getting server status");
                return GcmNetworkManager.RESULT_RESCHEDULE;
            } catch (RuntimeException e) {
                Timber.e(e, "Unknown error while getting server status");
                return GcmNetworkManager.RESULT_RESCHEDULE;
            }
            //endregion

            //region notifications
            if (notifyOrderUpdates) {
                PresentationInjector.provideEventBus().post(OrdersSyncedEvent.ordersSyncedBySchedule());
            }

//            if (notifyCustomerUpdates) {
//                PresentationInjector.provideEventBus().post(CustomersSyncedEvent.customersSynced());
//            }
//
//            if (notifyProductUpdates) {
//                PresentationInjector.provideEventBus().post(ProductsUpdatedEvent.productsUpdated());
//            }
            //endregion

            return GcmNetworkManager.RESULT_SUCCESS;
        }
        return GcmNetworkManager.RESULT_FAILURE;
    }

    // FIXME: Non Usage Updates
//        //region sending created customers
//        final List<Customer> createdCustomers = customerRepository.query(new CustomersByCompanySpecification(companyId).byStatus(CustomerStatus.STATUS_CREATED));
//
//        if (!createdCustomers.isEmpty()) {
//            Timber.i("%d new customers ready to sync", createdCustomers.size());
//
//            for (final Customer customer : createdCustomers) {
//                try {
//                    final Response<Customer> response = customerApi
//                            .createCustomer(companyCnpj, customer)
//                            .execute();
//
//                    if (response.isSuccessful() && response.body() != null) {
//                        Customer synced = response.body();
//
//                        customer
//                                .withCustomerId(synced.getCustomerId())
//                                .withCode(synced.getCode())
//                                .withLastChangeTime(synced.getLastChangeTime())
//                                .withActive(synced.isActive())
//                                .withStatus(CustomerStatus.STATUS_UNMODIFIED);
//
//                        customerRepository.save(customer);
//                    } else {
//                        Timber.i("Unsuccessful new customer sync. %s", response.message());
//                    }
//                } catch (IOException e) {
//                    Timber.e(e, "Server failure while syncing new customers");
//                    return GcmNetworkManager.RESULT_RESCHEDULE;
//                } catch (RuntimeException e) {
//                    Timber.e(e, "Unknown error while syncing new customers");
//                    return GcmNetworkManager.RESULT_RESCHEDULE;
//                }
//            }
//            notifyCustomerUpdates = true;
//        }
//        //endregion
//
//        //region sending modified customers
//        final List<Customer> modifiedCustomers = customerRepository
//                .query(new CustomersByCompanySpecification(companyId)
//                        .byStatus(CustomerStatus.STATUS_MODIFIED));
//
//        if (!modifiedCustomers.isEmpty()) {
//            Timber.i("%d modified customers ready to sync", modifiedCustomers.size());
//
//            try {
//                Response<List<Customer>> response = customerApi
//                        .updateCustomers(companyCnpj, modifiedCustomers)
//                        .execute();
//
//                if (response.isSuccessful()) {
//                    final List<Customer> bodyList = response.body();
//                    if (bodyList != null && !bodyList.isEmpty()) {
//
//                        for (Customer customer : bodyList) {
//                            final Integer customerId = customer.getCustomerId();
//                            final Customer existingCustomer = customerRepository
//                                    .findFirst(new CustomerByCustomerIdSpecification(customerId));
//
//                            if (existingCustomer != null) {
//                                customer
//                                        .withId(existingCustomer.getId())
//                                        .withStatus(CustomerStatus.STATUS_UNMODIFIED);
//                                customerRepository.save(customer);
//                            }
//                        }
//                    }
//                } else {
//                    Timber.i("Unsuccessful update customer sync. %s", response.message());
//                }
//            } catch (IOException e) {
//                Timber.e(e, "Server failure while syncing modified customers");
//                return GcmNetworkManager.RESULT_RESCHEDULE;
//            } catch (RuntimeException e) {
//                Timber.e(e, "Unknown error while syncing modified customers");
//                return GcmNetworkManager.RESULT_RESCHEDULE;
//            }
//            notifyCustomerUpdates = true;
//        }
//        //endregion
//
//        //region getting customer updates
//        try {
//            final Response<List<Customer>> response = customerApi
//                    .getUpdates(companyCnpj, lastSyncTime)
//                    .execute();
//
//            if (response.isSuccessful()) {
//                final List<Customer> updatedCustomers = response.body();
//
//                if (updatedCustomers != null && !updatedCustomers.isEmpty()) {
//                    final CompanyCustomerRepository companyCustomerRepository
//                            = LocalDataInjector.provideCompanyCustomerRepository();
//
//                    for (final Customer customer : updatedCustomers) {
//                        final Integer customerId = customer.getCustomerId();
//                        final Customer existingCustomer = customerRepository
//                                .findFirst(new CustomerByCustomerIdSpecification(customerId));
//
//                        if (existingCustomer != null) {
//                            customer
//                                    .withId(existingCustomer.getId())
//                                    .withStatus(existingCustomer.getStatus());
//                            customerRepository.save(customer);
//                        } else {
//                            final Customer savedCustomer = customerRepository.save(customer);
//                            final CompanyCustomer newCompanyCustomer = CompanyCustomer.from(loggedUser.getDefaultCompany(), savedCustomer);
//
//                            companyCustomerRepository.save(newCompanyCustomer);
//                        }
//                    }
//                    notifyCustomerUpdates = true;
//                }
//            } else {
//                Timber.i("Unsuccessful getting customer updates. %s", response.message());
//            }
//        } catch (IOException e) {
//            Timber.e(e, "Server failure while getting customer updates");
//            return GcmNetworkManager.RESULT_RESCHEDULE;
//        } catch (RuntimeException e) {
//            Timber.e(e, "Unknown error while getting customer updates");
//            return GcmNetworkManager.RESULT_RESCHEDULE;
//        }
//        //endregion
//
//        //region getting price table updates
//        try {
//            final Response<List<PriceTable>> response = RemoteDataInjector.providePriceTableApi()
//                    .getUpdates(companyCnpj, lastSyncTime)
//                    .execute();
//
//            if (response.isSuccessful()) {
//                final List<PriceTable> updatedPriceTables = response.body();
//
//                if (updatedPriceTables != null && !updatedPriceTables.isEmpty()) {
//                    final PriceTableRepository priceTableRepository = LocalDataInjector.providePriceTableRepository();
//                    final CompanyPriceTableRepository companyPriceTableRepository
//                            = LocalDataInjector.provideCompanyPriceTableRepository();
//
//                    for (final PriceTable priceTable : updatedPriceTables) {
//                        final Integer priceTableId = priceTable.getPriceTableId();
//                        final PriceTable existingPriceTable = priceTableRepository.findFirst(new PriceTableByIdSpecification(priceTableId));
//
//                        final PriceTable savedPriceTable = priceTableRepository.save(priceTable);
//
//                        if (existingPriceTable == null) {
//                            final CompanyPriceTable newCompanyPriceTable = CompanyPriceTable.from(loggedUser.getDefaultCompany(), savedPriceTable);
//                            companyPriceTableRepository.save(newCompanyPriceTable);
//                        }
//                    }
//                    notifyProductUpdates = true;
//                }
//            } else {
//                Timber.i("Unsuccessful getting price table updates. %s", response.message());
//            }
//        } catch (IOException e) {
//            Timber.e(e, "Server failure while getting price table updates");
//            return GcmNetworkManager.RESULT_RESCHEDULE;
//        } catch (RuntimeException e) {
//            Timber.e(e, "Unknown error while getting price table updates");
//            return GcmNetworkManager.RESULT_RESCHEDULE;
//        }
//        //endregion
//
//        //region getting product updates
//        try {
//            final Response<List<Product>> response = RemoteDataInjector.provideProductApi()
//                    .getUpdates(companyCnpj, lastSyncTime)
//                    .execute();
//
//            if (response.isSuccessful()) {
//                final List<Product> updatedProducts = response.body();
//
//                if (updatedProducts != null && !updatedProducts.isEmpty()) {
//                    final ProductRepository productRepository = LocalDataInjector.provideProductRepository();
//
//                    for (final Product product : updatedProducts) {
//                        productRepository.save(product);
//                    }
//                    notifyProductUpdates = true;
//                }
//            } else {
//                Timber.i("Unsuccessful getting product updates. %s", response.message());
//            }
//        } catch (IOException e) {
//            Timber.e(e, "Server failure while getting product updates");
//            return GcmNetworkManager.RESULT_RESCHEDULE;
//        } catch (RuntimeException e) {
//            Timber.e(e, "Unknown error while getting product updates");
//            return GcmNetworkManager.RESULT_RESCHEDULE;
//        }
//        //endregion
//    }

    private Boolean syncOrders(List<Order> orders) {
        if (orders != null && !orders.isEmpty()) {
            for (Order order : orders) {
                try {
                    final OrderDto postOrder = order.createPostOrder();

                    final Response<OrderDto> response = getOrderApi()
                            .createOrder(companyCnpj, salesmanCpfOrCnpj, postOrder)
                            .execute();

                    if (response.isSuccessful()) {
                        OrderDto syncedOrder = response.body();

                        if (syncedOrder != null) {
                            for (OrderItemDto syncedOrderItem : syncedOrder.items) {
                                for (OrderItem item : order.getItems()) {
                                    if (item.getId().compareTo(syncedOrderItem.id) == 0) {
                                        item
                                                .withOrderItemId(syncedOrderItem.orderItemId)
                                                .withLastChangeTime(syncedOrderItem.lastChangeTime);
                                        break;
                                    }
                                }
                            }

                            order
                                    .withOrderId(syncedOrder.orderId)
                                    .withLastChangeTime(syncedOrder.lastChangeTime)
                                    .withStatus(OrderStatus.STATUS_SYNCED);

                            getOrderRepository().save(order);
                        }
                    } else {
                        Timber.i("Unsuccessful orders sync. %s", response.message());
                    }
                } catch (IOException e) {
                    Timber.e(e, "Server failure while syncing orders");
                    return false;
                } catch (RuntimeException e) {
                    Timber.e(e, "Unknown error while syncing orders");
                    return false;
                }
            }
            return true;
        } else
            return null;
    }

    private OrderApi getOrderApi() {
        if (orderApi == null) {
            orderApi = RemoteDataInjector.provideOrderApi();
        }
        return orderApi;
    }

    private OrderRepository getOrderRepository() {
        if (orderRepository == null) {
            orderRepository = LocalDataInjector.providerOrderRepository();
        }
        return orderRepository;
    }
}

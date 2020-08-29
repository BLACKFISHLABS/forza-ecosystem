package io.github.blackfishlabs.forza.data.sync;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.IOException;
import java.util.List;

import io.github.blackfishlabs.forza.data.LocalDataInjector;
import io.github.blackfishlabs.forza.data.RemoteDataInjector;
import io.github.blackfishlabs.forza.data.customer.CustomerApi;
import io.github.blackfishlabs.forza.data.customer.CustomerRepository;
import io.github.blackfishlabs.forza.data.order.OrderApi;
import io.github.blackfishlabs.forza.data.order.OrderRepository;
import io.github.blackfishlabs.forza.data.settings.SettingsRepository;
import io.github.blackfishlabs.forza.domain.dto.OrderDto;
import io.github.blackfishlabs.forza.domain.dto.OrderItemDto;
import io.github.blackfishlabs.forza.domain.pojo.Customer;
import io.github.blackfishlabs.forza.domain.pojo.CustomerStatus;
import io.github.blackfishlabs.forza.domain.pojo.LoggedUser;
import io.github.blackfishlabs.forza.domain.pojo.Order;
import io.github.blackfishlabs.forza.domain.pojo.OrderItem;
import io.github.blackfishlabs.forza.domain.pojo.OrderStatus;
import io.github.blackfishlabs.forza.ui.PresentationInjector;
import io.github.blackfishlabs.forza.ui.orderlist.SyncOrdersEvent;
import retrofit2.Response;
import timber.log.Timber;

import static java.util.Collections.singletonList;

public class InstantSyncService extends IntentService {

    private static final String TAG = InstantSyncService.class.getSimpleName();

    public static void execute(@NonNull Context context) {
        context.startService(new Intent(context, InstantSyncService.class));
    }

    private SettingsRepository settingsRepository;

    private CustomerRepository customerRepository;

    private CustomerApi customerApi;

    private LoggedUser loggedUser;

    public InstantSyncService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(@Nullable final Intent intent) {
        Timber.d("running instant sync service");

        boolean notifyOrderUpdates = false;
        boolean syncCancelled = false;

        if (!getSettingsRepository().isUserLoggedIn()) {
            Timber.i("No user logged, sync will not be executed");
        } else {
            syncCancelled = SyncTaskService.cancelAll(this);
            notifyOrderUpdates = syncOrders();
        }

        PresentationInjector.provideEventBus().removeStickyEvent(SyncOrdersEvent.class);
        if (notifyOrderUpdates) {
            PresentationInjector.provideEventBus().post(OrdersSyncedEvent.ordersInstantlySynced());
        }

        if (syncCancelled) {
            SyncTaskService.schedule(this, getSettingsRepository().getSettings().getSyncPeriodicity());
        }
    }

    private boolean syncOrders() {
        SyncOrdersEvent syncOrdersEvent = PresentationInjector.provideEventBus().getStickyEvent(SyncOrdersEvent.class);
        if (syncOrdersEvent == null)
            return false;

        List<Order> orders = syncOrdersEvent.getOrders();
        if (orders == null || orders.isEmpty())
            return false;

        final String salesmanCpfOrCnpj = getLoggedUser().getSalesman().getCpfOrCnpj();
        final OrderApi orderApi = RemoteDataInjector.provideOrderApi();
        final OrderRepository orderRepository = LocalDataInjector.providerOrderRepository();

        boolean anyOrderSynced = false;

        for (Order order : orders) {
            final Customer customer = order.getCustomer();

            Customer syncedCustomer = null;
            if (customer.getStatus() == CustomerStatus.STATUS_CREATED) {
                syncedCustomer = syncNewCustomer(customer);
                if (syncedCustomer == null)
                    continue;
            } else if (customer.getStatus() == CustomerStatus.STATUS_MODIFIED) {
                syncedCustomer = syncModifiedCustomer(customer);
                if (syncedCustomer == null)
                    continue;
            }

            if (syncedCustomer != null) {
                order.withCustomer(syncedCustomer);
            }

            try {
                final OrderDto postOrder = order.createPostOrder();

                final Response<OrderDto> response = orderApi
                        .createOrder(getCompanyCnpj(), salesmanCpfOrCnpj, postOrder)
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

                        orderRepository.save(order);
                        anyOrderSynced = true;
                    }
                } else {
                    Timber.e("Unsuccessful orders sync. %s", response.message());
                }
            } catch (IOException e) {
                Timber.e(e, "Server failure while syncing orders");
            } catch (RuntimeException e) {
                Timber.e(e, "Unknown error while syncing orders");
            }
        }
        return anyOrderSynced;
    }

    private Customer syncNewCustomer(Customer newCustomer) {
        try {
            final Response<Customer> response = getCustomerApi().createCustomer(getCompanyCnpj(), newCustomer).execute();

            if (response.isSuccessful() && response.body() != null) {
                return getCustomerRepository().save(response.body());
            } else {
                Timber.e("Unsuccessful new customer sync. %s", response.message());
            }
        } catch (IOException e) {
            Timber.e(e, "Server failure while syncing new customer");
        } catch (RuntimeException e) {
            Timber.e(e, "Unknown error while syncing new customer");
        }

        return null;
    }

    private Customer syncModifiedCustomer(Customer modifiedCustomer) {
        try {
            Response<List<Customer>> response = getCustomerApi()
                    .updateCustomers(getCompanyCnpj(), singletonList(modifiedCustomer))
                    .execute();

            if (response.isSuccessful()) {
                final List<Customer> bodyList = response.body();
                if (bodyList != null && !bodyList.isEmpty()) {
                    return getCustomerRepository().save(bodyList.get(0));
                }
            } else {
                Timber.i("Unsuccessful update customer sync. %s", response.message());
            }
        } catch (IOException e) {
            Timber.e(e, "Server failure while syncing modified customer");
        } catch (RuntimeException e) {
            Timber.e(e, "Unknown error while syncing modified customer");
        }

        return null;
    }

    private LoggedUser getLoggedUser() {
        if (loggedUser == null) {
            loggedUser = getSettingsRepository().getLoggedUser();
        }
        return loggedUser;
    }

    private SettingsRepository getSettingsRepository() {
        if (settingsRepository == null) {
            settingsRepository = PresentationInjector.provideSettingsRepository();
        }
        return settingsRepository;
    }

    private String getCompanyCnpj() {
        return getLoggedUser().getDefaultCompany().getCnpj();
    }

    private CustomerApi getCustomerApi() {
        if (customerApi == null) {
            customerApi = RemoteDataInjector.provideCustomerApi();
        }
        return customerApi;
    }

    private CustomerRepository getCustomerRepository() {
        if (customerRepository == null) {
            customerRepository = LocalDataInjector.provideCustomerRepository();
        }
        return customerRepository;
    }
}

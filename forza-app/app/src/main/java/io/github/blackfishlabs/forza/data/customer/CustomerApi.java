package io.github.blackfishlabs.forza.data.customer;

import java.util.List;

import io.github.blackfishlabs.forza.domain.pojo.Customer;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface CustomerApi {

    @GET("api/v1/customer")
    Call<List<Customer>> get(@Query("cnpj") String cnpj);

    @GET("api/v1/customer/mobile")
    Call<List<Customer>> getByCharge(@Query("code") String code, @Query("cnpj") String cnpj);

    @POST("api/v1/customer")
    Call<Customer> createCustomer(@Query("cnpj") String companyCnpj, @Body Customer customer);

    @PUT("api/v1/customer")
    Call<List<Customer>> updateCustomers(@Query("cnpj") String companyCnpj, @Body List<Customer> customers);

    @GET("api/v1/customer/update")
    Call<List<Customer>> getUpdates(@Query("cnpj") String cnpj, @Query("ultimaAtualizacao") String lastUpdateTime);
}

package io.github.blackfishlabs.forza.data.paymentmethod;

import java.util.List;

import io.github.blackfishlabs.forza.domain.pojo.PaymentMethod;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PaymentMethodApi {

    @GET("api/v1/payment")
    Call<List<PaymentMethod>> get(@Query("cnpj") String cnpj);

    @GET("api/v1/payment/update")
    Call<List<PaymentMethod>> getUpdates(@Query("cnpj") String cnpj, @Query("ultimaAtualizacao") String lastUpdateTime);
}

package io.github.blackfishlabs.forza.data.order;

import java.util.List;

import io.github.blackfishlabs.forza.domain.dto.OrderDto;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface OrderApi {

    @POST("api/v1/order")
    Call<OrderDto> createOrder(
            @Query("cnpj") String companyCnpj,
            @Query("cpfCnpj") String salesmanCpfOrCnpj, @Body OrderDto order);

    @GET("api/v1/order")
    Call<List<OrderDto>> getUpdates(
            @Query("cnpj") String companyCnpj,
            @Query("cpfCnpj") String salesmanCpfOrCnpj,
            @Query("ultimaAtualizacao") String lastUpdateTime);
}

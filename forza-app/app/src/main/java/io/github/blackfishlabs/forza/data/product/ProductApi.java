package io.github.blackfishlabs.forza.data.product;

import java.util.List;

import io.github.blackfishlabs.forza.domain.pojo.Product;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ProductApi {

    @GET("api/v1/product")
    Call<List<Product>> getUpdates(@Query("cnpj") String cnpj, @Query("ultimaAtualizacao") String lastUpdateTime);
}

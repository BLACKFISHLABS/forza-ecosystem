package io.github.blackfishlabs.forza.data.pricetable;

import java.util.List;

import io.github.blackfishlabs.forza.domain.pojo.PriceTable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PriceTableApi {

    @GET("api/v1/pricetable")
    Call<List<PriceTable>> get(@Query("cnpj") String cnpj);

    @GET("api/v1/pricetable/mobile")
    Call<List<PriceTable>> getByCharge(@Query("code") String code, @Query("cnpj") String cnpj);

    @GET("api/v1/pricetable/update")
    Call<List<PriceTable>> getUpdates(@Query("cnpj") String cnpj, @Query("ultimaAtualizacao") String lastUpdateTime);
}

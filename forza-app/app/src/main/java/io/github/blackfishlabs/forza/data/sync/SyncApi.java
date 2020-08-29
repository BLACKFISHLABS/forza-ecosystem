package io.github.blackfishlabs.forza.data.sync;

import io.github.blackfishlabs.forza.domain.dto.ServerStatus;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface SyncApi {

    @GET("api/v1/server")
    Call<ServerStatus> serverStatus();

    @GET("api/v1/server")
    Call<ServerStatus> getServerStatus();

    @GET("api/v1/charge/search/mobile")
    Call<ResponseBody> getCharge(@Query("code") String code, @Query("cnpj") String cnpj, @Query("salesman") String salesman);

    @Headers("Content-Type: application/json")
    @PUT("api/v1/charge")
    Call<ResponseBody> updateCharge(@Body String body);
}

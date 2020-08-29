package io.github.blackfishlabs.forza.data.salesman;

import io.github.blackfishlabs.forza.domain.dto.SalesmanDto;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SalesmanApi {

    @GET("api/v1/salesman")
    Call<SalesmanDto> get(@Query("cpfCnpj") String cpfOrCnpj, @Query("senha") String password);
}

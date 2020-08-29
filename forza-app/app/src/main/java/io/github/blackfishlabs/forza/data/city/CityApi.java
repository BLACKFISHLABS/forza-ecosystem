package io.github.blackfishlabs.forza.data.city;

import java.util.List;

import io.github.blackfishlabs.forza.domain.pojo.City;
import retrofit2.Call;
import retrofit2.http.GET;

public interface CityApi {

    @GET("api/v1/city")
    Call<List<City>> get();
}

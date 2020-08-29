package io.github.blackfishlabs.forza.data.postalcode;

import io.github.blackfishlabs.forza.domain.pojo.PostalCode;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface PostalCodeApi {

    @GET("http://api.postmon.com.br/v1/cep/{postalCode}")
    Call<PostalCode> get(@Path("postalCode") String postalCode);
}

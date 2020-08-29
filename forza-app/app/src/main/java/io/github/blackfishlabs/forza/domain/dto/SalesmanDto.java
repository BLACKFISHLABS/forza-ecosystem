package io.github.blackfishlabs.forza.domain.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.github.blackfishlabs.forza.domain.pojo.Salesman;

public class SalesmanDto {

    @Expose
    @SerializedName("error")
    public boolean error;

    @Expose
    @SerializedName("mensagem")
    public String message;

    @Expose
    @SerializedName("Vendedor")
    public Salesman salesman;
}

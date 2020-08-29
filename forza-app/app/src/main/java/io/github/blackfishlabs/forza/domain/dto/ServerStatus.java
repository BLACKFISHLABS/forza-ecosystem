package io.github.blackfishlabs.forza.domain.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ServerStatus {

    @SerializedName("dataAtual")
    @Expose
    public String currentTime;
}

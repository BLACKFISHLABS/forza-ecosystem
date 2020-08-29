package io.github.blackfishlabs.forza.dashboard.json;

import io.github.blackfishlabs.forza.core.infra.json.Json;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DashboardJson extends Json {

    private int sales;
    private int products;
    private int salesman;
    private int customers;
}

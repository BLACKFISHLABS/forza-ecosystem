package io.github.blackfishlabs.forza;

import io.github.blackfishlabs.forza.api.ChargeAPI;
import io.github.blackfishlabs.forza.api.OrderAPI;
import io.github.blackfishlabs.forza.api.ServerStatusAPI;

import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

public class ApplicationForza extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new HashSet<>();
        resources.add(ServerStatusAPI.class);
        resources.add(ChargeAPI.class);
        resources.add(OrderAPI.class);
        return resources;
    }

}

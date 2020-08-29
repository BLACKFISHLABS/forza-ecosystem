package io.github.blackfishlabs.forza.ui.citylist;

import io.github.blackfishlabs.forza.domain.pojo.City;

public class SelectedCityEvent {

    private final City mCity;

    private SelectedCityEvent(final City city) {
        mCity = city;
    }

    static SelectedCityEvent newEvent(final City city) {
        return new SelectedCityEvent(city);
    }

    public City getCity() {
        return mCity;
    }
}

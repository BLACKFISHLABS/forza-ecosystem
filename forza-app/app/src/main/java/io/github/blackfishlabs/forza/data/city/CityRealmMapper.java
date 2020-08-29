package io.github.blackfishlabs.forza.data.city;

import io.github.blackfishlabs.forza.data.LocalDataInjector;
import io.github.blackfishlabs.forza.data.realm.RealmMapper;
import io.github.blackfishlabs.forza.domain.entity.CityEntity;
import io.github.blackfishlabs.forza.domain.pojo.City;

public class CityRealmMapper extends RealmMapper<City, CityEntity> {

    @Override
    public CityEntity toEntity(final City object) {
        return new CityEntity()
                .withCityId(object.getCityId())
                .withMunicipalityCode(object.getMunicipalityCode())
                .withName(object.getName())
                .withState(LocalDataInjector.stateMapper().toEntity(object.getState()));
    }

    @Override
    public City toViewObject(final CityEntity entity) {
        return new City()
                .withCityId(entity.getCityId())
                .withMunicipalityCode(entity.getMunicipalityCode())
                .withName(entity.getName())
                .withState(LocalDataInjector.stateMapper().toViewObject(entity.getState()));
    }
}

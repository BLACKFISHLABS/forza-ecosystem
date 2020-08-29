package io.github.blackfishlabs.forza.data.city;

import io.github.blackfishlabs.forza.data.LocalDataInjector;
import io.github.blackfishlabs.forza.data.realm.RealmRepository;
import io.github.blackfishlabs.forza.domain.entity.CityEntity;
import io.github.blackfishlabs.forza.domain.pojo.City;

public class CityRealmRepository extends RealmRepository<City, CityEntity> implements CityRepository {

    public CityRealmRepository() {
        super(CityEntity.class, LocalDataInjector.cityMapper());
    }
}

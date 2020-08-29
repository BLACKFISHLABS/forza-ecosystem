package io.github.blackfishlabs.forza.data.city;

import io.github.blackfishlabs.forza.data.realm.RealmSingleSpecification;
import io.github.blackfishlabs.forza.domain.entity.CityEntity;
import io.realm.Case;
import io.realm.Realm;

public class CityByNameSpecification implements RealmSingleSpecification<CityEntity> {

    private final String name;

    public CityByNameSpecification(final String name) {
        this.name = name; //StringUtils.removeAccents(name);
    }

    @Override
    public CityEntity toSingle(final Realm realm) {
        return realm.where(CityEntity.class)
                .equalTo(CityEntity.Fields.NAME, name, Case.INSENSITIVE)
                .findFirst();
    }
}

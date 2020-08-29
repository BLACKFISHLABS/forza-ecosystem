package io.github.blackfishlabs.forza.data.city;

import io.github.blackfishlabs.forza.data.realm.RealmResultsSpecification;
import io.github.blackfishlabs.forza.domain.entity.CityEntity;
import io.realm.Realm;
import io.realm.RealmResults;

import static io.realm.Sort.ASCENDING;

public class CitiesByStateSpecification implements RealmResultsSpecification<CityEntity> {

    private final int stateId;

    public CitiesByStateSpecification(final int stateId) {
        this.stateId = stateId;
    }

    @Override
    public RealmResults<CityEntity> toRealmResults(final Realm realm) {
        return realm.where(CityEntity.class)
                .equalTo(CityEntity.Fields.STATE_ID_RELATION, stateId)
                .sort(CityEntity.Fields.NAME, ASCENDING).findAll();
    }
}

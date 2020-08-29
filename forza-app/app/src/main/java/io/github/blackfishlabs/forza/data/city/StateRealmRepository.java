package io.github.blackfishlabs.forza.data.city;

import io.github.blackfishlabs.forza.data.LocalDataInjector;
import io.github.blackfishlabs.forza.data.realm.RealmRepository;
import io.github.blackfishlabs.forza.domain.entity.StateEntity;
import io.github.blackfishlabs.forza.domain.pojo.State;

public class StateRealmRepository extends RealmRepository<State, StateEntity> implements StateRepository {

    public StateRealmRepository() {
        super(StateEntity.class, LocalDataInjector.stateMapper());
    }
}

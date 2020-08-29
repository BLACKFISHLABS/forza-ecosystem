package io.github.blackfishlabs.forza.data.city;

import io.github.blackfishlabs.forza.data.realm.RealmMapper;
import io.github.blackfishlabs.forza.domain.entity.StateEntity;
import io.github.blackfishlabs.forza.domain.pojo.State;

public class StateRealmMapper extends RealmMapper<State, StateEntity> {

    @Override
    public StateEntity toEntity(final State object) {
        return new StateEntity()
                .withStateId(object.getStateId())
                .withFederativeUnit(object.getFederativeUnit())
                .withName(object.getName());
    }

    @Override
    public State toViewObject(final StateEntity entity) {
        return new State()
                .withStateId(entity.getStateId())
                .withFederativeUnit(entity.getFederativeUnit())
                .withName(entity.getName());
    }
}

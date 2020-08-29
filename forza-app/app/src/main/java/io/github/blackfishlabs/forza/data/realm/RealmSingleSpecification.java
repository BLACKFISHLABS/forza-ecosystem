package io.github.blackfishlabs.forza.data.realm;

import io.github.blackfishlabs.forza.data.repository.Specification;
import io.realm.Realm;
import io.realm.RealmModel;

public interface RealmSingleSpecification<E extends RealmModel> extends Specification {

    E toSingle(Realm realm);
}

package io.github.blackfishlabs.forza.data.realm;

import io.github.blackfishlabs.forza.data.repository.Specification;
import io.realm.Realm;
import io.realm.RealmModel;
import io.realm.RealmResults;

public interface RealmResultsSpecification<E extends RealmModel> extends Specification {

    RealmResults<E> toRealmResults(Realm realm);
}

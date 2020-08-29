package io.github.blackfishlabs.forza.data.realm;

import java.util.List;

import io.github.blackfishlabs.forza.data.repository.Mapper;
import io.github.blackfishlabs.forza.data.repository.Repository;
import io.github.blackfishlabs.forza.data.repository.Specification;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmModel;
import io.realm.RealmResults;
import timber.log.Timber;

import static io.realm.Sort.ASCENDING;
import static io.realm.Sort.DESCENDING;

public abstract class RealmRepository<T, E extends RealmModel> implements Repository<T> {

    private final Class<E> mEntityClass;
    private final Mapper<T, E> mRealmMapper;
    private Realm mRealm;

    protected RealmRepository(final Class<E> entityClass, final Mapper<T, E> realmMapper) {
        mEntityClass = entityClass;
        mRealmMapper = realmMapper;
        mRealm = Realm.getDefaultInstance();
    }

    @Override
    public T save(final T object) {
        Timber.d("%s.save(%s)", mEntityClass.getSimpleName(), object);
        mRealm.beginTransaction();
        E e = mRealm.copyToRealmOrUpdate(mRealmMapper.toEntity(object));
        mRealm.commitTransaction();
        Timber.d("%s saved %s", mEntityClass.getSimpleName(), object);
        return mRealmMapper.toViewObject(e);
    }

    @Override
    public List<T> save(final List<T> objects) {
        Timber.d("%s.save(list of %d)", mEntityClass.getSimpleName(), objects.size());
        mRealm.beginTransaction();
        List<E> entities = mRealm.copyToRealmOrUpdate(mRealmMapper.toEntities(objects));
        mRealm.commitTransaction();
        Timber.d("%s saved %d", mEntityClass.getSimpleName(), objects.size());

        RealmList<E> result = new RealmList<>();
        result.addAll(entities);

        return mRealmMapper.toViewObjects(result);
    }

    @Override
    public T findFirst(final Specification specification) {
        RealmSingleSpecification<E> spec = (RealmSingleSpecification<E>) specification;

        Timber.d("%s.findFirst()", mEntityClass.getSimpleName());
        E entity = spec.toSingle(mRealm);
        if (entity == null) return null;
        Timber.i("%s found %s", mEntityClass.getSimpleName(), entity);

        return mRealmMapper.toViewObject(entity);
    }

    @Override
    public List<T> findAll(final String sortField, final boolean ascending) {
        Timber.d("%s.findAll()", mEntityClass.getSimpleName());
        RealmResults<E> entities = mRealm.where(mEntityClass).sort(sortField, ascending ? ASCENDING : DESCENDING).findAll();
        Timber.i("%s found %d", mEntityClass.getSimpleName(), entities.size());

        return mRealmMapper.toViewObjects(entities);
    }

    @Override
    public void findAllAndDelete() {
        Timber.d("%s.findAllAndDelete()", mEntityClass.getSimpleName());
        mRealm.beginTransaction();
        RealmResults<E> entities = mRealm.where(mEntityClass).findAll();
        entities.deleteAllFromRealm();
        mRealm.commitTransaction();
    }

    @Override
    public List<T> query(final Specification specification) {
        RealmResultsSpecification<E> spec = (RealmResultsSpecification<E>) specification;
        Timber.d("%s.query()", mEntityClass.getSimpleName());
        RealmResults<E> entities = spec.toRealmResults(mRealm);
        Timber.i("%s found %d", mEntityClass.getSimpleName(), entities.size());

        return mRealmMapper.toViewObjects(entities);
    }
}


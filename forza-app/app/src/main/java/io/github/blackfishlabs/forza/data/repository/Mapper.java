package io.github.blackfishlabs.forza.data.repository;

import java.util.List;

public interface Mapper<T, E> {

    E toEntity(T object);

    List<E> toEntities(List<T> objects);

    T toViewObject(E entity);

    List<T> toViewObjects(List<E> entities);
}

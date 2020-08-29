package io.github.blackfishlabs.forza.data.repository;

import java.util.List;

public interface Repository<T> {

    T save(T object);

    List<T> save(List<T> objects);

    T findFirst(Specification specification);

    List<T> findAll(String sortField, boolean ascending);

    List<T> query(Specification specification);

    void findAllAndDelete();
}

package io.github.blackfishlabs.forza.core.infra.service;

import io.github.blackfishlabs.forza.core.infra.exception.ForzaRuntimeException;
import io.github.blackfishlabs.forza.core.infra.model.BasicEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;
import java.util.List;

public abstract class GenericService<T extends BasicEntity<PK>, PK extends Serializable> {

    private static final Logger LOGGER = LoggerFactory.getLogger(GenericService.class);

    @Autowired
    protected JpaRepository<T, PK> repository;

    public List<T> findAll() {
        LOGGER.info("Requesting all records.");
        return this.repository.findAll();
    }

    public T insert(T entity) {
        LOGGER.info(String.format("Saving the entity [%s].", entity));
        return this.repository.save(entity);
    }

    public T update(T entity) {
        LOGGER.info(String.format("Request to update the record [%s].", entity));

        if (entity.getId() == null) {
            String errorMessage = String.format("ID of entity [%s] cannot be null.", entity.getClass());
            LOGGER.error(errorMessage);
            throw new ForzaRuntimeException(errorMessage);
        }

        return this.repository.save(entity);
    }

    public void delete(T entity) {
        LOGGER.info(String.format("Request to delete the record [%s].", entity));
        this.repository.delete(entity);
    }

}

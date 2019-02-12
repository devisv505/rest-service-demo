package com.devisv.rest.service;

import com.devisv.rest.exception.NotFoundException;
import com.devisv.rest.model.Model;
import com.devisv.rest.repository.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.util.Objects.requireNonNull;

/**
 * Abstract service
 *
 * @param <E>
 */
public abstract class AbstractService<E extends Model> {

    private final Repository<E> repository;

    protected AbstractService(Repository repository) {
        this.repository = repository;
    }

    /**
     * Return Entity with certain UUID
     *
     * @param uuid
     * @return
     */
    public E getByUuid(UUID uuid) {
        requireNonNull(uuid, "UUID can't be NULL");

        return Optional
                .ofNullable(repository.getByUuid(uuid))
                .orElseThrow(() -> notFound(uuid));
    }

    /**
     * Return all Entities
     *
     * @return
     */
    public List<E> getAll() {
        return repository.getAll();
    }

    /**
     * Return exception NotFound
     *
     * @param uuid
     * @return
     */
    protected abstract NotFoundException notFound(UUID uuid);

}

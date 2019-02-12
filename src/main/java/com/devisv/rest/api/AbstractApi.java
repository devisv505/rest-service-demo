package com.devisv.rest.api;

import com.devisv.rest.model.Model;
import com.devisv.rest.service.AbstractService;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Abstract API
 *
 * @param <OUT> DTO Response
 * @param <E>   Entity
 */
public abstract class AbstractApi<OUT, E extends Model> {

    private final AbstractService<E> service;

    public AbstractApi(AbstractService service) {
        this.service = service;
    }

    /**
     * Return Entity with certain UUID
     *
     * @param uuid
     * @return
     */
    public OUT getByUuid(String uuid) {
        return transformToDto(
                service.getByUuid(UUID.fromString(uuid))
        );
    }

    /**
     * Return all Entity
     *
     * @return
     */
    public List<OUT> getAll() {
        return service.getAll()
                      .stream()
                      .map(this::transformToDto)
                      .collect(Collectors.toList());
    }

    /**
     * Convert Entity to DTO
     *
     * @param entity
     * @return
     */
    protected abstract OUT transformToDto(E entity);

}

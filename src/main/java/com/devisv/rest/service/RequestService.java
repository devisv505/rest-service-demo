package com.devisv.rest.service;

import com.devisv.rest.exception.NotFoundException;
import com.devisv.rest.model.Request;
import com.devisv.rest.model.Transfer;
import com.devisv.rest.repository.RequestRepository;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.UUID;

/**
 * Request Service
 */
@Singleton
public class RequestService extends AbstractService<Transfer> {

    private final RequestRepository repository;

    @Inject
    public RequestService(RequestRepository repository) {
        super(repository);
        this.repository = repository;
    }

    /**
     * Create new Request
     *
     * @param request
     * @return
     */
    public Request create(Request request) {
        return repository.create(request);
    }

    /**
     * Return Request with certain ID
     *
     * @param id
     * @return
     */
    public Request getById(String id) {
        return repository.getById(id);
    }

    /**
     * Not supported
     *
     * @param uuid
     * @return
     */
    @Override
    protected NotFoundException notFound(UUID uuid) {
        return null;
    }

}

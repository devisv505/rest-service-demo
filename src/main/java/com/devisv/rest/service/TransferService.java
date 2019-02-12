package com.devisv.rest.service;

import com.devisv.rest.exception.NotFoundException;
import com.devisv.rest.exception.TransferNotFountException;
import com.devisv.rest.model.State;
import com.devisv.rest.model.Transfer;
import com.devisv.rest.repository.TransferRepository;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.sql.Timestamp;
import java.util.UUID;

@Singleton
public class TransferService extends AbstractService<Transfer> {

    private final TransferRepository repository;

    @Inject
    public TransferService(TransferRepository repository) {
        super(repository);
        this.repository = repository;
    }

    public Transfer create(Transfer transfer) {

        transfer.setState(State.CREATED);

        return repository.create(transfer);
    }

    public Transfer getById(Long id) {
        return repository.getById(id);
    }

    /**
     * Update State and date of complete
     *
     * @param uuid
     * @param state
     * @return
     */
    public Transfer setState(UUID uuid, State state) {

        Transfer transfer = repository.getByUuid(uuid);
        transfer.setState(state);

        if (state != State.CREATED) {
            transfer.setCompletedAt(
                    new Timestamp(System.currentTimeMillis())
            );
        }

        return repository.update(transfer);
    }

    @Override
    protected NotFoundException notFound(UUID uuid) {
        return new TransferNotFountException(uuid);
    }

}
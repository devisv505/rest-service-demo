package com.devisv.rest.service;

import com.devisv.rest.exception.AccountNotFountException;
import com.devisv.rest.exception.NotFoundException;
import com.devisv.rest.model.Account;
import com.devisv.rest.repository.AccountRepository;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.UUID;

import static java.util.Objects.requireNonNull;

@Singleton
public class AccountService extends AbstractService<Account> {

    private final AccountRepository repository;

    @Inject
    public AccountService(AccountRepository repository) {
        super(repository);
        this.repository = repository;
    }

    @Override
    protected NotFoundException notFound(UUID uuid) {
        return new AccountNotFountException(uuid);
    }

    public void update(Account account) {
        requireNonNull(account.getId(), "Account.ID can't be NULL");

        repository.update(account);
    }
}

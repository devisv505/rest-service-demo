package com.devisv.rest.api;

import com.devisv.rest.dto.AccountRequestDto;
import com.devisv.rest.dto.AccountResponseDto;
import com.devisv.rest.model.Account;
import com.devisv.rest.service.AccountService;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.UUID;

/**
 * API Account
 */
@Singleton
public class AccountApi extends AbstractApi<AccountResponseDto, Account> {

    private final AccountService service;

    @Inject
    public AccountApi(AccountService service) {
        super(service);
        this.service = service;
    }

    public AccountResponseDto create(AccountRequestDto accountRequestDto) {

        Account account = new Account();
        account.setUuid(UUID.randomUUID().toString());
        account.setBalance(accountRequestDto.getBalance());
        account.setCurrency(accountRequestDto.getCurrency());
        account.setDescription(account.getDescription());

        return transformToDto(
                service.create(account)
        );
    }

    /**
     * Convert entity Account to AccountResponseDto
     *
     * @param account
     * @return
     */
    @Override
    protected AccountResponseDto transformToDto(Account account) {
        AccountResponseDto accountResponseDto = new AccountResponseDto();

        accountResponseDto.setBalance(account.getBalance());
        accountResponseDto.setCurrency(account.getCurrency());
        accountResponseDto.setDescription(account.getDescription());
        accountResponseDto.setId(account.getUuid());

        return accountResponseDto;
    }
}

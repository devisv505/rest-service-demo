package com.devisv.rest.api;

import com.devisv.rest.dto.AccountResponseDto;
import com.devisv.rest.model.Account;
import com.devisv.rest.service.AccountService;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * API Account
 */
@Singleton
public class AccountApi extends AbstractApi<AccountResponseDto, Account> {

    @Inject
    public AccountApi(AccountService service) {
        super(service);
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

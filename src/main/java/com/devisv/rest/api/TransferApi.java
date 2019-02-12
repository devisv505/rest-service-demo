package com.devisv.rest.api;

import com.devisv.rest.dto.TransferRequestDto;
import com.devisv.rest.dto.TransferResponseDto;
import com.devisv.rest.exception.IdenticalAccountsException;
import com.devisv.rest.exception.IncorrectCurrencyException;
import com.devisv.rest.exception.NotEnoughMoneyException;
import com.devisv.rest.model.Account;
import com.devisv.rest.model.Request;
import com.devisv.rest.model.State;
import com.devisv.rest.model.Transfer;
import com.devisv.rest.service.AccountService;
import com.devisv.rest.service.RequestService;
import com.devisv.rest.service.TransferService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.sql.Timestamp;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static java.util.Objects.isNull;

/**
 * API Transfer
 */
@Singleton
public class TransferApi extends AbstractApi<TransferResponseDto, Transfer> {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransferApi.class);

    private final TransferService transferService;

    private final AccountService accountService;

    private final RequestService requestService;

    private final Map<String, Lock> locks = new ConcurrentHashMap<>();

    @Inject
    public TransferApi(TransferService transferService,
                       AccountService accountService,
                       RequestService requestService) {
        super(transferService);
        this.transferService = transferService;
        this.accountService = accountService;
        this.requestService = requestService;
    }

    /**
     * Create Transfer between two accounts with all checks.
     * This method thread-safe and does not allow to make few debit operation from source Account
     *
     * @param requestDto
     * @return
     */
    public TransferResponseDto create(TransferRequestDto requestDto) {

        checkIdenticalAccounts(requestDto);

        Lock lock =
                locks.computeIfAbsent(
                        requestDto.getSourceAccountId(),
                        key -> new ReentrantLock()
                );

        lock.lock();

        final UUID transferUuid = UUID.randomUUID();

        //TODO: Transaction support
        try {

            Request request = requestService.getById(requestDto.getRequestId());

            if (request != null) {
                return transformToDto(
                        transferService.getById(request.getTransferId())
                );
            }

            Account sourceAccount = getAccountByUuid(requestDto.getSourceAccountId());
            Account targetAccount = getAccountByUuid(requestDto.getTargetAccountId());

            checkCurrency(requestDto, sourceAccount, targetAccount);
            checkBalance(requestDto, sourceAccount);

            Transfer transfer = transferService.create(
                    createTransfer(requestDto, transferUuid, sourceAccount, targetAccount)
            );

            Request requestCreate = new Request();
            requestCreate.setRequestId(requestDto.getRequestId());
            requestCreate.setTransferId(transfer.getId());

            requestService.create(requestCreate);

            try {

                sourceAccount.setBalance(
                        sourceAccount.getBalance()
                                     .subtract(
                                             transfer.getAmount()
                                     )
                );

                targetAccount.setBalance(
                        targetAccount.getBalance()
                                     .add(
                                             transfer.getAmount()
                                     )
                );

                accountService.update(sourceAccount);
                accountService.update(targetAccount);

            } catch (Exception e) {
                LOGGER.error(e.getLocalizedMessage(), e);

                return transformToDto(
                        transferService.setState(transferUuid, State.ERROR)
                );
            }

            return transformToDto(
                    transferService.setState(transferUuid, State.COMPLETED)
            );

        } finally {
            lock.unlock();
            locks.remove(requestDto.getSourceAccountId());
        }

    }

    private void checkCurrency(TransferRequestDto requestDto, Account sourceAccount, Account targetAccount) {
        if (!requestDto.getCurrency()
                       .equals(sourceAccount.getCurrency())) {
            throw new IncorrectCurrencyException(sourceAccount.getUuid(), requestDto.getCurrency());

        }

        if (!requestDto.getCurrency()
                       .equals(targetAccount.getCurrency())) {
            throw new IncorrectCurrencyException(sourceAccount.getUuid(), requestDto.getCurrency());
        }
    }

    /**
     * return Account by UUID
     *
     * @param requestDto
     * @return
     */
    private Account getAccountByUuid(String requestDto) {
        return accountService.getByUuid(
                UUID.fromString(requestDto)
        );
    }

    /**
     * Check balance
     *
     * @param requestDto
     * @param sourceAccount
     */
    private void checkBalance(TransferRequestDto requestDto, Account sourceAccount) {
        if (sourceAccount.getBalance()
                         .compareTo(requestDto.getAmount()) == -1) {
            throw new NotEnoughMoneyException(sourceAccount.getUuid());
        }
    }

    /**
     * Checking source and target accounts, it can't be same
     *
     * @param requestDto
     */
    private void checkIdenticalAccounts(TransferRequestDto requestDto) {
        if (requestDto.getTargetAccountId()
                      .equals(requestDto.getSourceAccountId())) {
            throw new IdenticalAccountsException(requestDto.getSourceAccountId(), requestDto.getTargetAccountId());
        }
    }

    /**
     * Convert TransferRequestDto to Transfer entity with check of existence accounts
     *
     * @param transferRequestDto
     * @param transferUuid
     * @param sourceAccount
     * @param targetAccount      @return
     */
    private Transfer createTransfer(TransferRequestDto transferRequestDto,
                                    UUID transferUuid,
                                    Account sourceAccount,
                                    Account targetAccount) {
        Transfer transfer = new Transfer();

        transfer.setUuid(transferUuid.toString());
        transfer.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        transfer.setSourceAccountId(sourceAccount.getId());
        transfer.setTargetAccountId(targetAccount.getId());

        transfer.setAmount(transferRequestDto.getAmount());
        transfer.setCurrency(transferRequestDto.getCurrency());
        transfer.setDescription(transferRequestDto.getDescription());

        return transfer;
    }

    /**
     * Convert entity Transfer to TransferResponseDto
     *
     * @param transfer
     * @return
     */
    @Override
    protected TransferResponseDto transformToDto(Transfer transfer) {
        TransferResponseDto transferDto = new TransferResponseDto();

        transferDto.setId(transfer.getUuid());

        transferDto.setCompletedAt(
                !isNull(transfer.getCompletedAt())
                        ? transfer.getCompletedAt().toString()
                        : null
        );

        transferDto.setCreatedAt(
                !isNull(transfer.getCreatedAt())
                        ? transfer.getCreatedAt().toString()
                        : null
        );

        transferDto.setState(transfer.getState().toString());

        return transferDto;
    }

}

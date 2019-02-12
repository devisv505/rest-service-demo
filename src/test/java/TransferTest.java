import com.devisv.rest.dto.AccountResponseDto;
import com.devisv.rest.dto.TransferRequestDto;
import com.devisv.rest.dto.TransferResponseDto;
import com.devisv.rest.exception.NotEnoughMoneyException;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

public class TransferTest extends AbstractTest {

    public static final String API_TRANSFERS = "api/transfers";

    public TransferTest() {
        super();
    }

    @Test
    public void getAllTransfer() {
        try {
            createTransfer();

            Assert.assertNotEquals(
                    Unirest.get(URL + API_TRANSFERS)
                           .asJson()
                           .getBody()
                           .getArray().length(),
                    0
            );
        } catch (UnirestException e) {
            Assert.assertFalse(e.getLocalizedMessage(), true);
        }
    }

    @Test
    public void getByUUID() {
        try {
            TransferResponseDto transfer = createTransfer();
            TransferResponseDto transfer2 = getTransferByUuid(transfer.getId());

            Assert.assertEquals(transfer.getId(), transfer2.getId());
            Assert.assertEquals(transfer.getState(), transfer2.getState());

        } catch (Exception e) {
            Assert.assertFalse(e.getLocalizedMessage(), true);
        }
    }

    @Test
    public void checkOverBalance() {

        AccountResponseDto account1 = null;

        try {
            account1 = AccountTest.createAccount();
            AccountResponseDto account2 = AccountTest.createAccount();

            TransferRequestDto transfer = new TransferRequestDto();
            transfer.setAmount(account1.getBalance().add(BigDecimal.ONE));
            transfer.setDescription("Description");
            transfer.setCurrency(account1.getCurrency());
            transfer.setRequestId(RandomStringUtils.random(36));
            transfer.setSourceAccountId(account1.getId());
            transfer.setTargetAccountId(account2.getId());

            NotEnoughMoneyException notEnoughMoneyException =
                    Unirest.post(URL + API_TRANSFERS)
                           .body(transfer)
                           .asObject(NotEnoughMoneyException.class)
                           .getBody();

            throw notEnoughMoneyException;

        } catch (UnirestException e) {
            Assert.assertFalse(e.getLocalizedMessage(), true);
        } catch (NotEnoughMoneyException e) {
            Assert.assertEquals(e.getUuid(), account1.getId());
        }

    }

    @Test
    public void checkRequestId() {

        try {
            AccountResponseDto account1 = AccountTest.createAccount();
            AccountResponseDto account2 = AccountTest.createAccount();

            TransferRequestDto transfer = new TransferRequestDto();
            transfer.setAmount(BigDecimal.ONE);
            transfer.setDescription("Description");
            transfer.setCurrency(account1.getCurrency());
            transfer.setRequestId(RandomStringUtils.randomAlphabetic(36));
            transfer.setSourceAccountId(account1.getId());
            transfer.setTargetAccountId(account2.getId());

            Unirest.post(URL + API_TRANSFERS)
                   .body(transfer)
                   .asJson()
                   .getBody();

            Unirest.post(URL + API_TRANSFERS)
                   .body(transfer)
                   .asJson()
                   .getBody();

            AccountResponseDto accountUpdated = AccountTest.getAccountByUuid(account1.getId());

            Assert.assertEquals(account1.getBalance().subtract(BigDecimal.ONE), accountUpdated.getBalance());

        } catch (UnirestException e) {
            Assert.assertFalse(e.getLocalizedMessage(), true);
        }

    }

    @Test
    public void checkCreatingTransfer() {
        try {

            AccountResponseDto source = AccountTest.createAccount();
            AccountResponseDto target = AccountTest.createAccount();

            BigDecimal amount = source.getBalance().subtract(BigDecimal.ONE);

            TransferRequestDto transfer = new TransferRequestDto();
            transfer.setAmount(amount);
            transfer.setDescription("Description");
            transfer.setCurrency(source.getCurrency());
            transfer.setRequestId(RandomStringUtils.randomAlphabetic(36));
            transfer.setSourceAccountId(source.getId());
            transfer.setTargetAccountId(target.getId());

            Unirest.post(URL + API_TRANSFERS)
                   .body(transfer)
                   .asJson();

            AccountResponseDto source2 = AccountTest.getAccountByUuid(source.getId());
            AccountResponseDto target2 = AccountTest.getAccountByUuid(target.getId());

            Assert.assertEquals(source2.getBalance(), source.getBalance().subtract(amount));
            Assert.assertEquals(target.getBalance().add(amount), target2.getBalance());

        } catch (UnirestException e) {
            Assert.assertFalse(e.getLocalizedMessage(), true);
        }
    }

    private TransferResponseDto createTransfer() throws UnirestException {

        AccountResponseDto account1 = AccountTest.createAccount();
        AccountResponseDto account2 = AccountTest.createAccount();

        TransferRequestDto transfer = new TransferRequestDto();
        transfer.setAmount(BigDecimal.ONE);
        transfer.setDescription("Description");
        transfer.setCurrency("EUR");
        transfer.setRequestId(RandomStringUtils.random(36));
        transfer.setSourceAccountId(account1.getId());
        transfer.setTargetAccountId(account2.getId());

        return Unirest.post(URL + API_TRANSFERS)
                      .body(transfer)
                      .asObject(TransferResponseDto.class)
                      .getBody();
    }

    public static TransferResponseDto getTransferByUuid(String uuid) throws UnirestException {
        return Unirest.get(URL + API_TRANSFERS + "/" + uuid)
                      .asObject(TransferResponseDto.class)
                      .getBody();
    }

}

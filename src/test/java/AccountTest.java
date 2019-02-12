import com.devisv.rest.dto.AccountRequestDto;
import com.devisv.rest.dto.AccountResponseDto;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

public class AccountTest extends AbstractTest {

    public static final String API_ACCOUNTS = "api/accounts";

    public AccountTest() {
    }

    @Test
    public void getAllAccounts() {
        try {
            createAccount();

            Assert.assertNotEquals(
                    Unirest.get(URL + API_ACCOUNTS)
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

            AccountResponseDto account1 = createAccount();
            AccountResponseDto account2 = getAccountByUuid(account1.getId());

            Assert.assertEquals(account1.getId(), account2.getId());
            Assert.assertEquals(account1.getBalance(), account2.getBalance());
            Assert.assertEquals(account1.getCurrency(), account2.getCurrency());
            Assert.assertEquals(account1.getDescription(), account2.getDescription());

        } catch (Exception e) {
            Assert.assertFalse(e.getLocalizedMessage(), true);
        }
    }

    public static AccountResponseDto getAccountByUuid(String uuid) throws UnirestException {
        return Unirest.get(URL + API_ACCOUNTS + "/" + uuid)
                      .asObject(AccountResponseDto.class)
                      .getBody();
    }

    public static AccountResponseDto createAccount() throws UnirestException {

        AccountRequestDto requestDto = new AccountRequestDto();
        requestDto.setBalance(BigDecimal.valueOf(1000));
        requestDto.setCurrency("EUR");
        requestDto.setDescription("Description");

        return Unirest.post(URL + API_ACCOUNTS)
                      .body(requestDto)
                      .asObject(AccountResponseDto.class)
                      .getBody();
    }

}

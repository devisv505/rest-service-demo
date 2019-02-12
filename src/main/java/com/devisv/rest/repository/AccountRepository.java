package com.devisv.rest.repository;

import com.devisv.rest.JdbcTemplate;
import com.devisv.rest.model.Account;
import org.flywaydb.core.internal.jdbc.RowMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Account Repository
 */
@Singleton
public class AccountRepository implements Repository<Account> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountRepository.class);

    private static final String SELECT_FROM_ACCOUNTS_WHERE_UUID = "select * from accounts a where a.uuid = ?";

    private static final String SELECT_FROM_ACCOUNTS = "select * from accounts";

    private static final String UPDATE_ACCOUNT =
            "update accounts "
                    + "set "
                    + "  balance = ? "
                    + "where id = ?";

    private static final RowMapper<Account> ACCOUNT_ROW_MAPPER = rs -> {
        Account account = new Account();

        account.setId(rs.getLong("id"));
        account.setUuid(rs.getString("uuid"));
        account.setBalance(rs.getBigDecimal("balance"));
        account.setDescription(rs.getString("description"));
        account.setCurrency(rs.getString("currency"));

        return account;
    };

    private final JdbcTemplate jdbcTemplate;

    @Inject
    public AccountRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Return Account with certain UUID
     *
     * @param uuid
     * @return
     */
    public Account getByUuid(UUID uuid) {
        try {
            return jdbcTemplate.querySingleRow(
                    SELECT_FROM_ACCOUNTS_WHERE_UUID,
                    new Object[] {uuid},
                    ACCOUNT_ROW_MAPPER
            );
        } catch (SQLException e) {
            LOGGER.error(e.getLocalizedMessage(), e);
        }

        return null;

    }

    /**
     * Return all Account in DB
     *
     * @return
     */
    @Override
    public List<Account> getAll() {
        try {
            return jdbcTemplate.query(SELECT_FROM_ACCOUNTS, new Object[] {}, ACCOUNT_ROW_MAPPER);
        } catch (SQLException e) {
            LOGGER.error(e.getLocalizedMessage(), e);
        }

        return Collections.emptyList();
    }

    /**
     * Update Account
     *
     * @param account
     * @return
     */
    public Account update(Account account) {

        try {
            jdbcTemplate.update(
                    UPDATE_ACCOUNT,
                    new Object[] {account.getBalance(), account.getId()}
            );
        } catch (SQLException e) {
            LOGGER.error(e.getLocalizedMessage(), e);
        }

        return account;

    }
}

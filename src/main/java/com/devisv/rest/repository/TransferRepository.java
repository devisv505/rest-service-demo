package com.devisv.rest.repository;

import com.devisv.rest.JdbcTemplate;
import com.devisv.rest.model.State;
import com.devisv.rest.model.Transfer;
import org.flywaydb.core.internal.jdbc.RowMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Singleton
public class TransferRepository implements Repository<Transfer> {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransferRepository.class);

    private static final String SELECT_FROM_TRANSFER_WHERE_UUID = "select * from transfers a where a.uuid = ?";

    private static final String SELECT_FROM_TRANSFER_WHERE_ID = "select * from transfers a where a.id = ?";

    private static final String SELECT_FROM_TRANSFER = "select * from transfers";

    private static final String INSERT_INTO_TRANSFER =
            "insert into transfers "
                    + "(uuid, source_account_id, target_account_id, amount, created_at, description, currency, state) "
                    + "values "
                    + "(?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String UPDATE_INTO_TRANSFER =
            "update transfers "
                    + "set "
                    + "  state = ?, completed_at = ? "
                    + "where uuid = ?";

    private static final RowMapper<Transfer> TRANSFER_ROW_MAPPER = rs -> {

        Transfer transfer = new Transfer();

        transfer.setId(rs.getLong("id"));
        transfer.setAmount(rs.getBigDecimal("amount"));
        transfer.setCurrency(rs.getString("currency"));
        transfer.setDescription(rs.getString("description"));
        transfer.setSourceAccountId(rs.getLong("source_account_id"));
        transfer.setTargetAccountId(rs.getLong("target_account_id"));
        transfer.setUuid(rs.getString("uuid"));
        transfer.setCreatedAt(rs.getTimestamp("created_at"));
        transfer.setCompletedAt(rs.getTimestamp("completed_at"));
        transfer.setState(State.valueOf(rs.getString("state")));

        return transfer;
    };

    private final JdbcTemplate jdbcTemplate;

    @Inject
    public TransferRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Transfer getByUuid(UUID uuid) {
        try {
            return jdbcTemplate.querySingleRow(
                    SELECT_FROM_TRANSFER_WHERE_UUID,
                    new Object[] {uuid},
                    TRANSFER_ROW_MAPPER
            );
        } catch (SQLException e) {
            LOGGER.error(e.getLocalizedMessage(), e);
        }

        return null;

    }

    public Transfer getById(Long id) {
        try {
            return jdbcTemplate.querySingleRow(
                    SELECT_FROM_TRANSFER_WHERE_ID,
                    new Object[] {id},
                    TRANSFER_ROW_MAPPER
            );
        } catch (SQLException e) {
            LOGGER.error(e.getLocalizedMessage(), e);
        }

        return null;
    }

    @Override
    public List<Transfer> getAll() {
        try {
            return jdbcTemplate.query(SELECT_FROM_TRANSFER, new Object[] {}, TRANSFER_ROW_MAPPER);
        } catch (SQLException e) {
            LOGGER.error(e.getLocalizedMessage(), e);
        }

        return Collections.emptyList();
    }

    public Transfer create(Transfer transfer) {

        try {
            final Long id =
                    jdbcTemplate.execute(
                            INSERT_INTO_TRANSFER,
                            new Object[] {transfer.getUuid(), transfer.getSourceAccountId(),
                                    transfer.getTargetAccountId(),
                                    transfer.getAmount(), transfer.getCreatedAt(), transfer.getDescription(), "EUR",
                                    transfer.getState()}
                    );

            transfer.setId(id);

            return transfer;

        } catch (SQLException e) {
            LOGGER.error(e.getLocalizedMessage(), e);
        }

        return null;

    }

    public Transfer update(Transfer transfer) {

        try {
            jdbcTemplate.update(
                    UPDATE_INTO_TRANSFER,
                    new Object[] {transfer.getState(), transfer.getCompletedAt(), transfer.getUuid()}
            );
        } catch (SQLException e) {
            LOGGER.error(e.getLocalizedMessage(), e);
        }

        return transfer;
    }

}

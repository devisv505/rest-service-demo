package com.devisv.rest.repository;

import com.devisv.rest.JdbcTemplate;
import com.devisv.rest.model.Request;
import org.flywaydb.core.internal.jdbc.RowMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

/**
 * Request repository
 */
@Singleton
public class RequestRepository implements Repository<Request> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RequestRepository.class);

    private static final String SELECT_FROM_REQUEST_WHERE_ID = "select * from requests r where r.request_id = ?";

    private static final String INSERT_INTO_REQUEST =
            "insert into requests "
                    + "(request_id, transfer_id) "
                    + "values "
                    + "(?, ?)";

    private static final RowMapper<Request> REQUEST_ROW_MAPPER = rs -> {

        Request request = new Request();

        request.setId(rs.getLong("id"));
        request.setRequestId(rs.getString("request_id"));
        request.setTransferId(rs.getLong("transfer_id"));

        return request;
    };

    private final JdbcTemplate jdbcTemplate;

    @Inject
    public RequestRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Return request with certain ID
     *
     * @param id
     * @return
     */
    public Request getById(String id) {
        try {
            return jdbcTemplate.querySingleRow(
                    SELECT_FROM_REQUEST_WHERE_ID,
                    new Object[] {id},
                    REQUEST_ROW_MAPPER
            );
        } catch (SQLException e) {
            LOGGER.error(e.getLocalizedMessage(), e);
        }

        return null;

    }

    /**
     * Create new Request
     *
     * @param request
     * @return
     */
    public Request create(Request request) {
        try {
            final Long id =
                    jdbcTemplate.execute(
                            INSERT_INTO_REQUEST,
                            new Object[] {request.getRequestId(), request.getTransferId()}
                    );

            request.setId(id);

            return request;

        } catch (SQLException e) {
            LOGGER.error(e.getLocalizedMessage(), e);
        }

        return null;
    }

    /**
     * Not supported operation
     *
     * @param uuid
     * @return
     */
    @Override
    public Request getByUuid(UUID uuid) {
        throw new UnsupportedOperationException("This operation not supported");
    }

    /**
     * Not supported operation
     *
     * @return
     */
    @Override
    public List<Request> getAll() {
        throw new UnsupportedOperationException("This operation not supported");
    }
}

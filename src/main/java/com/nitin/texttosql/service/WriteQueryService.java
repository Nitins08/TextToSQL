package com.nitin.texttosql.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.Savepoint;

@Service
public class WriteQueryService {

    @Value("${app.sql.writes.max-affected-rows:10}")
    private int maxAffectedRows;

    private final JdbcTemplate jdbcTemplate;

    public WriteQueryService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int executeWrite(String sql) {

        return jdbcTemplate.execute((Connection connection) -> {

            boolean originalAutoCommit = connection.getAutoCommit();

            try {
                connection.setAutoCommit(false);

                Savepoint savepoint =
                        connection.setSavepoint("BEFORE_WRITE");

                try {
                    int affectedRows =
                            connection.createStatement()
                                    .executeUpdate(sql);

                    if (affectedRows <= 0) {
                        connection.rollback(savepoint);

                        throw new IllegalArgumentException(
                                "Write operation did not modify any rows."
                        );
                    }

                    if (affectedRows > maxAffectedRows) {
                        connection.rollback(savepoint);

                        throw new IllegalArgumentException(
                                "Write operation would modify too many rows. Maximum allowed: "
                                        + maxAffectedRows
                        );
                    }

                    connection.commit();

                    return affectedRows;

                } catch (Exception exception) {

                    connection.rollback(savepoint);
                    connection.rollback();

                    throw exception;
                }

            } finally {
                connection.setAutoCommit(originalAutoCommit);
            }
        });
    }
}
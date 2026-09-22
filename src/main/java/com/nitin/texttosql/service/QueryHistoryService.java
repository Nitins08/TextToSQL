package com.nitin.texttosql.service;

import com.nitin.texttosql.model.QueryHistory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class QueryHistoryService {

    private final JdbcTemplate jdbcTemplate;

    public QueryHistoryService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void record(
            String question,
            String sql,
            String operationType,
            boolean successful,
            long executionTimeMs) {

        String query = """
                INSERT INTO QUERY_HISTORY
                (QUESTION, SQL_QUERY, OPERATION_TYPE, SUCCESSFUL,
                 EXECUTION_TIME_MS, TIMESTAMP)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        jdbcTemplate.update(
                query,
                question,
                sql,
                operationType,
                successful,
                executionTimeMs,
                LocalDateTime.now()
        );
    }

    public List<QueryHistory> getHistory() {

        String query = """
                SELECT ID, QUESTION, SQL_QUERY, OPERATION_TYPE,
                       SUCCESSFUL, EXECUTION_TIME_MS, TIMESTAMP
                FROM QUERY_HISTORY
                ORDER BY ID DESC
                """;

        return jdbcTemplate.query(query, (rs, rowNum) ->
                new QueryHistory(
                        rs.getLong("ID"),
                        rs.getString("QUESTION"),
                        rs.getString("SQL_QUERY"),
                        rs.getString("OPERATION_TYPE"),
                        rs.getBoolean("SUCCESSFUL"),
                        rs.getLong("EXECUTION_TIME_MS"),
                        rs.getTimestamp("TIMESTAMP").toLocalDateTime()
                )
        );
    }
}
package com.nitin.texttosql.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class QueryService {

    private static final int MAX_ROWS = 100;

    private final JdbcTemplate jdbcTemplate;

    public QueryService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Map<String, Object>> executeQuery(String sql) {

        String normalizedSql = sql.trim();

        /*
         * Remove one trailing semicolon.
         */
        normalizedSql = normalizedSql.replaceAll(";\\s*$", "");

        /*
         * Apply the maximum row limit only when the query
         * does not already contain a LIMIT clause.
         */
        if (!normalizedSql.matches("(?is).*\\blimit\\s+\\d+\\s*$")) {
            normalizedSql = normalizedSql
                    + " LIMIT " + MAX_ROWS;
        }

        return jdbcTemplate.queryForList(normalizedSql);
    }
}
package com.nitin.texttosql.model;

import java.util.List;
import java.util.Map;

public class QueryResponse {

    private final String question;
    private final String sql;
    private final String type;
    private final boolean success;
    private final long executionTimeMs;
    private final Integer rowCount;
    private final Integer affectedRows;
    private final List<Map<String, Object>> data;

    public QueryResponse(
            String question,
            String sql,
            String type,
            boolean success,
            long executionTimeMs,
            Integer rowCount,
            Integer affectedRows,
            List<Map<String, Object>> data) {

        this.question = question;
        this.sql = sql;
        this.type = type;
        this.success = success;
        this.executionTimeMs = executionTimeMs;
        this.rowCount = rowCount;
        this.affectedRows = affectedRows;
        this.data = data;
    }

    public String getQuestion() {
        return question;
    }

    public String getSql() {
        return sql;
    }

    public String getType() {
        return type;
    }

    public boolean isSuccess() {
        return success;
    }

    public long getExecutionTimeMs() {
        return executionTimeMs;
    }

    public Integer getRowCount() {
        return rowCount;
    }

    public Integer getAffectedRows() {
        return affectedRows;
    }

    public List<Map<String, Object>> getData() {
        return data;
    }
}
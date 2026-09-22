package com.nitin.texttosql.model;

import java.time.LocalDateTime;

public class QueryHistory {

    private final long id;
    private final String question;
    private final String sql;
    private final String operationType;
    private final boolean successful;
    private final long executionTimeMs;
    private final LocalDateTime timestamp;

    public QueryHistory(
            long id,
            String question,
            String sql,
            String operationType,
            boolean successful,
            long executionTimeMs,
            LocalDateTime timestamp) {

        this.id = id;
        this.question = question;
        this.sql = sql;
        this.operationType = operationType;
        this.successful = successful;
        this.executionTimeMs = executionTimeMs;
        this.timestamp = timestamp;
    }

    public long getId() {
        return id;
    }

    public String getQuestion() {
        return question;
    }

    public String getSql() {
        return sql;
    }

    public String getOperationType() {
        return operationType;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public long getExecutionTimeMs() {
        return executionTimeMs;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
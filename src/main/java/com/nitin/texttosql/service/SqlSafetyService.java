package com.nitin.texttosql.service;

import org.springframework.stereotype.Service;

@Service
public class SqlSafetyService {

    private static final String[] DANGEROUS_KEYWORDS = {
            "insert",
            "update",
            "delete",
            "drop",
            "alter",
            "truncate",
            "create",
            "merge",
            "grant",
            "revoke",
            "call",
            "execute",
            "commit",
            "rollback",
            "savepoint"
    };

    public boolean isSafe(String sql) {

        if (sql == null || sql.isBlank()) {
            return false;
        }

        String normalizedSql = normalize(sql);

        if (!normalizedSql.startsWith("select ")) {
            return false;
        }

        if (containsMultipleStatements(normalizedSql)) {
            return false;
        }

        if (containsComments(normalizedSql)) {
            return false;
        }

        if (containsDangerousKeyword(normalizedSql)) {
            return false;
        }

        if (containsIntoClause(normalizedSql)) {
            return false;
        }

        if (containsForUpdate(normalizedSql)) {
            return false;
        }

        return true;
    }

    private String normalize(String sql) {

        String normalized = sql.trim();

        normalized = normalized.replaceAll(";\\s*$", "");

        normalized = normalized
                .replaceAll("\\s+", " ")
                .toLowerCase();

        return normalized;
    }

    private boolean containsMultipleStatements(String sql) {
        return sql.contains(";");
    }

    private boolean containsComments(String sql) {
        return sql.contains("--")
                || sql.contains("/*")
                || sql.contains("*/");
    }

    private boolean containsDangerousKeyword(String sql) {

        for (String keyword : DANGEROUS_KEYWORDS) {

            if (sql.matches(".*\\b" + keyword + "\\b.*")) {
                return true;
            }
        }

        return false;
    }

    private boolean containsIntoClause(String sql) {
        return sql.matches(".*\\binto\\b.*");
    }

    private boolean containsForUpdate(String sql) {
        return sql.matches(".*\\bfor\\s+update\\b.*");
    }
}
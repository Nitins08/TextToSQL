package com.nitin.texttosql.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SqlWriteSafetyService {

    @Value("${app.sql.writes.enabled:false}")
    private boolean writesEnabled;

    private static final String[] ALLOWED_TABLES = {
            "users",
            "products",
            "orders"
    };

    private static final String[] DANGEROUS_KEYWORDS = {
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

    public boolean areWritesEnabled() {
        return writesEnabled;
    }

    public boolean isWriteOperation(String sql) {

        if (sql == null || sql.isBlank()) {
            return false;
        }

        String normalizedSql = normalize(sql);

        return normalizedSql.startsWith("insert ")
                || normalizedSql.startsWith("update ")
                || normalizedSql.startsWith("delete ");
    }

    public boolean isSafeWrite(String sql) {

        if (!writesEnabled || !isWriteOperation(sql)) {
            return false;
        }

        String normalizedSql = normalize(sql);

        if (containsMultipleStatements(normalizedSql)) {
            return false;
        }

        if (containsComments(normalizedSql)) {
            return false;
        }

        if (containsDangerousKeyword(normalizedSql)) {
            return false;
        }

        if (!usesAllowedTable(normalizedSql)) {
            return false;
        }

        if (isUpdateOrDelete(normalizedSql)
                && !containsWhereClause(normalizedSql)) {
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

    private boolean isUpdateOrDelete(String sql) {
        return sql.startsWith("update ")
                || sql.startsWith("delete ");
    }

    private boolean containsWhereClause(String sql) {
        return sql.matches(".*\\bwhere\\b.*");
    }

    private boolean usesAllowedTable(String sql) {

        for (String table : ALLOWED_TABLES) {

            if (sql.matches("^(insert\\s+into|update|delete\\s+from)\\s+"
                    + table + "\\b.*")) {
                return true;
            }
        }

        return false;
    }
}
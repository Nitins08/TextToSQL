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

    public boolean areWritesEnabled() {
        return writesEnabled;
    }

    public boolean isWriteOperation(String sql) {

        if (sql == null || sql.isBlank()) {
            return false;
        }

        String normalizedSql = sql.trim().toLowerCase();

        return normalizedSql.startsWith("insert ")
                || normalizedSql.startsWith("update ")
                || normalizedSql.startsWith("delete ");
    }

    public boolean isSafeWrite(String sql) {

        if (!writesEnabled) {
            return false;
        }

        if (!isWriteOperation(sql)) {
            return false;
        }

        String normalizedSql = sql.trim().toLowerCase();

        /*
         * Remove one trailing semicolon.
         */
        String withoutTrailingSemicolon =
                normalizedSql.replaceAll(";\\s*$", "");

        /*
         * Reject multiple SQL statements.
         */
        if (withoutTrailingSemicolon.contains(";")) {
            return false;
        }

        /*
         * Reject SQL comments.
         */
        if (normalizedSql.contains("--")
                || normalizedSql.contains("/*")
                || normalizedSql.contains("*/")) {
            return false;
        }

        /*
         * Reject dangerous database operations.
         */
        String[] dangerousKeywords = {
                "drop",
                "alter",
                "truncate",
                "create",
                "merge",
                "grant",
                "revoke",
                "call",
                "execute"
        };

        for (String keyword : dangerousKeywords) {

            if (normalizedSql.matches(
                    ".*\\b" + keyword + "\\b.*")) {

                return false;
            }
        }

        /*
         * UPDATE and DELETE must contain WHERE.
         */
        if (normalizedSql.startsWith("update ")
                || normalizedSql.startsWith("delete ")) {

            if (!normalizedSql.matches(".*\\bwhere\\b.*")) {
                return false;
            }
        }

        /*
         * Verify that the write targets an allowed table.
         */
        if (!usesAllowedTable(normalizedSql)) {
            return false;
        }

        return true;
    }

    private boolean usesAllowedTable(String sql) {

        for (String table : ALLOWED_TABLES) {

            if (sql.matches(
                    ".*\\b" + table + "\\b.*")) {

                return true;
            }
        }

        return false;
    }
}
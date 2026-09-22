package com.nitin.texttosql.service;

import org.springframework.stereotype.Service;

@Service
public class SqlSafetyService {

    public boolean isSafe(String sql) {

        if (sql == null || sql.isBlank()) {
            return false;
        }

        String normalizedSql = sql.trim().toLowerCase();

        /*
         * Only SELECT statements are allowed through the READ path.
         */
        if (!normalizedSql.startsWith("select ")) {
            return false;
        }

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
         * Reject operations that can modify database structure
         * or execute non-read operations.
         */
        String[] dangerousKeywords = {
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
                "execute"
        };

        for (String keyword : dangerousKeywords) {

            if (normalizedSql.matches(
                    ".*\\b" + keyword + "\\b.*")) {

                return false;
            }
        }

        /*
         * SELECT INTO can create or modify database objects
         * depending on the database.
         */
        if (normalizedSql.matches(".*\\binto\\b.*")) {
            return false;
        }

        /*
         * Locking rows is not required for our READ-only endpoint.
         */
        if (normalizedSql.matches(".*\\bfor\\s+update\\b.*")) {
            return false;
        }

        return true;
    }
}
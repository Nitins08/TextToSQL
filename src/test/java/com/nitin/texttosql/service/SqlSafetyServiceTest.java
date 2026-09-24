package com.nitin.texttosql.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SqlSafetyServiceTest {

    private final SqlSafetyService service = new SqlSafetyService();

    @Test
    void shouldAllowSimpleSelect() {
        assertTrue(service.isSafe(
                "SELECT * FROM users"
        ));
    }

    @Test
    void shouldAllowSelectWithJoin() {
        assertTrue(service.isSafe("""
                SELECT users.name, orders.quantity
                FROM users
                JOIN orders ON users.id = orders.user_id
                """));
    }

    @Test
    void shouldRejectInsert() {
        assertFalse(service.isSafe(
                "INSERT INTO users VALUES (5, 'David', 'david@test.com', 'Chennai')"
        ));
    }

    @Test
    void shouldRejectUpdate() {
        assertFalse(service.isSafe(
                "UPDATE users SET city = 'Chennai' WHERE id = 1"
        ));
    }

    @Test
    void shouldRejectDelete() {
        assertFalse(service.isSafe(
                "DELETE FROM users WHERE id = 1"
        ));
    }

    @Test
    void shouldRejectDrop() {
        assertFalse(service.isSafe(
                "SELECT * FROM users; DROP TABLE users"
        ));
    }

    @Test
    void shouldRejectMultipleStatements() {
        assertFalse(service.isSafe(
                "SELECT * FROM users; SELECT * FROM products"
        ));
    }

    @Test
    void shouldRejectSqlComments() {
        assertFalse(service.isSafe(
                "SELECT * FROM users -- comment"
        ));
    }

    @Test
    void shouldRejectSelectInto() {
        assertFalse(service.isSafe(
                "SELECT * INTO new_table FROM users"
        ));
    }

    @Test
    void shouldRejectForUpdate() {
        assertFalse(service.isSafe(
                "SELECT * FROM users FOR UPDATE"
        ));
    }

    @Test
    void shouldRejectBlankSql() {
        assertFalse(service.isSafe(""));
    }

    @Test
    void shouldRejectNullSql() {
        assertFalse(service.isSafe(null));
    }
}
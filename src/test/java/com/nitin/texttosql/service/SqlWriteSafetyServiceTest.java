package com.nitin.texttosql.service;

import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class SqlWriteSafetyServiceTest {

    private SqlWriteSafetyService createService(boolean enabled) {
        SqlWriteSafetyService service = new SqlWriteSafetyService();

        ReflectionTestUtils.setField(
                service,
                "writesEnabled",
                enabled
        );

        return service;
    }

    @Test
    void shouldRecognizeInsert() {
        SqlWriteSafetyService service = createService(true);

        assertTrue(service.isWriteOperation(
                "INSERT INTO users (id, name) VALUES (5, 'David')"
        ));
    }

    @Test
    void shouldRecognizeUpdate() {
        SqlWriteSafetyService service = createService(true);

        assertTrue(service.isWriteOperation(
                "UPDATE users SET city = 'Chennai' WHERE id = 2"
        ));
    }

    @Test
    void shouldRecognizeDelete() {
        SqlWriteSafetyService service = createService(true);

        assertTrue(service.isWriteOperation(
                "DELETE FROM users WHERE id = 2"
        ));
    }

    @Test
    void shouldRejectWritesWhenDisabled() {
        SqlWriteSafetyService service = createService(false);

        assertFalse(service.isSafeWrite(
                "UPDATE users SET city = 'Chennai' WHERE id = 2"
        ));
    }

    @Test
    void shouldAllowSafeUpdate() {
        SqlWriteSafetyService service = createService(true);

        assertTrue(service.isSafeWrite(
                "UPDATE users SET city = 'Chennai' WHERE id = 2"
        ));
    }

    @Test
    void shouldRejectUpdateWithoutWhere() {
        SqlWriteSafetyService service = createService(true);

        assertFalse(service.isSafeWrite(
                "UPDATE users SET city = 'Chennai'"
        ));
    }

    @Test
    void shouldRejectDeleteWithoutWhere() {
        SqlWriteSafetyService service = createService(true);

        assertFalse(service.isSafeWrite(
                "DELETE FROM users"
        ));
    }

    @Test
    void shouldRejectMultipleStatements() {
        SqlWriteSafetyService service = createService(true);

        assertFalse(service.isSafeWrite(
                "UPDATE users SET city = 'Chennai' WHERE id = 2; DELETE FROM users WHERE id = 3"
        ));
    }

    @Test
    void shouldRejectComments() {
        SqlWriteSafetyService service = createService(true);

        assertFalse(service.isSafeWrite(
                "UPDATE users SET city = 'Chennai' WHERE id = 2 -- comment"
        ));
    }

    @Test
    void shouldRejectUnknownTable() {
        SqlWriteSafetyService service = createService(true);

        assertFalse(service.isSafeWrite(
                "DELETE FROM payments WHERE id = 1"
        ));
    }

    @Test
    void shouldRejectDangerousKeyword() {
        SqlWriteSafetyService service = createService(true);

        assertFalse(service.isSafeWrite(
                "UPDATE users SET city = 'Chennai' WHERE id = 2 DROP TABLE users"
        ));
    }
}
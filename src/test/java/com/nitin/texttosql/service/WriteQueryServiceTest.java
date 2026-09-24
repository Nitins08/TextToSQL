package com.nitin.texttosql.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class WriteQueryServiceTest {

    @Autowired
    private WriteQueryService writeQueryService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void cleanDatabase() {
        jdbcTemplate.update("DELETE FROM orders");
        jdbcTemplate.update("DELETE FROM users");
        jdbcTemplate.update("DELETE FROM products");

        jdbcTemplate.update("""
                INSERT INTO users (id, name, email, city)
                VALUES (1, 'Alice', 'alice@test.com', 'Chennai')
                """);

        jdbcTemplate.update("""
                INSERT INTO products (id, name, category, price)
                VALUES (1, 'Laptop', 'Electronics', 75000)
                """);
    }

    @Test
    void shouldInsertSuccessfully() {

        int affectedRows = writeQueryService.executeWrite("""
                INSERT INTO users (id, name, email, city)
                VALUES (2, 'Bob', 'bob@test.com', 'Chennai')
                """);

        assertEquals(1, affectedRows);

        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM users WHERE id = 2",
                Integer.class
        );

        assertEquals(1, count);
    }

    @Test
    void shouldUpdateSuccessfully() {

        int affectedRows = writeQueryService.executeWrite("""
                UPDATE users
                SET city = 'Bangalore'
                WHERE id = 1
                """);

        assertEquals(1, affectedRows);

        String city = jdbcTemplate.queryForObject(
                "SELECT city FROM users WHERE id = 1",
                String.class
        );

        assertEquals("Bangalore", city);
    }

    @Test
    void shouldDeleteSuccessfully() {

        int affectedRows = writeQueryService.executeWrite("""
                DELETE FROM users
                WHERE id = 1
                """);

        assertEquals(1, affectedRows);

        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM users WHERE id = 1",
                Integer.class
        );

        assertEquals(0, count);
    }

    @Test
    void shouldRollbackWhenNoRowsAreAffected() {

        assertThrows(
                IllegalArgumentException.class,
                () -> writeQueryService.executeWrite("""
                        UPDATE users
                        SET city = 'Mumbai'
                        WHERE id = 999
                        """)
        );

        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM users WHERE id = 1",
                Integer.class
        );

        assertEquals(1, count);
    }

    @Test
    void shouldRollbackWhenForeignKeyConstraintFails() {

        assertThrows(
                RuntimeException.class,
                () -> writeQueryService.executeWrite("""
                        INSERT INTO orders
                        (id, user_id, product_id, quantity, order_date)
                        VALUES
                        (1, 999, 1, 1, '2026-01-01')
                        """)
        );

        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM orders",
                Integer.class
        );

        assertEquals(0, count);
    }

    @Test
    void shouldRollbackWhenDuplicatePrimaryKeyFails() {

        assertThrows(
                RuntimeException.class,
                () -> writeQueryService.executeWrite("""
                        INSERT INTO users
                        (id, name, email, city)
                        VALUES
                        (1, 'Duplicate', 'duplicate@test.com', 'Chennai')
                        """)
        );

        String name = jdbcTemplate.queryForObject(
                "SELECT name FROM users WHERE id = 1",
                String.class
        );

        assertEquals("Alice", name);
    }
}
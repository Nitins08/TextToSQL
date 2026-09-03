INSERT INTO users (id, name, email, city) VALUES
(1, 'Alice', 'alice@gmail.com', 'Chennai'),
(2, 'Bob', 'bob@gmail.com', 'Bangalore'),
(3, 'Charlie', 'charlie@gmail.com', 'Hyderabad');

INSERT INTO products (id, name, category, price) VALUES
(1, 'Laptop', 'Electronics', 75000.00),
(2, 'Phone', 'Electronics', 30000.00),
(3, 'Keyboard', 'Accessories', 2500.00),
(4, 'Mouse', 'Accessories', 1200.00);

INSERT INTO orders (id, user_id, product_id, quantity, order_date) VALUES
(1, 1, 1, 1, '2025-01-10'),
(2, 1, 3, 2, '2025-02-15'),
(3, 2, 2, 1, '2025-03-18'),
(4, 3, 4, 3, '2025-04-12'),
(5, 2, 1, 1, '2025-05-01');
CREATE OR REPLACE VIEW customers AS
SELECT id, first_name, last_name, market, date_of_birth, phone_number, created_at
FROM customers_ro_remote

UNION ALL

SELECT id, first_name, last_name, market, date_of_birth, phone_number, created_at
FROM customers_bg_remote;



CREATE OR REPLACE VIEW addresses AS
SELECT id, customer_id, street, city, region, country, postal_code
FROM addresses_ro_remote

UNION ALL

SELECT id, customer_id, street, city, region, country, postal_code
FROM addresses_bg_remote;



CREATE OR REPLACE VIEW accounts AS
SELECT id, customer_id, username, email, password, role
FROM accounts_ro_remote

UNION ALL

SELECT id, customer_id, username, email, password, role
FROM accounts_bg_remote

UNION ALL

SELECT id, NULL AS customer_id, username, email, password, role
FROM accounts_admin;



CREATE OR REPLACE VIEW tokens AS
SELECT id, account_id, token, revoked, expired
FROM tokens_ro_remote

UNION ALL

SELECT id, account_id, token, revoked, expired
FROM tokens_bg_remote

UNION ALL

SELECT id, account_id, token, revoked, expired
FROM tokens_admin;



CREATE OR REPLACE VIEW orders AS
SELECT id, customer_id, created_at, status, payment_method
FROM orders_ro_remote

UNION ALL

SELECT id, customer_id, created_at, status, payment_method
FROM orders_bg_remote;



CREATE OR REPLACE VIEW order_items AS
SELECT id, order_id, product_id, price_id, quantity
FROM order_items_ro_remote

UNION ALL

SELECT id, order_id, product_id, price_id, quantity
FROM order_items_bg_remote;



CREATE OR REPLACE VIEW deliveries AS
SELECT id, order_id, address_id, shipped_by, awb
FROM deliveries_ro_remote

UNION ALL

SELECT id, order_id, address_id, shipped_by, awb
FROM deliveries_bg_remote;



CREATE OR REPLACE VIEW prices AS
SELECT id, product_id, market, value, currency, status, created_at
FROM prices_ro_remote

UNION ALL

SELECT id, product_id, market, value, currency, status, created_at
FROM prices_bg_remote;



CREATE OR REPLACE VIEW inventories AS
SELECT id, product_id, warehouse_code, stock_available, updated_at
FROM inventories_ro_remote

UNION ALL

SELECT id, product_id, warehouse_code, stock_available, updated_at
FROM inventories_bg_remote;
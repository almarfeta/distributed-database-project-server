CREATE OR REPLACE TRIGGER trg_customers_global_ins
    INSTEAD OF INSERT
    ON customers
    FOR EACH ROW
BEGIN
    IF :NEW.market = 'RO' THEN
        INSERT INTO customers_ro_remote (id, first_name, last_name, market, date_of_birth, phone_number, created_at)
        VALUES (NVL(:NEW.id, SYS_GUID()),
                :NEW.first_name,
                :NEW.last_name,
                'RO',
                :NEW.date_of_birth,
                :NEW.phone_number,
                NVL(:NEW.created_at, SYSTIMESTAMP));

    ELSIF :NEW.market = 'BG' THEN
        INSERT INTO customers_bg_remote (id, first_name, last_name, market, date_of_birth, phone_number, created_at)
        VALUES (NVL(:NEW.id, SYS_GUID()),
                :NEW.first_name,
                :NEW.last_name,
                'BG',
                :NEW.date_of_birth,
                :NEW.phone_number,
                NVL(:NEW.created_at, SYSTIMESTAMP));

    ELSE
        RAISE_APPLICATION_ERROR(-20001, 'Invalid market.');
    END IF;
END;
/

CREATE OR REPLACE TRIGGER trg_addresses_global_ins
    INSTEAD OF INSERT
    ON addresses
    FOR EACH ROW
DECLARE
    v_market VARCHAR2(10);
BEGIN
    SELECT market
    INTO v_market
    FROM customers
    WHERE id = :NEW.customer_id;

    IF v_market = 'RO' THEN
        INSERT INTO addresses_ro_remote (id, customer_id, street, city, region, country, postal_code)
        VALUES (NVL(:NEW.id, SYS_GUID()),
                :NEW.customer_id,
                :NEW.street,
                :NEW.city,
                :NEW.region,
                :NEW.country,
                :NEW.postal_code);

    ELSIF v_market = 'BG' THEN
        INSERT INTO addresses_bg_remote (id, customer_id, street, city, region, country, postal_code)
        VALUES (NVL(:NEW.id, SYS_GUID()),
                :NEW.customer_id,
                :NEW.street,
                :NEW.city,
                :NEW.region,
                :NEW.country,
                :NEW.postal_code);
    END IF;

EXCEPTION
    WHEN NO_DATA_FOUND THEN
        RAISE_APPLICATION_ERROR(-20002, 'Customer not found.');
END;
/

CREATE OR REPLACE TRIGGER trg_accounts_global_ins
    INSTEAD OF INSERT
    ON accounts
    FOR EACH ROW
DECLARE
    v_market VARCHAR2(10);
    v_count  NUMBER;
BEGIN
    SELECT COUNT(*)
    INTO v_count
    FROM accounts
    WHERE username = :NEW.username;

    IF v_count > 0 THEN
        RAISE_APPLICATION_ERROR(-20003, 'Username already exists.');
    END IF;

    SELECT COUNT(*)
    INTO v_count
    FROM accounts
    WHERE email = :NEW.email;

    IF v_count > 0 THEN
        RAISE_APPLICATION_ERROR(-20004, 'Email already exists.');
    END IF;

    IF :NEW.role = 'ADMIN' THEN
        IF :NEW.customer_id IS NOT NULL THEN
            RAISE_APPLICATION_ERROR(-20005, 'ADMIN account must not have customer_id.');
        END IF;

        INSERT INTO accounts_admin (id, username, email, password, role)
        VALUES (NVL(:NEW.id, SYS_GUID()),
                :NEW.username,
                :NEW.email,
                :NEW.password,
                'ADMIN');

    ELSIF :NEW.role = 'USER' THEN
        IF :NEW.customer_id IS NULL THEN
            RAISE_APPLICATION_ERROR(-20006, 'USER account must have customer_id.');
        END IF;

        SELECT market
        INTO v_market
        FROM customers
        WHERE id = :NEW.customer_id;

        IF v_market = 'RO' THEN
            INSERT INTO accounts_ro_remote (id, customer_id, username, email, password, role)
            VALUES (NVL(:NEW.id, SYS_GUID()),
                    :NEW.customer_id,
                    :NEW.username,
                    :NEW.email,
                    :NEW.password,
                    'USER');

        ELSIF v_market = 'BG' THEN
            INSERT INTO accounts_bg_remote (id, customer_id, username, email, password, role)
            VALUES (NVL(:NEW.id, SYS_GUID()),
                    :NEW.customer_id,
                    :NEW.username,
                    :NEW.email,
                    :NEW.password,
                    'USER');
        END IF;

    ELSE
        RAISE_APPLICATION_ERROR(-20007, 'Invalid role.');
    END IF;

EXCEPTION
    WHEN NO_DATA_FOUND THEN
        RAISE_APPLICATION_ERROR(-20008, 'Customer not found.');
END;
/

CREATE OR REPLACE TRIGGER trg_tokens_global_ins
    INSTEAD OF INSERT
    ON tokens
    FOR EACH ROW
DECLARE
    v_role VARCHAR2(50);
BEGIN
    SELECT role
    INTO v_role
    FROM accounts
    WHERE id = :NEW.account_id;

    IF v_role = 'ADMIN' THEN
        INSERT INTO tokens_admin (id, account_id, token, revoked, expired)
        VALUES (NVL(:NEW.id, SYS_GUID()),
                :NEW.account_id,
                :NEW.token,
                NVL(:NEW.revoked, 0),
                NVL(:NEW.expired, 0));

    ELSIF v_role = 'USER' THEN
        DECLARE
            v_customer_id RAW(16);
            v_market      VARCHAR2(10);
        BEGIN
            SELECT customer_id
            INTO v_customer_id
            FROM accounts
            WHERE id = :NEW.account_id;

            SELECT market
            INTO v_market
            FROM customers
            WHERE id = v_customer_id;

            IF v_market = 'RO' THEN
                INSERT INTO tokens_ro_remote (id, account_id, token, revoked, expired)
                VALUES (NVL(:NEW.id, SYS_GUID()),
                        :NEW.account_id,
                        :NEW.token,
                        NVL(:NEW.revoked, 0),
                        NVL(:NEW.expired, 0));

            ELSIF v_market = 'BG' THEN
                INSERT INTO tokens_bg_remote (id, account_id, token, revoked, expired)
                VALUES (NVL(:NEW.id, SYS_GUID()),
                        :NEW.account_id,
                        :NEW.token,
                        NVL(:NEW.revoked, 0),
                        NVL(:NEW.expired, 0));
            END IF;
        END;
    END IF;

EXCEPTION
    WHEN NO_DATA_FOUND THEN
        RAISE_APPLICATION_ERROR(-20009, 'Account not found.');
END;
/

CREATE OR REPLACE TRIGGER trg_orders_global_ins
    INSTEAD OF INSERT
    ON orders
    FOR EACH ROW
DECLARE
    v_market VARCHAR2(10);
BEGIN
    SELECT market
    INTO v_market
    FROM customers
    WHERE id = :NEW.customer_id;

    IF v_market = 'RO' THEN
        INSERT INTO orders_ro_remote (id, customer_id, created_at, status, payment_method)
        VALUES (NVL(:NEW.id, SYS_GUID()),
                :NEW.customer_id,
                NVL(:NEW.created_at, SYSTIMESTAMP),
                NVL(:NEW.status, 'CREATED'),
                :NEW.payment_method);

    ELSIF v_market = 'BG' THEN
        INSERT INTO orders_bg_remote (id, customer_id, created_at, status, payment_method)
        VALUES (NVL(:NEW.id, SYS_GUID()),
                :NEW.customer_id,
                NVL(:NEW.created_at, SYSTIMESTAMP),
                NVL(:NEW.status, 'CREATED'),
                :NEW.payment_method);
    END IF;

EXCEPTION
    WHEN NO_DATA_FOUND THEN
        RAISE_APPLICATION_ERROR(-20010, 'Customer not found.');
END;
/

CREATE OR REPLACE TRIGGER trg_prices_global_ins
    INSTEAD OF INSERT
    ON prices
    FOR EACH ROW
BEGIN
    IF :NEW.market = 'RO' THEN
        INSERT INTO prices_ro_remote (id, product_id, market, value, currency, status, created_at)
        VALUES (NVL(:NEW.id, SYS_GUID()),
                :NEW.product_id,
                'RO',
                :NEW.value,
                :NEW.currency,
                NVL(:NEW.status, 'DEFAULT'),
                NVL(:NEW.created_at, SYSTIMESTAMP));

    ELSIF :NEW.market = 'BG' THEN
        INSERT INTO prices_bg_remote (id, product_id, market, value, currency, status, created_at)
        VALUES (NVL(:NEW.id, SYS_GUID()),
                :NEW.product_id,
                'BG',
                :NEW.value,
                :NEW.currency,
                NVL(:NEW.status, 'DEFAULT'),
                NVL(:NEW.created_at, SYSTIMESTAMP));

    ELSE
        RAISE_APPLICATION_ERROR(-20011, 'Invalid market.');
    END IF;
END;
/

CREATE OR REPLACE TRIGGER trg_inventories_global_ins
    INSTEAD OF INSERT
    ON inventories
    FOR EACH ROW
BEGIN
    IF :NEW.warehouse_code = 'RO_WH' THEN
        INSERT INTO inventories_ro_remote (id, product_id, warehouse_code, stock_available, updated_at)
        VALUES (NVL(:NEW.id, SYS_GUID()),
                :NEW.product_id,
                'RO_WH',
                NVL(:NEW.stock_available, 0),
                NVL(:NEW.updated_at, SYSTIMESTAMP));

    ELSIF :NEW.warehouse_code = 'BG_WH' THEN
        INSERT INTO inventories_bg_remote (id, product_id, warehouse_code, stock_available, updated_at)
        VALUES (NVL(:NEW.id, SYS_GUID()),
                :NEW.product_id,
                'BG_WH',
                NVL(:NEW.stock_available, 0),
                NVL(:NEW.updated_at, SYSTIMESTAMP));

    ELSE
        RAISE_APPLICATION_ERROR(-20012, 'Invalid warehouse_code.');
    END IF;
END;
/

CREATE OR REPLACE TRIGGER trg_order_items_global_ins
    INSTEAD OF INSERT
    ON order_items
    FOR EACH ROW
DECLARE
    v_customer_id RAW(16);
    v_market      VARCHAR2(10);
BEGIN
    SELECT customer_id
    INTO v_customer_id
    FROM orders
    WHERE id = :NEW.order_id;

    SELECT market
    INTO v_market
    FROM customers
    WHERE id = v_customer_id;

    IF v_market = 'RO' THEN
        INSERT INTO order_items_ro_remote (id, order_id, product_id, price_id, quantity)
        VALUES (NVL(:NEW.id, SYS_GUID()),
                :NEW.order_id,
                :NEW.product_id,
                :NEW.price_id,
                :NEW.quantity);

    ELSIF v_market = 'BG' THEN
        INSERT INTO order_items_bg_remote (id, order_id, product_id, price_id, quantity)
        VALUES (NVL(:NEW.id, SYS_GUID()),
                :NEW.order_id,
                :NEW.product_id,
                :NEW.price_id,
                :NEW.quantity);
    END IF;

EXCEPTION
    WHEN NO_DATA_FOUND THEN
        RAISE_APPLICATION_ERROR(-20013, 'Order not found.');
END;
/

CREATE OR REPLACE TRIGGER trg_deliveries_global_ins
    INSTEAD OF INSERT
    ON deliveries
    FOR EACH ROW
DECLARE
    v_customer_id RAW(16);
    v_market      VARCHAR2(10);
BEGIN
    SELECT customer_id
    INTO v_customer_id
    FROM orders
    WHERE id = :NEW.order_id;

    SELECT market
    INTO v_market
    FROM customers
    WHERE id = v_customer_id;

    IF v_market = 'RO' THEN
        INSERT INTO deliveries_ro_remote (id, order_id, address_id, shipped_by, awb)
        VALUES (NVL(:NEW.id, SYS_GUID()),
                :NEW.order_id,
                :NEW.address_id,
                :NEW.shipped_by,
                :NEW.awb);

    ELSIF v_market = 'BG' THEN
        INSERT INTO deliveries_bg_remote (id, order_id, address_id, shipped_by, awb)
        VALUES (NVL(:NEW.id, SYS_GUID()),
                :NEW.order_id,
                :NEW.address_id,
                :NEW.shipped_by,
                :NEW.awb);
    END IF;

EXCEPTION
    WHEN NO_DATA_FOUND THEN
        RAISE_APPLICATION_ERROR(-20014, 'Order not found.');
END;
/

CREATE OR REPLACE TRIGGER trg_products_global_ai
    AFTER INSERT
    ON products
    FOR EACH ROW
BEGIN
    INSERT INTO products_ro_remote (id, brand_id, category_id, sku, product_name, description, status)
    VALUES (:NEW.id,
            :NEW.brand_id,
            :NEW.category_id,
            :NEW.sku,
            :NEW.product_name,
            :NEW.description,
            :NEW.status);

    INSERT INTO products_bg_remote (id, brand_id, category_id, sku, product_name, description, status)
    VALUES (:NEW.id,
            :NEW.brand_id,
            :NEW.category_id,
            :NEW.sku,
            :NEW.product_name,
            :NEW.description,
            :NEW.status);
END;
/
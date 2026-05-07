CREATE OR REPLACE TRIGGER trg_customers_global_upd
    INSTEAD OF UPDATE ON customers
    FOR EACH ROW
BEGIN
    IF :OLD.market <> :NEW.market THEN
        RAISE_APPLICATION_ERROR(-20101, 'Market modification is not allowed.');
    END IF;

    IF :OLD.market = 'RO' THEN
        UPDATE customers_ro_remote
        SET first_name    = :NEW.first_name,
            last_name     = :NEW.last_name,
            date_of_birth = :NEW.date_of_birth,
            phone_number  = :NEW.phone_number
        WHERE id = :OLD.id;

    ELSIF :OLD.market = 'BG' THEN
        UPDATE customers_bg_remote
        SET first_name    = :NEW.first_name,
            last_name     = :NEW.last_name,
            date_of_birth = :NEW.date_of_birth,
            phone_number  = :NEW.phone_number
        WHERE id = :OLD.id;
    END IF;
END;
/

CREATE OR REPLACE TRIGGER trg_addresses_global_upd
    INSTEAD OF UPDATE ON addresses
    FOR EACH ROW
BEGIN
    IF :OLD.customer_id <> :NEW.customer_id THEN
        RAISE_APPLICATION_ERROR(-20102, 'Customer modification is not allowed for address.');
    END IF;

    UPDATE addresses_ro_remote
    SET street      = :NEW.street,
        city        = :NEW.city,
        region      = :NEW.region,
        country     = :NEW.country,
        postal_code = :NEW.postal_code
    WHERE id = :OLD.id;

    IF SQL%ROWCOUNT = 0 THEN
        UPDATE addresses_bg_remote
        SET street      = :NEW.street,
            city        = :NEW.city,
            region      = :NEW.region,
            country     = :NEW.country,
            postal_code = :NEW.postal_code
        WHERE id = :OLD.id;
    END IF;
END;
/

CREATE OR REPLACE TRIGGER trg_accounts_global_upd
    INSTEAD OF UPDATE ON accounts
    FOR EACH ROW
DECLARE
    v_count NUMBER;
BEGIN
    IF :OLD.role <> :NEW.role THEN
        RAISE_APPLICATION_ERROR(-20103, 'Role modification is not allowed.');
    END IF;

    IF NVL(:OLD.customer_id, HEXTORAW('00')) <> NVL(:NEW.customer_id, HEXTORAW('00')) THEN
        RAISE_APPLICATION_ERROR(-20104, 'Customer modification is not allowed for account.');
    END IF;

    SELECT COUNT(*)
    INTO v_count
    FROM accounts
    WHERE username = :NEW.username
      AND id <> :OLD.id;

    IF v_count > 0 THEN
        RAISE_APPLICATION_ERROR(-20105, 'Username already exists.');
    END IF;

    SELECT COUNT(*)
    INTO v_count
    FROM accounts
    WHERE email = :NEW.email
      AND id <> :OLD.id;

    IF v_count > 0 THEN
        RAISE_APPLICATION_ERROR(-20106, 'Email already exists.');
    END IF;

    IF :OLD.role = 'ADMIN' THEN
        UPDATE accounts_admin
        SET username = :NEW.username,
            email    = :NEW.email,
            password = :NEW.password
        WHERE id = :OLD.id;

    ELSE
        UPDATE accounts_ro_remote
        SET username = :NEW.username,
            email    = :NEW.email,
            password = :NEW.password
        WHERE id = :OLD.id;

        IF SQL%ROWCOUNT = 0 THEN
            UPDATE accounts_bg_remote
            SET username = :NEW.username,
                email    = :NEW.email,
                password = :NEW.password
            WHERE id = :OLD.id;
        END IF;
    END IF;
END;
/

CREATE OR REPLACE TRIGGER trg_tokens_global_upd
    INSTEAD OF UPDATE ON tokens
    FOR EACH ROW
BEGIN
    IF :OLD.account_id <> :NEW.account_id THEN
        RAISE_APPLICATION_ERROR(-20107, 'Account modification is not allowed for token.');
    END IF;

    UPDATE tokens_admin
    SET token   = :NEW.token,
        revoked = :NEW.revoked,
        expired = :NEW.expired
    WHERE id = :OLD.id;

    IF SQL%ROWCOUNT = 0 THEN
        UPDATE tokens_ro_remote
        SET token   = :NEW.token,
            revoked = :NEW.revoked,
            expired = :NEW.expired
        WHERE id = :OLD.id;
    END IF;

    IF SQL%ROWCOUNT = 0 THEN
        UPDATE tokens_bg_remote
        SET token   = :NEW.token,
            revoked = :NEW.revoked,
            expired = :NEW.expired
        WHERE id = :OLD.id;
    END IF;
END;
/

CREATE OR REPLACE TRIGGER trg_orders_global_upd
    INSTEAD OF UPDATE ON orders
    FOR EACH ROW
BEGIN
    IF :OLD.customer_id <> :NEW.customer_id THEN
        RAISE_APPLICATION_ERROR(-20108, 'Customer modification is not allowed for order.');
    END IF;

    UPDATE orders_ro_remote
    SET status         = :NEW.status,
        payment_method = :NEW.payment_method
    WHERE id = :OLD.id;

    IF SQL%ROWCOUNT = 0 THEN
        UPDATE orders_bg_remote
        SET status         = :NEW.status,
            payment_method = :NEW.payment_method
        WHERE id = :OLD.id;
    END IF;
END;
/

CREATE OR REPLACE TRIGGER trg_prices_global_upd
    INSTEAD OF UPDATE ON prices
    FOR EACH ROW
BEGIN
    IF :OLD.market <> :NEW.market THEN
        RAISE_APPLICATION_ERROR(-20109, 'Market modification is not allowed for price.');
    END IF;

    IF :OLD.product_id <> :NEW.product_id THEN
        RAISE_APPLICATION_ERROR(-20110, 'Product modification is not allowed for price.');
    END IF;

    IF :OLD.market = 'RO' THEN
        UPDATE prices_ro_remote
        SET value    = :NEW.value,
            currency = :NEW.currency,
            status   = :NEW.status
        WHERE id = :OLD.id;

    ELSIF :OLD.market = 'BG' THEN
        UPDATE prices_bg_remote
        SET value    = :NEW.value,
            currency = :NEW.currency,
            status   = :NEW.status
        WHERE id = :OLD.id;
    END IF;
END;
/

CREATE OR REPLACE TRIGGER trg_inventories_global_upd
    INSTEAD OF UPDATE ON inventories
    FOR EACH ROW
BEGIN
    IF :OLD.product_id <> :NEW.product_id THEN
        RAISE_APPLICATION_ERROR(-20111, 'Product modification is not allowed for inventory.');
    END IF;

    IF :OLD.warehouse_code <> :NEW.warehouse_code THEN
        RAISE_APPLICATION_ERROR(-20112, 'Warehouse modification is not allowed for inventory.');
    END IF;

    IF :OLD.warehouse_code = 'RO_WH' THEN
        UPDATE inventories_ro_remote
        SET stock_available = :NEW.stock_available,
            updated_at      = NVL(:NEW.updated_at, SYSTIMESTAMP)
        WHERE id = :OLD.id;

    ELSIF :OLD.warehouse_code = 'BG_WH' THEN
        UPDATE inventories_bg_remote
        SET stock_available = :NEW.stock_available,
            updated_at      = NVL(:NEW.updated_at, SYSTIMESTAMP)
        WHERE id = :OLD.id;
    END IF;
END;
/

CREATE OR REPLACE TRIGGER trg_order_items_global_upd
    INSTEAD OF UPDATE ON order_items
    FOR EACH ROW
BEGIN
    IF :OLD.order_id <> :NEW.order_id THEN
        RAISE_APPLICATION_ERROR(-20113, 'Order modification is not allowed for order item.');
    END IF;

    UPDATE order_items_ro_remote
    SET product_id = :NEW.product_id,
        price_id   = :NEW.price_id,
        quantity   = :NEW.quantity
    WHERE id = :OLD.id;

    IF SQL%ROWCOUNT = 0 THEN
        UPDATE order_items_bg_remote
        SET product_id = :NEW.product_id,
            price_id   = :NEW.price_id,
            quantity   = :NEW.quantity
        WHERE id = :OLD.id;
    END IF;
END;
/

CREATE OR REPLACE TRIGGER trg_deliveries_global_upd
    INSTEAD OF UPDATE ON deliveries
    FOR EACH ROW
BEGIN
    IF :OLD.order_id <> :NEW.order_id THEN
        RAISE_APPLICATION_ERROR(-20114, 'Order modification is not allowed for delivery.');
    END IF;

    UPDATE deliveries_ro_remote
    SET address_id = :NEW.address_id,
        shipped_by = :NEW.shipped_by,
        awb        = :NEW.awb
    WHERE id = :OLD.id;

    IF SQL%ROWCOUNT = 0 THEN
        UPDATE deliveries_bg_remote
        SET address_id = :NEW.address_id,
            shipped_by = :NEW.shipped_by,
            awb        = :NEW.awb
        WHERE id = :OLD.id;
    END IF;
END;
/

CREATE OR REPLACE TRIGGER trg_products_global_au
    AFTER UPDATE ON products
    FOR EACH ROW
BEGIN
    UPDATE products_ro_remote
    SET brand_id      = :NEW.brand_id,
        category_id   = :NEW.category_id,
        sku           = :NEW.sku,
        product_name  = :NEW.product_name,
        description   = :NEW.description,
        status        = :NEW.status
    WHERE id = :OLD.id;

    UPDATE products_bg_remote
    SET brand_id      = :NEW.brand_id,
        category_id   = :NEW.category_id,
        sku           = :NEW.sku,
        product_name  = :NEW.product_name,
        description   = :NEW.description,
        status        = :NEW.status
    WHERE id = :OLD.id;
END;
/
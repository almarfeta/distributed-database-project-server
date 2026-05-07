CREATE OR REPLACE TRIGGER trg_customers_global_del
    INSTEAD OF DELETE ON customers
    FOR EACH ROW
BEGIN
    IF :OLD.market = 'RO' THEN
        DELETE FROM customers_ro_remote
        WHERE id = :OLD.id;

    ELSIF :OLD.market = 'BG' THEN
        DELETE FROM customers_bg_remote
        WHERE id = :OLD.id;
    END IF;
END;
/

CREATE OR REPLACE TRIGGER trg_addresses_global_del
    INSTEAD OF DELETE ON addresses
    FOR EACH ROW
BEGIN
    DELETE FROM addresses_ro_remote
    WHERE id = :OLD.id;

    IF SQL%ROWCOUNT = 0 THEN
        DELETE FROM addresses_bg_remote
        WHERE id = :OLD.id;
    END IF;
END;
/

CREATE OR REPLACE TRIGGER trg_accounts_global_del
    INSTEAD OF DELETE ON accounts
    FOR EACH ROW
BEGIN
    IF :OLD.role = 'ADMIN' THEN
        DELETE FROM accounts_admin
        WHERE id = :OLD.id;

    ELSE
        DELETE FROM accounts_ro_remote
        WHERE id = :OLD.id;

        IF SQL%ROWCOUNT = 0 THEN
            DELETE FROM accounts_bg_remote
            WHERE id = :OLD.id;
        END IF;
    END IF;
END;
/

CREATE OR REPLACE TRIGGER trg_tokens_global_del
    INSTEAD OF DELETE ON tokens
    FOR EACH ROW
BEGIN
    DELETE FROM tokens_admin
    WHERE id = :OLD.id;

    IF SQL%ROWCOUNT = 0 THEN
        DELETE FROM tokens_ro_remote
        WHERE id = :OLD.id;
    END IF;

    IF SQL%ROWCOUNT = 0 THEN
        DELETE FROM tokens_bg_remote
        WHERE id = :OLD.id;
    END IF;
END;
/

CREATE OR REPLACE TRIGGER trg_orders_global_del
    INSTEAD OF DELETE ON orders
    FOR EACH ROW
BEGIN
    DELETE FROM orders_ro_remote
    WHERE id = :OLD.id;

    IF SQL%ROWCOUNT = 0 THEN
        DELETE FROM orders_bg_remote
        WHERE id = :OLD.id;
    END IF;
END;
/

CREATE OR REPLACE TRIGGER trg_prices_global_del
    INSTEAD OF DELETE ON prices
    FOR EACH ROW
BEGIN
    IF :OLD.market = 'RO' THEN
        DELETE FROM prices_ro_remote
        WHERE id = :OLD.id;

    ELSIF :OLD.market = 'BG' THEN
        DELETE FROM prices_bg_remote
        WHERE id = :OLD.id;
    END IF;
END;
/

CREATE OR REPLACE TRIGGER trg_inventories_global_del
    INSTEAD OF DELETE ON inventories
    FOR EACH ROW
BEGIN
    IF :OLD.warehouse_code = 'RO_WH' THEN
        DELETE FROM inventories_ro_remote
        WHERE id = :OLD.id;

    ELSIF :OLD.warehouse_code = 'BG_WH' THEN
        DELETE FROM inventories_bg_remote
        WHERE id = :OLD.id;
    END IF;
END;
/

CREATE OR REPLACE TRIGGER trg_order_items_global_del
    INSTEAD OF DELETE ON order_items
    FOR EACH ROW
BEGIN
    DELETE FROM order_items_ro_remote
    WHERE id = :OLD.id;

    IF SQL%ROWCOUNT = 0 THEN
        DELETE FROM order_items_bg_remote
        WHERE id = :OLD.id;
    END IF;
END;
/

CREATE OR REPLACE TRIGGER trg_deliveries_global_del
    INSTEAD OF DELETE ON deliveries
    FOR EACH ROW
BEGIN
    DELETE FROM deliveries_ro_remote
    WHERE id = :OLD.id;

    IF SQL%ROWCOUNT = 0 THEN
        DELETE FROM deliveries_bg_remote
        WHERE id = :OLD.id;
    END IF;
END;
/

CREATE OR REPLACE TRIGGER trg_products_global_bd
    BEFORE DELETE ON products
    FOR EACH ROW
BEGIN
    DELETE FROM products_ro_remote
    WHERE id = :OLD.id;

    DELETE FROM products_bg_remote
    WHERE id = :OLD.id;
END;
/
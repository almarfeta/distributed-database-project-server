CREATE DATABASE LINK ro_db_link
    CONNECT TO app_ro_user IDENTIFIED BY Parola1234
    USING '(DESCRIPTION=
        (ADDRESS=(PROTOCOL=TCP)(HOST=localhost)(PORT=1521))
        (CONNECT_DATA=(SERVICE_NAME=ORCLPDB1)))';

CREATE DATABASE LINK bg_db_link
    CONNECT TO app_bg_user IDENTIFIED BY Parola1234
    USING '(DESCRIPTION=
        (ADDRESS=(PROTOCOL=TCP)(HOST=localhost)(PORT=1521))
        (CONNECT_DATA=(SERVICE_NAME=ORCLPDB1)))';

CREATE SYNONYM customers_ro_remote
    FOR customers_ro@ro_db_link;

CREATE SYNONYM customers_bg_remote
    FOR customers_bg@bg_db_link;

CREATE SYNONYM addresses_ro_remote
    FOR addresses_ro@ro_db_link;

CREATE SYNONYM addresses_bg_remote
    FOR addresses_bg@bg_db_link;

CREATE SYNONYM accounts_ro_remote
    FOR accounts_ro@ro_db_link;

CREATE SYNONYM accounts_bg_remote
    FOR accounts_bg@bg_db_link;

CREATE SYNONYM tokens_ro_remote
    FOR tokens_ro@ro_db_link;

CREATE SYNONYM tokens_bg_remote
    FOR tokens_bg@bg_db_link;

CREATE SYNONYM orders_ro_remote
    FOR orders_ro@ro_db_link;

CREATE SYNONYM orders_bg_remote
    FOR orders_bg@bg_db_link;

CREATE SYNONYM order_items_ro_remote
    FOR order_items_ro@ro_db_link;

CREATE SYNONYM order_items_bg_remote
    FOR order_items_bg@bg_db_link;

CREATE SYNONYM deliveries_ro_remote
    FOR deliveries_ro@ro_db_link;

CREATE SYNONYM deliveries_bg_remote
    FOR deliveries_bg@bg_db_link;

CREATE SYNONYM prices_ro_remote
    FOR prices_ro@ro_db_link;

CREATE SYNONYM prices_bg_remote
    FOR prices_bg@bg_db_link;

CREATE SYNONYM inventories_ro_remote
    FOR inventories_ro@ro_db_link;

CREATE SYNONYM inventories_bg_remote
    FOR inventories_bg@bg_db_link;

CREATE SYNONYM products_ro_remote
    FOR products@ro_db_link;

CREATE SYNONYM products_bg_remote
    FOR products@bg_db_link;
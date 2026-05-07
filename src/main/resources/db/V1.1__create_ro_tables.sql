CREATE TABLE customers_ro
(
    id            RAW(16)                  DEFAULT ON NULL SYS_GUID() PRIMARY KEY,
    first_name    VARCHAR2(100)                                         NOT NULL,
    last_name     VARCHAR2(100)                                         NOT NULL,
    market        VARCHAR2(10)             DEFAULT ON NULL 'RO'         NOT NULL,
    date_of_birth DATE,
    phone_number  VARCHAR2(50),
    created_at    TIMESTAMP WITH TIME ZONE DEFAULT ON NULL SYSTIMESTAMP NOT NULL,

    CONSTRAINT chk_customers_ro_market
        CHECK (market = 'RO')
);

CREATE TABLE addresses_ro
(
    id          RAW(16) DEFAULT ON NULL SYS_GUID() PRIMARY KEY,
    customer_id RAW(16)       NOT NULL,
    street      VARCHAR2(255) NOT NULL,
    city        VARCHAR2(100) NOT NULL,
    region      VARCHAR2(100) NOT NULL,
    country     VARCHAR2(100) NOT NULL,
    postal_code VARCHAR2(20)  NOT NULL,

    CONSTRAINT fk_addresses_ro_customer
        FOREIGN KEY (customer_id)
            REFERENCES customers_ro (id)
);

CREATE TABLE accounts_ro
(
    id          RAW(16)      DEFAULT ON NULL SYS_GUID() PRIMARY KEY,
    customer_id RAW(16)                             NOT NULL UNIQUE,
    username    VARCHAR2(100)                       NOT NULL,
    email       VARCHAR2(255)                       NOT NULL,
    password    VARCHAR2(255)                       NOT NULL,
    role        VARCHAR2(50) DEFAULT ON NULL 'USER' NOT NULL,

    CONSTRAINT chk_accounts_ro_role
        CHECK (role = 'USER'),

    CONSTRAINT fk_accounts_ro_customer
        FOREIGN KEY (customer_id)
            REFERENCES customers_ro (id)
);

CREATE TABLE tokens_ro
(
    id         RAW(16)   DEFAULT ON NULL SYS_GUID() PRIMARY KEY,
    account_id RAW(16)                     NOT NULL,
    token      VARCHAR2(512)               NOT NULL,
    revoked    NUMBER(1) DEFAULT ON NULL 0 NOT NULL,
    expired    NUMBER(1) DEFAULT ON NULL 0 NOT NULL,

    CONSTRAINT chk_tokens_ro_revoked
        CHECK (revoked IN (0, 1)),

    CONSTRAINT chk_tokens_ro_expired
        CHECK (expired IN (0, 1)),

    CONSTRAINT fk_tokens_ro_account
        FOREIGN KEY (account_id)
            REFERENCES accounts_ro (id)
);

CREATE TABLE products
(
    id           RAW(16) PRIMARY KEY,
    brand_id     RAW(16)       NOT NULL,
    category_id  RAW(16)       NOT NULL,
    sku          VARCHAR2(100) NOT NULL UNIQUE,
    product_name VARCHAR2(255) NOT NULL,
    description  CLOB,
    status       VARCHAR2(50)  NOT NULL,

    CONSTRAINT chk_products_ro_status
        CHECK (status IN ('ACTIVE', 'INACTIVE'))
);

CREATE TABLE prices_ro
(
    id         RAW(16)                  DEFAULT ON NULL SYS_GUID() PRIMARY KEY,
    product_id RAW(16)                                               NOT NULL,
    market     VARCHAR2(10)             DEFAULT ON NULL 'RO'         NOT NULL,
    value      NUMBER(10, 2)                                         NOT NULL,
    currency   VARCHAR2(10)                                          NOT NULL,
    status     VARCHAR2(50)             DEFAULT ON NULL 'DEFAULT'    NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT ON NULL SYSTIMESTAMP NOT NULL,

    CONSTRAINT chk_prices_ro_market
        CHECK (market = 'RO'),

    CONSTRAINT chk_prices_ro_currency
        CHECK (currency IN ('RON', 'EUR', 'BGN')),

    CONSTRAINT chk_prices_ro_status
        CHECK (status IN ('DEFAULT', 'ACTIVE', 'INACTIVE')),

    CONSTRAINT chk_prices_ro_value
        CHECK (value >= 0),

    CONSTRAINT fk_prices_ro_product
        FOREIGN KEY (product_id)
            REFERENCES products (id)
);

CREATE TABLE inventories_ro
(
    id              RAW(16)                  DEFAULT ON NULL SYS_GUID() PRIMARY KEY,
    product_id      RAW(16)                                               NOT NULL,
    warehouse_code  VARCHAR2(20)             DEFAULT ON NULL 'RO_WH'      NOT NULL,
    stock_available NUMBER(10)               DEFAULT ON NULL 0            NOT NULL,
    updated_at      TIMESTAMP WITH TIME ZONE DEFAULT ON NULL SYSTIMESTAMP NOT NULL,

    CONSTRAINT uq_inventories_ro_product_warehouse
        UNIQUE (product_id, warehouse_code),

    CONSTRAINT chk_inventories_ro_warehouse
        CHECK (warehouse_code = 'RO_WH'),

    CONSTRAINT chk_inventories_ro_stock
        CHECK (stock_available >= 0),

    CONSTRAINT fk_inventories_ro_product
        FOREIGN KEY (product_id)
            REFERENCES products (id)
);

CREATE TABLE orders_ro
(
    id             RAW(16)                  DEFAULT ON NULL SYS_GUID() PRIMARY KEY,
    customer_id    RAW(16)                                               NOT NULL,
    created_at     TIMESTAMP WITH TIME ZONE DEFAULT ON NULL SYSTIMESTAMP NOT NULL,
    status         VARCHAR2(50)             DEFAULT ON NULL 'CREATED'    NOT NULL,
    payment_method VARCHAR2(50)                                          NOT NULL,

    CONSTRAINT chk_orders_ro_status
        CHECK (status IN ('CREATED', 'ACCEPTED', 'DECLINED', 'CANCELLED', 'DELIVERING', 'DELIVERED')),

    CONSTRAINT chk_orders_ro_payment_method
        CHECK (payment_method IN ('CARD', 'CASH', 'TRANSFER')),

    CONSTRAINT fk_orders_ro_customer
        FOREIGN KEY (customer_id)
            REFERENCES customers_ro (id)
);

CREATE TABLE order_items_ro
(
    id         RAW(16) DEFAULT ON NULL SYS_GUID() PRIMARY KEY,
    order_id   RAW(16)    NOT NULL,
    product_id RAW(16)    NOT NULL,
    price_id   RAW(16)    NOT NULL,
    quantity   NUMBER(10) NOT NULL,

    CONSTRAINT chk_order_items_ro_quantity
        CHECK (quantity > 0),

    CONSTRAINT fk_order_items_ro_order
        FOREIGN KEY (order_id)
            REFERENCES orders_ro (id),

    CONSTRAINT fk_order_items_ro_product
        FOREIGN KEY (product_id)
            REFERENCES products (id),

    CONSTRAINT fk_order_items_ro_price
        FOREIGN KEY (price_id)
            REFERENCES prices_ro (id)
);

CREATE TABLE deliveries_ro
(
    id         RAW(16) DEFAULT ON NULL SYS_GUID() PRIMARY KEY,
    order_id   RAW(16) NOT NULL UNIQUE,
    address_id RAW(16) NOT NULL,
    shipped_by VARCHAR2(100),
    awb        VARCHAR2(100),

    CONSTRAINT fk_deliveries_ro_order
        FOREIGN KEY (order_id)
            REFERENCES orders_ro (id),

    CONSTRAINT fk_deliveries_ro_address
        FOREIGN KEY (address_id)
            REFERENCES addresses_ro (id)
);
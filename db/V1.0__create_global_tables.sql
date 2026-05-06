CREATE TABLE accounts_admin
(
    id       RAW(16)      DEFAULT ON NULL SYS_GUID() PRIMARY KEY,
    username VARCHAR2(100)                        NOT NULL UNIQUE,
    email    VARCHAR2(255)                        NOT NULL UNIQUE,
    password VARCHAR2(255)                        NOT NULL,
    role     VARCHAR2(50) DEFAULT ON NULL 'ADMIN' NOT NULL,

    CONSTRAINT chk_accounts_admin_role
        CHECK (role = 'ADMIN')
);

CREATE TABLE tokens_admin
(
    id         RAW(16)   DEFAULT ON NULL SYS_GUID() PRIMARY KEY,
    account_id RAW(16)                     NOT NULL,
    token      VARCHAR2(512)               NOT NULL,
    revoked    NUMBER(1) DEFAULT ON NULL 0 NOT NULL,
    expired    NUMBER(1) DEFAULT ON NULL 0 NOT NULL,

    CONSTRAINT chk_tokens_admin_revoked
        CHECK (revoked IN (0, 1)),

    CONSTRAINT chk_tokens_admin_expired
        CHECK (expired IN (0, 1)),

    CONSTRAINT fk_tokens_admin_account
        FOREIGN KEY (account_id)
            REFERENCES accounts_admin (id)
);

CREATE TABLE brands
(
    id         RAW(16) DEFAULT ON NULL SYS_GUID() PRIMARY KEY,
    brand_name VARCHAR2(255) NOT NULL UNIQUE
);

CREATE TABLE categories
(
    id                 RAW(16) DEFAULT ON NULL SYS_GUID() PRIMARY KEY,
    parent_category_id RAW(16),
    category_name      VARCHAR2(255) NOT NULL,

    CONSTRAINT uq_categories_parent_name
        UNIQUE (parent_category_id, category_name),

    CONSTRAINT fk_categories_parent
        FOREIGN KEY (parent_category_id)
            REFERENCES categories (id)
);

CREATE TABLE products
(
    id           RAW(16)      DEFAULT ON NULL SYS_GUID() PRIMARY KEY,
    brand_id     RAW(16)                                 NOT NULL,
    category_id  RAW(16)                                 NOT NULL,
    sku          VARCHAR2(100)                           NOT NULL UNIQUE,
    product_name VARCHAR2(255)                           NOT NULL,
    description  CLOB,
    status       VARCHAR2(50) DEFAULT ON NULL 'INACTIVE' NOT NULL,

    CONSTRAINT chk_products_status
        CHECK (status IN ('ACTIVE', 'INACTIVE')),

    CONSTRAINT fk_products_brand
        FOREIGN KEY (brand_id)
            REFERENCES brands (id),

    CONSTRAINT fk_products_category
        FOREIGN KEY (category_id)
            REFERENCES categories (id)
);
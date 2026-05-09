CREATE OR REPLACE PROCEDURE refresh_brands_replicas AS
BEGIN
    DBMS_MVIEW.REFRESH@ro_db_link('BRANDS');
    DBMS_MVIEW.REFRESH@bg_db_link('BRANDS');
END;
/

CREATE OR REPLACE PROCEDURE refresh_categories_replicas AS
BEGIN
    DBMS_MVIEW.REFRESH@ro_db_link('CATEGORIES');
    DBMS_MVIEW.REFRESH@bg_db_link('CATEGORIES');
END;
/
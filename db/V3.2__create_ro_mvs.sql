CREATE MATERIALIZED VIEW brands
            BUILD IMMEDIATE
    REFRESH COMPLETE ON DEMAND
AS
SELECT id, brand_name
FROM brands_global_remote;

CREATE MATERIALIZED VIEW categories
            BUILD IMMEDIATE
    REFRESH COMPLETE ON DEMAND
AS
SELECT id, parent_category_id, category_name
FROM categories_global_remote;

BEGIN
    DBMS_MVIEW.REFRESH('BRANDS');
    DBMS_MVIEW.REFRESH('CATEGORIES');
END;
/
CREATE DATABASE LINK global_db_link
    CONNECT TO app_global_user IDENTIFIED BY Parola1234
    USING '(DESCRIPTION=
        (ADDRESS=(PROTOCOL=TCP)(HOST=localhost)(PORT=1521))
        (CONNECT_DATA=(SERVICE_NAME=ORCLPDB1)))';

CREATE SYNONYM brands_global_remote
    FOR brands@global_db_link;

CREATE SYNONYM categories_global_remote
    FOR categories@global_db_link;
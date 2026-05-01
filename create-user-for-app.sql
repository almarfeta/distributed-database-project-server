-- Run as SYSTEM

-- Change the container to the root one (Default)
ALTER SESSION SET CONTAINER = CDB$ROOT;

-- See if you are on the root container or not
SELECT sys_context('USERENV', 'CON_NAME') AS current_container FROM dual;

-- See all the PDBs
SELECT name FROM v$pdbs;

-- Connect to a PDB
ALTER SESSION SET CONTAINER = ORCLPDB1;

-- Create local and global users on that PDB and give them privileges to create stuff
CREATE USER app_ro_user IDENTIFIED BY Parola1234;
ALTER USER app_ro_user QUOTA UNLIMITED ON USERS;
GRANT CREATE SESSION TO app_ro_user;
GRANT CREATE TABLE TO app_ro_user;
GRANT CREATE VIEW TO app_ro_user;
GRANT CREATE SEQUENCE TO app_ro_user;
GRANT CREATE TRIGGER TO app_ro_user;
GRANT CREATE PROCEDURE TO app_ro_user;
GRANT CREATE DATABASE LINK TO app_ro_user;
GRANT CREATE SYNONYM TO app_ro_user;
GRANT CREATE MATERIALIZED VIEW TO app_ro_user;

CREATE USER app_bg_user IDENTIFIED BY Parola1234;
ALTER USER app_bg_user QUOTA UNLIMITED ON USERS;
GRANT CREATE SESSION TO app_bg_user;
GRANT CREATE TABLE TO app_bg_user;
GRANT CREATE VIEW TO app_bg_user;
GRANT CREATE SEQUENCE TO app_bg_user;
GRANT CREATE TRIGGER TO app_bg_user;
GRANT CREATE PROCEDURE TO app_bg_user;
GRANT CREATE DATABASE LINK TO app_bg_user;
GRANT CREATE SYNONYM TO app_bg_user;
GRANT CREATE MATERIALIZED VIEW TO app_bg_user;

CREATE USER app_global_user IDENTIFIED BY Parola1234;
ALTER USER app_global_user QUOTA UNLIMITED ON USERS;
GRANT CREATE SESSION TO app_global_user;
GRANT CREATE TABLE TO app_global_user;
GRANT CREATE VIEW TO app_global_user;
GRANT CREATE SEQUENCE TO app_global_user;
GRANT CREATE TRIGGER TO app_global_user;
GRANT CREATE PROCEDURE TO app_global_user;
GRANT CREATE DATABASE LINK TO app_global_user;
GRANT CREATE SYNONYM TO app_global_user;
GRANT CREATE MATERIALIZED VIEW TO app_global_user;

-- Verify
SELECT username, common, profile
FROM dba_users
WHERE username = 'APP_RO_USER' OR USERNAME = 'APP_BG_USER' OR USERNAME = 'APP_GLOBAL_USER';

-- Utils if password expires/account is locked
ALTER USER app_ro_user IDENTIFIED BY Parola1234;
ALTER USER app_bg_user IDENTIFIED BY Parola1234;
ALTER USER app_global_user IDENTIFIED BY Parola1234;
ALTER USER app_ro_user ACCOUNT UNLOCK;
ALTER USER app_bg_user ACCOUNT UNLOCK;
ALTER USER app_global_user ACCOUNT UNLOCK;
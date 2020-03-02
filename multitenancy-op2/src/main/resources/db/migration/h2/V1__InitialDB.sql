create table if not exists PUBLIC.DATASOURCECONFIG (
	ID BIGINT primary key,
	DRIVERCLASSNAME varchar(255),
	URL varchar(255),
	SCHEMANAME varchar(10),
	TENANTID varchar(30),
	USERNAME varchar(30),
	PASSWORD varchar(10)
);

create schema if not exists SCHEMA1;
create schema if not exists SCHEMA2;
create table SCHEMA1.catalogtable(id bigint, tenantid varchar(10), catalogname varchar(30), supplierid varchar(30), source varchar(10));
create table SCHEMA2.catalogtable(id bigint, tenantid varchar(10), catalogname varchar(30), supplierid varchar(30), source varchar(10));

CREATE SEQUENCE SCHEMA1.hibernate_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
CREATE SEQUENCE SCHEMA2.hibernate_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


/*
Different TENANTS are using same DB but different SCHEMA.
*/

INSERT INTO DATASOURCECONFIG VALUES (1, 'org.h2.Driver', 'jdbc:h2:mem:dbA', 'SCHEMA1','TENANT1', 'sa', '');
INSERT INTO DATASOURCECONFIG VALUES (2, 'org.h2.Driver', 'jdbc:h2:mem:dbA', 'SCHEMA2', 'TENANT2', 'sa', '');


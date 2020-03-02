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

create table SCHEMA1.catalogtable(id bigint, tenantid varchar(10), catalogname varchar(30), supplierid varchar(30), source varchar(10));


CREATE SEQUENCE SCHEMA1.hibernate_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


/*
TENANT1 is uses dedicated database dbA and schema SCHEMA1 as it has the higher data load.
*/
INSERT INTO DATASOURCECONFIG VALUES (1, 'org.h2.Driver', 'jdbc:h2:mem:dbA', 'SCHEMA1','TENANT1', 'sa', '');

/*
TENANT2 and TENANT3 are using shared database db2
*/
INSERT INTO DATASOURCECONFIG VALUES (2, 'org.h2.Driver', 'jdbc:h2:mem:dbB', 'SHARED', 'TENANT2', 'sa', '');
INSERT INTO DATASOURCECONFIG VALUES (3, 'org.h2.Driver', 'jdbc:h2:mem:dbB', 'SHARED', 'TENANT3', 'sa', '');


/*

Create the following manaully for the database DATABASEc

create schema if not exists SHARED;
create table SHARED.catalogtable(id bigint, tenantid varchar(10), catalogname varchar(30), supplierid varchar(30), source varchar(10));
CREATE SEQUENCE SHARED.hibernate_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
*/
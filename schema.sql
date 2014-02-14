/* username: postgres, password: fujiko
 * databases: postchi_testing, mainbase 
*/

\connect postchi_testing;

drop schema public cascade;  

create schema public;

create table currentrevision(
id varchar(20),
revision serial primary key,
from_revision int,
comment varchar(200)
);

create table tag(
revision int,
uid varchar(50),
entity_type varchar(10),
entity_id varchar(30),
tag varchar(40)
);

create table lastRevision(
uid varchar(50),
entity_id varchar(50),
workingrevision int,
revision int,
last_subRevision int,
entity_type varchar(10)
);

create table subrevision_table(
uid varchar(50),
entity_id varchar(50),
revision int,
last_subRevision int,
entity_type varchar(10),
sequence_id int
);

create table images (
field_name varchar(50),
path varchar(50)
);

\connect mainbase;

create table projects(
project_name varchar(50),
project_description varchar(50)
);

create table users(
username varchar(50),
pwd varchar(50)
);

create table user_project(
project_name varchar(50),
user_name varchar(50)
);


create unique index idx_test_user_username on users using btree (username);

create extension pgcrypto ;




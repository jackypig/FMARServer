# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table restaurant (
  id                        bigint auto_increment not null,
  name                      varchar(255) not null,
  telephone                 varchar(255) not null,
  location                  varchar(255) not null,
  introduction              varchar(255) not null,
  created_timestamp         datetime,
  constraint pk_restaurant primary key (id))
;




# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table restaurant;

SET FOREIGN_KEY_CHECKS=1;


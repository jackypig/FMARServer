# Database structure update log

#12/04/2014 - local database
alter table restaurant add approved boolean;
update restaurant set approved = true;
create table special_offer (
  id                        bigint(20) auto_increment not null,
  active                    boolean not null,
  description               varchar(255) not null,
  restaurant_id             bigint(20) not null,
  start_date                datetime,
  end_date                  datetime,
  primary key (id),
  foreign key (restaurant_id) references restaurant(id)
  );



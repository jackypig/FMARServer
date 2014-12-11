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

#12/05/2014 - local database
alter table restaurant add special_offer varchar(255);

#12/10/2014 - local database
create table image (
  id                    BIGINT(20) auto_increment not null,
  blob_key              VARCHAR(255),
  filename              VARCHAR(255),
  file_length           BIGINT(20),
  status                VARCHAR(7) not null,
  mime_type             VARCHAR(20) not null,
  created_timestamp     DATETIME not null,
  uploaded_by_id        BIGINT(20),
  constraint pk_image primary key (id))
;
alter table image add constraint fk_image_uploaded_by_id foreign key (uploaded_by_id) references user (id) on delete restrict on update restrict;
alter table restaurant add image_id bigint(20);
alter table restaurant add constraint fk_restaurant_image_id foreign key (image_id) references image (id) on delete restrict on update restrict;

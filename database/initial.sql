# Updates to the database structure

# 5/29/2013  - Added initialization time to utterance
# 6/7/2013   - Deployed to PROD
alter table utterance add initialization_time double;

# 5/31/2013  - Added ad_id to ad_request
# 6/7/2013   - Deployed to PROD
alter table ad_request add ad_id bigint;

# 6/7/2013   - Script to update blob keys for ad preview
# 6/7/2013   - Deployed to PROD
update audio set unprocessed_blob_key = concat(substr(blob_key, 1, length(blob_key)-3), 'mp3');

# 6/18/2013 - Script to add email to app_user
# ???       - DEV
# 7/2/2013  - PROD
alter table app_user add email varchar(255);

# 6/18/2013 - Script to add index to xapp_session
# ???       - DEV how do we ensure this gets run every time?
# 7/2/2013  - PROD
ALTER TABLE xapp_session ADD INDEX(session_key);

# 6/24/2013 - Script to add county fips code
# 6/24/2013 - DEV
# 7/2/2013  - PROD
ALTER TABLE geo_unit add county_fips_code varchar(3);
alter table geo_unit modify type varchar(10) not null;
ALTER TABLE geo_unit drop index uq_geo_unit_1;
alter table geo_unit add constraint UNIQUE (country, state, county, zip_code);

# 6/25/2013 - Index for zip_code
# 6/27/2013 - DEV
# 7/2/2013  - PROD
alter table geo_unit add index(type, zip_code);

# 7/2/2013 - Updates to utterance table
# 7/2/2013 - DEV
# 7/2/2013 - PROD
alter table utterance add os varchar(255);
alter table utterance add platform varchar(255);
alter table utterance add model varchar(255);

# 7/8/2013  - Updates to media tables
# 7/8/2013  - DEV
# 7/12/2013 - PROD
alter table audio add uploaded_by_id bigint not null;
alter table image add uploaded_by_id bigint not null;
update audio set uploaded_by_id=1;
update image set uploaded_by_id=1;
alter table audio add constraint fk_audio_uploaded_by_id foreign key (uploaded_by_id) references admin (id) on delete restrict on update restrict;
alter table image add constraint fk_image_uploaded_by_id foreign key (uploaded_by_id) references admin (id) on delete restrict on update restrict;

#7/9/2013   - Updates to email body columns for ad_action
#7/9/2013   - DEV
#7/12/2013  - PROD
alter table ad_action modify email_body_plain text;
alter table ad_action modify email_body_html text;

#7/23/2013  - Added campaign quota table
#8/16/2013  - Added demo to ad_request table
#8/28/2013  - PROD
#9/12/2013  - LOAD
create table campaign_quota (
  id                        bigint auto_increment not null,
  budget_amount             decimal(20,2) not null,
  campaign_id               bigint not null,
  current_amount            decimal(20,2) not null,
  quota_year                integer not null,
  quota_month               integer not null,
  quota_day_in_month        integer,
  quota_timestamp           datetime not null,
  quota_type                varchar(7) not null,
  constraint ck_campaign_quota_quota_type check (quota_type in ('DAILY','MONTHLY')),
  constraint uq_campaign_quota_1 unique (campaign_id,quota_type,quota_year,quota_month,quota_day_in_month),
  constraint pk_campaign_quota primary key (id))
;
alter table campaign_quota add constraint fk_campaign_quota_campaign_25 foreign key (campaign_id) references campaign (id) on delete restrict on update restrict;
create index ix_campaign_quota_campaign_25 on campaign_quota (campaign_id);

#8/16/2013  - Added demo to ad_request table
alter table ad_request add demo boolean;
update ad_request set demo = false;

#8/28/2013  - Added updated_timestamp to ad table
#8/28/2013  - DEV
#9/04/2013  - PROD
#9/04/2013  - LOAD
alter table ad add updated_timestamp datetime null;

#10/2/2013  - Changes for Xapp for Publishers
alter table campaign add account_id bigint not null;
alter table campaign drop foreign key fk_campaign_advertiser_23;
alter table campaign add constraint fk_campaign_account_24 foreign key (account_id) references account (id) on delete restrict on update restrict;
create index ix_campaign_account_24 on campaign (advertiser_id);

#10/28/2013  - Add requestId in AdImpression for transferring AdResponse Record from Dynamodb to Mysql
alter table ad_impression add ad_request_uuid varchar(255) null;

#11/21/2013  - Add key in ad_response table
#11/21/2013  - LING
#11/22/2013  - DEV
#01/07/2013  - PROD2
alter table ad_response add journal_key varchar(255) null;
create unique index uq_ad_response_1 on ad_response (journal_key);

#11/25/2013 - Lots of stuff for cues and custom actions
#11/25/2013 - DEV
#12/02/2013 - STAGING
alter table publisher add introductory_cue_id bigint;
alter table publisher add listening_start_cue_id bigint;
alter table publisher add listening_end_cue_id bigint;
alter table publisher add recognition_success_cue_id bigint;
alter table publisher add recognition_failure_cue_id bigint;

alter table publisher add constraint fk_publisher_introductoryCue_36 foreign key (introductory_cue_id) references audio (id) on delete restrict on update restrict;
create index ix_publisher_introductoryCue_36 on publisher (introductory_cue_id);
alter table publisher add constraint fk_publisher_listeningStartCue_37 foreign key (listening_start_cue_id) references audio (id) on delete restrict on update restrict;
create index ix_publisher_listeningStartCue_37 on publisher (listening_start_cue_id);
alter table publisher add constraint fk_publisher_listeningEndCue_38 foreign key (listening_end_cue_id) references audio (id) on delete restrict on update restrict;
create index ix_publisher_listeningEndCue_38 on publisher (listening_end_cue_id);
alter table publisher add constraint fk_publisher_recognitionSuccessCue_39 foreign key (recognition_success_cue_id) references audio (id) on delete restrict on update restrict;
create index ix_publisher_recognitionSuccessCue_39 on publisher (recognition_success_cue_id);
alter table publisher add constraint fk_publisher_recognitionFailureCue_40 foreign key (recognition_failure_cue_id) references audio (id) on delete restrict on update restrict;
create index ix_publisher_recognitionFailureCue_40 on publisher (recognition_failure_cue_id);

alter table audio add audio_type                varchar(9) not null;
update audio set audio_type='PROMPT';

alter table ad_action add custom                    tinyint(1) default 0 not null;
alter table ad_action add  custom_class              varchar(255);
alter table ad_action add  custom_data               varchar(255);
alter table ad_action add   custom_phrase             varchar(255);

update audio set audio_type='EXPANSION' where filename like '%MoreInfo%';
update ad set ad_type='ENGAGEMENT' where id=3;

#12/03/2013 - Homophone service added
#12/17/2013 - DEV
#12/24/2013 - PROD2
#12/24/2013 - STAGING
create table homophone (
  id                        bigint auto_increment not null,
  phrase                    varchar(255) not null,
  homophone                 varchar(255) not null,
  constraint uq_homophone_1 unique (phrase, homophone),
  constraint pk_homophone primary key (id))
;

alter table ad_action drop custom_class;
alter table ad_action add  custom_class_ios varchar(255);
alter table ad_action add  custom_class_android varchar(255);
alter table ad_action drop custom;
drop index uq_ad_action_1 on ad_action;

alter table ad_action add  download_url_ios varchar(255);
alter table ad_action add  download_url_android varchar(255);

alter table homophone add keyword tinyint(1) default 0 not null;

#12/18/2013 - Add inactive_timestamp in account
#12/18/2013 - Add active in admin
#12/18/2013 - LING
#12/23/2013 - DEV
#12/24/2013 - PROD2
#12/24/2013 - STAGING
alter table account add inactive_timestamp datetime null;
alter table admin add active boolean;

#12/18/2013 - Add access_configuration in publisher and reset the unique index in account
#12/18/2013 - LING
#12/23/2013 - DEV
#12/24/2013 - PROD2
#12/24/2013 - STAGING
drop index uq_account_1 on account;
create unique index uq_account_1 on account (publisher_id, name, active, inactive_timestamp);
alter table publisher add access_configuration boolean;

#12/31/2013 - Add pixel_tracking_url in ad
#12/31/2013 - LING
#01/02/2014 - DEV
#01/13/2014 - PROD2
alter table ad add pixel_tracking_url varchar(255);

#01/02/2014 - Add ad_approval_required in publisher
#01/02/2014 - LING
#01/02/2014 - DEV
#01/13/2014 - PROD2
alter table publisher add ad_approval_required boolean default false;

#01/13/2014 - PROD2 ONLY, other databases once applied the evolution script
alter table ad_request add journal_key varchar(255) null;
create unique index uq_ad_request_journal_1 on ad_request (journal_key);
alter table ad_request add created_timestamp datetime;
alter table ad_request add error_code varchar(12);
alter table ad_request add error_message varchar(255);
alter table ad_request add success boolean not null;

########Everything below this line needs to be added to liquibase

#01/16/2014 - Add background_trailing_audio_id and foreground_trailing_audio_id to ad_action
#01/16/2014 - Ling
#01/16/2014 - DEV
#01/17/2014 - PROD2
alter table ad_action add trailing_background_audio_id bigint null;
alter table ad_action add trailing_foreground_audio_id bigint null;

#01/20/2014 - PROD2
create index ix_utterance_1 on utterance (created_timestamp);

#01/20/2014 - Add network_type to utterance
#01/20/2014 - LING
#01/20/2014 - DEV
#01/30/2014 - PROD2
alter table utterance add network_type varchar(10);

#01/20/2014
#01/20/2014 - PROD2
#01/20/2014 - DEV
#01/23/2014 - LING
create index ix_utterance_1 on utterance(created_timestamp);
insert into homophone (phrase, homophone, keyword) values ('DOWNLOAD APP', 'DOWNLOAD APPS', 0);



# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table bb_category (
  id                        bigint auto_increment not null,
  user_id                   bigint,
  name                      varchar(171),
  document_count            integer,
  word_count                integer,
  constraint uq_bb_category_1 unique (user_id,name),
  constraint pk_bb_category primary key (id))
;

create table bb_item (
  id                        bigint auto_increment not null,
  id_date                   varchar(10) not null,
  id_index                  varchar(3) not null,
  date_show                 varchar(191),
  date_exec                 varchar(191),
  author                    longtext,
  title                     longtext,
  body                      longtext,
  last_update               datetime,
  constraint uq_bb_item_1 unique (id_date,id_index),
  constraint pk_bb_item primary key (id))
;

create table bb_item_classifier (
  id                        bigint auto_increment not null,
  bb_user_cluster_id        bigint,
  class_number              integer,
  prob_prior                double,
  prob_cond                 longtext,
  training_count            integer,
  training_data_count       integer,
  constraint pk_bb_item_classifier primary key (id))
;

create table bb_item_word_count (
  id                        bigint auto_increment not null,
  bb_item_id                bigint,
  bb_word_id                bigint,
  count                     integer,
  constraint pk_bb_item_word_count primary key (id))
;

create table bb_read_history (
  id                        bigint auto_increment not null,
  user_id                   bigint,
  bb_item_id                bigint,
  open_time                 bigint,
  read_time_length          bigint,
  referer                   varchar(255),
  filter                    varchar(255),
  constraint pk_bb_read_history primary key (id))
;

create table bb_user_cluster (
  id                        bigint auto_increment not null,
  cluster_depth             bigint,
  cluster_id                bigint,
  feature                   longtext,
  parent_id                 bigint,
  distance_from_parent      double,
  constraint pk_bb_user_cluster primary key (id))
;

create table bb_word (
  id                        bigint auto_increment not null,
  surface                   varchar(191),
  is_known                  tinyint(1) default 0,
  constraint pk_bb_word primary key (id))
;

create table mock_bb_item (
  id_date                   varchar(10) not null,
  id_index                  integer not null,
  date_show                 datetime,
  date_exec                 datetime,
  author                    varchar(191),
  title                     varchar(191),
  is_read                   tinyint(1) default 0,
  is_reference              tinyint(1) default 0,
  is_flagged                tinyint(1) default 0,
  body                      longtext,
  constraint pk_mock_bb_item primary key (id_date, id_index))
;

create table user (
  id                        bigint auto_increment not null,
  hashed_nitech_id          varchar(191),
  constraint pk_user primary key (id))
;

alter table bb_category add constraint fk_bb_category_user_1 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_bb_category_user_1 on bb_category (user_id);
alter table bb_item_classifier add constraint fk_bb_item_classifier_bbUserCluster_2 foreign key (bb_user_cluster_id) references bb_user_cluster (id) on delete restrict on update restrict;
create index ix_bb_item_classifier_bbUserCluster_2 on bb_item_classifier (bb_user_cluster_id);
alter table bb_item_word_count add constraint fk_bb_item_word_count_item_3 foreign key (bb_item_id) references bb_item (id) on delete restrict on update restrict;
create index ix_bb_item_word_count_item_3 on bb_item_word_count (bb_item_id);
alter table bb_item_word_count add constraint fk_bb_item_word_count_word_4 foreign key (bb_word_id) references bb_word (id) on delete restrict on update restrict;
create index ix_bb_item_word_count_word_4 on bb_item_word_count (bb_word_id);
alter table bb_read_history add constraint fk_bb_read_history_user_5 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_bb_read_history_user_5 on bb_read_history (user_id);
alter table bb_read_history add constraint fk_bb_read_history_item_6 foreign key (bb_item_id) references bb_item (id) on delete restrict on update restrict;
create index ix_bb_read_history_item_6 on bb_read_history (bb_item_id);
alter table bb_user_cluster add constraint fk_bb_user_cluster_parent_7 foreign key (parent_id) references bb_user_cluster (id) on delete restrict on update restrict;
create index ix_bb_user_cluster_parent_7 on bb_user_cluster (parent_id);



# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table bb_category;

drop table bb_item;

drop table bb_item_classifier;

drop table bb_item_word_count;

drop table bb_read_history;

drop table bb_user_cluster;

drop table bb_word;

drop table mock_bb_item;

drop table user;

SET FOREIGN_KEY_CHECKS=1;


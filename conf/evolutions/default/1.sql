# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table bba_category (
  id                        bigint auto_increment not null,
  user_id                   bigint,
  name                      varchar(171),
  document_count            integer,
  word_count                integer,
  constraint uq_bba_category_1 unique (user_id,name),
  constraint pk_bba_category primary key (id))
;

create table bba_item (
  id                        bigint auto_increment not null,
  id_date                   varchar(10) not null,
  id_index                  varchar(3) not null,
  date_show                 varchar(191),
  date_exec                 varchar(191),
  author                    longtext,
  title                     longtext,
  body                      longtext,
  last_update               datetime,
  constraint uq_bba_item_1 unique (id_date,id_index),
  constraint pk_bba_item primary key (id))
;

create table bba_item_classifier (
  id                        bigint auto_increment not null,
  bb_user_cluster_id        bigint,
  class_number              integer,
  prob_prior                double,
  prob_cond                 longtext,
  training_count            integer,
  training_data_count       integer,
  constraint pk_bba_item_classifier primary key (id))
;

create table bba_item_word_count (
  id                        bigint auto_increment not null,
  bb_item_id                bigint,
  bb_word_id                bigint,
  count                     integer,
  constraint pk_bba_item_word_count primary key (id))
;

create table bba_read_history (
  id                        bigint auto_increment not null,
  user_id                   bigint,
  bb_item_id                bigint,
  open_time                 bigint,
  read_time_length          bigint,
  referer                   varchar(255),
  filter                    varchar(255),
  constraint pk_bba_read_history primary key (id))
;

create table bba_user_cluster (
  id                        bigint auto_increment not null,
  cluster_depth             integer,
  cluster_id                bigint,
  feature                   longtext,
  parent_id                 bigint,
  distance_from_parent      double,
  constraint pk_bba_user_cluster primary key (id))
;

create table bba_word (
  id                        bigint auto_increment not null,
  surface                   varchar(191),
  is_known                  tinyint(1) default 0,
  constraint pk_bba_word primary key (id))
;

create table configuration (
  id                        bigint auto_increment not null,
  conf_key                  varchar(191),
  conf_value                longtext,
  constraint uq_configuration_1 unique (conf_key),
  constraint pk_configuration primary key (id))
;

create table bb_estimation (
  nitech_user_id            bigint,
  depth                     integer,
  post_id                   bigint,
  class                     tinyint default null,
  liklihood                 double,
  constraint pk_bb_estimation primary key (nitech_user_id, depth, post_id))
;

create table bb_history (
  id                        bigint auto_increment not null,
  nitech_user_id            bigint,
  post_id                   bigint,
  timestamp                 bigint,
  constraint pk_bb_history primary key (id))
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

create table nitech_user (
  id                        bigint auto_increment not null,
  hashed_id                 varchar(191),
  last_login                bigint,
  OPTLOCK                   integer not null,
  constraint pk_nitech_user primary key (id))
;

create table bb_possession (
  nitech_user_id            bigint,
  post_id                   bigint,
  is_favorite               tinyint(1) default null,
  class                     tinyint(1) default null,
  constraint pk_bb_possession primary key (nitech_user_id, post_id))
;

create table bb_post (
  id                        bigint auto_increment not null,
  id_date                   varchar(10),
  id_index                  integer,
  author                    varchar(255),
  title                     varchar(255),
  last_sampled              datetime,
  last_modified             datetime not null,
  constraint uq_bb_post_1 unique (id_date,id_index),
  constraint pk_bb_post primary key (id))
;

create table bb_post_distance (
  from_post_id              bigint,
  to_post_id                bigint,
  distance                  double,
  constraint pk_bb_post_distance primary key (from_post_id, to_post_id))
;

create table user (
  id                        bigint auto_increment not null,
  hashed_nitech_id          varchar(191),
  constraint pk_user primary key (id))
;

create table bb_user_cluster (
  id                        bigint auto_increment not null,
  nitech_user_id            bigint,
  depth                     integer,
  weight                    bigint,
  prior_1                   double,
  prior_0                   double,
  parent_id                 bigint,
  constraint uq_bb_user_cluster_1 unique (nitech_user_id),
  constraint pk_bb_user_cluster primary key (id))
;

create table bb_user_cluster_vector (
  cluster_id                bigint,
  class                     tinyint(1) default 0,
  word_id                   bigint,
  value                     double,
  constraint pk_bb_user_cluster_vector primary key (cluster_id, class, word_id))
;

create table bb_word (
  id                        bigint auto_increment not null,
  base_form                 varchar(191),
  constraint uq_bb_word_1 unique (base_form),
  constraint pk_bb_word primary key (id))
;

create table bb_word_in_post (
  post_id                   bigint,
  word_id                   bigint,
  value                     tinyint(1) default 0,
  constraint pk_bb_word_in_post primary key (post_id, word_id))
;

alter table bba_category add constraint fk_bba_category_user_1 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_bba_category_user_1 on bba_category (user_id);
alter table bba_item_classifier add constraint fk_bba_item_classifier_bbUserCluster_2 foreign key (bb_user_cluster_id) references bba_user_cluster (id) on delete restrict on update restrict;
create index ix_bba_item_classifier_bbUserCluster_2 on bba_item_classifier (bb_user_cluster_id);
alter table bba_item_word_count add constraint fk_bba_item_word_count_item_3 foreign key (bb_item_id) references bba_item (id) on delete restrict on update restrict;
create index ix_bba_item_word_count_item_3 on bba_item_word_count (bb_item_id);
alter table bba_item_word_count add constraint fk_bba_item_word_count_word_4 foreign key (bb_word_id) references bba_word (id) on delete restrict on update restrict;
create index ix_bba_item_word_count_word_4 on bba_item_word_count (bb_word_id);
alter table bba_read_history add constraint fk_bba_read_history_user_5 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_bba_read_history_user_5 on bba_read_history (user_id);
alter table bba_read_history add constraint fk_bba_read_history_item_6 foreign key (bb_item_id) references bba_item (id) on delete restrict on update restrict;
create index ix_bba_read_history_item_6 on bba_read_history (bb_item_id);
alter table bba_user_cluster add constraint fk_bba_user_cluster_parent_7 foreign key (parent_id) references bba_user_cluster (id) on delete restrict on update restrict;
create index ix_bba_user_cluster_parent_7 on bba_user_cluster (parent_id);
alter table bb_estimation add constraint fk_bb_estimation_nitechUser_8 foreign key (nitech_user_id) references nitech_user (id) on delete restrict on update restrict;
create index ix_bb_estimation_nitechUser_8 on bb_estimation (nitech_user_id);
alter table bb_estimation add constraint fk_bb_estimation_post_9 foreign key (post_id) references bb_post (id) on delete restrict on update restrict;
create index ix_bb_estimation_post_9 on bb_estimation (post_id);
alter table bb_history add constraint fk_bb_history_nitechUser_10 foreign key (nitech_user_id) references nitech_user (id) on delete restrict on update restrict;
create index ix_bb_history_nitechUser_10 on bb_history (nitech_user_id);
alter table bb_history add constraint fk_bb_history_post_11 foreign key (post_id) references bb_post (id) on delete restrict on update restrict;
create index ix_bb_history_post_11 on bb_history (post_id);
alter table bb_possession add constraint fk_bb_possession_nitechUser_12 foreign key (nitech_user_id) references nitech_user (id) on delete restrict on update restrict;
create index ix_bb_possession_nitechUser_12 on bb_possession (nitech_user_id);
alter table bb_possession add constraint fk_bb_possession_post_13 foreign key (post_id) references bb_post (id) on delete restrict on update restrict;
create index ix_bb_possession_post_13 on bb_possession (post_id);
alter table bb_post_distance add constraint fk_bb_post_distance_fromPost_14 foreign key (from_post_id) references bb_post (id) on delete restrict on update restrict;
create index ix_bb_post_distance_fromPost_14 on bb_post_distance (from_post_id);
alter table bb_post_distance add constraint fk_bb_post_distance_toPost_15 foreign key (to_post_id) references bb_post (id) on delete restrict on update restrict;
create index ix_bb_post_distance_toPost_15 on bb_post_distance (to_post_id);
alter table bb_user_cluster add constraint fk_bb_user_cluster_nitechUser_16 foreign key (nitech_user_id) references nitech_user (id) on delete restrict on update restrict;
create index ix_bb_user_cluster_nitechUser_16 on bb_user_cluster (nitech_user_id);
alter table bb_user_cluster add constraint fk_bb_user_cluster_parent_17 foreign key (parent_id) references bb_user_cluster (id) on delete restrict on update restrict;
create index ix_bb_user_cluster_parent_17 on bb_user_cluster (parent_id);
alter table bb_user_cluster_vector add constraint fk_bb_user_cluster_vector_cluster_18 foreign key (cluster_id) references bb_user_cluster (id) on delete restrict on update restrict;
create index ix_bb_user_cluster_vector_cluster_18 on bb_user_cluster_vector (cluster_id);
alter table bb_user_cluster_vector add constraint fk_bb_user_cluster_vector_word_19 foreign key (word_id) references bb_word (id) on delete restrict on update restrict;
create index ix_bb_user_cluster_vector_word_19 on bb_user_cluster_vector (word_id);
alter table bb_word_in_post add constraint fk_bb_word_in_post_post_20 foreign key (post_id) references bb_post (id) on delete restrict on update restrict;
create index ix_bb_word_in_post_post_20 on bb_word_in_post (post_id);
alter table bb_word_in_post add constraint fk_bb_word_in_post_word_21 foreign key (word_id) references bb_word (id) on delete restrict on update restrict;
create index ix_bb_word_in_post_word_21 on bb_word_in_post (word_id);



# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table bba_category;

drop table bba_item;

drop table bba_item_classifier;

drop table bba_item_word_count;

drop table bba_read_history;

drop table bba_user_cluster;

drop table bba_word;

drop table configuration;

drop table bb_estimation;

drop table bb_history;

drop table mock_bb_item;

drop table nitech_user;

drop table bb_possession;

drop table bb_post;

drop table bb_post_distance;

drop table user;

drop table bb_user_cluster;

drop table bb_user_cluster_vector;

drop table bb_word;

drop table bb_word_in_post;

SET FOREIGN_KEY_CHECKS=1;


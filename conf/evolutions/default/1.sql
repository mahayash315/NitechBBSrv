# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table bb_author_cluster (
  id                        bigint auto_increment not null,
  user_id                   bigint,
  depth                     integer,
  parent_id                 bigint,
  constraint pk_bb_author_cluster primary key (id))
;

create table bb_author_cluster_vector (
  cluster_id                bigint,
  word_id                   bigint,
  value                     double,
  constraint pk_bb_author_cluster_vector primary key (cluster_id, word_id))
;

create table bb_author_vector (
  post_id                   bigint,
  word_id                   bigint,
  value                     tinyint(1) default 0,
  constraint pk_bb_author_vector primary key (post_id, word_id))
;

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
  cluster_depth             integer,
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
  constraint pk_nitech_user primary key (id))
;

create table bb_post (
  id                        bigint auto_increment not null,
  id_date                   varchar(10),
  id_index                  integer,
  constraint uq_bb_post_1 unique (id_date,id_index),
  constraint pk_bb_post primary key (id))
;

create table bb_title_cluster (
  id                        bigint auto_increment not null,
  user_id                   bigint,
  depth                     integer,
  parent_id                 bigint,
  constraint pk_bb_title_cluster primary key (id))
;

create table bb_title_cluster_vector (
  cluster_id                bigint,
  word_id                   bigint,
  value                     double,
  constraint pk_bb_title_cluster_vector primary key (cluster_id, word_id))
;

create table bb_title_vector (
  post_id                   bigint,
  word_id                   bigint,
  value                     tinyint(1) default 0,
  constraint pk_bb_title_vector primary key (post_id, word_id))
;

create table user (
  id                        bigint auto_increment not null,
  hashed_nitech_id          varchar(191),
  constraint pk_user primary key (id))
;

create table bb_word2 (
  id                        bigint auto_increment not null,
  base_form                 varchar(197),
  constraint pk_bb_word2 primary key (id))
;

alter table bb_author_cluster add constraint fk_bb_author_cluster_user_1 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_bb_author_cluster_user_1 on bb_author_cluster (user_id);
alter table bb_author_cluster add constraint fk_bb_author_cluster_parent_2 foreign key (parent_id) references bb_author_cluster (id) on delete restrict on update restrict;
create index ix_bb_author_cluster_parent_2 on bb_author_cluster (parent_id);
alter table bb_author_cluster_vector add constraint fk_bb_author_cluster_vector_cluster_3 foreign key (cluster_id) references bb_author_cluster (id) on delete restrict on update restrict;
create index ix_bb_author_cluster_vector_cluster_3 on bb_author_cluster_vector (cluster_id);
alter table bb_author_cluster_vector add constraint fk_bb_author_cluster_vector_word_4 foreign key (word_id) references bb_word2 (id) on delete restrict on update restrict;
create index ix_bb_author_cluster_vector_word_4 on bb_author_cluster_vector (word_id);
alter table bb_author_vector add constraint fk_bb_author_vector_post_5 foreign key (post_id) references bb_post (id) on delete restrict on update restrict;
create index ix_bb_author_vector_post_5 on bb_author_vector (post_id);
alter table bb_author_vector add constraint fk_bb_author_vector_word_6 foreign key (word_id) references bb_word2 (id) on delete restrict on update restrict;
create index ix_bb_author_vector_word_6 on bb_author_vector (word_id);
alter table bb_category add constraint fk_bb_category_user_7 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_bb_category_user_7 on bb_category (user_id);
alter table bb_item_classifier add constraint fk_bb_item_classifier_bbUserCluster_8 foreign key (bb_user_cluster_id) references bb_user_cluster (id) on delete restrict on update restrict;
create index ix_bb_item_classifier_bbUserCluster_8 on bb_item_classifier (bb_user_cluster_id);
alter table bb_item_word_count add constraint fk_bb_item_word_count_item_9 foreign key (bb_item_id) references bb_item (id) on delete restrict on update restrict;
create index ix_bb_item_word_count_item_9 on bb_item_word_count (bb_item_id);
alter table bb_item_word_count add constraint fk_bb_item_word_count_word_10 foreign key (bb_word_id) references bb_word (id) on delete restrict on update restrict;
create index ix_bb_item_word_count_word_10 on bb_item_word_count (bb_word_id);
alter table bb_read_history add constraint fk_bb_read_history_user_11 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_bb_read_history_user_11 on bb_read_history (user_id);
alter table bb_read_history add constraint fk_bb_read_history_item_12 foreign key (bb_item_id) references bb_item (id) on delete restrict on update restrict;
create index ix_bb_read_history_item_12 on bb_read_history (bb_item_id);
alter table bb_user_cluster add constraint fk_bb_user_cluster_parent_13 foreign key (parent_id) references bb_user_cluster (id) on delete restrict on update restrict;
create index ix_bb_user_cluster_parent_13 on bb_user_cluster (parent_id);
alter table bb_history add constraint fk_bb_history_nitechUser_14 foreign key (nitech_user_id) references nitech_user (id) on delete restrict on update restrict;
create index ix_bb_history_nitechUser_14 on bb_history (nitech_user_id);
alter table bb_history add constraint fk_bb_history_post_15 foreign key (post_id) references bb_post (id) on delete restrict on update restrict;
create index ix_bb_history_post_15 on bb_history (post_id);
alter table bb_title_cluster add constraint fk_bb_title_cluster_user_16 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_bb_title_cluster_user_16 on bb_title_cluster (user_id);
alter table bb_title_cluster add constraint fk_bb_title_cluster_parent_17 foreign key (parent_id) references bb_title_cluster (id) on delete restrict on update restrict;
create index ix_bb_title_cluster_parent_17 on bb_title_cluster (parent_id);
alter table bb_title_cluster_vector add constraint fk_bb_title_cluster_vector_cluster_18 foreign key (cluster_id) references bb_title_cluster (id) on delete restrict on update restrict;
create index ix_bb_title_cluster_vector_cluster_18 on bb_title_cluster_vector (cluster_id);
alter table bb_title_cluster_vector add constraint fk_bb_title_cluster_vector_word_19 foreign key (word_id) references bb_word2 (id) on delete restrict on update restrict;
create index ix_bb_title_cluster_vector_word_19 on bb_title_cluster_vector (word_id);
alter table bb_title_vector add constraint fk_bb_title_vector_post_20 foreign key (post_id) references bb_post (id) on delete restrict on update restrict;
create index ix_bb_title_vector_post_20 on bb_title_vector (post_id);
alter table bb_title_vector add constraint fk_bb_title_vector_word_21 foreign key (word_id) references bb_word2 (id) on delete restrict on update restrict;
create index ix_bb_title_vector_word_21 on bb_title_vector (word_id);



# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table bb_author_cluster;

drop table bb_author_cluster_vector;

drop table bb_author_vector;

drop table bb_category;

drop table bb_item;

drop table bb_item_classifier;

drop table bb_item_word_count;

drop table bb_read_history;

drop table bb_user_cluster;

drop table bb_word;

drop table bb_history;

drop table mock_bb_item;

drop table nitech_user;

drop table bb_post;

drop table bb_title_cluster;

drop table bb_title_cluster_vector;

drop table bb_title_vector;

drop table user;

drop table bb_word2;

SET FOREIGN_KEY_CHECKS=1;


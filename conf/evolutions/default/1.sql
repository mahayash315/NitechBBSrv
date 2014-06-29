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

create table bb_item_appendix (
  id                        bigint auto_increment not null,
  bb_category_id            bigint,
  constraint pk_bb_item_appendix primary key (id))
;

create table bb_item_head (
  id                        bigint auto_increment not null,
  id_date                   varchar(10) not null,
  id_index                  varchar(3) not null,
  user_id                   bigint,
  date_show                 varchar(191),
  date_exec                 varchar(191),
  author                    longtext,
  title                     longtext,
  last_update               datetime,
  bb_item_appendix_id       bigint,
  constraint uq_bb_item_head_1 unique (user_id,id_date,id_index),
  constraint pk_bb_item_head primary key (id))
;

create table bb_item_word_count (
  id                        bigint auto_increment not null,
  bb_item_id                bigint,
  bb_word_id                bigint,
  count                     integer,
  constraint pk_bb_item_word_count primary key (id))
;

create table bb_naive_bayes_param (
  id                        bigint auto_increment not null,
  user_id                   bigint,
  bb_word_id                bigint,
  bb_category_id            bigint,
  count                     integer,
  gauss_myu                 double,
  poisson_lambda            double,
  OPTLOCK                   integer not null,
  constraint uq_bb_naive_bayes_param_1 unique (user_id,bb_word_id,bb_category_id),
  constraint pk_bb_naive_bayes_param primary key (id))
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

create table bb_word_count (
  id                        bigint auto_increment not null,
  bb_item_id                bigint,
  bb_word_id                bigint,
  count                     integer,
  constraint pk_bb_word_count primary key (id))
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
  bb_analyzer_document_count integer,
  bb_analyzer_word_count    integer,
  constraint pk_user primary key (id))
;

alter table bb_category add constraint fk_bb_category_user_1 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_bb_category_user_1 on bb_category (user_id);
alter table bb_item_appendix add constraint fk_bb_item_appendix_category_2 foreign key (bb_category_id) references bb_category (id) on delete restrict on update restrict;
create index ix_bb_item_appendix_category_2 on bb_item_appendix (bb_category_id);
alter table bb_item_head add constraint fk_bb_item_head_user_3 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_bb_item_head_user_3 on bb_item_head (user_id);
alter table bb_item_head add constraint fk_bb_item_head_appendix_4 foreign key (bb_item_appendix_id) references bb_item_appendix (id) on delete restrict on update restrict;
create index ix_bb_item_head_appendix_4 on bb_item_head (bb_item_appendix_id);
alter table bb_item_word_count add constraint fk_bb_item_word_count_item_5 foreign key (bb_item_id) references bb_item (id) on delete restrict on update restrict;
create index ix_bb_item_word_count_item_5 on bb_item_word_count (bb_item_id);
alter table bb_item_word_count add constraint fk_bb_item_word_count_word_6 foreign key (bb_word_id) references bb_word (id) on delete restrict on update restrict;
create index ix_bb_item_word_count_word_6 on bb_item_word_count (bb_word_id);
alter table bb_naive_bayes_param add constraint fk_bb_naive_bayes_param_user_7 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_bb_naive_bayes_param_user_7 on bb_naive_bayes_param (user_id);
alter table bb_naive_bayes_param add constraint fk_bb_naive_bayes_param_word_8 foreign key (bb_word_id) references bb_word (id) on delete restrict on update restrict;
create index ix_bb_naive_bayes_param_word_8 on bb_naive_bayes_param (bb_word_id);
alter table bb_naive_bayes_param add constraint fk_bb_naive_bayes_param_category_9 foreign key (bb_category_id) references bb_category (id) on delete restrict on update restrict;
create index ix_bb_naive_bayes_param_category_9 on bb_naive_bayes_param (bb_category_id);
alter table bb_read_history add constraint fk_bb_read_history_user_10 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_bb_read_history_user_10 on bb_read_history (user_id);
alter table bb_read_history add constraint fk_bb_read_history_item_11 foreign key (bb_item_id) references bb_item (id) on delete restrict on update restrict;
create index ix_bb_read_history_item_11 on bb_read_history (bb_item_id);
alter table bb_user_cluster add constraint fk_bb_user_cluster_parent_12 foreign key (parent_id) references bb_user_cluster (id) on delete restrict on update restrict;
create index ix_bb_user_cluster_parent_12 on bb_user_cluster (parent_id);
alter table bb_word_count add constraint fk_bb_word_count_item_13 foreign key (bb_item_id) references bb_item (id) on delete restrict on update restrict;
create index ix_bb_word_count_item_13 on bb_word_count (bb_item_id);
alter table bb_word_count add constraint fk_bb_word_count_word_14 foreign key (bb_word_id) references bb_word (id) on delete restrict on update restrict;
create index ix_bb_word_count_word_14 on bb_word_count (bb_word_id);



# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table bb_category;

drop table bb_item;

drop table bb_item_appendix;

drop table bb_item_head;

drop table bb_item_word_count;

drop table bb_naive_bayes_param;

drop table bb_read_history;

drop table bb_user_cluster;

drop table bb_word;

drop table bb_word_count;

drop table mock_bb_item;

drop table user;

SET FOREIGN_KEY_CHECKS=1;


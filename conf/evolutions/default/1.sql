# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

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
  constraint uq_bb_item_head_1 unique (user_id,id_date,id_index),
  constraint pk_bb_item_head primary key (id))
;

create table bb_read_history (
  id                        bigint auto_increment not null,
  user_id                   bigint,
  bb_item_head_id           bigint,
  open_time                 bigint,
  read_time_length          bigint,
  referer                   varchar(255),
  filter                    varchar(255),
  constraint pk_bb_read_history primary key (id))
;

create table user (
  id                        bigint auto_increment not null,
  nitech_id                 varchar(191),
  constraint pk_user primary key (id))
;

alter table bb_item_head add constraint fk_bb_item_head_user_1 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_bb_item_head_user_1 on bb_item_head (user_id);
alter table bb_read_history add constraint fk_bb_read_history_user_2 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_bb_read_history_user_2 on bb_read_history (user_id);
alter table bb_read_history add constraint fk_bb_read_history_item_3 foreign key (bb_item_head_id) references bb_item_head (id) on delete restrict on update restrict;
create index ix_bb_read_history_item_3 on bb_read_history (bb_item_head_id);



# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table bb_item_head;

drop table bb_read_history;

drop table user;

SET FOREIGN_KEY_CHECKS=1;


# --- !Ups

INSERT INTO `nitech_user` (hashed_id) VALUES ('user1');
INSERT INTO `nitech_user` (hashed_id) VALUES ('user2');
INSERT INTO `nitech_user` (hashed_id) VALUES ('user3');
INSERT INTO `nitech_user` (hashed_id) VALUES ('user4');
INSERT INTO `nitech_user` (hashed_id) VALUES ('user5');
INSERT INTO `nitech_user` (hashed_id) VALUES ('user6');
INSERT INTO `nitech_user` (hashed_id) VALUES ('user7');
INSERT INTO `nitech_user` (hashed_id) VALUES ('user8');

INSERT INTO `bb_post` (id_date, id_index) VALUES ('2014-08-24',1);
INSERT INTO `bb_post` (id_date, id_index) VALUES ('2014-08-24',2);
INSERT INTO `bb_post` (id_date, id_index) VALUES ('2014-08-24',3);
INSERT INTO `bb_post` (id_date, id_index) VALUES ('2014-08-24',4);
INSERT INTO `bb_post` (id_date, id_index) VALUES ('2014-08-24',5);
INSERT INTO `bb_post` (id_date, id_index) VALUES ('2014-08-24',6);
INSERT INTO `bb_post` (id_date, id_index) VALUES ('2014-08-24',7);
INSERT INTO `bb_post` (id_date, id_index) VALUES ('2014-08-24',8);
INSERT INTO `bb_post` (id_date, id_index) VALUES ('2014-08-24',9);
INSERT INTO `bb_post` (id_date, id_index) VALUES ('2014-08-24',10);
INSERT INTO `bb_post` (id_date, id_index) VALUES ('2014-08-24',11);
INSERT INTO `bb_post` (id_date, id_index) VALUES ('2014-08-24',12);
INSERT INTO `bb_post` (id_date, id_index) VALUES ('2014-08-24',13);
INSERT INTO `bb_post` (id_date, id_index) VALUES ('2014-08-24',14);
INSERT INTO `bb_post` (id_date, id_index) VALUES ('2014-08-24',15);
INSERT INTO `bb_post` (id_date, id_index) VALUES ('2014-08-24',16);
INSERT INTO `bb_post` (id_date, id_index) VALUES ('2014-08-24',17);
INSERT INTO `bb_post` (id_date, id_index) VALUES ('2014-08-24',18);
INSERT INTO `bb_post` (id_date, id_index) VALUES ('2014-08-24',19);
INSERT INTO `bb_post` (id_date, id_index) VALUES ('2014-08-24',20);
INSERT INTO `bb_post` (id_date, id_index) VALUES ('2014-08-24',21);
INSERT INTO `bb_post` (id_date, id_index) VALUES ('2014-08-24',22);
INSERT INTO `bb_post` (id_date, id_index) VALUES ('2014-08-24',23);
INSERT INTO `bb_post` (id_date, id_index) VALUES ('2014-08-24',24);
INSERT INTO `bb_post` (id_date, id_index) VALUES ('2014-08-24',25);
INSERT INTO `bb_post` (id_date, id_index) VALUES ('2014-08-24',26);
INSERT INTO `bb_post` (id_date, id_index) VALUES ('2014-08-24',27);
INSERT INTO `bb_post` (id_date, id_index) VALUES ('2014-08-24',28);
INSERT INTO `bb_post` (id_date, id_index) VALUES ('2014-08-24',29);
INSERT INTO `bb_post` (id_date, id_index) VALUES ('2014-08-24',30);

INSERT INTO `bb_word` (base_form) VALUES ('あ');
INSERT INTO `bb_word` (base_form) VALUES ('い');
INSERT INTO `bb_word` (base_form) VALUES ('う');
INSERT INTO `bb_word` (base_form) VALUES ('え');
INSERT INTO `bb_word` (base_form) VALUES ('お');
INSERT INTO `bb_word` (base_form) VALUES ('か');
INSERT INTO `bb_word` (base_form) VALUES ('き');
INSERT INTO `bb_word` (base_form) VALUES ('く');
INSERT INTO `bb_word` (base_form) VALUES ('け');
INSERT INTO `bb_word` (base_form) VALUES ('こ');

DROP PROCEDURE IF EXISTS InsertTestData;
CREATE PROCEDURE InsertTestData()
BEGIN
	DECLARE x INT;;
	DECLARE n INT;;
	DECLARE nid INT;;
	DECLARE pid INT;;
	DECLARE wid INT;;
	select (select count(id) from `nitech_user`)*(select count(id) from `bb_post`) into n;;
	SET x=1;;
	WHILE x <= n DO
		select (select id from `nitech_user` order by rand() limit 1) into nid;;
		select (select id from `bb_post` order by rand() limit 1) into pid;;
		insert ignore into bb_possession(nitech_user_id, post_id) VALUES(nid,pid);;
		SET x = x+1;;
	END WHILE;;
	
	SET x=1;;
	WHILE x <= n DO
		select (select id from `nitech_user` order by rand() limit 1) into nid;;
		select (select post_id from `bb_possession` where nitech_user_id=nid order by rand() limit 1) into pid;;
		insert ignore into bb_history(nitech_user_id, post_id) VALUES(nid,pid);;
		SET x = x+1;;
	END WHILE;;
	
	select (select count(id) from `bb_post`)*(select count(id) from `bb_word`)/2 into n;;
	SET x=1;;
	WHILE x <= n DO
		select (select id from `bb_post` order by rand() limit 1) into pid;;
		select (select id from `bb_word` order by rand() limit 1) into wid;;
		insert ignore into bb_word_in_post(post_id, word_id, `value`) VALUES(pid,wid,true);;
		SET x = x+1;;
	END WHILE;;
END;
       
call InsertTestData();

# --- !Downs
SET FOREIGN_KEY_CHECKS=0;

delete from `nitech_user`;
alter table `nitech_user` auto_increment=1;

delete from `bb_post`;
alter table `bb_post` auto_increment=1;

delete from `bb_possession`;
alter table `bb_possession` auto_increment=1;

delete from `bb_history`;
alter table `bb_history` auto_increment=1;

delete from `bb_word`;
alter table `bb_word` auto_increment=1;

delete from `bb_word_in_post`;
alter table `bb_word_in_post` auto_increment=1;

delete from `bb_user_cluster`;
alter table `bb_user_cluster` auto_increment=1;

delete from `bb_user_cluster_vector`;
alter table `bb_user_cluster_vector` auto_increment=1;

SET FOREIGN_KEY_CHECKS=1;
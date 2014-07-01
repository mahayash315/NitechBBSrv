# --- !Ups

INSERT INTO `user` (id, hashed_nitech_id) VALUES (1, 'user1');
INSERT INTO `user` (id, hashed_nitech_id) VALUES (2, 'user2');
INSERT INTO `user` (id, hashed_nitech_id) VALUES (3, 'user3');
INSERT INTO `user` (id, hashed_nitech_id) VALUES (4, 'user4');
INSERT INTO `user` (id, hashed_nitech_id) VALUES (5, 'user5');
INSERT INTO `user` (id, hashed_nitech_id) VALUES (6, 'user6');
INSERT INTO `user` (id, hashed_nitech_id) VALUES (7, 'user7');
INSERT INTO `user` (id, hashed_nitech_id) VALUES (8, 'user8');

INSERT INTO `bb_item` (id,id_date,id_index,author,title) VALUES (1,'2014-06-25','1','学生生活課（就職・キャリア支援係）','ジェネラルインターンシップ　第2次募集開始について');
INSERT INTO `bb_item` (id,id_date,id_index,author,title) VALUES (2,'2014-06-25','2','学生生活課（就職・キャリア支援係）','平成２６年度キャリア形成ガイダンス（第６回）のお知らせ');
INSERT INTO `bb_item` (id,id_date,id_index,author,title) VALUES (3,'2014-06-25','3','学生生活課（就職・キャリア支援係）','平成２６年度キャリア形成ガイダンス（第７回）のお知らせ');
INSERT INTO `bb_item` (id,id_date,id_index,author,title) VALUES (4,'2014-06-25','4','男女共同参画推進室','【男女共同・お知らせ】名工大の男女共同参画週間in附属図書館について ');

INSERT INTO `bb_read_history` (user_id,bb_item_id,open_time,read_time_length,referer,filter) VALUES (1,1,0,0,null,null);
INSERT INTO `bb_read_history` (user_id,bb_item_id,open_time,read_time_length,referer,filter) VALUES (1,2,0,0,null,null);
INSERT INTO `bb_read_history` (user_id,bb_item_id,open_time,read_time_length,referer,filter) VALUES (1,3,0,0,null,null);
INSERT INTO `bb_read_history` (user_id,bb_item_id,open_time,read_time_length,referer,filter) VALUES (2,1,0,0,null,null);
INSERT INTO `bb_read_history` (user_id,bb_item_id,open_time,read_time_length,referer,filter) VALUES (2,2,0,0,null,null);
INSERT INTO `bb_read_history` (user_id,bb_item_id,open_time,read_time_length,referer,filter) VALUES (2,3,0,0,null,null);
INSERT INTO `bb_read_history` (user_id,bb_item_id,open_time,read_time_length,referer,filter) VALUES (2,1,0,0,null,null);
INSERT INTO `bb_read_history` (user_id,bb_item_id,open_time,read_time_length,referer,filter) VALUES (2,2,0,0,null,null);
INSERT INTO `bb_read_history` (user_id,bb_item_id,open_time,read_time_length,referer,filter) VALUES (2,3,0,0,null,null);
INSERT INTO `bb_read_history` (user_id,bb_item_id,open_time,read_time_length,referer,filter) VALUES (3,3,0,0,null,null);
INSERT INTO `bb_read_history` (user_id,bb_item_id,open_time,read_time_length,referer,filter) VALUES (3,3,0,0,null,null);
INSERT INTO `bb_read_history` (user_id,bb_item_id,open_time,read_time_length,referer,filter) VALUES (3,3,0,0,null,null);
INSERT INTO `bb_read_history` (user_id,bb_item_id,open_time,read_time_length,referer,filter) VALUES (4,3,0,0,null,null);
INSERT INTO `bb_read_history` (user_id,bb_item_id,open_time,read_time_length,referer,filter) VALUES (5,3,0,0,null,null);
INSERT INTO `bb_read_history` (user_id,bb_item_id,open_time,read_time_length,referer,filter) VALUES (6,3,0,0,null,null);
INSERT INTO `bb_read_history` (user_id,bb_item_id,open_time,read_time_length,referer,filter) VALUES (7,3,0,0,null,null);
INSERT INTO `bb_read_history` (user_id,bb_item_id,open_time,read_time_length,referer,filter) VALUES (8,3,0,0,null,null);
INSERT INTO `bb_read_history` (user_id,bb_item_id,open_time,read_time_length,referer,filter) VALUES (8,3,0,0,null,null);

# --- !Downs
SET FOREIGN_KEY_CHECKS=0;

delete from `user`;

delete from `bb_item`;

delete from `bb_read_history`;

delete from `bb_word`;

delete from `bb_item_word_count`;

SET FOREIGN_KEY_CHECKS=1;
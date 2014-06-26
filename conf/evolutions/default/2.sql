# --- !Ups

INSERT INTO `user` (id, hashed_nitech_id, bb_analyzer_document_count, bb_analyzer_word_count) VALUES (1, 'user1', 0, 0);
INSERT INTO `user` (id, hashed_nitech_id, bb_analyzer_document_count, bb_analyzer_word_count) VALUES (2, 'user2', 0, 0);
INSERT INTO `user` (id, hashed_nitech_id, bb_analyzer_document_count, bb_analyzer_word_count) VALUES (3, 'user3', 0, 0);
INSERT INTO `user` (id, hashed_nitech_id, bb_analyzer_document_count, bb_analyzer_word_count) VALUES (4, 'user4', 0, 0);
INSERT INTO `user` (id, hashed_nitech_id, bb_analyzer_document_count, bb_analyzer_word_count) VALUES (5, 'user5', 0, 0);
INSERT INTO `user` (id, hashed_nitech_id, bb_analyzer_document_count, bb_analyzer_word_count) VALUES (6, 'user6', 0, 0);
INSERT INTO `user` (id, hashed_nitech_id, bb_analyzer_document_count, bb_analyzer_word_count) VALUES (7, 'user7', 0, 0);
INSERT INTO `user` (id, hashed_nitech_id, bb_analyzer_document_count, bb_analyzer_word_count) VALUES (8, 'user8', 0, 0);

INSERT INTO `bb_item` (id,id_date,id_index,author,title) VALUES (1,'2014-06-25','1','a','A');
INSERT INTO `bb_item` (id,id_date,id_index,author,title) VALUES (2,'2014-06-25','2','b','A');
INSERT INTO `bb_item` (id,id_date,id_index,author,title) VALUES (3,'2014-06-25','3','c','A');

INSERT INTO `bb_read_history` (id,user_id,bb_item_id,open_time,read_time_length,referer,filter) VALUES (1,1,1,0,0,null,null);
INSERT INTO `bb_read_history` (id,user_id,bb_item_id,open_time,read_time_length,referer,filter) VALUES (2,1,2,0,0,null,null);
INSERT INTO `bb_read_history` (id,user_id,bb_item_id,open_time,read_time_length,referer,filter) VALUES (3,1,3,0,0,null,null);
INSERT INTO `bb_read_history` (id,user_id,bb_item_id,open_time,read_time_length,referer,filter) VALUES (4,2,1,0,0,null,null);
INSERT INTO `bb_read_history` (id,user_id,bb_item_id,open_time,read_time_length,referer,filter) VALUES (5,2,2,0,0,null,null);
INSERT INTO `bb_read_history` (id,user_id,bb_item_id,open_time,read_time_length,referer,filter) VALUES (6,2,3,0,0,null,null);
INSERT INTO `bb_read_history` (id,user_id,bb_item_id,open_time,read_time_length,referer,filter) VALUES (7,2,1,0,0,null,null);
INSERT INTO `bb_read_history` (id,user_id,bb_item_id,open_time,read_time_length,referer,filter) VALUES (8,2,2,0,0,null,null);
INSERT INTO `bb_read_history` (id,user_id,bb_item_id,open_time,read_time_length,referer,filter) VALUES (9,2,3,0,0,null,null);
INSERT INTO `bb_read_history` (id,user_id,bb_item_id,open_time,read_time_length,referer,filter) VALUES (10,3,3,0,0,null,null);
INSERT INTO `bb_read_history` (id,user_id,bb_item_id,open_time,read_time_length,referer,filter) VALUES (11,3,3,0,0,null,null);
INSERT INTO `bb_read_history` (id,user_id,bb_item_id,open_time,read_time_length,referer,filter) VALUES (12,3,3,0,0,null,null);
INSERT INTO `bb_read_history` (id,user_id,bb_item_id,open_time,read_time_length,referer,filter) VALUES (13,4,3,0,0,null,null);
INSERT INTO `bb_read_history` (id,user_id,bb_item_id,open_time,read_time_length,referer,filter) VALUES (14,5,3,0,0,null,null);
INSERT INTO `bb_read_history` (id,user_id,bb_item_id,open_time,read_time_length,referer,filter) VALUES (15,6,3,0,0,null,null);
INSERT INTO `bb_read_history` (id,user_id,bb_item_id,open_time,read_time_length,referer,filter) VALUES (16,7,3,0,0,null,null);
INSERT INTO `bb_read_history` (id,user_id,bb_item_id,open_time,read_time_length,referer,filter) VALUES (17,8,3,0,0,null,null);
INSERT INTO `bb_read_history` (id,user_id,bb_item_id,open_time,read_time_length,referer,filter) VALUES (18,8,3,0,0,null,null);

# --- !Downs
SET FOREIGN_KEY_CHECKS=0;

delete from `user`;

delete from `bb_item`;

delete from `bb_read_history`;

SET FOREIGN_KEY_CHECKS=1;
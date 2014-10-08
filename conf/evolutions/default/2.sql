# --- !Ups

DROP PROCEDURE IF EXISTS debugMsg;
CREATE PROCEDURE debugMsg(msg VARCHAR(255))
BEGIN
    select concat("** ", msg) AS '** DEBUG:';;
END;


DROP FUNCTION IF EXISTS feature_multiply;
CREATE FUNCTION feature_multiply(_post_id1 bigint, _post_id2 bigint) RETURNS DOUBLE
BEGIN
	return
		(select if(v is null, 0, v) from
			(select sum(v) v from
				(select v1.v*v2.v v from
					(select `word_id`,`value` v from bb_word_in_post where post_id =_post_id1) v1
					join
					(select `word_id`,`value` v from bb_word_in_post where post_id =_post_id2) v2
					on v1.`word_id`=v2.`word_id`) t) t);;
END;


DROP FUNCTION IF EXISTS feature_length;
CREATE FUNCTION feature_length(_post_id bigint) RETURNS DOUBLE
BEGIN
	RETURN
		(select sqrt(sum) length from
			(select sum(v) sum from
				(select `value`*`value` v from bb_word_in_post where post_id=_post_id) t) t);;
END;


DROP FUNCTION IF EXISTS feature_cos;
CREATE FUNCTION feature_cos(_post_id1 bigint, _post_id2 bigint) RETURNS DOUBLE
BEGIN
	RETURN (select feature_multiply(_post_id1,_post_id2) / (feature_length(_post_id1)*feature_length(_post_id2)));;
END;


DROP FUNCTION IF EXISTS feature_distance;
CREATE FUNCTION feature_distance(_post_id1 bigint, _post_id2 bigint) RETURNS DOUBLE
BEGIN
	RETURN (select 1-feature_cos(_post_id1,_post_id2));;
END;


-- 掲示の距離を計算して格納する
DROP PROCEDURE IF EXISTS CalculatePostDistance;
CREATE PROCEDURE CalculatePostDistance(IN _post_id BIGINT)
BEGIN
	DECLARE hasNext int;;
	DECLARE _from bigint;;
	DECLARE _to bigint;;
	DECLARE _distance double;;
    DECLARE cur CURSOR FOR
    	select id1,id2,feature_distance(id1,id2) v from
			(select
				case
					when _post_id < id then _post_id
					when _post_id > id then id
				end id1,
				case
					when _post_id < id then id
					when _post_id > id then _post_id
				end id2
			from
				bb_post
			where _post_id != id) t;;
	DECLARE EXIT HANDLER FOR NOT FOUND SET hasNext = 0;;

	SET hasNext = 1;;
	OPEN cur;;
	WHILE hasNext DO
		FETCH cur INTO _from, _to, _distance;;
		IF NOT EXISTS (select distance from bb_post_distance where `from_post_id`=_from and `to_post_id`=_to) THEN
			insert into bb_post_distance (`from_post_id`,`to_post_id`) values (_from,_to);;
		END IF;;
		update bb_post_distance set distance=_distance where `from_post_id`=_from and `to_post_id`=_to;;
	END WHILE;;
	CLOSE cur;;
END;


-- 全ての掲示の距離を計算して格納する
DROP PROCEDURE IF EXISTS CalculatePostDistances;
CREATE PROCEDURE CalculatePostDistances()
BEGIN
	DECLARE hasNext int;;
	DECLARE _from bigint;;
	DECLARE _to bigint;;
	DECLARE _distance double;;
    DECLARE cur CURSOR FOR
		select t1.id,t2.id,feature_distance(t1.id,t2.id) from
			(select id from bb_post) t1
		join
			(select id from bb_post) t2
		where t1.id < t2.id;;
	DECLARE EXIT HANDLER FOR NOT FOUND SET hasNext = 0;;

	SET hasNext = 1;;
	OPEN cur;;
	WHILE hasNext DO
		FETCH cur INTO _from, _to, _distance;;
		IF NOT EXISTS (select distance from bb_post_distance where `from_post_id`=_from and `to_post_id`=_to) THEN
			insert into bb_post_distance (`from_post_id`,`to_post_id`) values (_from,_to);;
		END IF;;
		update bb_post_distance set distance=_distance where `from_post_id`=_from and `to_post_id`=_to;;
	END WHILE;;
	CLOSE cur;;
END;


-- ユーザ1人についてその閲覧履歴を基に掲示クラスを定め、学習データを準備する
DROP PROCEDURE IF EXISTS PrepareTrainDataFor;
CREATE PROCEDURE PrepareTrainDataFor(IN _nitech_user_id BIGINT, IN threshold DOUBLE)
BEGIN
	DECLARE hasNext int;;
	DECLARE _post_id bigint;;
	DECLARE _v double;;
    DECLARE cur CURSOR FOR
		select 
			post_id, (c - avg) / std v
				from
				    (select t1.post_id, t1.c, t2.avg, t2.std from
						(select post_id, c from
				        	(select t1.post_id, if(t2.n is null, 0, t2.n) c from
				        		(select post_id from bb_possession where nitech_user_id=_nitech_user_id) t1 left join
				    			(select post_id, count(id) n from bb_history where nitech_user_id=_nitech_user_id group by post_id) t2
				    		ON t1.post_id = t2.post_id) t) t1
					join 
				    	(select avg(n) avg, std(n) std from
				        	(select t1.post_id, if(t2.n is null, 0, t2.n) n from
				        		(select post_id from bb_possession where nitech_user_id=_nitech_user_id) t1 left join
				    			(select post_id, count(id) n from bb_history where nitech_user_id=_nitech_user_id group by post_id) t2
				    		ON t1.post_id = t2.post_id) t) t2) t;;
	DECLARE EXIT HANDLER FOR NOT FOUND SET hasNext = 0;;

	SET hasNext = 1;;
	OPEN cur;;
	WHILE hasNext DO
		FETCH cur INTO _post_id, _v;;
		IF _v >= threshold THEN
			update bb_possession set `class` = 1 where nitech_user_id=_nitech_user_id and post_id=_post_id;;
		ELSE
			update bb_possession set `class` = 0 where nitech_user_id=_nitech_user_id and post_id=_post_id;;
		END IF;;
	END WHILE;;
	CLOSE cur;;
END;


-- 全てのユーザについてその閲覧履歴を基に掲示クラスを定め、学習データを準備する
DROP PROCEDURE IF EXISTS PrepareTrainData;
CREATE PROCEDURE PrepareTrainData(IN threshold DOUBLE)
BEGIN
	DECLARE hasNext int;;
	DECLARE _nitech_user_id bigint;;
    DECLARE cur CURSOR FOR
    	select id from nitech_user;;
	DECLARE EXIT HANDLER FOR NOT FOUND SET hasNext = 0;;

	SET hasNext = 1;;
	OPEN cur;;
	WHILE hasNext DO
		FETCH cur INTO _nitech_user_id;;
		call PrepareTrainDataFor(_nitech_user_id, threshold);;
		update bb_possession set `class` = 1 where nitech_user_id=_nitech_user_id and is_favorite=1;;
	END WHILE;;
	CLOSE cur;;
END;


-- ユーザ1人について、その閲覧履歴から掲示のクラス分類を学習する
DROP PROCEDURE IF EXISTS TrainFor;
CREATE PROCEDURE TrainFor(IN _nitech_user_id BIGINT, IN _class TINYINT)
BEGIN
	DECLARE hasNext int;;
	DECLARE _n bigint;;
	DECLARE _cluster_id bigint;;
	DECLARE _word_id bigint;;
	DECLARE _v double;;
    DECLARE cur CURSOR FOR
--		select t1.id,
--			case
--				when t2.n <= 0 then (select if(n=0,0.5,1/n)  from (select count(`post_id`) n from `bb_history` where `nitech_user_id` = _nitech_user_id) t)
--				else t1.v/t2.n
--			end v
--		from
--			(select t1.id, if(t2.v is null, 1, 1+t2.v) v from
--				`bb_word` t1
--			left join
--				(select t2.`word_id`, sum(t1.n*t2.value) v from
--					(select t1.`post_id` `post_id`, t2.n n from
--						(select `post_id` from `bb_possession` where `nitech_user_id`=_nitech_user_id and `class`=_class) t1
--					join
--						(select count(id) n, `post_id` from `bb_history` where `nitech_user_id`=_nitech_user_id group by `post_id`) t2
--					on t1.`post_id`=t2.`post_id`) t1
--				join
--					`bb_word_in_post` t2
--				on t1.`post_id`=t2.`post_id`
--				group by t2.word_id) t2
--			on t1.id=t2.`word_id`) t1
--		join
--			(select 2*count(t1.`post_id`) n from
--				(select `post_id` from `bb_possession` where `nitech_user_id` = _nitech_user_id and `class`=_class) t1
--			join
--				(select `post_id` from `bb_history` where `nitech_user_id` = _nitech_user_id) t2
--			on t1.`post_id` = t2.`post_id`) t2;;
	select
		t1.id, if(t2.v is null, 1, 1+t2.v) v
	from
		`bb_word` t1
	left join
		(select t2.`word_id`, sum(t2.`value`) v from
			(select `post_id` from `bb_possession` where `nitech_user_id`=_nitech_user_id and `class`=_class) t1
		join
			`bb_word_in_post` t2
		on t1.`post_id`=t2.`post_id`
		group by t2.word_id) t2
	on t1.id=t2.`word_id`;;
	DECLARE EXIT HANDLER FOR NOT FOUND SET hasNext = 0;;
	
--	ユーザクラスタが存在しなければ作成する
	IF NOT EXISTS (select id from bb_user_cluster where `nitech_user_id`=_nitech_user_id) THEN
		insert ignore into bb_user_cluster (`nitech_user_id`,`depth`,`weight`,`parent_id`) values (_nitech_user_id,0,1,null);;
	END IF;;
	
--	ユーザクラスタの事前確率 p(c) を更新する
	IF _class >= 1 THEN
		update bb_user_cluster set
			prior_1 = (select count(post_id) from bb_possession where nitech_user_id=_nitech_user_id and `class`=1)
					 /(select count(post_id) from bb_possession where nitech_user_id=_nitech_user_id)
		where nitech_user_id=_nitech_user_id;;
	ELSE
		update bb_user_cluster set
			prior_0 = (select count(post_id) from bb_possession where nitech_user_id=_nitech_user_id and `class`=0)
					 /(select count(post_id) from bb_possession where nitech_user_id=_nitech_user_id)
		where nitech_user_id=_nitech_user_id;;
	END IF;;
	
--	条件付き確率 p(w|c) の計算に必要な分母の値を求める
	select
		count(t2.`value`) n
	from
		(select
			post_id
		from `bb_possession`
		where
			`nitech_user_id`=_nitech_user_id
			and
			`class`=_class
		) t1
	join
		`bb_word_in_post` t2
	on t1.`post_id`=t2.`post_id`
	into _n;;
	
--	各単語について条件付き確率 p(w|c) を更新し、ユーザベクトルを更新する
	SET hasNext = 1;;
	OPEN cur;;
	WHILE hasNext DO
		FETCH cur INTO _word_id, _v;;
		select id from bb_user_cluster where `nitech_user_id`=_nitech_user_id into _cluster_id;;
		IF NOT EXISTS (select `value` from bb_user_cluster_vector where `cluster_id`=_cluster_id and `class`=_class and `word_id`=_word_id) THEN
			insert into bb_user_cluster_vector (`cluster_id`,`class`,`word_id`,`value`) values (_cluster_id,_class,_word_id,null);;
		END IF;;
		update bb_user_cluster_vector set `value` = _v/_n where `cluster_id`=_cluster_id and `class`=_class and `word_id`=_word_id;;
	END WHILE;;
	CLOSE cur;;
END;


-- すべてのユーザについて掲示の閲覧履歴からクラス分類を学習する
DROP PROCEDURE IF EXISTS Train;
CREATE PROCEDURE Train()
BEGIN
	DECLARE hasNext int;;
	DECLARE _nitech_user_id bigint;;
    DECLARE cur CURSOR FOR
    	select id from nitech_user;;
	DECLARE EXIT HANDLER FOR NOT FOUND SET hasNext = 0;;

	SET hasNext = 1;;
	OPEN cur;;
	WHILE hasNext DO
		FETCH cur INTO _nitech_user_id;;
		call TrainFor(_nitech_user_id, 1);;
		call TrainFor(_nitech_user_id, 0);;
	END WHILE;;
	CLOSE cur;;
END;


-- ユーザ別の事前確率(の log)を返す
DROP FUNCTION IF EXISTS p_of_class;
CREATE FUNCTION p_of_class(_cluster_id bigint, _class tinyint) RETURNS DOUBLE
BEGIN
	IF _class >= 1 THEN
		RETURN
			(select log(prior_1) from bb_user_cluster where id=_cluster_id);;
	ELSE
		RETURN
			(select log(prior_0) from bb_user_cluster where id=_cluster_id);;
	END IF;;
END;


-- クラスタ別の条件付き確率(の log)を返す
DROP FUNCTION IF EXISTS p_of_words_given_class;
CREATE FUNCTION p_of_words_given_class(_cluster_id bigint, _post_id bigint, _class tinyint) RETURNS DOUBLE
BEGIN
	RETURN
		(select sum(p) from
			(select
				case
					when v  = 0 then log(1-t2.`value`)
					when 1 <= v then log(t2.`value`)
				end p
			from
				(select t1.id word_id, if(t2.`value` is null, 0, t2.value) v from
					bb_word t1
				left join
					(select word_id,`value` from bb_word_in_post where post_id=_post_id) t2
				on t1.id=t2.word_id) t1
			join
				(select word_id,`value` from bb_user_cluster_vector where cluster_id = _cluster_id and class=_class) t2
			on t1.word_id=t2.word_id) t);;
END;


-- ユーザ、クラスタごとの事後確率を返す
DROP FUNCTION IF EXISTS p_of_class_given_words;
CREATE FUNCTION p_of_class_given_words(_cluster_id bigint, _post_id bigint, _class int) RETURNS DOUBLE
BEGIN
	RETURN (select p_of_class(_cluster_id, _class)+p_of_words_given_class(_cluster_id, _post_id, _class));;
END;


-- 掲示のクラスを推定する（再帰用）
DROP PROCEDURE IF EXISTS EstimateFor;
CREATE PROCEDURE EstimateFor(IN _nitech_user_id BIGINT, IN _cluster_id BIGINT, IN _post_id BIGINT)
BEGIN
	DECLARE _depth bigint;;
	DECLARE _parent_cluster_id bigint;;
	DECLARE _v double;;
	select depth,parent_id from bb_user_cluster where id=_cluster_id into _depth, _parent_cluster_id;;
	
	IF NOT EXISTS (select `class` from bb_estimation where nitech_user_id=_nitech_user_id and depth=_depth and post_id=_post_id) THEN
		insert into bb_estimation (nitech_user_id,depth,post_id,class,liklihood) values (_nitech_user_id,_depth,_post_id,null,null);;
	END IF;;
	
	select p_of_class_given_words(_cluster_id,_post_id,1) - p_of_class_given_words(_cluster_id,_post_id,0) into _v;;
	IF _v >= 0 THEN
--		クラス 1
		update bb_estimation set class=1, liklihood= _v where nitech_user_id=_nitech_user_id and depth=_depth and post_id=_post_id;;
	ELSEIF _depth = 0 THEN
--		クラス 0
		update bb_estimation set class=0, liklihood=-_v where nitech_user_id=_nitech_user_id and depth=_depth and post_id=_post_id;;
	END IF;;
	
	IF _parent_cluster_id IS NOT NULL AND _cluster_id != _parent_cluster_id THEN
		call EstimateFor(_nitech_user_id, _parent_cluster_id, _post_id);;
	END IF;;
END;


-- 掲示のクラスを推定する
DROP PROCEDURE IF EXISTS Estimate;
CREATE PROCEDURE Estimate(IN _nitech_user_id BIGINT, IN _post_id BIGINT)
BEGIN
	DECLARE _cluster_id bigint;;
	select id from bb_user_cluster where nitech_user_id=_nitech_user_id into _cluster_id;; 
	SET `max_sp_recursion_depth` = 255;;
	
	call EstimateFor(_nitech_user_id, _cluster_id, _post_id);;
END;


# --- !Downs
SET FOREIGN_KEY_CHECKS=0;

DROP FUNCTION IF EXISTS feature_multiply;
DROP FUNCTION IF EXISTS feature_length;
DROP FUNCTION IF EXISTS feature_cos;
DROP FUNCTION IF EXISTS feature_distance;
DROP PROCEDURE IF EXISTS debugMsg;
DROP PROCEDURE IF EXISTS CalculatePostDistance;
DROP PROCEDURE IF EXISTS CalculatePostDistances;
DROP PROCEDURE IF EXISTS PrepareTrainDataFor;
DROP PROCEDURE IF EXISTS PrepareTrainData;
DROP PROCEDURE IF EXISTS TrainFor;
DROP PROCEDURE IF EXISTS Train;
DROP FUNCTION IF EXISTS p_of_class;
DROP FUNCTION IF EXISTS p_of_words_given_class;
DROP FUNCTION IF EXISTS p_of_class_given_words;
DROP PROCEDURE IF EXISTS EstimateFor;
DROP PROCEDURE IF EXISTS Estimate;

SET FOREIGN_KEY_CHECKS=1;
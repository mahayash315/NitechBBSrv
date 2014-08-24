# --- !Ups

CREATE PROCEDURE debugMsg(msg VARCHAR(255))
BEGIN
    select concat("** ", msg) AS '** DEBUG:';;
END;

CREATE PROCEDURE PrepareTrainDataFor(IN _nitech_user_id BIGINT, IN threshold DOUBLE)
BEGIN
	DECLARE hasNext int;;
	DECLARE _post_id bigint;;
	DECLARE _v double;;
    DECLARE cur CURSOR FOR
		select 
		    post_id, (c - avg) / (std * std) v
		from
		    (select t1.post_id, t1.c, t2.avg, t2.std from
		    	(select post_id,count(id) c from
		    		(select id,post_id from bb_history where nitech_user_id=_nitech_user_id) t
		    	group by post_id) t1 join 
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
			update bb_possession set `is_interesting` = 1 where nitech_user_id=_nitech_user_id and post_id=_post_id;;
		END IF;;
	END WHILE;;
	CLOSE cur;;
END;

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
	END WHILE;;
	CLOSE cur;;
END;


CREATE PROCEDURE TrainFor(IN _nitech_user_id BIGINT, IN _is_interesting TINYINT)
BEGIN
	DECLARE hasNext int;;
	DECLARE _cluster_id bigint;;
	DECLARE _word_id bigint;;
	DECLARE _v double;;
    DECLARE cur CURSOR FOR
		select 
		    t1.word_id, if(t1.sum is null, 0, t1.sum/t2.n) v
		from
		    (select t1.id word_id, t2.sum sum from bb_word t1 left join
		        (select t2.word_id, sum(t2.value) sum from
		        	(select post_id from bb_possession where nitech_user_id = _nitech_user_id and is_interesting = _is_interesting) t1
		    	join bb_word_in_post t2 ON t1.post_id = t2.post_id group by t2.word_id) t2
		    	ON t1.id = t2.word_id) t1
		join
	    	(select count(t1.post_id) as n from
	        	(select post_id from bb_possession where nitech_user_id = _nitech_user_id and is_interesting = _is_interesting) t1
	   		join
	    		(select post_id from bb_history where nitech_user_id = _nitech_user_id) t2
	    	ON t1.post_id = t2.post_id) t2;;
	DECLARE EXIT HANDLER FOR NOT FOUND SET hasNext = 0;;

	SET hasNext = 1;;
	OPEN cur;;
	WHILE hasNext DO
		FETCH cur INTO _word_id, _v;;
		IF NOT EXISTS (select id from bb_user_cluster where nitech_user_id=_nitech_user_id) THEN
			insert ignore into bb_user_cluster (nitech_user_id,depth,parent_id) values (_nitech_user_id,0,null);;
			select last_insert_id() into _cluster_id;;
		ELSE 
			select id from bb_user_cluster where nitech_user_id=_nitech_user_id into _cluster_id;;
		END IF;;
		insert into bb_user_cluster_vector (cluster_id,word_id,value) values (_cluster_id,_word_id,_v)
			on duplicate key update value = _v;;
	END WHILE;;
	CLOSE cur;;
END;


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

CREATE FUNCTION feature_multiply(_cluster_id1 bigint, _cluster_id2 bigint) RETURNS DOUBLE
BEGIN
	IF (select count(id) from bb_user_cluster_vector where cluster_id=_cluster_id1)
		= (select count(id) from bb_user_cluster_vector where cluster_id=_cluster_id2) THEN
		return
			(select sum(v) from
				(select v1.v*v2.v v from
					(select `word_id`,`value` v from bb_user_cluster_vector where cluster_id =_cluster_id1) v1
					join
					(select `word_id`,`value` v from bb_user_cluster_vector where cluster_id =_cluster_id2) v2
					on v1.`word_id`=v2.`word_id`) t);;
	END IF;;
	RETURN 0;;
END;

CREATE FUNCTION feature_length(_cluster_id bigint) RETURNS DOUBLE
BEGIN
	RETURN
		(select sqrt(sum) length from
			(select sum(v) sum from
				(select POW(`value`,2) v from bb_user_cluster_vector where cluster_id=_cluster_id) t) t);;
END;

CREATE FUNCTION feature_distance(_cluster_id1 bigint, _cluster_id2 bigint) RETURNS DOUBLE
BEGIN
	RETURN (select feature_multiply(_cluster_id1,_cluster_id2) / feature_length(_cluster_id1)*feature_length(_cluster_id2));;
END;

CREATE PROCEDURE KMeans()
BEGIN
	
END;

# --- !Downs
SET FOREIGN_KEY_CHECKS=0;

DROP PROCEDURE IF EXISTS debugMsg;
DROP PROCEDURE IF EXISTS PrepareTrainDataFor;
DROP PROCEDURE IF EXISTS PrepareTrainData;
DROP PROCEDURE IF EXISTS TrainFor;
DROP PROCEDURE IF EXISTS Train;

DROP FUNCTION IF EXISTS feature_multiply;
DROP FUNCTION IF EXISTS feature_length;
DROP FUNCTION IF EXISTS feature_distance;
DROP PROCEDURE IF EXISTS KMeans;

SET FOREIGN_KEY_CHECKS=1;
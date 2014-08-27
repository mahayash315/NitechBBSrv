# --- !Ups

CREATE FUNCTION cluster_vector_multiply(_cluster_id1 bigint, _cluster_id2 bigint) RETURNS DOUBLE
BEGIN
	IF (select count(`value`) from bb_user_cluster_vector where cluster_id=_cluster_id1)
		= (select count(`value`) from bb_user_cluster_vector where cluster_id=_cluster_id2) THEN
		return
			(select sum(v) from
				(select v1.v*v2.v v from
					(select `class`,`word_id`,`value` v from bb_user_cluster_vector where cluster_id =_cluster_id1) v1
					join
					(select `class`,`word_id`,`value` v from bb_user_cluster_vector where cluster_id =_cluster_id2) v2
					on v1.`class`=v2.`class` and v1.`word_id`=v2.`word_id`) t);;
	END IF;;
	RETURN 0;;
END;


CREATE FUNCTION cluster_vector_length( _cluster_id bigint) RETURNS DOUBLE
BEGIN
	RETURN
		(select sqrt(sum) length from
			(select sum(v) sum from
				(select `value`*`value` v from bb_user_cluster_vector where cluster_id=_cluster_id) t) t);;
END;


CREATE FUNCTION cluster_vector_cos(_cluster_id1 bigint, _cluster_id2 bigint) RETURNS DOUBLE
BEGIN
	RETURN (select cluster_vector_multiply(_cluster_id1,_cluster_id2) / (cluster_vector_length(_cluster_id1)*cluster_vector_length(_cluster_id2)));;
END;


CREATE FUNCTION cluster_vector_distance(_cluster_id1 bigint, _cluster_id2 bigint) RETURNS DOUBLE
BEGIN
	RETURN (select 1-cluster_vector_cos(_cluster_id1,_cluster_id2));;
END;


-- 親クラスタへ weight の修正を伝搬
CREATE PROCEDURE PropagateClusterWeight(IN _parent_cluster_id BIGINT, IN _diff DOUBLE)
BEGIN
	DECLARE _id bigint;;
	update bb_user_cluster set weight=weight+_diff where id=_parent_cluster_id;;
	select parent_id from bb_user_cluster where id=_parent_cluster_id into _id;; 
	IF _id IS NOT NULL THEN
		call PropagateClusterWeight(_id, _diff);;
	END IF;;
END;


-- クラスタの中心ベクトルを取得する操作
CREATE PROCEDURE GetClusterFeature(IN _cluster_id BIGINT)
BEGIN
	select `class`,`word_id`,`value` from bb_user_cluster_vector where cluster_id=_cluster_id;;
END;


-- クラスタの中心ベクトルを更新する操作
CREATE PROCEDURE UpdateClusterFeature(IN _cluster_id BIGINT)
BEGIN
	DECLARE hasNext int;;
	DECLARE _class int;;
	DECLARE _word_id bigint;;
	DECLARE _v double;;
	DECLARE cur CURSOR FOR
		select t1.`class` `class`, t1.`word_id` `word_id`, t1.`sum`/t2.`n` `v` from
			(select `class`,`word_id`,sum(`value`*`weight`) `sum` from
				(select t1.`class` `class`, t1.`word_id` `word_id`, t1.`value` `value`, t2.`weight` `weight` from
					(select `cluster_id`,`class`,`word_id`,`value` from bb_user_cluster_vector
					where cluster_id in (select id from bb_user_cluster where parent_id = _cluster_id)) t1
				join
					(select id,weight from bb_user_cluster where parent_id=_cluster_id) t2
				on t1.`cluster_id`=t2.`id`) t
			group by `class`, `word_id`) t1
		join
			(select sum(weight) `n` from bb_user_cluster where parent_id=_cluster_id) t2;;
	DECLARE EXIT HANDLER FOR NOT FOUND SET hasNext = 0;;
	
	IF (select depth from bb_user_cluster where id=_cluster_id) > 0 THEN
		SET hasNext = 1;;
		OPEN cur;;
		WHILE hasNext DO
			FETCH cur INTO _class, _word_id, _v;;
			update bb_user_cluster_vector set `value`=_v where cluster_id=_cluster_id and `class`=_class and word_id=_word_id;;
		END WHILE;;
		CLOSE cur;;
	END IF;;
END;


-- クラスタの親クラスタを設定する操作
CREATE PROCEDURE SetParentCluster(IN _child_cluster_id BIGINT, IN _parent_cluster_id BIGINT)
BEGIN
	DECLARE _id bigint;;
	DECLARE _w double;;
	select weight,parent_id from bb_user_cluster where id=_child_cluster_id into _w,_id;;
	SET `max_sp_recursion_depth` = 255;;
	
--	現在の parent から weight を引く
	IF _id IS NOT NULL THEN
		call PropagateClusterWeight(_id, -_w);;
	END IF;;
	
--	新しい parent に weight を足す
	call PropagateClusterWeight(_parent_cluster_id, _w);;
	
--  親クラスタを設定
	update bb_user_cluster set parent_id = _parent_cluster_id where id=_child_cluster_id;;

END;


--  第 _depth 層に一つ下の層の _child_cluster_id を基とする親クラスタを作成する操作
CREATE PROCEDURE CreateParentCluster(IN _depth INT, IN _child_cluster_id BIGINT)
BEGIN
	DECLARE hasNext int;;
	DECLARE _parent_cluster_id bigint;;
	DECLARE _class int;;
	DECLARE _word_id bigint;;
	DECLARE _value double;;
	DECLARE cur CURSOR FOR
		select `class`,`word_id`,`value` from bb_user_cluster_vector where cluster_id=_child_cluster_id;;
	DECLARE EXIT HANDLER FOR NOT FOUND SET hasNext = 0;;
	
	insert into bb_user_cluster (depth,weight) values (_depth,0);;
	select last_insert_id() into _parent_cluster_id;;
	
	SET hasNext=1;;
	OPEN cur;;
	WHILE hasNext DO
		FETCH cur INTO _class,_word_id,_value;;
		insert into bb_user_cluster_vector (`cluster_id`,`class`,`word_id`,`value`) values (_parent_cluster_id,_class,_word_id,_value);;
	END WHILE;;
	CLOSE cur;;
END;


-- クラスタ _cluster_id を削除する
CREATE PROCEDURE DeleteCluster(IN _cluster_id BIGINT)
BEGIN
	DECLARE _w double;;
	DECLARE _parent_cluster_id bigint;;
	delete from bb_user_cluster_vector where cluster_id=_cluster_id;;
	select weight,parent_id from bb_user_cluster where id=_cluster_id into _w,_parent_cluster_id;;
	update bb_user_cluster set parent_id = NULL where parent_id = _cluster_id;; 
	call PropagateClusterWeight(_parent_cluster_id, -_w);;
	delete from bb_user_cluster where id=_cluster_id;;
END;


-- 第 _depth 層のクラスタを全て削除
CREATE PROCEDURE DeleteClustersFor(IN _depth INT)
BEGIN
	DECLARE hasNext int;;
	DECLARE _id bigint;;
	DECLARE cur CURSOR FOR
		select id from bb_user_cluster where depth=_depth;;
	DECLARE EXIT HANDLER FOR NOT FOUND SET hasNext = 0;;
	
	IF 0 < _depth and exists (select id from bb_user_cluster where depth=_depth) THEN
		SET hasNext = 1;;
		OPEN cur;;
		WHILE hasNext DO
			FETCH cur INTO _id;;
			call DeleteCluster(_id);;
		END WHILE;;
		CLOSE cur;;
	END IF;;
END;


-- 第 _depth 層に _k 個の親クラスタを作り、初期化する操作
CREATE PROCEDURE InitKMeansFor(IN _depth INT, IN _k INT)
BEGIN
	DECLARE i int;;
	DECLARE k int;;
	DECLARE _child_cluster_id bigint;;
	DECLARE _d double;;
	
--	既存の第 _depth 層のクラスタを削除
	call DeleteClustersFor(_depth);;
	
--	引数の _k よりも子クラスタが少なければ k をその数にする	
	select min(n) from
		(select count(id) n from bb_user_cluster where depth=(_depth-1) union select _k n) t
	into k;;
	
--	親クラスタ作成
	IF 0 < _depth and 1 <= k THEN
		select id from bb_user_cluster order by rand() limit 1 into _child_cluster_id;;
		call CreateParentCluster(_depth, _child_cluster_id);;
		
		SET i=2;;
		WHILE i <= k DO
			select id2, min(d) mind from
				(select id1, id2, cluster_vector_distance(id1,id2) d from
					(select t1.id id1, t2.id id2 from
						(select id from bb_user_cluster where depth=_depth) t1
					join
						(select id from bb_user_cluster where depth=_depth-1) t2) t) t
			group by id2
			order by mind desc
			limit 1
			into _child_cluster_id, _d;;
			call CreateParentCluster(_depth, _child_cluster_id);;
			SET i=i+1;;
		END WHILE;;
	END IF;;
END;


-- 第 depth-1 層の子クラスタを depth 層の親クラスタに分類
CREATE PROCEDURE ClassifyClustersFor(IN _depth INT)
BEGIN
	DECLARE hasNext int;;
	DECLARE changed bigint;;
	DECLARE _child_cluster_id bigint;;
	DECLARE _parent_cluster_id1 bigint;;
	DECLARE _parent_cluster_id2 bigint;;
	DECLARE _d double;;
	DECLARE cur CURSOR FOR
		select id,parent_id from bb_user_cluster where depth=_depth-1;;
	DECLARE EXIT HANDLER FOR NOT FOUND SET hasNext = 0;;
	
	IF 0 < _depth THEN
		REPEAT
			SET changed = 0;;
			
			SET hasNext = 1;;
			OPEN cur;;
			WHILE hasNext DO
				FETCH cur INTO _child_cluster_id, _parent_cluster_id1;;
--				自分から一番近い親クラスタを見つける
				select id, cluster_vector_distance(_child_cluster_id, id) d from
					(select id from bb_user_cluster where depth=_depth) t
				order by d asc
				limit 1
				into _parent_cluster_id2, _d;;
--				見つかった最近傍の親クラスタが今の親クラスタでなければ変更
				IF _parent_cluster_id1 IS NULL OR _parent_cluster_id1 != _parent_cluster_id2 THEN
					call SetParentCluster(_child_cluster_id, _parent_cluster_id2);;
					SET changed=changed+1;;
					call UpdateClusterFeature(_parent_cluster_id1);;
					call UpdateClusterFeature(_parent_cluster_id2);;
				END IF;;
			END WHILE;;
			CLOSE cur;;
		UNTIL changed <= 0
		END REPEAT;;
	END IF;;
END;


CREATE PROCEDURE ClassifyClusters()
BEGIN
	DECLARE hasNext int;;
	DECLARE _depth int;;
	DECLARE cur CURSOR FOR
		select distinct depth from bb_user_cluster order by depth asc;;
	DECLARE EXIT HANDLER FOR NOT FOUND SET hasNext = 0;;
	
	SET hasNext = 1;;
	OPEN cur;;
	WHILE hasNext DO
		FETCH cur INTO _depth;;
		call ClassifyClustersFor(_depth);;
	END WHILE;;
	CLOSE cur;;
END;

# --- !Downs
SET FOREIGN_KEY_CHECKS=0;

DROP FUNCTION IF EXISTS cluster_vector_multiply;
DROP FUNCTION IF EXISTS cluster_vector_length;
DROP FUNCTION IF EXISTS cluster_vector_cos;
DROP FUNCTION IF EXISTS cluster_vector_distance;
DROP PROCEDURE IF EXISTS PropagateClusterWeight;
DROP PROCEDURE IF EXISTS GetClusterFeature;
DROP PROCEDURE IF EXISTS UpdateClusterFeature;
DROP PROCEDURE IF EXISTS SetParentCluster;
DROP PROCEDURE IF EXISTS CreateParentCluster;
DROP PROCEDURE IF EXISTS DeleteCluster;
DROP PROCEDURE IF EXISTS DeleteClustersFor;
DROP PROCEDURE IF EXISTS InitKMeansFor;
DROP PROCEDURE IF EXISTS InitKMeans;
DROP PROCEDURE IF EXISTS ClassifyClustersFor;
DROP PROCEDURE IF EXISTS ClassifyClusters;

SET FOREIGN_KEY_CHECKS=1;
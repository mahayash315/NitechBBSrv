package models.service.model.bb;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Table;

import models.entity.NitechUser;
import models.entity.bb.Post;
import models.entity.bb.PostDistance;
import models.entity.bb.Word;
import models.service.model.ModelService;
import models.setting.api.bb.BBSetting;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Expr;
import com.avaje.ebean.Query;
import com.avaje.ebean.RawSql;
import com.avaje.ebean.RawSqlBuilder;

public class PostModelService implements ModelService<Long, Post> {
	
	private static final String SQL_SELECT_POPULAR_POSTS =
				"select " +
				"t1.id, t1.id_date, t1.id_index, t1.author, t1.title, t1.last_modified, sum(t2.post_id) n" +
				"from post t1 " +
				"join " +
				"(select post_id,last_modified from history " +
				" where last_modified > :minTimeout) t2 " +
				"on t1.id=t2.post_id" +
				"group by t1.id ";
	
	@Table(name="popular_post")
	protected class PopularPost {
		Post post;
		Long popularity;
	}

	@Override
	public Post findById(Long id) {
		if (id != null) {
			return Post.find.byId(id);
		}
		return null;
	}

	@Override
	public Post save(Post entry) {
		if (entry != null) {
			Ebean.save(entry);
			if (entry.getId() != null) {
				return entry;
			}
		}
		return null;
	}

	@Override
	public void delete(Post entry) {
		if (entry != null) {
			Ebean.delete(entry);
		}
	}
	
	/**
	 * idDate と idIndex で行を見つける
	 * @param idDate
	 * @param idIndex
	 * @return
	 */
	public Post findByIdDateIdIndex(String idDate, int idIndex) { 
		return Post.find.where().eq("idDate", idDate).eq("idIndex", idIndex).findUnique();
	}
	
	public List<Post> findList() {
		return Post.find.findList();
	}
	
	public List<Word> findWordsInPost(Post post) {
		if (post == null) {
			return null;
		}
		return Word.find.where().eq("posts.post", post).findList();
	}
	
	/**
	 * 人気掲示のエントリとその人気度(popularity)を返す
	 * @param nitechUser
	 * @param threshold
	 * @param limit
	 * @return
	 */
	public Map<Post,Long> findPopularPosts(NitechUser nitechUser, Long threshold, Integer limit) {
		if (nitechUser == null) {
			return null;
		}
		if (threshold == null) {
			threshold = BBSetting.POPULAR_POSTS_POPULARITY_THRESHOLD;
		}
		
		Map<Post,Long> map = new HashMap<Post,Long>();
		
		long minTimestamp = System.currentTimeMillis() - BBSetting.POPULAR_POSTS_DELTA_TIMESTAMP;
		Query<PopularPost> query = Ebean.createQuery(PopularPost.class);
		query.setRawSql(
			RawSqlBuilder.parse(SQL_SELECT_POPULAR_POSTS)
						 .columnMapping("t1.id", "popular_post.post.id")
						 .columnMapping("t1.id_date",  "popular_post.post.id_date")
						 .columnMapping("t1.id_index", "popular_post.post.id_index")
						 .columnMapping("t1.title", "popular_post.post.title")
						 .columnMapping("t1.author", "popular_post.post.author")
						 .columnMapping("t1.last_modified", "popular_post.post.last_modified")
						 .create()
		);
		query.setParameter("minTimestamp", minTimestamp);
		
		if (threshold != null) {
			query.having()
				 .ge("n", threshold);
		}
		if (limit != null) {
			query.setMaxRows(limit);
		}
		List<PopularPost> list = query.findList();
		for (PopularPost item : list) {
			map.put(item.post, item.popularity);
		}
		
		return map;
	}
	
	/**
	 * 関連掲示のエントリと関連度(likelihood)を返す
	 * @param post
	 * @param threshold
	 * @param limit
	 * @return
	 */
	public Map<Post,Double> findRelevants(Post post, Double threshold, Integer limit) {
		if (post == null) {
			return null;
		}
		if (threshold == null) {
			threshold = BBSetting.RELEVANT_POST_DISTANCE_THRESHOLD;
		};
		
		Map<Post,Double> map = new LinkedHashMap<Post, Double>();
		Query<PostDistance> q = PostDistance.find
			.where()
				.or(Expr.eq("fromPost", post), Expr.eq("toPost", post))
				.add(Expr.isNotNull("distance"))
				.add(Expr.le("distance", threshold))
			.order("distance asc");
		if (limit != null && 0 <= limit) {
			q.setMaxRows(limit);
		}
		List<PostDistance> list	= q.findList();
		
		for (PostDistance e : list) {
			if (e.getFromPost().equals(post)) {
				map.put(e.getToPost(), e.getDistance());
			} else {
				map.put(e.getFromPost(), e.getDistance());
			}
		}
		
		return map;
	}
	
}

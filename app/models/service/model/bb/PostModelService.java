package models.service.model.bb;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import models.entity.bb.Post;
import models.entity.bb.PostDistance;
import models.entity.bb.Word;
import models.service.model.ModelService;
import models.setting.api.bb.BBSetting;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Expr;

public class PostModelService implements ModelService<Long, Post> {
	
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
	
	public Map<Post,Double> findRelevants(Post post) {
		if (post == null) {
			return null;
		}
		
		Map<Post,Double> map = new LinkedHashMap<Post, Double>();
		List<PostDistance> list = PostDistance.find
			.where()
				.or(Expr.eq("fromPost", post), Expr.eq("toPost", post))
				.add(Expr.isNotNull("distance"))
				.add(Expr.le("distance", BBSetting.RELEVANT_POST_DISTANCE_THRESHOLD))
			.order("distance asc")
			.setMaxRows(BBSetting.RELEVANT_POST_MAX_ROW_NUM)
			.findList();
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

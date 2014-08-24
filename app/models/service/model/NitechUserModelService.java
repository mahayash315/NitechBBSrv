package models.service.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.entity.NitechUser;
import models.entity.bb.Post;
import models.entity.bb.Word;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.SqlRow;

public class NitechUserModelService implements ModelService<Long, NitechUser> {
	
	private static final String SQL_GET_FEATURE_VECTOR = 
			 "select t3.word_id word_id ,sum(t3.value)/n value from"+
			 " (select id,nitech_user_id,post_id from bb_history where nitech_user_id=? and post_id in (?)) t1"+
			 " join (select count(id) n from bb_history where nitech_user_id=? and post_id in (?)) t2"+
			 " join bb_word_in_post t3 on t1.post_id=t3.post_id"+
			 " group by t3.word_id";
	
	@Override
	public NitechUser findById(Long id) {
		if (id != null) {
			return NitechUser.find.byId(id);
		}
		return null;
	}

	@Override
	public NitechUser save(NitechUser entry) {
		if (entry != null) {
			Ebean.save(entry);
			if (entry.getId() != null) {
				return entry;
			}
		}
		return null;
	}

	@Override
	public void delete(NitechUser entry) {
		if (entry != null) {
			Ebean.delete(entry);
		}
	}

	
	public NitechUser findByHashedId(String hashedId) {
		if (hashedId != null) {
			return NitechUser.find.where().eq("hashedId", hashedId).findUnique();
		}
		return null;
	}
	
	public List<Post> findPossessingPosts(NitechUser nitechUser) {
		if (nitechUser != null) {
			return Post.find.fetch("possessions.nitechUser").where().eq("possessions.nitechUser", nitechUser).findList();
		}
		return null;
	}
	
	public Map<Word,Double> getFeatureVector(NitechUser nitechUser, List<Long> postIds) {
		if (nitechUser != null) {
			List<SqlRow> list = Ebean.createSqlQuery(SQL_GET_FEATURE_VECTOR)
				.setParameter(1, nitechUser.getId())
				.setParameter(2, postIds)
				.setParameter(3, nitechUser.getId())
				.setParameter(4, postIds)
				.findList();

			HashMap<Word,Double> vector = new HashMap<Word,Double>();
			for (SqlRow row : list) {
				vector.put(new Word(row.getLong("word_id")), row.getDouble("value"));
			}
			return vector;
		}
		return null;
	}
}

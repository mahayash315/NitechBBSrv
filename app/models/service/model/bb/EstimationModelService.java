package models.service.model.bb;

import java.util.List;

import models.entity.NitechUser;
import models.entity.bb.Estimation;
import models.entity.bb.Estimation.PK;
import models.service.model.ModelService;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.RawSql;
import com.avaje.ebean.RawSqlBuilder;

public class EstimationModelService implements
		ModelService<Estimation.PK, Estimation> {
	
	private static RawSql SQL_FIND_SUGGESTIONS = RawSqlBuilder.parse(
			" select nitech_user_id, depth, post_id, class, liklihood " +
			" from bb_estimation " +
			" where nitech_user_id=? and class=1 " +
			" group by post_id " +
			" order by liklihood desc ")
			.columnMapping("nitech_user_id", "id.nitechUserId")
			.columnMapping("depth", "id.depth")
			.columnMapping("post_id", "id.postId")
			.columnMapping("class", "clazz")
			.columnMapping("liklihood", "likelihood")
			.create();

	@Override
	public Estimation findById(PK id) {
		if (id != null) {
			return Estimation.find.byId(id);
		}
		return null;
	}

	@Override
	public Estimation save(Estimation entry) {
		if (entry != null) {
			Ebean.save(entry);
			if (entry.getId() != null) {
				return entry;
			}
		}
		return null;
	}

	@Override
	public void delete(Estimation entry) {
		if (entry != null) {
			Ebean.delete(entry);
		}
	}
	
	/**
	 * nitechUser に対するおすすめ掲示を返す
	 * @param nitechUser
	 * @return
	 */
	public List<Estimation> findSuggestions(NitechUser nitechUser) {
		if (nitechUser == null) {
			return null;
		}
		
		return Estimation.find
						 .setRawSql(SQL_FIND_SUGGESTIONS)
						 .setParameter(1, nitechUser.getId())
						 .fetch("post")
						 .findList();
	}

}

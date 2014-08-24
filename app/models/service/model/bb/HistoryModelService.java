package models.service.model.bb;

import java.util.List;

import models.entity.NitechUser;
import models.entity.bb.History;
import models.entity.bb.Post;
import models.service.model.ModelService;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.ExpressionList;

public class HistoryModelService implements ModelService<Long, History> {
	
	@Override
	public History findById(Long id) {
		if (id != null) {
			return History.find.byId(id);
		}
		return null;
	}

	@Override
	public History save(History entry) {
		if (entry != null) {
			Ebean.save(entry);
			if (entry.getId() != null) {
				return entry;
			}
		}
		return null;
	}

	@Override
	public void delete(History entry) {
		if (entry != null) {
			Ebean.delete(entry);
		}
	}


	/**
	 * リストを返す
	 * @param nitechUser
	 * @param post
	 * @param minTimestamp 最小のタイムスタンプ, null の場合無視
	 * @return
	 */
	public List<History> findList(NitechUser nitechUser, Post post, Long minTimestamp) {
		ExpressionList<History> expr = History.find.where();
		if (nitechUser != null) {
			expr.eq("nitechUser", nitechUser);
		}
		if (post != null) {
			expr.eq("post", post);
		}
		if (minTimestamp != null) {
			expr.ge("timestamp", minTimestamp);
		}
		return expr.findList();
	}
	
}

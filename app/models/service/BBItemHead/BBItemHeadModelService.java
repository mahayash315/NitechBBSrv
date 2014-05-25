package models.service.BBItemHead;

import java.util.List;
import java.util.Set;

import models.entity.BBCategory;
import models.entity.BBItemHead;
import models.entity.User;
import models.service.Model.ModelService;

public class BBItemHeadModelService implements ModelService<Long, BBItemHead> {
	
	public static BBItemHeadModelService use() {
		return new BBItemHeadModelService();
	}

	@Override
	public BBItemHead findById(Long id) {
		if (id != null) {
			return BBItemHead.find.byId(id);
		}
		return null;
	}

	@Override
	public BBItemHead save(BBItemHead entry) {
		if (entry != null) {
			entry.save();
			if (entry.getId() != null) {
				return entry;
			}
		}
		return null;
	}

	@Override
	public BBItemHead update(BBItemHead entry) {
		if (entry != null) {
			entry.update();
			return entry;
		}
		return null;
	}

	@Override
	public BBItemHead update(BBItemHead entry, Long id) {
		if (entry != null && id != null) {
			entry.update(id);
			if (entry.getId().equals(id)) {
				return entry;
			}
		}
		return null;
	}

	@Override
	public void delete(BBItemHead entry) {
		if (entry != null) {
			entry.delete();
		}
	}
	
	
	/**
	 * user, idDate, idIndex でエントリを取り出す
	 * @param user
	 * @param idDate
	 * @param idIndex
	 * @return
	 */
	public BBItemHead findByUserDateIndex(User user, String idDate, String idIndex) {
		if (user != null && idDate != null && idIndex != null) {
			return BBItemHead.find
						.where()
							.eq("user", user)
							.eq("idDate", idDate)
							.eq("idIndex", idIndex)
						.findUnique();
		}
		return null;
	}
	
	/**
	 * user, idDate, idIndex でエントリを取り出す
	 * @param entry
	 * @return
	 */
	public BBItemHead findByUserDateIndex(BBItemHead entry) {
		if (entry != null) {
			return findByUserDateIndex(entry.getUser(), entry.getIdDate(), entry.getIdIndex());
		}
		return null;
	}
	
	/**
	 * user に対するエントリ一覧を取得する
	 * @param user
	 * @return
	 */
	public List<BBItemHead> findListForUser(User user) {
		if (user != null) {
			return BBItemHead.find
						.where()
							.eq("user", user)
						.findList();
		}
		return null;
	}

	/**
	 * user に対するエントリ一覧を取得する
	 * @param user
	 * @return
	 */
	public Set<BBItemHead> findSetForUser(User user) {
		if (user != null) {
			return BBItemHead.find
						.where()
							.eq("user", user)
						.findSet();
		}
		return null;
	}
	
	public Set<BBItemHead> findSetByUserCategory(User user, BBCategory category) {
		if (user != null && category != null) {
			return BBItemHead.find
						.where()
							.eq("user", user)
							.eq("appendix.category", category)
						.findSet();
		}
		return null;
	}
}

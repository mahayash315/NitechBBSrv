package models.service.model;

import java.util.List;
import java.util.Set;

import models.entity.BBItem;
import models.entity.BBReadHistory;
import models.entity.User;

public class BBReadHistoryModelService implements ModelService<Long, BBReadHistory> {
	
	private static final String FIND_LIST_FOR_DEFAULT_ORDER_BY = "openTime desc";

	public static BBReadHistoryModelService use() {
		return new BBReadHistoryModelService();
	}
	
	@Override
	public BBReadHistory findById(Long id) {
		if (id != null) {
			return BBReadHistory.find.byId(id);
		}
		return null;
	}

	@Override
	public BBReadHistory save(BBReadHistory entry) {
		if (entry != null) {
			entry.save();
			if (entry.getId() != null) {
				return entry;
			}
		}
		return null;
	}

	@Override
	public BBReadHistory update(BBReadHistory entry) {
		if (entry != null) {
			entry.update();
			return entry;
		}
		return null;
	}

	@Override
	public BBReadHistory update(BBReadHistory entry, Long id) {
		if (entry != null && id != null) {
			entry.update(id);
			if (entry.getId().equals(id)) {
				return entry;
			}
		}
		return null;
	}

	@Override
	public void delete(BBReadHistory entry) {
		if (entry != null) {
			entry.delete();
		}
	}

	
	/**
	 * User, BBItem, openTime, readTimeLength でエントリを取得する
	 * @param user
	 * @param item
	 * @param openTime
	 * @return
	 */
	public BBReadHistory findByUserItemTime(User user, BBItem item, Long openTime) {
		if (user != null && item != null && openTime != null) {
			return BBReadHistory.find
						.where()
							.eq("user", user)
							.eq("item", item)
							.eq("openTime", openTime)
						.findUnique();
		}
		return null;
	}
	
	/**
	 * User に対するエントリの一覧を List で取得する
	 * @param user ユーザ
	 * @param minOpenTime 最小の openTime, 指定しない場合は null
	 * @param orderByClause 出力順の指定文, 指定しない場合は null
	 * @return
	 */
	public List<BBReadHistory> findListForUser(User user, Long minOpenTime, String orderByClause) {
		if (user != null) {
			if (minOpenTime == null) {
				minOpenTime = Long.valueOf(0L);
			}
			if (orderByClause == null) {
				orderByClause = FIND_LIST_FOR_DEFAULT_ORDER_BY;
			}
			return BBReadHistory.find
						.where()
							.eq("user", user)
							.ge("openTime", minOpenTime)
						.order(orderByClause)
						.findList();
		}
		return null;
	}
	
	/**
	 * User に対するエントリの一覧を Set で取得する
	 * @param user ユーザ
	 * @param minOpenTime 最小の openTime, 指定しない場合は null
	 * @return
	 */
	public Set<BBReadHistory> findSetForUser(User user, Long minOpenTime) {
		if (user != null) {
			if (minOpenTime == null) {
				minOpenTime = Long.valueOf(0L);
			}
			return BBReadHistory.find
						.where()
							.eq("user", user)
							.ge("openTime", minOpenTime)
						.findSet();
		}
		return null;
	}
}

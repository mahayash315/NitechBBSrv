package models.service.BBReadHistory;

import models.entity.BBItemHead;
import models.entity.BBReadHistory;
import models.entity.User;
import models.service.Model.ModelService;

public class BBReadHistoryModelService implements ModelService<BBReadHistory> {

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

	
	/**
	 * User, BBItemHead, openTime, readTimeLength でエントリを取得する
	 * @param user
	 * @param item
	 * @param openTime
	 * @return
	 */
	public BBReadHistory findByUserHeadTime(User user, BBItemHead item, Long openTime) {
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
}

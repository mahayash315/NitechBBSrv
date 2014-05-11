package models.service.BBItemHead;

import models.entity.BBItemHead;
import models.entity.User;
import models.service.Model.ModelService;

public class BBItemHeadModelService implements ModelService<BBItemHead> {
	
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
	

}
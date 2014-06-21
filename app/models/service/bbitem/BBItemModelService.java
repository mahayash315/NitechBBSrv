package models.service.bbitem;

import models.entity.BBItem;
import models.service.model.ModelService;

public class BBItemModelService implements ModelService<Long, BBItem> {
	
	public static BBItemModelService use() {
		return new BBItemModelService();
	}

	@Override
	public BBItem findById(Long id) {
		if (id != null) {
			return BBItem.find.byId(id);
		}
		return null;
	}

	@Override
	public BBItem save(BBItem entry) {
		if (entry != null) {
			entry.save();
			if (entry.getId() != null) {
				return entry;
			}
		}
		return null;
	}

	@Override
	public BBItem update(BBItem entry) {
		if (entry != null) {
			entry.update();
			return entry;
		}
		return null;
	}

	@Override
	public BBItem update(BBItem entry, Long id) {
		if (entry != null && id != null) {
			entry.update(id);
			if (entry.getId().equals(id)) {
				return entry;
			}
		}
		return null;
	}

	@Override
	public void delete(BBItem entry) {
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
	public BBItem findByDateIndex(String idDate, String idIndex) {
		if (idDate != null && idIndex != null) {
			return BBItem.find
						.where()
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
	public BBItem findByDateIndex(BBItem entry) {
		if (entry != null) {
			return findByDateIndex(entry.getIdDate(), entry.getIdIndex());
		}
		return null;
	}
	
	
}

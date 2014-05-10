package models.service.BBReadHistory;

import models.entity.BBReadHistory;
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

}

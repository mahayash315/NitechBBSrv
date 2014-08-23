package models.service.model.bb;

import models.entity.bb.History;
import models.service.model.ModelService;

import com.avaje.ebean.Ebean;

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

	
	
}

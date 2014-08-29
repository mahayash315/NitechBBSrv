package models.service.model.bb;

import models.entity.bb.Possession;
import models.entity.bb.Possession.PK;
import models.service.model.ModelService;

import com.avaje.ebean.Ebean;

public class PossessionModelService implements
		ModelService<Possession.PK, Possession> {

	@Override
	public Possession findById(PK id) {
		return Possession.find.byId(id);
	}

	@Override
	public Possession save(Possession entry) {
		if (entry != null) {
			Ebean.save(entry);
			if (entry.getId() != null) {
				return entry;
			}
		}
		return null;
	}

	@Override
	public void delete(Possession entry) {
		Ebean.delete(entry);
	}

}

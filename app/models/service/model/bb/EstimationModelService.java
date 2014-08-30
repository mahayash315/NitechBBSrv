package models.service.model.bb;

import java.util.List;

import models.entity.NitechUser;
import models.entity.bb.Estimation;
import models.entity.bb.Estimation.PK;
import models.service.model.ModelService;

import com.avaje.ebean.Ebean;

public class EstimationModelService implements
		ModelService<Estimation.PK, Estimation> {

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
		
		return Estimation.find.where().eq("nitechUser", nitechUser).eq("clazz", 1).order("liklihood desc").findList();
	}

}

package models.service.BBNaiveBayesParam;

import models.entity.BBNaiveBayesParam;
import models.service.Model.ModelService;

public class BBNaiveBayesParamModelService implements ModelService<Long, BBNaiveBayesParam> {
	
	public static BBNaiveBayesParamModelService use() {
		return new BBNaiveBayesParamModelService();
	}

	@Override
	public BBNaiveBayesParam findById(Long id) {
		if (id != null) {
			return BBNaiveBayesParam.find.byId(id);
		}
		return null;
	}

	@Override
	public BBNaiveBayesParam save(BBNaiveBayesParam entry) {
		if (entry != null) {
			entry.save();
			if (entry.getId() != null) {
				return entry;
			}
		}
		return null;
	}

	@Override
	public BBNaiveBayesParam update(BBNaiveBayesParam entry) {
		if (entry != null) {
			entry.update();
			if (entry.getId() != null) {
				return entry;
			}
		}
		return null;
	}

	@Override
	public BBNaiveBayesParam update(BBNaiveBayesParam entry, Long id) {
		if (entry != null && id != null) {
			entry.update(id);
			if (entry.getId().equals(id)) {
				return entry;
			}
		}
		return null;
	}

	@Override
	public void delete(BBNaiveBayesParam entry) {
		if (entry != null) {
			entry.delete();
		}
	}

}

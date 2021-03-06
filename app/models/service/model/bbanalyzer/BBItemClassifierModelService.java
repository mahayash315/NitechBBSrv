package models.service.model.bbanalyzer;

import java.util.Set;

import models.entity.bbanalyzer.BBItemClassifier;
import models.entity.bbanalyzer.BBUserCluster;
import models.service.model.ModelService;

public class BBItemClassifierModelService implements ModelService<Long, BBItemClassifier> {
	
	public static BBItemClassifierModelService use() {
		return new BBItemClassifierModelService();
	}

	@Override
	public BBItemClassifier findById(Long id) {
		if (id != null) {
			return BBItemClassifier.find.byId(id);
		}
		return null;
	}

	@Override
	public BBItemClassifier save(BBItemClassifier entry) {
		if (entry != null) {
			entry.save();
			if (entry.getId() != null) {
				return entry;
			}
		}
		return null;
	}

	@Override
	public void delete(BBItemClassifier entry) {
		if (entry != null) {
			entry.delete();
		}
	}
	
	public BBItemClassifier findByBBUserClusterClassNumber(BBUserCluster bbUserCluster, int classNumber) {
		return BBItemClassifier.find
								 .where()
								 	.eq("bbUserCluster", bbUserCluster)
								 	.eq("classNumber", classNumber)
								 .findUnique();
	}
	
	public Set<BBItemClassifier> findSetByBBUserCluster(BBUserCluster bbUserCluster) {
		return BBItemClassifier.find
								 .where()
								 	.eq("bbUserCluster", bbUserCluster)
								 .findSet();
	}
	
}

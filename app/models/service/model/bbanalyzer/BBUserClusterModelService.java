package models.service.model.bbanalyzer;

import java.util.Set;

import models.entity.bbanalyzer.BBUserCluster;
import models.service.model.ModelService;

public class BBUserClusterModelService implements ModelService<Long, BBUserCluster> {
	
	public static BBUserClusterModelService use() {
		return new BBUserClusterModelService();
	}

	@Override
	public BBUserCluster findById(Long id) {
		if (id != null) {
			return BBUserCluster.find.byId(id);
		}
		return null;
	}

	@Override
	public BBUserCluster save(BBUserCluster entry) {
		if (entry != null) {
			entry.save();
			if (entry.getId() != null) {
				return entry;
			}
		}
		return null;
	}

	@Override
	public void delete(BBUserCluster entry) {
		if (entry != null) {
			entry.delete();
		}
	}
	
	public BBUserCluster findByClusterDepthId(long clusterDepth, long clusterId) {
		return BBUserCluster.find
							.where()
								.eq("clusterDepth", clusterDepth)
								.eq("clusterId", clusterId)
							.findUnique();
	}
	
	public Set<BBUserCluster> findSetByClusterDepth(int clusterDepth) {
		return BBUserCluster.find
							.where()
								.eq("clusterDepth", clusterDepth)
							.findSet();
	}
	
}

package models.service.bbusercluster;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import models.entity.BBUserCluster;
import models.entity.User;
import models.service.AbstractService;
import models.service.bbanalyzer.AtomUserCluster;
import models.service.bbanalyzer.UserCluster;


public class BBUserClusterService extends AbstractService {
	
	private static Map<BBUserCluster, UserCluster> sUserClusterMap = new HashMap<BBUserCluster, UserCluster>();
	
	public static BBUserClusterService use() {
		return new BBUserClusterService();
	}
	
	public UserCluster convertBBUserClusterToUserCluster(BBUserCluster bbUserCluster) throws SQLException {
		if (bbUserCluster != null) {
			UserCluster userCluster = sUserClusterMap.get(bbUserCluster);
			if (userCluster == null) {
				long depth = bbUserCluster.getClusterDepth();
				long id = bbUserCluster.getClusterId();
				if (depth == 0) {
					// atom cluster
					userCluster = new AtomUserCluster(new User(Long.valueOf(id)).unique());
				} else {
					// cluster
					userCluster = new UserCluster(depth, id);
					userCluster.feature = bbUserCluster.getFeature();
					for(BBUserCluster child : bbUserCluster.getChildren()) {
						userCluster.children.put(convertBBUserClusterToUserCluster(child), child.getDistanceFromParent());
					}
				}
			}
			return userCluster;
		}
		return null;
	}
	
}

package models.service.bbanalyzer;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import models.entity.User;
import models.entity.bbanalyzer.BBUserCluster;
import models.service.AbstractService;


public class BBUserClusterService extends AbstractService {
	
	private static Map<BBUserCluster, UserCluster> sUserClusterMap = new HashMap<BBUserCluster, UserCluster>();
	
	public static BBUserClusterService use() {
		return new BBUserClusterService();
	}
	
	public UserCluster convertBBUserClusterToUserCluster(BBUserCluster bbUserCluster) throws SQLException {
		if (bbUserCluster != null) {
			UserCluster userCluster = sUserClusterMap.get(bbUserCluster);
			if (userCluster == null) {
				int depth = bbUserCluster.getClusterDepth();
				long id = bbUserCluster.getClusterId();
				userCluster = (depth == 0) ? new AtomUserCluster(new User(Long.valueOf(id)).unique()) : new UserCluster(depth, id);
				sUserClusterMap.put(bbUserCluster, userCluster);
				
				if (0 < depth) {
					userCluster.feature = bbUserCluster.getFeature();
					for(BBUserCluster child : bbUserCluster.getChildren()) {
						userCluster.children.put(convertBBUserClusterToUserCluster(child), child.getDistanceFromParent());
					}
				}
				
				BBUserCluster parent = bbUserCluster.getParent();
				if (parent != null) {
					userCluster.setParent(convertBBUserClusterToUserCluster(parent));
				}
			}
			return userCluster;
		}
		return null;
	}
	
}

package models.service.bbanalyzer;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import models.entity.bbanalyzer.BBUserCluster;
import models.service.AbstractService;


public class BBItemClassifierService extends AbstractService {
	
	private static Map<BBUserCluster, ItemClassifier> sUserClusterMap = new HashMap<BBUserCluster, ItemClassifier>();
	
	public static BBItemClassifierService use() {
		return new BBItemClassifierService();
	}
	
	public ItemClassifier getItemClassifierForBBUserCluster(BBUserCluster bbUserCluster) throws SQLException {
		return new ItemClassifier(bbUserCluster.convertToUserCluster());
	}
	
}

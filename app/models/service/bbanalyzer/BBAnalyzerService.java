package models.service.bbanalyzer;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import models.service.AbstractService;
import utils.api.bbanalyzer.LogUtil;

public class BBAnalyzerService extends AbstractService {
	
	private static UserClassifier userClassifier;
	
	public static BBAnalyzerService use() {
		return new BBAnalyzerService();
	}
	
	public BBAnalyzerService() {
		if (userClassifier == null) {
			try {
				userClassifier = new UserClassifier();
			} catch (SQLException e) {
				e.printStackTrace();
				LogUtil.error("BBAnalyzerService#this()", e);
			}
		}
	}
	
	public void classifyUsers() throws SQLException {
		if (userClassifier != null) {
			userClassifier.classify();
		}
	}
	
	public void trainAllItemClassifiers() throws SQLException {
		if (userClassifier != null) {
			List<SQLException> errors = new ArrayList<SQLException>();
			Set<UserCluster> allClusters = new HashSet<UserCluster>();
			Set<UserCluster> topClusters = userClassifier.getTopClusters();
			if (topClusters != null) {
				for(UserCluster cluster : topClusters) {
					allClusters.addAll(cluster.getAllClusters());
				}
			}
			
			for(UserCluster cluster : allClusters) {
				ItemClassifier itemClassifier = cluster.getItemClassifier();
				if (itemClassifier != null) {
					try {
						itemClassifier.train();
					} catch (SQLException e) {
						errors.add(e);
					}
				}
			}

			if (0 < errors.size()) {
				for(SQLException e : errors) {
					LogUtil.error("BBAnalyzerService#trainAllItemClassifiers()", e);
				}
				throw errors.get(0);
			}
		}
	}
	
}

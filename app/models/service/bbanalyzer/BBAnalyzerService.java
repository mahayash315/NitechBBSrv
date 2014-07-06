package models.service.bbanalyzer;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import models.entity.BBItem;
import models.entity.User;
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
	
	/**
	 * クラスタ分析により、全ユーザをクラスタに分類する
	 * @throws SQLException
	 */
	public void classifyAllUsers() throws SQLException {
		if (userClassifier != null) {
			userClassifier.classify();
		}
	}
	
	/**
	 * 各クラスタにの掲示識別器について、掲示閲覧履歴から学習させる
	 * @throws SQLException
	 */
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
	
	/**
	 * 掲示をクラス分類する
	 * @param user
	 * @param item
	 * @return
	 */
	public int classifyItem(User user, BBItem item) {
		Map<UserCluster, Integer> results = doClassifyItem(user, item);
		
		for(UserCluster cluster : results.keySet()) {
			int classNumber = results.get(cluster);
			
			// 2 > 0 > 1 の優先度で出力
			if (classNumber == 2) {
				return 3;
			} else if (classNumber == 0) {
				return 1;
			}
		}
		
		return (results.size() <= 0) ? (-1) : 1;
	}
	
	/**
	 * 掲示をクラス分類する。ユーザの属するすべてのユーザクラスタの持つ識別器で掲示を識別した結果を出力する
	 * @param user
	 * @param item
	 * @return
	 */
	private Map<UserCluster, Integer> doClassifyItem(User user, BBItem item) {
		if (userClassifier != null) {
			Map<UserCluster, Integer> resultMap = new HashMap<UserCluster, Integer>();
			AtomUserCluster atomUserCluster = userClassifier.getAtomUserCluster(user);
			UserCluster cluster = atomUserCluster;
			
			while(cluster != null) {
				ItemClassifier classifier = cluster.getItemClassifier();
				if (classifier != null) {
					int result = classifier.classify(item);
					if (result != ItemClassifier.NO_CLASS_NUM) {
						resultMap.put(cluster, Integer.valueOf(result));
					}
				}
				cluster = cluster.getParent();
			}
			
			return resultMap;
		}
		return null;
	}
	
}

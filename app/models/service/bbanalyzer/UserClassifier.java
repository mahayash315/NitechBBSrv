package models.service.bbanalyzer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import models.entity.User;
import models.entity.bbanalyzer.BBItem;
import models.entity.bbanalyzer.BBUserCluster;
import models.service.AbstractService;
import utils.bbanalyzer.LogUtil;

public class UserClassifier extends AbstractService {

	/* 定数 */
	public static final int CLUSTER_DEPTH = 2;				// クラスタの階層数 D
	public static final int[] CLUSTER_SIZES = {5, 1};		// 各階層のクラスタの数 K
	
	private static final double MAX_DISTANCE = 2.0;
	private static final int MAX_KMEANS_COUNT = 10;
	private static final int MIN_KMEANS_CHANGE = 0;
	
	private static class SQL_BBITEM {
		static final String SQL_SELECT_LENGTH_ID = "select count("+BBItem.PROPERTY.ID+") from "+BBItem.ENTITY;
		
		private static class STATEMENT {
			static PreparedStatement selectIdLength(Connection conn) throws SQLException {
				return conn.prepareStatement(SQL_SELECT_LENGTH_ID);
			}
		}
	}
	
	/* メンバ */
	
	// 各層におけるクラスタの集合を格納する
	//   0 層 : アトムクラスタ
	//   1 層 : 最初のクラスタリング
	//   ...
	// (CLUSTER_DEPTH)層 : 最後のクラスタリング
	private Map<Integer, Set<UserCluster>> clusterMap;
	
	// 各層における、その層のクラスタと一つ下の層のクラスタとの距離を格納する
	//cluster(children) -> cluster(parent) -> distance
	private Map<UserCluster, Map<UserCluster, Double>> distanceMap;
	
	// ユーザとユーザクラスタの紐付け
	private Map<User, AtomUserCluster> atomClusterMap;
	
	
	/* コンストラクタ */
	public UserClassifier() throws SQLException {
		init();
		loadUserClusters();
	}
	
	/* インスタンスメソッド */
	/**
	 * classify
	 * @throws SQLException 
	 */
	public void classify() throws SQLException {
		try {
			// 全層をクラスタ分割
			doClassify();
			saveUserClusters();
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			try {
				closeConnection();
			} catch (SQLException e) {
				e.printStackTrace();
				throw e;
			}
		}
	}
	
	/**
	 * 先頭クラスタ（depth が一番大きいクラスタ）の集合を返す
	 * @return
	 */
	public Set<UserCluster> getTopClusters() {
		if (clusterMap != null && clusterMap.containsKey(CLUSTER_DEPTH)) {
			return clusterMap.get(CLUSTER_DEPTH);
		}
		return null;
	}
	
	/**
	 * アトムユーザクラスタを返す
	 * @param user
	 * @return
	 */
	public AtomUserCluster getAtomUserCluster(User user) {
		if (clusterMap != null && clusterMap.containsKey(Integer.valueOf(0)) && atomClusterMap != null) {
			return atomClusterMap.get(user);
		}
		return null;
	}
	
	/**
	 * メンバを初期化する
	 */
	private void init() {
		clusterMap = new HashMap<Integer, Set<UserCluster>>();
		distanceMap = new HashMap<UserCluster, Map<UserCluster, Double>>();
		atomClusterMap = new HashMap<User, AtomUserCluster>();
		
		for(int i = 0; i <= CLUSTER_DEPTH; ++i) {
			clusterMap.put(Integer.valueOf(i), new HashSet<UserCluster>());
		}
	}
	
	/**
	 * 各ユーザが中心となる(ユーザ数と同じ数だけある)アトムクラスタを作る
	 */
	private void initAtomCluster() throws SQLException {
		Set<UserCluster> atomClusters = clusterMap.get(Integer.valueOf(0));
		if (atomClusters == null) {
			atomClusters = new HashSet<UserCluster>();
			clusterMap.put(Integer.valueOf(0), atomClusters);
		}
		
		// 既存のリストすべて削除
		atomClusters.clear();
		atomClusterMap.clear();
		
		// 全ユーザを取得
		Set<User> users = new User().findSet();
		
		// 各ユーザについて、アトムクラスタを作る
		for(User user : users) {
			AtomUserCluster userCluster = new AtomUserCluster(user);
			atomClusters.add(userCluster);
			atomClusterMap.put(user, userCluster);
		}
	}
	
	/**
	 * アトムクラスタの上の階層から始め、各階層でクラスタリングする。1階層終わったら上方向に進む。
	 * @throws SQLException 
	 */
	private void doClassify() throws SQLException {
		//   0 層 : アトムクラスタ
		//   1 層 : 最初のクラスタリング
		//   ...
		// (CLUSTER_DEPTH)層 : 最後のクラスタリング
		
		// 距離マップを初期化 (古いデータが残っているため)
		distanceMap.clear();
		
		// 0 層
		initAtomCluster();
		
		// 1 層 - (CLUSTER_DEPTH) 層
		for(int depth = 1; depth <= CLUSTER_DEPTH; ++depth) {
			LogUtil.info("UserClassifier#doClassify(): classifying depth = "+depth);
			Set<UserCluster> parents = clusterMap.get(Integer.valueOf(depth));
			Set<UserCluster> children = clusterMap.get(Integer.valueOf(depth-1));
			Map<UserCluster, UserCluster> prevClusters = new HashMap<UserCluster, UserCluster>();
			
			// 現在の状態を保存
			for(UserCluster child : children) {
				prevClusters.put(child, child.getParent());
			}
			
			// make CLUSTER_SIZES[depth-1] clusters in parents
			// for each cluster in parents
			// 		for each cluster in children
			// 			calculate distance (cosin) from parent
			//			merge the clusters which are the closest to the clusters in parents
			
			// depth 層のクラスタリング
			
			if (needsInitialization(depth)) {
				// 初期クラスタ中心を定める
				initKMeans(depth);
			}
			LogUtil.info("UserClassifier#doClassify(): selected "+parents.size()+" parents");
			
			// for i=1,2,...,n
			// 		for each cluster in children
			// 			insert this to the closest parent cluster
			//		if (i<n)
			//			for each cluster in parents
			//				update its center vector so that it refers to
			//				the average vector of its child clusters
			
			int count = 0;
			int changed = 0;
			do {
				LogUtil.info("UserClassifier#doClassify():   trial "+count);
				changed = 0;
				
				for(UserCluster parent : parents) {
					parent.clearChildren();
				}
				
				for(UserCluster child : children) {
					double minimumDistance = MAX_DISTANCE;
					UserCluster parent = null;
					for(UserCluster candidate : parents) {
						double d = getDistance(child, candidate);
						if (d < minimumDistance) {
							minimumDistance = d;
							parent = candidate;
						}
					}
					parent.addChild(child, minimumDistance);
					
					UserCluster prev = prevClusters.get(child);
					if (prev != null && !prev.equals(parent)) {
						++changed;
					}
					prevClusters.put(child, parent);
				}
				
				if (0 < depth) {
					// 各 parent クラスタのクラスタ中心を更新
					for(UserCluster parent : parents) {
						parent.updateFeature();
					}
				}
				
				++count;
			} while (MIN_KMEANS_CHANGE < changed && count < MAX_KMEANS_COUNT);
		}
	}
	
	/**
	 * depth 層に対して initKMeans() を行う必要があるか判断する
	 * @param depth
	 * @return
	 */
	private boolean needsInitialization(int depth) {
		if (depth == 0) {
			return true;
		} else if (0 < depth && depth <= CLUSTER_DEPTH) {
			Set<UserCluster> clusters = clusterMap.get(Integer.valueOf(depth));
			if (clusters == null || clusters.size() != CLUSTER_SIZES[depth-1]) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}
	
	/**
	 * depth 層のクラスタ中心を CLUSTER_SIZES[depth-1] 個作る
	 * @param depth
	 */
	private void initKMeans(int depth) {
		LogUtil.info("UserClassifier#initKMeans("+depth+"):");
		Set<UserCluster> parents = clusterMap.get(Integer.valueOf(depth));
		Set<UserCluster> children = clusterMap.get(Integer.valueOf(depth-1));
		Set<UserCluster> selected = new HashSet<UserCluster>();
		int num = CLUSTER_SIZES[depth-1];
		UserCluster clusters[] = new UserCluster[num];
		UserCluster furthestCluster = null;
		double furthestDistance = 0;
		
		// クラスタ中心を1つ決める
		UserCluster firstCluster = children.iterator().next();
		clusters[0] = new UserCluster(0, firstCluster);
		parents.add(clusters[0]);
		selected.add(firstCluster);
		LogUtil.info("UserClassifier#initKMeans("+depth+"): i = 0");
		LogUtil.info("UserClassifier#initKMeans("+depth+"): selected "+clusters[0]);
		
		// num 個のクラスタ中心まで増やす
		for(int i = 1; i < num; ++i) {
			LogUtil.info("UserClassifier#initKMeans("+depth+"): i = "+i);
			// calculate distance between clusters[0] and all clusters in children
			// insert them into distanceMap
			// take the furthest (or close to the furthest) cluster form children
			//   as next center cluster (cluster[i])
			
			
			// どの親クラスタからも一番遠い子クラスタを見つける
			furthestCluster = null;
			furthestDistance = 0;
			// for each cluster in children
			for(UserCluster child : children) {
				// すでに親なら除外
				if (selected.contains(child)) {
					continue;
				}
				// calculate the distance from the nearest parent cluster
				double minimumDistance = MAX_DISTANCE;
				for(UserCluster parent : parents) {
					double d = getDistance(child, parent);
					if (d < minimumDistance) {
						minimumDistance = d;
					}
				}
				if (furthestDistance <= minimumDistance) {
					furthestCluster = child;
					furthestDistance = minimumDistance;
				}
			}
			
			// furthestCluster を次のクラスタ中心 cluster[i] とする
			clusters[i] = new UserCluster(i, furthestCluster);
			parents.add(clusters[i]);
			selected.add(furthestCluster);
			LogUtil.info("UserClassifier#initKMeans("+depth+"): distance = "+furthestDistance+", selected "+clusters[i]);
		}
		
	}
	
	
	/**
	 * クラスタ間の距離を取得する
	 * @param c1
	 * @param c2
	 * @return
	 */
	private double getDistance(UserCluster c1, UserCluster c2) {
		UserCluster key1 = null, key2 = null;
		if (c1.depth <= c2.depth) {
			key1 = c1; key2 = c2;
		} else {
			key1 = c2; key2 = c1;
		}
		
		Map<UserCluster, Double> map = distanceMap.get(key1);
		if (map == null) {
			map = new HashMap<UserCluster, Double>();
		} else {
			Double d = map.get(key2);
			if (d != null) {
				return d;
			}
		}
		
		Double d = Double.valueOf(c1.distance(c2));
		map.put(key2, d);
		distanceMap.put(key1, map);
		
		return d;
	}
	
	
	
	
	public void loadUserClusters() throws SQLException {
		// atom clusters
		{
			Set<UserCluster> userClusters = clusterMap.get(Integer.valueOf(0));
			Set<BBUserCluster> bbUserClusters = new BBUserCluster().findSetByDepth(0);
			for(BBUserCluster bbUserCluster : bbUserClusters) {
				AtomUserCluster userCluster = (AtomUserCluster) bbUserCluster.convertToUserCluster();
				userClusters.add(userCluster);
				atomClusterMap.put(userCluster.getUser(), userCluster);
			}
		}
		
		// upper clusters
		for(int i = 1; i <= CLUSTER_DEPTH; ++i) {
			Set<UserCluster> userClusters = clusterMap.get(Integer.valueOf(i));
			Set<BBUserCluster> bbUserClusters = new BBUserCluster().findSetByDepth(i);
			for(BBUserCluster bbUserCluster : bbUserClusters) {
				UserCluster userCluster = bbUserCluster.convertToUserCluster();
				userClusters.add(userCluster);
			}
		}
	}
	
	public void saveUserClusters() {
		// TODO test this method
		for(UserCluster cluster : getTopClusters()) {
			saveUserCluster(cluster, null, 0.0);
		}
	}
	
	private BBUserCluster saveUserCluster(UserCluster cluster, BBUserCluster parent, double distanceFromParent) {
		long depth = cluster.depth;
		BBUserCluster bbUserCluster = new BBUserCluster(cluster.depth, cluster.id).uniqueOrStore();
		bbUserCluster.setDistanceFromParent(distanceFromParent);
		bbUserCluster.setParent(parent);
		if (0 < depth) {
			bbUserCluster.setFeature(cluster.feature);
			Set<BBUserCluster> children = new HashSet<BBUserCluster>();
			for(UserCluster child : cluster.children.keySet()) {
				children.add(saveUserCluster(child, bbUserCluster, cluster.children.get(child)));
			}
			bbUserCluster.setChildren(children);
		}
		bbUserCluster.store();
		
		return bbUserCluster;
	}
	
}

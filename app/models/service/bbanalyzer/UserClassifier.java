package models.service.bbanalyzer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import models.entity.BBItem;
import models.entity.User;
import play.Logger;
import play.db.DB;
import utils.bbanalyzer.BBAnalyzerUtil;

public class UserClassifier {

	// TODO ユーザをクラスタに分類させるクラスタ分析器を作る
	
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
	int userVectorSize = 0;
	
	// 最下層のクラスタを格納する
	Set<UserCluster> atomClusters;
	
	// 各層におけるクラスタの集合を格納する
	//   0 層 : アトムクラスタ
	//   1 層 : 最初のクラスタリング
	//   ...
	// (CLUSTER_DEPTH)層 : 最後のクラスタリング
	Map<Integer, Set<UserCluster>> clusterMap;
	
	// 各層における、その層のクラスタと一つ下の層のクラスタとの距離を格納する
	//depth -> cluster(children) -> cluster(parent) -> distance
	Map<Integer, Map<UserCluster, Map<UserCluster, Double>>> distanceMap;
	

	Connection conn;
	
	/* コンストラクタ */
	public UserClassifier() {
		
	}
	
	/* インスタンスメソッド */
	/**
	 * classify
	 * @throws SQLException 
	 */
	public void classify() throws SQLException {
		conn = DB.getConnection();
		
		// 初期化
		init();
		
		try {
			// ユーザベクトルの大きさを設定
			initUserVectorSize();
			
			// ユーザをアトムクラスタに入れる
			initAtomCluster();
			
			// 全層をクラスタ分割
			doClassify();
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
				throw e;
			}
		}
	}
	
	public Set<UserCluster> getTopCluster() {
		if (clusterMap != null && clusterMap.containsKey(CLUSTER_DEPTH)) {
			return clusterMap.get(CLUSTER_DEPTH);
		}
		return null;
	}
	
	/**
	 * メンバを初期化する
	 */
	private void init() {
		atomClusters = new HashSet<UserCluster>();
		clusterMap = new HashMap<Integer, Set<UserCluster>>();
		distanceMap = new HashMap<Integer, Map<UserCluster, Map<UserCluster, Double>>>();
		
		for(int i = 0; i <= CLUSTER_DEPTH; ++i) {
			clusterMap.put(Integer.valueOf(i), new HashSet<UserCluster>());
			distanceMap.put(Integer.valueOf(i), new HashMap<UserCluster, Map<UserCluster, Double>>());
		}
	}
	
	/**
	 * ユーザベクトルの大きさを設定する
	 * @throws SQLException 
	 */
	private void initUserVectorSize() throws SQLException {
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = SQL_BBITEM.STATEMENT.selectIdLength(conn);
			rs = st.executeQuery();
			
			if (rs.next()) {
				userVectorSize = rs.getInt(1);
			} else {
				userVectorSize = 0;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (st != null) {
				st.close();
			}
			if (rs != null) {
				rs.close();
			}
		}
	}
	
	/**
	 * 各ユーザが中心となる(ユーザ数と同じ数だけある)アトムクラスタを作る
	 */
	private void initAtomCluster() throws SQLException {
		// 全ユーザを取得
		Set<User> users = new User().findSet();
		
		// 各ユーザについて、アトムクラスタを作る
		for(User user : users) {
			AtomUserCluster cluster = new AtomUserCluster();
			cluster.user = user;
			cluster.vector = user.getUserVector(userVectorSize);
			cluster.children = null;
			atomClusters.add(cluster);
		}
	}
	
	/**
	 * アトムクラスタの上の階層から始め、各階層でクラスタリングする。1階層終わったら上方向に進む。
	 */
	private void doClassify() {
		//   0 層 : アトムクラスタ
		//   1 層 : 最初のクラスタリング
		//   ...
		// (CLUSTER_DEPTH)層 : 最後のクラスタリング
		
		// 0 層
		clusterMap.put(Integer.valueOf(0), atomClusters);
		
		// 1 層 - (CLUSTER_DEPTH) 層
		for(int depth = 1; depth <= CLUSTER_DEPTH; ++depth) {
			// TODO 階層 depth において、一つ下の階層 (depth-1) のクラスタをクラスタリングする
			Logger.info("UserClassifier#doClassify(): classifying depth = "+depth);
			Set<UserCluster> parents = clusterMap.get(Integer.valueOf(depth));
			Set<UserCluster> children = clusterMap.get(Integer.valueOf(depth-1));
			Map<UserCluster, Map<UserCluster, Double>> distances = distanceMap.get(Integer.valueOf(depth));
			Map<UserCluster, UserCluster> prevClusters = new HashMap<UserCluster, UserCluster>();
			
			// TODO implement here
			// make CLUSTER_SIZES[depth-1] clusters in parents
			// for each cluster in parents
			// 		for each cluster in children
			// 			calculate distance (cosin) from parent
			//			merge the clusters which are the closest to the clusters in parents
			
			// depth 層のクラスタリング
			
			// 初期クラスタ中心を定める
			initKMeans(depth);
			Logger.info("UserClassifier#doClassify(): selected "+parents.size()+" parents");
			
			// 変数初期化
			for(UserCluster parent : parents) {
				parent.children = new HashMap<UserCluster, Double>();
			}
			
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
				Logger.info("UserClassifier#doClassify():   trial "+count);
				changed = 0;
				if (0 < count) {
					// 各 parent クラスタのクラスタ中心を更新
					for(UserCluster parent : parents) {
						updateCenterVector(parent);
						parent.children.clear();
					}
				}
				
				for(UserCluster child : children) {
					Logger.info("UserClassifier#doClassify():    child cluster "+child.toString());
					Map<UserCluster, Double> dists = distances.get(child);
					double minimumDistance = MAX_DISTANCE;
					UserCluster minimumCluster = null;
					for(UserCluster parent : parents) {
						Logger.info("UserClassifier#doClassify():      +parent "+parent.toString());
						double d = dists.get(parent).doubleValue();
						Logger.info("UserClassifier#doClassify():        +distance = "+d);
						if (d < minimumDistance) {
							minimumDistance = d;
							minimumCluster = parent;
						}
					}
					Logger.info("UserClassifier#doClassify():      --> nearest parent = "+minimumCluster.toString()+", distance="+minimumDistance);
					minimumCluster.children.put(child, minimumDistance);
					if (!prevClusters.containsKey(child) || !prevClusters.get(child).equals(minimumCluster)) {
						++changed;
					}
					prevClusters.put(child, minimumCluster);
				}
				
				++count;
			} while (MIN_KMEANS_CHANGE < changed && count < MAX_KMEANS_COUNT);
			
//			// clusterMap に追加
//			clusterMap.put(Integer.valueOf(depth), parents);
		}
	}
	
	/**
	 * depth 層のクラスタ中心を CLUSTER_SIZES[depth-1] 個作る
	 * @param depth
	 */
	private void initKMeans(int depth) {
		Logger.info("UserClassifier#initKMeans("+depth+"):");
		Set<UserCluster> parents = clusterMap.get(Integer.valueOf(depth));
		Set<UserCluster> children = clusterMap.get(Integer.valueOf(depth-1));
		Map<UserCluster, Map<UserCluster, Double>> distances = distanceMap.get(Integer.valueOf(depth));
		int size = CLUSTER_SIZES[depth-1];
		UserCluster clusters[] = new UserCluster[size];
		UserCluster furthestCluster = null;
		double furthestDistance = 0;
		
		// クラスタ中心を1つ決める
		clusters[0] = children.iterator().next();
		parents.add(clusters[0]);
		
		// size 個のクラスタ中心まで増やす
		for(int i = 1; i < size; ++i) {
			Logger.info("UserClassifier#initKMeans("+depth+"): i = "+i);
			// calculate distance between clusters[0] and all clusters in children
			// insert them into distanceMap
			// take the furthest (or close to the furthest) cluster form children
			//   as next center cluster (cluster[i])
			
			// 一つ前のクラスタ
			UserCluster cluster = clusters[i-1];
			// その中心との距離を計算する
			calcDistances(depth, cluster);
			
			// どの親クラスタからも一番遠い子クラスタを見つける
			furthestCluster = null;
			furthestDistance = 0;
			// for each cluster in children
			for(UserCluster child : children) {
				// すでに親なら除外
				if (parents.contains(child)) {
					continue;
				}
				Map<UserCluster, Double> dists = distances.get(child);
				// calculate the distance from the nearest parent cluster
				double minimumDistance = MAX_DISTANCE;
				for(UserCluster parent : parents) {
					double d = dists.get(parent).doubleValue();
					if (d < minimumDistance) {
						minimumDistance = d;
					}
				}
				if (furthestDistance <= minimumDistance) {
					furthestCluster = child;
				}
			}
			
			// furthestCluster を次のクラスタ中心 cluster[i] とする
			clusters[i] = furthestCluster;
			parents.add(clusters[i]);
			Logger.info("UserClassifier#initKMeans("+depth+"): distance = "+furthestDistance+", selected "+clusters[i]);
		}
	}
	
	/**
	 * 階層 depth において、クラスタ parent から一つ下の階層の全クラスタとの距離を計算し
	 * distanceMap に入れる
	 * @param depth
	 * @param parent
	 */
	private void calcDistances(int depth, UserCluster parent) {
		Map<UserCluster, Map<UserCluster, Double>> distances = distanceMap.get(Integer.valueOf(depth));
		Set<UserCluster> children = clusterMap.get(Integer.valueOf(depth-1));
		
		for(UserCluster child : children) {
			double cosin = vectorMultiply(child.vector, parent.vector) / (vectorSize(child.vector) * vectorSize(parent.vector));
			double distance = 1.0 - cosin;
			if (distance <= 0.0) {
				Logger.info("UserClassifier#calcDistances(): distance = 0.0");
				Logger.info("UserClassifier#calcDistances():   <-- child.vec  = "+BBAnalyzerUtil.printVector(child.vector));
				Logger.info("UserClassifier#calcDistances():   <-- parent.vec = "+BBAnalyzerUtil.printVector(parent.vector));
			}
			
			if (!distances.containsKey(child)) {
				distances.put(child, new HashMap<UserCluster, Double>());
			}
			distances.get(child).put(parent, Double.valueOf(distance));
		}
	}
	
	/**
	 * parent クラスタのクラスタ中心ベクトルを更新
	 * @param cluster
	 */
	private void updateCenterVector(UserCluster cluster) {
		double vector[] = new double[userVectorSize];
		for(UserCluster child : cluster.children.keySet()) {
			vectorAdd(vector, child.vector);
		}
		vectorDivide(vector, Double.valueOf(cluster.children.size()));
		cluster.vector = vector;
	}
	
	private void vectorAdd(double[] dst, double[] v) throws IllegalArgumentException {
		if (dst.length != v.length) {
			throw new IllegalArgumentException("v1.length != v2.length");
		}
		for(int i = 0; i < dst.length; ++i) {
			dst[i] = dst[i] + v[i];
		}
	}
	
	private double vectorMultiply(double[] v1, double[] v2) throws IllegalArgumentException {
		if (v1.length != v2.length) {
			throw new IllegalArgumentException("v1.length != v2.length");
		}
		double res = 0;
		for(int i = 0; i < v1.length; ++i) {
			res = res + (v1[i] * v2[i]);
		}
		return res;
	}
	
	private void vectorDivide(double[] dst, double div) {
		for(int i = 0; i < dst.length; ++i) {
			dst[i] = dst[i] / div;
		}
	}
	
	private double vectorSize(double[] v) {
		double sum = 0;
		for(int i = 0; i < v.length; ++i) {
			sum = sum + (v[i] * v[i]);
		}
		return Math.sqrt(sum);
	}
}

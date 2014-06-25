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
import play.db.DB;

public class UserClassifier {

	// TODO ユーザをクラスタに分類させるクラスタ分析器を作る
	
	/* 定数 */
	public static final int CLUSTER_DEPTH = 2;				// クラスタの階層数 D
	public static final int[] CLUSTER_SIZES = {5, 1};		// 各階層のクラスタの数 K
	
	private static class SQL_BBITEM {
		static final String SQL_SELECT_LENGTH_ID = "select length("+BBItem.PROPERTY.ID+") from "+BBItem.ENTITY;
		
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
	 */
	public void classify() {
		conn = DB.getConnection();
		
		// 初期化
		init();
		
		try {
			// ユーザベクトルの大きさを設定
			initUserVectorSize();
			
			// ユーザをアトムクラスタに入れる
			initAtomCluster();
			
			// アトムクラスタをクラスタ分割
			doClassify();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * メンバを初期化する
	 */
	private void init() {
		atomClusters = new HashSet<UserCluster>();
		clusterMap = new HashMap<Integer, Set<UserCluster>>();
		distanceMap = new HashMap<Integer, Map<UserCluster, Map<UserCluster, Double>>>();
		
		for(int i = 0; i < CLUSTER_DEPTH; ++i) {
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
			
			userVectorSize = rs.getInt(0);
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
		// TODO 
		//   0 層 : アトムクラスタ
		//   1 層 : 最初のクラスタリング
		//   ...
		// (CLUSTER_DEPTH)層 : 最後のクラスタリング
		
		// 0 層
		clusterMap.put(Integer.valueOf(0), atomClusters);
		
		// 1 層 - (CLUSTER_DEPTH) 層
		for(int depth = 1; depth <= CLUSTER_DEPTH; ++depth) {
			// TODO 階層 depth において、一つ下の階層 (depth-1) のクラスタをクラスタリングする
			Set<UserCluster> parents = new HashSet<UserCluster>();
			Set<UserCluster> children = clusterMap.get(Integer.valueOf(depth-1));
			
			// TODO implement here
			// make CLUSTER_SIZES[depth-1] clusters in parents
			// for each cluster in parents
			// 		for each cluster in children
			// 			calculate distance (cosin) from parent
			//			merge the clusters which are the closest to the clusters in parents
			
			// depth 層のクラスタリング
			
			// 初期クラスタ中心を定める
			initKMeans(depth);
			
			// TODO implement here
			// for i=1,2,...,n
			// 		for each cluster in children
			// 			insert this to the closest parent cluster
			//		if (i<n)
			//			for each cluster in parents
			//				update its center vector so that it refers to
			//				the average vector of its child clusters
			
			// clusterMap に追加
			clusterMap.put(Integer.valueOf(depth), parents);
		}
	}
	
	/**
	 * depth 層のクラスタ中心を CLUSTER_SIZES[depth-1] 個作る
	 * @param depth
	 */
	private void initKMeans(int depth) {
		Set<UserCluster> parents = clusterMap.get(Integer.valueOf(depth));
		Set<UserCluster> children = clusterMap.get(Integer.valueOf(depth-1));
		Map<UserCluster, Map<UserCluster, Double>> distances = distanceMap.get(Integer.valueOf(depth));
		int size = CLUSTER_SIZES[depth-1];
		UserCluster clusters[] = new UserCluster[size];
		
		// クラスタ中心を1つ決める
		clusters[0] = children.iterator().next();
		
		// size 個のクラスタ中心まで増やす
		for(int i = 1; i < size; ++i) {
			// TODO implement here
			// calculate distance between clusters[0] and all clusters in children
			// insert them into distanceMap
			// take the furthest (or close to the furthest) cluster form children
			//   as next center cluster (cluster[i])
			
			// 一つ前のクラスタ
			UserCluster cluster = clusters[i-1];
			// その中心との距離を計算する
			calcDistances(depth, cluster);
			
			// TODO implement here
			// 遠いクラスタを次のクラスタ中心 cluster[i] とする
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
			double distance = vectorMultiply(child.vector, parent.vector) / (vectorSize(child.vector) * vectorSize(parent.vector));
			
			if (!distances.containsKey(child)) {
				distances.put(child, new HashMap<UserCluster, Double>());
			}
			distances.get(child).put(parent, Double.valueOf(distance));
		}
	}
	
	private int vectorMultiply(int[] v1, int[] v2) throws IllegalArgumentException {
		if (v1.length != v2.length) {
			throw new IllegalArgumentException("v1.length != v2.length");
		}
		int res = 0;
		for(int i = 0; i < v1.length; ++i) {
			res = res + (v1[i] * v2[i]);
		}
		return res;
	}
	
	private double vectorSize(int[] v) {
		double sum = 0;
		for(int i = 0; i < v.length; ++i) {
			sum = sum + (v[i] * v[i]);
		}
		return Math.sqrt(sum);
	}
}

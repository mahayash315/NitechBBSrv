package models.service.bbanalyzer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
	Set<UserCluster> atomClusters;
	Map<Integer, Set<UserCluster>> clusters;

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
		
		try {
			// ユーザベクトルの大きさを設定
			initUserVectorSize();
			
			// ユーザをアトムクラスタに入れる
			initAtomCluster();
			
			// アトムクラスタをクラスタ分割
			classifyAtomClusters();
		} catch (SQLException e) {
			e.printStackTrace();
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
		atomClusters = new HashSet<UserCluster>();
		
		// 全ユーザを取得
		Set<User> users = new User().findSet();
		
		// 各ユーザについて、アトムクラスタを作る
		for(User user : users) {
			UserCluster cluster = new UserCluster();
			cluster.centerUser = user;
			cluster.vector = user.getUserVector(userVectorSize);
			cluster.children = null;
			atomClusters.add(cluster);
		}
	}
	
	/**
	 * アトムクラスタの上の階層から始め、各階層でクラスタリングする。1階層終わったら上方向に進む。
	 */
	private void classifyAtomClusters() {
		// TODO 
		// (-1)層 : アトムクラスタ
		//   0 層 : 最初のクラスタリング
		//   ...
		// (CLUSTER_DEPTH-1)層 : 最後のクラスタリング
		for(int depth = 0; depth < CLUSTER_DEPTH; ++depth) {
			// TODO 階層 depth において、一つ下の階層 (depth-1) のクラスタをクラスタリングする
		}
	}
}

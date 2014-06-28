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
import models.entity.BBItemWordCount;
import models.entity.BBReadHistory;
import models.entity.BBWord;
import models.entity.User;
import models.service.AbstractService;

public class BBItemClassifier extends AbstractService {
	
	/* 定数 */
	public static final int NUM_CLASS = 3;
	private static final double THRESHOLDS[] = new double[]{-1.0, 1.0};
	
	private static class SQL_BB_READ_HISTORY {
		static final String SQL_SELECT_ITEM_COUNTS = "select "+BBReadHistory.PROPERTY.ITEM+", count("+BBReadHistory.PROPERTY.ID+")"
												  + " from "+BBReadHistory.ENTITY+" group by "+BBReadHistory.PROPERTY.ITEM;
		static final String SQL_SELECT_ITEM_COUNTS_WITH_MIN_OPEN_TIME = "select "+BBReadHistory.PROPERTY.ITEM+", count("+BBReadHistory.PROPERTY.ID+")"
																	  + " from "+BBReadHistory.ENTITY+" where "+BBReadHistory.PROPERTY.OPEN_TIME+" >= ? group by "+BBReadHistory.PROPERTY.ITEM;
		static final String SQL_SELECT_ITEM_COUNTS_FOR_USER = "select "+BBReadHistory.PROPERTY.ITEM+", count("+BBReadHistory.PROPERTY.ID+")"
															+ " from "+BBReadHistory.ENTITY+" where "+BBReadHistory.PROPERTY.USER+" in (?)"
															+ " group by "+BBReadHistory.PROPERTY.ITEM;
		static final String SQL_SELECT_ITEM_COUNTS_WITH_MIN_OPEN_TIME_FOR_USER = "select "+BBReadHistory.PROPERTY.ITEM+", count("+BBReadHistory.PROPERTY.ID+")"
																			   + " from "+BBReadHistory.ENTITY+" where "+BBReadHistory.PROPERTY.OPEN_TIME+" >= ? AND "+BBReadHistory.PROPERTY.USER+" in (?)"
																			   + " group by "+BBReadHistory.PROPERTY.ITEM;

		private static class STATEMENT {
			static PreparedStatement selectItemCounts(Connection conn) throws SQLException {
				return conn.prepareStatement(SQL_SELECT_ITEM_COUNTS);
			}
			static PreparedStatement selectItemCounts(Connection conn, long minOpenTime) throws SQLException {
				PreparedStatement pstmt = conn.prepareStatement(SQL_SELECT_ITEM_COUNTS_WITH_MIN_OPEN_TIME_FOR_USER);
				pstmt.setLong(1, minOpenTime);
				return pstmt;
			}
			static PreparedStatement selectItemCounts(Connection conn, Set<User> users) throws SQLException {
				StringBuilder inClause = new StringBuilder();
				for(User user : users) {
					inClause.append(user.getId());
					inClause.append(",");
				}
				inClause.deleteCharAt(inClause.length()-1);
				PreparedStatement pstmt = conn.prepareStatement(SQL_SELECT_ITEM_COUNTS_FOR_USER);
				pstmt.setString(1, inClause.toString());
				return pstmt;
			}
			static PreparedStatement selectItemCounts(Connection conn, long minOpenTime, Set<User> users) throws SQLException {
				StringBuilder inClause = new StringBuilder();
				for(User user : users) {
					inClause.append(user.getId());
					inClause.append(",");
				}
				inClause.deleteCharAt(inClause.length()-1);
				PreparedStatement pstmt = conn.prepareStatement(SQL_SELECT_ITEM_COUNTS_WITH_MIN_OPEN_TIME_FOR_USER);
				pstmt.setLong(1, minOpenTime);
				pstmt.setString(2, inClause.toString());
				return pstmt;
			}
		}
	}
	
	/* メンバ */
	
	// この識別器の取り付け先クラスタ
	UserCluster userCluster;
	
	// クラスごとの条件付き確率のパラメータ
	Map<Integer, Map<BBWord, Double>> probConds;
	
	/* コンストラクタ */
	private BBItemClassifier() {
		loadProbConds();
	}
	public BBItemClassifier(UserCluster userCluster) {
		this();
		this.userCluster = userCluster;
	}
	
	
	/* インスタンスメソッド */
	/**
	 * クラスタ内の全ユーザの掲示閲覧履歴を学習データとして、好みの掲示を学習する
	 */
	public void train() throws SQLException {
		// TODO implement here
		
		if (userCluster == null) {
			return ;
		}
		Set<User> users = userCluster.getAllUsers();
		Set<BBReadHistory> histories = userCluster.getAllReadHistories(users, null);
		
		try {
			Map<BBWord, Integer> totalWordCounts = new HashMap<BBWord, Integer>();
			
			// 各記事が何度読まれたかをカウントする
			Map<BBItem, Integer> countPerItems = countPerItems(getConnection(), null, users);
			
			// 掲示閲覧履歴から NUM_CLASS クラスへ分類
			Set<BBItem>[] itemSets = classifyFromReadHistory(countPerItems);
			// sets[0] : 興味なし
			//  .
			//  .
			//  .
			// sets[NUM_CLASS-1] : 興味あり
			
			// 記事が開かれた回数だけ、各単語についてカウントする
			// TODO implement here: 興味(あり/なし)をまだ取り入れていない
			// for 各クラス c について
			// 		bag of words を用意する (Map<BBWord, Integer> bagOfWords)
			// 		for クラスに属する掲示の閲覧履歴について
			// 			for 掲示に含まれる各単語について
			// 				bag of words のカウンタを 1 インクリメント
			// 			end
			// 		end
			// 		@    p(word_k in {0,1} | class=c) = p_i^(word_k) * (1-p_i)^(1 - word_k)
			// 		@ と置くと、
			// 		@    p_i = (bagOfWords.containsKey(word)) ? (bagOfWords.get(word) / n) : 0;
			// 		@    ( n : 学習データの総数 = 使った閲覧履歴の総数 )
			// 		クラス c の確率パラメータ P (consists of p_i for all i) を更新
			// end
			// 各クラスについて
			for(int i = 0; i < itemSets.length; ++i) {
				Map<BBWord, Double> probCond = probConds.get(i);
				if (probCond == null) {
					probCond = new HashMap<BBWord, Double>();
				}
				Set<BBItem> itemSet = itemSets[i];
				int totalReadCount = 0;
				totalWordCounts.clear();
				
				// クラスに属する各掲示について
				for(BBItem item : itemSet) {
					// 閲覧回数を取得
					int readCount = (countPerItems.containsKey(item)) ? countPerItems.get(item) : 0;
					totalReadCount = totalReadCount + readCount;
					
					// 掲示に含まれる各単語について
					Set<BBItemWordCount> itemWordCountSet = new BBItemWordCount().findSetByItem(item);
					for(BBItemWordCount itemWordCount : itemWordCountSet) {
						BBWord word = itemWordCount.getWord();
						if (!totalWordCounts.containsKey(word)) {
							totalWordCounts.put(word, Integer.valueOf(0));
						}
						totalWordCounts.put(word, Integer.valueOf(totalWordCounts.get(word)+readCount));
					}
				}
				
				// 条件付き確率のパラメータを計算
				double n = (double) totalReadCount;
				for(BBWord word : totalWordCounts.keySet()) {
					double count = totalWordCounts.get(word).doubleValue();
					double prob = count / n;
					probCond.put(word, Double.valueOf(prob));
				}
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			closeConnection();
		}
	}
	
	/**
	 * 掲示 item をクラス分類する
	 * @param item
	 */
	public void classify(BBItem item) {
		// TODO implement here
	}
	
	
	
	
	
	/**
	 * 掲示閲覧履歴から各掲示を CLASS_NUM クラス(興味なし～興味あり)に分類する
	 * @param countPerItems
	 * @return
	 */
	private Set<BBItem>[] classifyFromReadHistory(Map<BBItem, Integer> countPerItems) {
		Set<BBItem> sets[] = new Set[NUM_CLASS];
		for(Set<BBItem> set : sets) {
			set = new HashSet<BBItem>();
		}
		
		// 全掲示取得
		Set<BBItem> allItems = new BBItem().getAllItems();
		
		// 標準化されたカウントの格納先
		Map<BBItem, Double> standarized = new HashMap<BBItem, Double>();
		
		// 全記事のカウントの平均と分散を計算
		int sum = 0;
		for(BBItem item : allItems) {
			int count = countPerItems.containsKey(item) ? countPerItems.get(item) : 0;
			standarized.put(item, Double.valueOf(count));
			sum = sum + count;
		}
		double mean = ((double) sum) / allItems.size();
		double var = 0;
		for(Double count : standarized.values()) {
			double d = count - mean;
			var = var + d*d;
		}
		
		// 標準化とクラス分類
		for(BBItem item : standarized.keySet()) {
			double d = (standarized.get(item) - mean) / var;
			boolean isClassified = false;
			standarized.put(item, Double.valueOf(d));
			for(int i = 0; i < sets.length; ++i) {
				if (THRESHOLDS[i] < d) {
					sets[i].add(item);
					isClassified = true;
					break;
				}
			}
			if (!isClassified) {
				sets[(sets.length-1)].add(item);
			}
		}
		
		return sets;
	}
	
	
	/**
	 * 掲示閲覧履歴から掲示ごとに開かれた回数をカウントして返す
	 * @param conn
	 * @param minOpenTime
	 * @param users
	 * @return
	 * @throws SQLException
	 */
	private Map<BBItem, Integer> countPerItems(Connection conn, Long minOpenTime, Set<User> users) throws SQLException {
		ResultSet rs = null;
		try {
			HashMap<BBItem, Integer> counts = new HashMap<BBItem, Integer>();
			if (minOpenTime == null) {
				rs = SQL_BB_READ_HISTORY.STATEMENT.selectItemCounts(conn, users).executeQuery();
			} else {
				rs = SQL_BB_READ_HISTORY.STATEMENT.selectItemCounts(conn, minOpenTime, users).executeQuery();
			}
			while(rs.next()) {
				long id = rs.getLong(1);
				int count = rs.getInt(2);
				
				BBItem item = new BBItem(Long.valueOf(id)).unique();
				if (item != null) {
					counts.put(item, Integer.valueOf(count));
				}
			}
			return counts;
		} catch (SQLException e) {
			throw e;
		} finally {
			if (rs != null) {
				rs.close();
			}
		}
	}
	
	
	
	private void loadProbConds() {
		probConds = new HashMap<Integer, Map<BBWord, Double>>();
	}
}

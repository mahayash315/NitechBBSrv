package models.service.bbanalyzer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import models.entity.User;
import models.entity.bbanalyzer.BBItem;
import models.entity.bbanalyzer.BBItemClassifier;
import models.entity.bbanalyzer.BBItemWordCount;
import models.entity.bbanalyzer.BBReadHistory;
import models.entity.bbanalyzer.BBUserCluster;
import models.entity.bbanalyzer.BBWord;
import models.service.AbstractService;
import utils.bbanalyzer.BBAnalyzerUtil;
import utils.bbanalyzer.LogUtil;

public class ItemClassifier extends AbstractService {
	
	/* 定数 */
	public static final int NUM_CLASS = 3;
	public static final int NO_CLASS_NUM = -1;
	private static final double THRESHOLDS[] = new double[]{-1.0, 1.0};
	private static final int DEFAULT_CLASSIFY_TO = NO_CLASS_NUM;
	private static final int MIN_TRAINIG_DATA_COUNT = 1;
	
	private static class SQL_BB_READ_HISTORY {
		public static final int PARAM_SIZE = 100;
		static final String PARAMS = "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?";;
		
		static final String SQL_SELECT_ITEM_COUNTS = "select "+BBReadHistory.PROPERTY.ITEM+", count("+BBReadHistory.PROPERTY.ID+")"
												   + " from "+BBReadHistory.ENTITY+" group by "+BBReadHistory.PROPERTY.ITEM;
		static final String SQL_SELECT_ITEM_COUNTS_WITH_MIN_OPEN_TIME = "select "+BBReadHistory.PROPERTY.ITEM+", count("+BBReadHistory.PROPERTY.ID+")"
																	  + " from "+BBReadHistory.ENTITY+" where "+BBReadHistory.PROPERTY.OPEN_TIME+" >= ? group by "+BBReadHistory.PROPERTY.ITEM;
		static final String SQL_SELECT_ITEM_COUNTS_FOR_USER = "select "+BBReadHistory.PROPERTY.ITEM+", count("+BBReadHistory.PROPERTY.ID+")"
															+ " from "+BBReadHistory.ENTITY+" where "+BBReadHistory.PROPERTY.USER+" in ("+PARAMS+")"
															+ " group by "+BBReadHistory.PROPERTY.ITEM;
		static final String SQL_SELECT_ITEM_COUNTS_WITH_MIN_OPEN_TIME_FOR_USER = "select "+BBReadHistory.PROPERTY.ITEM+", count("+BBReadHistory.PROPERTY.ID+")"
																			   + " from "+BBReadHistory.ENTITY+" where "+BBReadHistory.PROPERTY.USER+" in ("+PARAMS+") and "+BBReadHistory.PROPERTY.OPEN_TIME+" >= ?"
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
				PreparedStatement pstmt = conn.prepareStatement(SQL_SELECT_ITEM_COUNTS_FOR_USER);

				int size = users.size();
				if (PARAM_SIZE < size) {
					pstmt.close();
					throw new SQLException("The input param size "+size+" is larger than supported size "+PARAM_SIZE);
				}
				
				int i = 1;
				for(User user : users) {
					pstmt.setLong(i, user.getId());
					++i;
				}
				for(; i <= PARAM_SIZE; ++i) {
					pstmt.setNull(i, java.sql.Types.BIGINT);
				}
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
				
				int size = users.size();
				if (PARAM_SIZE < size) {
					pstmt.close();
					throw new SQLException("The input param size "+size+" is larger than supported size "+PARAM_SIZE);
				}
				
				int i = 1;
				for(User user : users) {
					pstmt.setLong(i, user.getId());
					++i;
				}
				for(; i <= PARAM_SIZE; ++i) {
					pstmt.setNull(i, java.sql.Types.BIGINT);
				}

				pstmt.setLong(2, minOpenTime);
				return pstmt;
			}
		}
	}
	
	/* メンバ */
	
	// この識別器の取り付け先クラスタ
	protected UserCluster userCluster;
	protected BBUserCluster bbUserCluster;
	
	// 事前確率のパラメータ
	protected Map<Integer, Double> probPrior;
	
	// クラスごとの条件付き確率のパラメータ
	protected Map<Integer, Map<BBWord, Double>> probConds;
	
	// 学習回数
	protected int trainingCount;
	
	// 学習データの総数
	protected int trainingDataCount;
	
	/* コンストラクタ */
	public ItemClassifier() {
		init();
	}
	public ItemClassifier(UserCluster userCluster) {
		this();
		this.userCluster = userCluster;
		this.bbUserCluster = new BBUserCluster(userCluster).unique();
		if (!loadProbs() || !loadCounters()) {
			init();
		}
	}
	
	
	/* インスタンスメソッド */
	/**
	 * クラスタ内の全ユーザの掲示閲覧履歴を学習データとして、好みの掲示を学習する
	 */
	public void train() throws SQLException {
		// TODO test this method
		
		if (userCluster == null) {
			return ;
		}
		
		try {
			Set<User> users = userCluster.getAllUsers();
			Map<BBWord, Integer> totalWordCounts = new HashMap<BBWord, Integer>();
			int totalItemCount = 0;
			
			// 各記事が何度読まれたかをカウントする
			Map<BBItem, Integer> countPerItems = calcCountPerItems(getConnection(), null, users);
			
			// 掲示閲覧履歴から NUM_CLASS クラスへ分類
			Map<Integer, Set<BBItem>> itemSets = classifyItemsFromReadHistory(countPerItems);
			// sets[0](興味なし), ..., sets[NUM_CLASS-1](興味あり)
			
			// 記事が開かれた回数だけ、各単語についてカウントする
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
			for(int i = 1; i <= NUM_CLASS; ++i) {
				Map<BBWord, Double> probCond = probConds.get(i);
				if (probCond == null) {
					probCond = new HashMap<BBWord, Double>();
				}
				Set<BBItem> itemSet = itemSets.get(Integer.valueOf(i));
				int totalReadCount = 0;
				totalWordCounts.clear();
				
				// クラスに属する各掲示について
				for(BBItem item : itemSet) {
					++totalItemCount;
					
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
				if (0 < totalReadCount) {
					double n = (double) totalReadCount;
					for(BBWord word : totalWordCounts.keySet()) {
						double count = totalWordCounts.get(word).doubleValue();
						double prob = count / n;
						probCond.put(word, Double.valueOf(prob));
					}
				}
				probConds.put(Integer.valueOf(i), probCond);
			}
			
			
			// 事前確率の計算
			if (0 < totalItemCount) {
				double div = (double) totalItemCount;
				for(int i = 1; i <= NUM_CLASS; ++i) {
					double prob = (double) itemSets.get(Integer.valueOf(i)).size() / div;
					probPrior.put(Integer.valueOf(i), Double.valueOf(prob));
				}
			}
			
			// カウンタ更新
			++trainingCount;
			trainingDataCount = trainingDataCount + countPerItems.size();
			
			// パラメータの保存
			saveProbs();
			saveCounters();
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
	public int classify(BBItem item) {
		// TODO test this method
		
		if (trainingDataCount < MIN_TRAINIG_DATA_COUNT) {
			LogUtil.info("BBItemClassifier#classify(): (classifier["+toString()+"]) not trained enough");
			return DEFAULT_CLASSIFY_TO;
		}

		// クラスごとの事後確率の格納先
		Map<Integer, Double> probs = new HashMap<Integer, Double>();
		int maxClass = 0;
		double maxProb = 0;

		Set<BBWord> features = BBAnalyzerUtil.use().extractFeatures(item);
		
		for(int i = 1; i <= NUM_CLASS; ++i) {
			double prior = probPrior.get(Integer.valueOf(i)).doubleValue();
			Map<BBWord, Double> probCond = probConds.get(Integer.valueOf(i));
			
			double prob = Math.log(1.0+prior);
//			LogUtil.info("BBItemClassifier#classify(): CLASS["+i+"] prob = "+Math.log(1.0+prior));
			for(BBWord word : probCond.keySet()) {
				double cond = probCond.get(word).doubleValue();
				if (features.contains(word)) {
					prob = prob + Math.log(1.0+cond);
//					LogUtil.info("BBItemClassifier#classify(): CLASS["+i+"] prob = prob + "+Math.log(1.0+cond)+" (cond="+cond+") <- "+word);
				} else {
					prob = prob + Math.log(2.0-cond);
//					LogUtil.info("BBItemClassifier#classify(): CLASS["+i+"] prob = prob + "+Math.log(2.0-cond)+" (cond="+cond+") <- "+word);
				}
			}
			probs.put(Integer.valueOf(i), prob);
			
			if (maxProb < prob) {
				maxClass = i;
				maxProb = prob;
			}
			LogUtil.info("BBItemClassifier#classify(): (depth="+userCluster.depth+") CLASS["+i+"] calculated prob="+prob);
		}
		LogUtil.info("BBItemClassifier#classify(): (depth="+userCluster.depth+") classified as CLASS["+maxClass+"]");
		
		return maxClass;
	}
	
	/**
	 * 掲示閲覧履歴から各掲示を CLASS_NUM クラス(興味なし～興味あり)に分類する
	 * @param countPerItems
	 * @return
	 */
	private Map<Integer, Set<BBItem>> classifyItemsFromReadHistory(Map<BBItem, Integer> countPerItems) {
		Map<Integer, Set<BBItem>> sets = new HashMap<Integer, Set<BBItem>>();
		for(int i = 1; i <= NUM_CLASS; ++i) {
			sets.put(Integer.valueOf(i), new HashSet<BBItem>());
		}
		
		// 全掲示取得
		List<BBItem> allItems = new BBItem().getAllItemsAsList();
		int size = allItems.size();
		double dsize = (double) size;
		
		// 一時変数用意
		int i = 0, j = 0;
		int sum = 0;
		double var = 0;
		double mean = 0;
		int counts[] = new int[size];
		double standarized[] = new double[size];
		
		// 全記事のカウント
		for(i = 0; i < size; ++i) {
			BBItem item = allItems.get(i);
			int count = countPerItems.containsKey(item) ? countPerItems.get(item) : 0;
			counts[i] = count;
			sum = sum + count;
		}

		// 全記事のカウントの平均と分散を計算
		mean = ((double) sum) / dsize;
		for(double count : standarized) {
			double d = count - mean;
			var = var + d*d;
		}
		var = var / (dsize - 1.0);
		double div = (var == 0.0) ? 1.0 : var;
		
		// 標準化
		for(i = 0; i < size; ++i) {
			double d = ((double) counts[i] - mean) / div;
			standarized[i] = d;
		}
		
		// クラス分類
		for(i = 0; i < size; ++i) {
			BBItem item = allItems.get(i);
			int classifiedTo = DEFAULT_CLASSIFY_TO;
			
			double eval = standarized[i];
			
			if (eval <= THRESHOLDS[0]) {
				sets.get(Integer.valueOf(1)).add(item);
				classifiedTo = 1;
			} else if (THRESHOLDS[NUM_CLASS-2] <= eval) {
				sets.get(Integer.valueOf(NUM_CLASS-1)).add(item);
				classifiedTo = NUM_CLASS;
			} else {
				for(j = 1; j < NUM_CLASS-1; ++j) {
					if (THRESHOLDS[j-1] <= eval && eval < THRESHOLDS[j]) {
						sets.get(Integer.valueOf(j+1)).add(item);
						classifiedTo = j+1;
						break;
					}
				}
			}
			LogUtil.info("BBItemClassifier#classifyItemsFromReadHistory():\n classified to CLASS["+classifiedTo+"] with eval="+eval+", item "+item);
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
	private Map<BBItem, Integer> calcCountPerItems(Connection conn, Long minOpenTime, Set<User> users) throws SQLException {
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
	
	
	private void init() {
		probPrior = new HashMap<Integer, Double>();
		probConds = new HashMap<Integer, Map<BBWord, Double>>();
		trainingCount = 0;
		trainingDataCount = 0;
		
		for(int i = 1; i <= NUM_CLASS; ++i) {
			probPrior.put(Integer.valueOf(i), Double.valueOf(0));
			probConds.put(Integer.valueOf(i), new HashMap<BBWord, Double>());
		}
	}
	
	private boolean loadProbs() {
		// TODO test this method
		for(int i = 1; i <= NUM_CLASS; ++i) {
			BBItemClassifier bbItemClassifier = new BBItemClassifier(bbUserCluster, i).unique();
			if (bbItemClassifier == null) {
				return false;
			}
			probPrior.put(Integer.valueOf(i), bbItemClassifier.getProbPrior());
			probConds.put(Integer.valueOf(i), bbItemClassifier.getProbCond());
		}
		return true;
	}
	
	private void saveProbs() {
		for(int i = 1; i <= NUM_CLASS; ++i) {
			BBItemClassifier bbItemClassifier = new BBItemClassifier(bbUserCluster, i).uniqueOrStore();
			bbItemClassifier.setProbPrior(probPrior.get(Integer.valueOf(i)));
			bbItemClassifier.setProbCond(probConds.get(Integer.valueOf(i)));
			bbItemClassifier.store();
		}
	}
	
	private boolean loadCounters() {
		BBItemClassifier bbItemClassifier = new BBItemClassifier(bbUserCluster, 0).unique();
		if (bbItemClassifier == null) {
			return false;
		}
		trainingCount = bbItemClassifier.getTrainingCount();
		trainingDataCount = bbItemClassifier.getTrainintDataCount();
		return true;
	}
	
	private void saveCounters() {
		BBItemClassifier bbItemClassifier = new BBItemClassifier(bbUserCluster, 0).uniqueOrStore();
		bbItemClassifier.setTrainingCount(trainingCount);
		bbItemClassifier.setTrainintDataCount(trainingDataCount);
		bbItemClassifier.store();
	}

	/* hashCode, equals */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((userCluster == null) ? 0 : userCluster.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ItemClassifier other = (ItemClassifier) obj;
		if (userCluster == null) {
			if (other.userCluster != null)
				return false;
		} else if (!userCluster.equals(other.userCluster))
			return false;
		return true;
	}
	
	/* toString */
	@Override
	public String toString() {
		return "probPrior="+probPrior+", probConds="+probConds+", trainingCount="+trainingCount+", trainingDataCount="+trainingDataCount+", userCluster="+userCluster;
	}
}

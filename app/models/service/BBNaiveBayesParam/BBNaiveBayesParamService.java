package models.service.BBNaiveBayesParam;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import models.entity.BBCategory;
import models.entity.BBItemHead;
import models.entity.BBReadHistory;
import models.entity.User;
import models.setting.BBItemAppendixSetting;

public class BBNaiveBayesParamService {
	
	private static final long DEFAULT_FETCH_OPENTIME_SPAN = 30L * 24L * 60L * 60L * 1000L;
	
	private User user;

	public static BBNaiveBayesParamService use() {
		return new BBNaiveBayesParamService();
	}
	
	public void calcParam(User user) throws Exception {
		this.user = user;
		
		// 表示履歴を取得
		Long minOpenTime = Long.valueOf(System.currentTimeMillis() - DEFAULT_FETCH_OPENTIME_SPAN);
		List<BBReadHistory> readHistories = new BBReadHistory().findListForUser(user, minOpenTime, null);
		
		// 表示履歴から掲示をカテゴリ分け
		Map<BBCategory, Set<BBItemHead>> categorized = categorize(readHistories);
		
		// 各カテゴリで NaiveBayes パラメータを計算する
		
	}
	
	/**
	 * 掲示表示履歴から掲示をカテゴリ分け
	 * @param readHistories
	 * @return
	 * @throws Exception 
	 */
	private Map<BBCategory,Set<BBItemHead>> categorize(List<BBReadHistory> readHistories) throws Exception {
		Map<BBCategory, Set<BBItemHead>> result = new HashMap<BBCategory, Set<BBItemHead>>();
		Map<BBItemHead, Double> weights = new HashMap<BBItemHead, Double>();
		
		// Map 初期化
		for(String catName : BBItemAppendixSetting.CATEGORY_NAMES) {
			BBCategory category = new BBCategory(user, catName).unique();
			if (category == null) {
				throw new Exception("Missing category "+catName+" for user "+user.toString());
			}
			result.put(category, new HashSet<BBItemHead>());
		}
		for(BBReadHistory history : readHistories) {
			BBItemHead item = history.getItem();
			if (!weights.containsKey(item)) {
				weights.put(item, Double.valueOf(0.0));
			}
		}
		
		Set<BBItemHead> items = weights.keySet();
		
		// 各記事の閲覧回数・閲覧時間から重みを計算
		for(BBReadHistory history : readHistories) {
			BBItemHead item = history.getItem();
			Double weight = weights.get(item);
			
			// TODO openTime からの経過時間を考慮した（時間が経つ程興味が減る）掲示の重み weight を計算
		}
		
		// 重みから各記事をカテゴリ分け
		for(BBItemHead item : items) {
			Double weight = weights.get(item);
			
			// TODO カテゴリ分け
			BBCategory category = null;
			
			result.get(category).add(item);
		}
		
		return result;
	}
	
	
	/**
	 * NaiveBayes パラメータを計算する
	 * @param category
	 * @param items
	 */
	private void calcParamPerCategory(BBCategory category, Set<BBItemHead> items) {
		
	}
}

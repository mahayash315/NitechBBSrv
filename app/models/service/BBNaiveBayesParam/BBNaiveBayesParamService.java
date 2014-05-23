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
	private static final double PARAM_CATEGORIZE_SPAN = 0.5;
	
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
		Map<BBItemHead, ParamHolder> params = new HashMap<BBItemHead, ParamHolder>();
		
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
			if (!params.containsKey(item)) {
				params.put(item, new ParamHolder());
			}
		}
		
		Set<BBItemHead> items = params.keySet();
		
		// 各記事の閲覧回数を集計
		for(BBReadHistory history : readHistories) {
			BBItemHead item = history.getItem();
			ParamHolder param = params.get(item);
			
			param.count = param.count + 1.0;
		}
		
		// 記事閲覧回数の平均と分散を計算
		double d_tmp = 0.0;
		double d_diff_from_average = 0.0;
		double count_average = 0.0;
		double count_variance = 0.0;
		double pow_count_variance = 0.0;
		double total_item_num = (double) items.size();
		double normalized_value = 0.0;
		for(BBItemHead item : items) {
			ParamHolder param = params.get(item);
			count_average += param.count / total_item_num;
		}
		for(BBItemHead item : items) {
			ParamHolder param = params.get(item);
			d_tmp = (param.count - count_average);
			d_diff_from_average = d_tmp*d_tmp;
		}
		count_variance = d_diff_from_average / total_item_num;
		pow_count_variance = count_variance * count_variance;
		
		double total_category_num = (double) BBItemAppendixSetting.CATEGORY_NAMES.length;
		double half_category_num = total_category_num / 2.0;
		// 集計結果から各記事をカテゴリ分け
		for(BBItemHead item : items) {
			ParamHolder param = params.get(item);
			
			// 標準化
			normalized_value = (param.count - count_average) / pow_count_variance;
			
			// カテゴリ分類
			int catNum = (int) ((normalized_value + half_category_num) / (total_category_num-1.0));
			catNum = catNum + 1;
			if (catNum < 1) {
				catNum = 1;
			} else if (BBItemAppendixSetting.CATEGORY_NAMES.length <= catNum) {
				catNum = BBItemAppendixSetting.CATEGORY_NAMES.length;
			}

			BBCategory category = new BBCategory(user, String.valueOf(catNum)).unique();
			if (category == null) {
				throw new Exception("Missing category "+catNum+" for user "+user.toString());
			}
			
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
	
	
	
	private class ParamHolder {
		public double count = 0;
	}
}

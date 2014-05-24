package models.service.BBNaiveBayesParam;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import models.entity.BBCategory;
import models.entity.BBItemHead;
import models.entity.BBNaiveBayesParam;
import models.entity.BBWord;
import models.entity.User;
import models.setting.BBItemAppendixSetting;

import org.atilika.kuromoji.Token;
import org.atilika.kuromoji.Tokenizer;

public class BBNaiveBayesParamService {
	
	private static final double DEFAULT_GAUSS_MYU_VALUE = 0.0;
	private static final double DEFAULT_POISSON_LAMBDA_VALUE = 0.0;
	
	private Tokenizer tokenizer;
	private User user;
	private Set<BBCategory> categories;

	public static BBNaiveBayesParamService use() {
		return new BBNaiveBayesParamService();
	}
	
	public BBNaiveBayesParamService() {
		tokenizer = Tokenizer.builder().build();
	}
	
	public void calcParam(User user) throws Exception {
		this.user = user;
		
		// パラメータ初期化
		initParamsForUser();

		// カテゴリ一覧取得
		categories = new HashSet<BBCategory>();
		for(String catName : BBItemAppendixSetting.CATEGORY_NAMES) {
			BBCategory category = new BBCategory(user, catName).unique();
			if (category == null) {
				throw new Exception("Missing category "+catName+" for user "+user.toString());
			}
			categories.add(category);
		}
		
		// 各カテゴリで NaiveBayes パラメータを計算する
		for(BBCategory category : categories) {
			Set<BBItemHead> itemsInCategory = new BBItemHead().findSetByUserCategory(user, category);
			calcParamPerCategory(category, itemsInCategory);
		}
	}
	
	/**
	 * ユーザに対する推定に使用するパラメータを初期化する。
	 * 現在取得済みの BBItemHead すべてに含まれる単語に関して
	 * 重みを初期化する
	 * @throws Exception 
	 */
	public void initParamsForUser() throws Exception {
		if (user == null) {
			return;
		}
		
		// 全記事取得
		List<BBItemHead> items = new BBItemHead().findListForUser(user);
		if (items == null) {
			return;
		}
		
		// 全カテゴリ取得
		Set<BBCategory> categories = new HashSet<BBCategory>();
		for(String catName : BBItemAppendixSetting.CATEGORY_NAMES) {
			categories.add(new BBCategory(user, catName).uniqueOrStore());
		}
		
		// パラメータを初期化
		Set<BBWord> words = new HashSet<BBWord>();
		for(BBItemHead item : items) {
			String title = item.getTitle();
			if (title == null || title.isEmpty()) {
				continue;
			}
			
			List<Token> tokens = tokenizer.tokenize(title);
			for(Token token : tokens) {
				BBWord word = new BBWord(token.getSurfaceForm(), token.getAllFeaturesArray(), token.isKnown()).uniqueOrStore();
				if (word == null) {
					throw new Exception("Failed to unique() or store() word "+word.toString()+" for user "+user.toString());
				}
				
				for(BBCategory category : categories) {
					BBNaiveBayesParam param = new BBNaiveBayesParam(user, word, category, DEFAULT_GAUSS_MYU_VALUE, DEFAULT_POISSON_LAMBDA_VALUE);
					param.store();
				}
			}
		}
	}
	
	/**
	 * NaiveBayes パラメータを計算する
	 * @param category
	 * @param items
	 * @throws Exception 
	 */
	private void calcParamPerCategory(BBCategory category, Set<BBItemHead> items) throws Exception {
		// Map 初期化
		Map<BBWord, Double> words = new HashMap<BBWord, Double>();
		
		double total_item_num = (double) items.size();
		
		// 各掲示から単語を集計
		for(BBItemHead item : items) {
			// 掲示タイトルを形態素解析
			List<Token> tokens = tokenizer.tokenize(item.getTitle());

			Map<BBWord, Integer> wordsPerItem = new HashMap<BBWord, Integer>();
			// List<Token> から Map<BBWord, Integer> に変換, およびパラメータの取得
			for(Token token : tokens) {
				if (isNounOrVerb(token)) {
					BBWord word = new BBWord(token.getSurfaceForm()).unique();
					if (word == null) {
						throw new Exception("Missing param for "+token.getSurfaceForm()+", user "+user.toString());
					}
					if (!wordsPerItem.containsKey(word)) {
						wordsPerItem.put(word, Integer.valueOf(0));
					}
					wordsPerItem.put(word, wordsPerItem.get(word) + 1);
				}
			}

			// 各単語の出現数の平均をパラメータとする
			for(BBWord word : wordsPerItem.keySet()) {
				Integer count = wordsPerItem.get(word);
				if (count == null) {
					count = Integer.valueOf(0);
				}
				if (!words.containsKey(word)) {
					words.put(word, Double.valueOf(0.0));
				}
				Double d = words.get(word);
				Double new_d = Double.valueOf(d.doubleValue() + (count.doubleValue() / total_item_num));
				words.put(word, new_d);
			}
		}
		
		// 結果の保存
		for(BBWord word : words.keySet()) {
			Double d = words.get(word);
			BBNaiveBayesParam param = new BBNaiveBayesParam(user, word, category).uniqueOrStore();
			if (param == null) {
				throw new Exception("Failed to unique() or store() BBNaiveBayesParam for user "+user.toString()+", word "+word.toString()+", category "+category.toString());
			}
			param.setGaussMyu(d);
			param.setPoissonLambda(d);
			param.store();
		}
	}
	
	private boolean isNounOrVerb(Token t) {
		String[] arr = t.getAllFeaturesArray();
		return (arr[0].contains("名詞") || arr[0].equals("動詞"));
	}
	
	
	
	private class ParamHolder {
		public double count = 0;
	}
}

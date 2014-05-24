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

import play.Logger;

public class BBNaiveBayesParamService {
	
	private static final double DEFAULT_GAUSS_MYU_VALUE = 0.0;
	private static final double DEFAULT_POISSON_LAMBDA_VALUE = 10E-5;
	
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
		
		// 全カテゴリ取得
		categories = new HashSet<BBCategory>();
		for(String catName : BBItemAppendixSetting.CATEGORY_NAMES) {
			categories.add(new BBCategory(user, catName).uniqueOrStore());
		}
		
		// パラメータ初期化
		Logger.info("BBNaiveBayesParamService#calcParam(): ----------------- initParamForUser(): began -----------------");
		initParamsForUser();
		Logger.info("BBNaiveBayesParamService#calcParam(): ----------------- initParamForUser(): done  -----------------");

		// 各カテゴリで NaiveBayes パラメータを計算する
		for(BBCategory category : categories) {
			Set<BBItemHead> itemsInCategory = new BBItemHead().findSetByUserCategory(user, category);
			Logger.info("BBNaiveBayesParamService#calcParam(): began (category = "+category.getName()+")");
			calcParamPerCategory(category, itemsInCategory);
			Logger.info("BBNaiveBayesParamService#calcParam(): end");
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
		
		Map<String, Token> allTokens = new HashMap<String, Token>();
		
		// 全記事取得
		List<BBItemHead> items = new BBItemHead().findListForUser(user);
		if (items == null) {
			return;
		}
		
		// トークンを抽出
		Set<BBWord> words = new HashSet<BBWord>();
		for(BBItemHead item : items) {
			String title = item.getTitle();
			if (title == null || title.isEmpty()) {
				continue;
			}
			
			List<Token> tokens = tokenizer.tokenize(title);
			for(Token token : tokens) {
				if (isNounOrVerbAndNotNumber(token) && !allTokens.containsKey(token.getSurfaceForm())) {
					allTokens.put(token.getSurfaceForm(), token);
				}
			}
		}
		
		for(String surface : allTokens.keySet()) {
			Token token = allTokens.get(surface);
			Logger.info("surface = "+token.getSurfaceForm()+", features = "+token.getAllFeatures());
		}
		
		// パラメータ初期化
		for(String surface : allTokens.keySet()) {
			Token token = allTokens.get(surface);
			BBWord word = new BBWord(token.getSurfaceForm(), token.isKnown()).uniqueOrStore();
			if (word == null) {
				throw new Exception("Failed to unique() or store() word "+word.toString()+" for user "+user.toString());
			}

			for(BBCategory category : categories) {
				BBNaiveBayesParam param = new BBNaiveBayesParam(user, word, category).uniqueOrStore();
				param.setGaussMyu(DEFAULT_GAUSS_MYU_VALUE);
				param.setPoissonLambda(DEFAULT_POISSON_LAMBDA_VALUE);
				param = param.store();
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
		Map<String, Double> wordCounts = new HashMap<String, Double>();
		
		double total_item_num = (double) items.size();
		
		// 各掲示から単語を集計
		for(BBItemHead item : items) {
			// 掲示タイトルを形態素解析
			List<Token> tokens = tokenizer.tokenize(item.getTitle());

			Map<String, Integer> wordsPerItem = new HashMap<String, Integer>();
			// List<Token> から Map<BBWord, Integer> に変換, およびパラメータの取得
			for(Token token : tokens) {
				if (isNounOrVerbAndNotNumber(token)) {
					String surface = token.getSurfaceForm();
					if (!wordsPerItem.containsKey(surface)) {
						wordsPerItem.put(surface, Integer.valueOf(0));
					}
					wordsPerItem.put(surface, wordsPerItem.get(surface) + 1);
				}
			}

			// 各単語の出現数の平均をパラメータとする
			for(String surface : wordsPerItem.keySet()) {
				Integer count = wordsPerItem.get(surface);
				if (count == null) {
					count = Integer.valueOf(0);
				}
				if (!wordCounts.containsKey(surface)) {
					wordCounts.put(surface, Double.valueOf(0.0));
				}
				Double d = wordCounts.get(surface);
				Double new_d = Double.valueOf(d.doubleValue() + (count.doubleValue() / total_item_num));
				wordCounts.put(surface, new_d);
			}
		}
		
		// 結果の保存
		for(String surface : wordCounts.keySet()) {
			BBWord word = new BBWord(surface).unique();
			BBNaiveBayesParam param = new BBNaiveBayesParam(user, word, category).unique();
			if (param == null) {
				throw new Exception("Missing BBNaiveBayesParam for user "+user.toString()+", word "+word.toString()+", category "+category.toString());
			}
			Double d = wordCounts.get(surface);
			param.setGaussMyu(d);
			param.setPoissonLambda(d);
			param.store();
		}
	}
	
	private boolean isNounOrVerbAndNotNumber(Token t) {
		String[] arr = t.getAllFeaturesArray();
		return ((arr[0].contains("名詞") || arr[0].equals("動詞")) && !arr[1].equals("数"));
	}
	
	
	
	private class ParamHolder {
		public double count = 0;
	}
}

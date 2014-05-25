package models.service.BBAnalyzer;

import java.util.ArrayList;
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
import utils.bbanalyzer.MathUtil;

public class BBAnalyzerService {
	
	private static final double DEFAULT_GAUSS_MYU_VALUE = 0.0;
	private static final double DEFAULT_POISSON_LAMBDA_VALUE = 10E-4;
	
	private MathUtil mathUtil;
	private Tokenizer tokenizer;
	private User user;
	private Map<BBCategory, Set<BBItemHead>> categorized;
	private Set<String> surfaceSet;
	
	public static BBAnalyzerService use() {
		return new BBAnalyzerService();
	}
	
	public BBAnalyzerService() {
		// init
		mathUtil = new MathUtil();
		tokenizer = Tokenizer.builder().build();
	}
	
	
	public void train(User user) throws Exception {
		this.user = user;
		
		// categorized 初期化
		categorized = new HashMap<BBCategory, Set<BBItemHead>>();
		for(String catName : BBItemAppendixSetting.CATEGORY_NAMES) {
			BBCategory category = new BBCategory(user, catName).uniqueOrStore();
			categorized.put(category, new HashSet<BBItemHead>());
		}
		
		// パラメータ初期化
		initParams();

		// 各カテゴリで NaiveBayes パラメータを計算する
		for(BBCategory category : categorized.keySet()) {
			Logger.info("BBNaiveBayesParamService#calcParam(): began (category = "+category.getName()+")");
			calcParamPerCategory(category);
			Logger.info("BBNaiveBayesParamService#calcParam(): end");
		}
		
		surfaceSet = null;
		categorized = null;
		user = null;
	}
	
	public BBCategory estimate(User user, BBItemHead item) throws Exception {
		this.user = user;
		
		return estimateCategoryUsingNaiveBayes(item);
	}
	
	
	/**
	 * 解析の準備
	 *   - 単語の抽出 → 辞書の作成
	 *   - カテゴリ既知掲示のカテゴリ別分類
	 * @throws Exception 
	 */
	private void initParams() throws Exception {
		if (user == null) {
			return;
		}
		
		// 全記事取得
		List<BBItemHead> items = new BBItemHead().findListForUser(user);
		if (items == null) {
			return;
		}
		
		surfaceSet = new HashSet<String>();
		
		// トークンの抽出とカテゴリ分類
		for(BBItemHead item : items) {
			String title = item.getTitle();
			if (title == null || title.isEmpty()) {
				continue;
			}
			
			List<Token> tokens = tokenizer.tokenize(title);
			for(Token token : tokens) {
				String surface = token.getSurfaceForm();
				if (isNounOrVerbAndNotNumber(token) && !surfaceSet.contains(surface)) {
					surfaceSet.add(token.getSurfaceForm());
				}
			}
			
			BBCategory category = item.getAppendix().getCategory();
			if (category == null) {
				throw new Exception("BBItem has null category");
			}
			categorized.get(category).add(item);
		}
		
		// 初期化
		for(String surface : surfaceSet) {
			BBWord word = new BBWord(surface).uniqueOrStore();
			
			Logger.info("surface = "+surface);
			
			for(BBCategory category : categorized.keySet()) {
				BBNaiveBayesParam param = new BBNaiveBayesParam(user, word, category).uniqueOrStore();
				param.setGaussMyu(DEFAULT_GAUSS_MYU_VALUE);
				param.setPoissonLambda(DEFAULT_POISSON_LAMBDA_VALUE);
				param.store();
			}
		}
	}
	
	
	/**
	 * NaiveBayes パラメータを計算する
	 * @param category
	 * @param items
	 * @throws Exception 
	 */
	private void calcParamPerCategory(BBCategory category) throws Exception {
		// Map 初期化
		Map<String, Double> averageCounter = new HashMap<String, Double>();

		// 記事取得
		Set<BBItemHead> items = categorized.get(category);
		double total_item_num = (double) items.size();
		double weight_per_word = 1.0 / total_item_num;
		
		// 各掲示から単語を集計
		for(BBItemHead item : items) {
			// 掲示タイトルを形態素解析
			List<Token> tokens = tokenizer.tokenize(item.getTitle());

			for(Token token : tokens) {
				if (isNounOrVerbAndNotNumber(token)) {
					String surface = token.getSurfaceForm();
					if (!averageCounter.containsKey(surface)) {
						averageCounter.put(surface, Double.valueOf(0.0));
					}
					double d = averageCounter.get(surface).doubleValue();
					d = d + weight_per_word;
					averageCounter.put(surface, Double.valueOf(d));
				}
			}
		}
		
		// 結果の保存
		for(String surface : averageCounter.keySet()) {
			// 平均の出現数を取得
			Double avg = averageCounter.get(surface);

			// オブジェクト取得
			BBWord word = new BBWord(surface).unique();
			BBNaiveBayesParam param = new BBNaiveBayesParam(user, word, category).unique();
			if (param == null) {
				throw new Exception("Missing BBNaiveBayesParam for user "+user.toString()+", word "+word.toString()+", category "+category.toString());
			}
			
			// 保存
			param.setGaussMyu(avg);
			param.setPoissonLambda(avg);
			param.store();
		}
	}
	
	/**
	 * ナイーブベイズ推定で掲示のカテゴリを推定する
	 * @param item
	 * @return
	 * @throws Exception
	 */
	private BBCategory estimateCategoryUsingNaiveBayes(BBItemHead item) throws Exception {
		// アサート
		if (item == null || item.getTitle() == null) {
			Logger.error("BBAnalyzerService#estimateCategoryUsingNaiveBayes(item): null item given, or item.getTitle() == null");
			return null;
		}
		
		// カテゴリ一覧を取得
		List<BBCategory> categories = new ArrayList<BBCategory>();
		for(String catName : BBItemAppendixSetting.CATEGORY_NAMES) {
			BBCategory category = new BBCategory(user, catName).unique();
			if (category == null) {
				throw new Exception("Missing category "+catName+" for user "+user.toString());
			}
			categories.add(category);
		}
		
		// 掲示タイトルを形態素解析
		List<Token> tokens = tokenizer.tokenize(item.getTitle());
		
		// List<Token> から Map<BBWord, Integer> に変換, およびパラメータの取得
		Map<String, Integer> wordCounter = new HashMap<String, Integer>();
		for(Token token : tokens) {
			if (isNounOrVerbAndNotNumber(token)) {
				String surface = token.getSurfaceForm();
				if (!wordCounter.containsKey(surface)) {
					wordCounter.put(surface, Integer.valueOf(0));
				}
				wordCounter.put(surface, Integer.valueOf(wordCounter.get(surface).intValue() + 1));
			}
		}

		// ナイーブベイズ推定
		BBCategory maxCategory = null;
		double maxPcd = 0;
		for(BBCategory category : categories) {
			double Pcd = calcProbCGivenD(category, wordCounter);
			Logger.info("Pcd for category "+category.getName()+" = "+Pcd);
			if (maxPcd < Pcd) {
				maxCategory = category;
				maxPcd = Pcd;
			}
		}
		
		Logger.info("estimated as category "+maxCategory.getName()+" with its probability "+maxPcd);
		
		return maxCategory;
	}
	
	/**
	 * 事後確率 P_{C|D} (c | d), D=[W_1, W_2, ..., W_D] を計算する
	 * @param head
	 * @param category カテゴリ
	 * @param words 単語の集合
	 * @param params パラメータ
	 * @return 事後確率 P_{C|D} (c | d), D=[W_1, W_2, ..., W_D]
	 * @throws Exception 
	 */
	private double calcProbCGivenD(BBCategory category, Map<String, Integer> words) throws Exception {
		double P = 1;
		double Pc = getProbC(category);
		double d = 0;
		
		for(String surface : words.keySet()) {
			BBWord word = new BBWord(surface).unique();
			if (word == null) {
				Logger.info("(category "+category.getName()+") calcProbWGivenC_Poisson("+words.get(surface).intValue()+", DEFAULT, "+Pc+")");
				d = DEFAULT_POISSON_LAMBDA_VALUE;
			} else {
				BBNaiveBayesParam param = new BBNaiveBayesParam(user, word, category).unique();
				if (param == null) {
					throw new Exception("Missing param for user = "+user+", surface = "+surface+", category = "+category);
				}
				Logger.info("(category "+category.getName()+") calcProbWGivenC_Poisson("+words.get(surface).intValue()+", "+param.getPoissonLambda()+", "+Pc+")");
				d = calcProbWGivenC_Poisson(words.get(surface).intValue(), param.getPoissonLambda(), Pc);
			}
			P = P * d;
			Logger.info("                     --> P = "+P);
		}
		P = P * Pc;
		
		return P;
	}
	
	/**
	 * 条件付き確率 P_{W_i|C} (w_i* | c) を計算する
	 * @param w word の出現回数
	 * @param p1 パラメータ p1
	 * @param Pc P_{C} (c) 事前確率
	 * @return 条件付き確率 P_{W_i|C} (w_i | c)
	 */
	private double calcProbWGivenC_Poisson(int w, double poissonLambda, double Pc) {
		return Math.pow(poissonLambda, (double)w) / mathUtil.factorial(w).doubleValue() * Math.exp(- poissonLambda);
	}
	
	/**
	 * カテゴリ C の事前確率を求める
	 * @param category カテゴリ
	 * @return
	 */
	private double getProbC(BBCategory category) {
		// TODO
		return (1.0 / BBItemAppendixSetting.CATEGORY_NAMES.length);
	}
	
	
	private boolean isNounOrVerbAndNotNumber(Token t) {
		String[] arr = t.getAllFeaturesArray();
		return ((arr[0].contains("名詞") || arr[0].equals("動詞")) && !arr[1].equals("数"));
	}
}

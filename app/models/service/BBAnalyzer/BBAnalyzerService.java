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
import models.setting.BBAnalyzerSetting;

import org.atilika.kuromoji.Token;
import org.atilika.kuromoji.Tokenizer;

import utils.api.bbanalyzer.LogUtil;
import utils.bbanalyzer.MathUtil;

public class BBAnalyzerService {

	private static final int DEFAULT_COUNT = 0;
	private static final double DEFAULT_GAUSS_MYU_VALUE = 0.0;
	private static final double DEFAULT_POISSON_LAMBDA_VALUE = 10E-4;
	private static final double MINIMUM_PROB_C = 10E-4;
	private static final double SMOOTHING_ALPHA = 1.0;
	
	private MathUtil mathUtil;
	private Tokenizer tokenizer;
	private User user;
	private Map<BBItemHead, List<Token>> documents;
	private Map<BBCategory, Set<BBItemHead>> categorized;
	private Map<BBCategory, Set<String>> wordsPerCategory;
	private Set<String> surfaceSet;
	private double smoothFactor = 0.0;
	
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
		wordsPerCategory = new HashMap<BBCategory, Set<String>>();
		for(String catName : BBAnalyzerSetting.CATEGORY_NAMES) {
			BBCategory category = new BBCategory(user, catName).uniqueOrStore();
			categorized.put(category, new HashSet<BBItemHead>());
			wordsPerCategory.put(category, new HashSet<String>());
		}
		
		// 掲示取得とトークン抽出
		tokenize();
		
		// パラメータ初期化
		initParams();

		// 単語数設定
		setCounters();
		
		// 各カテゴリで NaiveBayes パラメータを計算する
		for(BBCategory category : categorized.keySet()) {
			LogUtil.info("BBNaiveBayesParamService#calcParam(): began (category = "+category.getName()+")");
			calcParamPerCategory(category);
			LogUtil.info("BBNaiveBayesParamService#calcParam(): end");
		}
		
		surfaceSet = null;
		categorized = null;
		user = null;
	}
	
	public BBCategory estimate(User user, BBItemHead item) throws Exception {
		this.user = user;
		
		// しきい値との比較
		if (user.getDocumentCount() < BBAnalyzerSetting.MINIMUM_USER_DOCUMENT_COUNT_TO_ESTIMATE) {
			return null;
		}
		if (user.getWordCount() < BBAnalyzerSetting.MINIMUM_USER_WORD_COUNT_TO_ESTIMATE) {
			return null;
		}
		
		return estimateCategory(item);
	}
	
	/**
	 * ユーザの持つ全記事をメンバ documents に入れ、
	 * それらからトークンを documents に入れる
	 */
	private void tokenize() {
		documents = new HashMap<BBItemHead, List<Token>>();
		
		// 全記事取得
		Set<BBItemHead> items = new BBItemHead().findSetForUserWithCategory(user);
		if (items == null) {
			return;
		}
		
		for(BBItemHead item : items) {
			String title = item.getTitle();
			if (title == null || title.isEmpty()) {
				continue;
			}
			List<Token> tokens = tokenizer.tokenize(title);
			documents.put(item, tokens);
		}
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
		
		surfaceSet = new HashSet<String>();
		
		// カテゴリ分類とトークンの抽出
		for(BBItemHead item : documents.keySet()) {
			String title = item.getTitle();
			if (title == null || title.isEmpty()) {
				continue;
			}

			BBCategory category = item.getAppendix().getCategory();
			if (category == null) {
				throw new Exception("BBItem has null category");
			}
			categorized.get(category).add(item);
			
			List<Token> tokens = documents.get(item);
			for(Token token : tokens) {
				String surface = token.getSurfaceForm();
				if (isNounOrVerbAndNotNumber(token)) {
					if (!surfaceSet.contains(surface)) {
						surfaceSet.add(surface);
					}
					Set<String> catWords = wordsPerCategory.get(category);
					if (!catWords.contains(surface)) {
						catWords.add(surface);
					}
				}
			}
		}
		
		// すでに登録されている値の初期化
		for(String surface : surfaceSet) {
			BBWord word = new BBWord(surface).uniqueOrStore();
			
			LogUtil.info("surface = "+surface);
			
			for(BBCategory category : categorized.keySet()) {
				BBNaiveBayesParam param = new BBNaiveBayesParam(user, word, category).unique();
				if (param != null) {
					param.setCount(DEFAULT_COUNT);
					param.setGaussMyu(DEFAULT_GAUSS_MYU_VALUE);
					param.setPoissonLambda(DEFAULT_POISSON_LAMBDA_VALUE);
					param.store();
//					Logger.info("initialized param word = "+word.getSurface()+" .cat="+category.getName());
				}
			}
		}
		
	}
	
	/**
	 * ユーザの既知総単語数、カテゴリ内の単語数を設定する
	 */
	private void setCounters() {
		// ユーザの既知総単語数を設定する
		user.setDocumentCount(documents.size());
		user.setWordCount(surfaceSet.size());
		user.store();
		
		// カテゴリ内の単語数を設定する
		for(BBCategory category : wordsPerCategory.keySet()) {
			category.setDocumentCount(categorized.get(category).size());
			category.setWordCount(wordsPerCategory.get(category).size());
			category.store();
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
		Map<String, TrainParamHolder> holder = new HashMap<String, TrainParamHolder>();

		// 記事取得
		Set<BBItemHead> items = categorized.get(category);
		double total_item_num = (double) items.size();
		double weight_per_word = 1.0 / total_item_num;
		
		// 各掲示から単語を集計
		for(BBItemHead item : items) {
			// 掲示タイトルを形態素解析
			List<Token> tokens = documents.get(item);

			for(Token token : tokens) {
				if (isNounOrVerbAndNotNumber(token)) {
					String surface = token.getSurfaceForm();
					if (!holder.containsKey(surface)) {
						holder.put(surface, new TrainParamHolder());
					}
					TrainParamHolder trainParam = holder.get(surface);
					trainParam.count += 1;
					trainParam.average += weight_per_word;
					holder.put(surface, trainParam);
				}
			}
		}
		
		// 結果の保存
		for(String surface : holder.keySet()) {
			// 平均の出現数を取得
			TrainParamHolder trainParam = holder.get(surface);

			// オブジェクト取得
			BBWord word = new BBWord(surface).unique();
			BBNaiveBayesParam param = new BBNaiveBayesParam(user, word, category).uniqueOrStore();
			if (param == null) {
				throw new Exception("Failed to uniqueOrStore() BBNaiveBayesParam for user "+user.toString()+", word "+word.toString()+", category "+category.toString());
			}
			
			// 保存
			param.setCount(trainParam.count);
			param.setGaussMyu(trainParam.average);
			param.setPoissonLambda(trainParam.average);
			param.store();
//			Logger.info("saved param word = "+word.getSurface()+" .cat="+category.getName()+", count="+trainParam.count);
		}
	}
	
	/**
	 * ナイーブベイズ推定で掲示のカテゴリを推定する
	 * @param item
	 * @return
	 * @throws Exception
	 */
	private BBCategory estimateCategory(BBItemHead item) throws Exception {
		// アサート
		if (item == null || item.getTitle() == null) {
			LogUtil.error("BBAnalyzerService#estimateCategory(item): null item given, or item.getTitle() == null");
			return null;
		}
		
		// カテゴリ一覧を取得
		List<BBCategory> categories = new ArrayList<BBCategory>();
		for(String catName : BBAnalyzerSetting.CATEGORY_NAMES) {
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
		
		// 値の準備
		smoothFactor = SMOOTHING_ALPHA * user.getWordCount();

		// ナイーブベイズ推定
		LogUtil.info("BBAnalyzerService#estimateCategory(): estimating "+item.getTitle());
		BBCategory maxCategory = null;
		double maxPcd = - Integer.MAX_VALUE;
		StringBuilder sb = new StringBuilder();
		for(BBCategory category : categories) {
			double Pcd = calcProbCGivenD(category, wordCounter, sb);
			sb.append("BBAnalyzerService#estimateCategory(): Prob (cat="+category.getName()+") = "+Pcd);
			sb.append("\n");
			if (maxPcd < Pcd) {
				maxCategory = category;
				maxPcd = Pcd;
			}
		}
		LogUtil.info(sb.toString());
		LogUtil.info("estimated as category "+((maxCategory != null) ? maxCategory.getName() : "(null)")+", Prob = "+maxPcd);
		
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
	private double calcProbCGivenD(BBCategory category, Map<String, Integer> words, StringBuilder sb) throws Exception {
		double P = 1;
		double Pc = getProbC(category);
		double d = 0;
		
		for(String surface : words.keySet()) {
			d = calcProbWGivenC(words.get(surface).intValue(), surface, category, Pc);
			d = Math.log(d);
			P = P + d;
			sb.append("    "+P+"\t<--- ("+d+")\t"+surface);
		}
		P = P + Math.log(Pc);
		
		return P;
	}
	
	/**
	 * 条件付き確率 P_{W_i|C} (w_i* | c) を計算する
	 * @param w word の出現回数
	 * @param p1 パラメータ p1
	 * @param Pc P_{C} (c) 事前確率
	 * @return 条件付き確率 P_{W_i|C} (w_i | c)
	 */
//	private double calcProbWGivenC_Poisson(int w, double poissonLambda, double Pc) {
//		return Math.pow(poissonLambda, (double)w) / mathUtil.factorial(w).doubleValue() * Math.exp(- poissonLambda);
//	}
	private double calcProbWGivenC(int w, String surface, BBCategory category, double Pc) {
		BBWord word = new BBWord(surface).unique();
		if (word != null) {
			BBNaiveBayesParam param = new BBNaiveBayesParam(user, word, category).unique();
			if (param != null) {
				return (param.getCount() + SMOOTHING_ALPHA) / (category.getWordCount() + smoothFactor);
			}
		}
		return (DEFAULT_COUNT + SMOOTHING_ALPHA) / (category.getWordCount() + smoothFactor);
	}
	
	/**
	 * カテゴリ C の事前確率を求める
	 * @param category カテゴリ
	 * @return
	 */
	private double getProbC(BBCategory category) {
		double d = MINIMUM_PROB_C;
		if (0 < user.getDocumentCount()) {
			d = (category.getDocumentCount() / user.getDocumentCount());
		}
		if (d < MINIMUM_PROB_C) {
			d = MINIMUM_PROB_C;
		}
		return d;
	}
	
	
	private boolean isNounOrVerbAndNotNumber(Token t) {
		String[] arr = t.getAllFeaturesArray();
		return ((arr[0].contains("名詞") || arr[0].equals("動詞")) && !arr[1].equals("数"));
	}
	
	
	private class TrainParamHolder {
		int count = 0;
		double average = 0.0;
	}
}

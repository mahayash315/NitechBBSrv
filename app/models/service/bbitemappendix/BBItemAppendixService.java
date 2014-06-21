package models.service.bbitemappendix;


public class BBItemAppendixService {
	
//	private MathUtil mathUtil = new MathUtil();
//	private BBItemHead head;
//	private User user;
	
	public static BBItemAppendixService use() {
		return new BBItemAppendixService();
	}
	
	
//	/**
//	 * 掲示のカテゴリを推定する
//	 * @param head
//	 * @return
//	 */
//	public BBCategory estimateCategory(BBItemHead head) {
//		try {
//			return estimateCategoryUsingNaiveBayes(head);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
//
//	
//	
//	
//	
//	private BBCategory estimateCategoryUsingNaiveBayes(BBItemHead head) throws Exception {
//		// アサート
//		if (head == null || head.getUser() == null || head.getTitle() == null) {
//			Logger.error("BBItemAppendixService#estimateCategoryUsingNaiveBayes(head): null head given, or head.getUser() == null, or head.getTitle() == null");
//			return null;
//		}
//		this.head = head;
//		this.user = head.getUser();
//		
//		// 形態素解析器を初期化
//		Tokenizer tokenizer = Tokenizer.builder().build();
//		
//		// カテゴリ一覧を取得
//		List<BBCategory> categories = new ArrayList<BBCategory>();
//		for(String catName : BBItemAppendixSetting.CATEGORY_NAMES) {
//			BBCategory category = new BBCategory(user, catName).unique();
//			if (category == null) {
//				throw new Exception("Missing category "+catName+" for user "+user.toString());
//			}
//			categories.add(category);
//		}
//		
//		// 掲示タイトルを形態素解析
//		List<Token> tokens = tokenizer.tokenize(head.getTitle());
//		
//		// List<Token> から Map<BBWord, Integer> に変換, およびパラメータの取得
//		Map<BBWord, Integer> words = new HashMap<BBWord, Integer>();
//		for(Token token : tokens) {
//			if (isNounOrVerb(token)) {
//				BBWord word = new BBWord(token.getSurfaceForm()).unique();
//				if (word != null) {
//					if (!words.containsKey(word)) {
//						words.put(word, Integer.valueOf(0));
//					}
//					words.put(word, words.get(word) + 1);
//				}
//			}
//		}
//
//		// ナイーブベイズ推定
//		BBCategory maxCategory = null;
//		double maxPcd = 0;
//		for(BBCategory category : categories) {
//			double Pcd = calcProbCGivenD(category, words);
//			Logger.info("Pcd for category "+category.getName()+" = "+Pcd);
//			if (maxPcd <= Pcd) {
//				maxCategory = category;
//				maxPcd = Pcd;
//			}
//		}
//		
//		Logger.info("estimated as category "+maxCategory.getName()+" with its probability "+maxPcd);
//		
//		return maxCategory;
//	}
//	
//	/**
//	 * 事後確率 P_{C|D} (c | d), D=[W_1, W_2, ..., W_D] を計算する
//	 * @param head
//	 * @param category カテゴリ
//	 * @param words 単語の集合
//	 * @param params パラメータ
//	 * @return 事後確率 P_{C|D} (c | d), D=[W_1, W_2, ..., W_D]
//	 * @throws Exception 
//	 */
//	private double calcProbCGivenD(BBCategory category, Map<BBWord, Integer> words) throws Exception {
//		double P = 1;
//		double Pc = getProbC(category);
//		
//		for(BBWord word : words.keySet()) {
//			BBNaiveBayesParam param = new BBNaiveBayesParam(user, word, category).unique();
//			if (param == null) {
//				throw new Exception("Missing a param necessary for calculating Pcd");
//			}
//			
//			Integer w = words.get(word);
//			double d = calcProbWGivenC_Poisson(w.intValue(), param.getPoissonLambda(), Pc);
//			Logger.info("(category "+category.getName()+") calcProbWGivenC_Poisson("+w.intValue()+", "+param.getPoissonLambda()+", "+Pc+")");
//			P = P * d;
//			Logger.info("                     --> P = "+P);
//		}
//		P = P * Pc;
//		
//		return P;
//	}
//	
//	/**
//	 * 条件付き確率 P_{W_i|C} (w_i | c) を計算する
//	 * @param w word の出現回数
//	 * @param p1 パラメータ p1
//	 * @param Pc P_{C} (c) 事前確率
//	 * @return 条件付き確率 P_{W_i|C} (w_i | c)
//	 */
////	private double calcProbWGivenC_Gauss(int w, double gaussMyu, double Pc) {
////		return (1.0 / Math.sqrt(2*Math.PI)) * Math.exp(- (Math.pow((double)w - gaussMyu, 2.0)) / 2.0);
////	}
//	private double calcProbWGivenC_Poisson(int w, double poissonLambda, double Pc) {
//		return Math.pow(poissonLambda, (double)w) / mathUtil.factorial(w).doubleValue() * Math.exp(- poissonLambda);
//	}
//	
//	/**
//	 * カテゴリ C の事前確率を求める
//	 * @param category カテゴリ
//	 * @return
//	 */
//	private double getProbC(BBCategory category) {
//		// TODO
//		return (1.0 / BBItemAppendixSetting.CATEGORY_NAMES.length);
//	}
//	
//	private boolean isNounOrVerb(Token t) {
//		String[] arr = t.getAllFeaturesArray();
//		return (arr[0].contains("名詞") || arr[0].equals("動詞"));
//	}
}

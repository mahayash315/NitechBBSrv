package models.service.BBItemAppendix;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.entity.BBCategory;
import models.entity.BBItemHead;
import models.entity.BBNaiveBayesParam;
import models.entity.BBWord;

import org.atilika.kuromoji.Token;
import org.atilika.kuromoji.Tokenizer;

public class BBItemAppendixService {
	
	public static BBItemAppendixService use() {
		return new BBItemAppendixService();
	}
	
	
	/**
	 * 掲示のカテゴリを推定する
	 * @param head
	 * @return
	 */
	public BBCategory estimateCategory(BBItemHead head) {
		try {
			return estimateCategoryUsingNaiveBayes(head);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	
	
	
	
	private BBCategory estimateCategoryUsingNaiveBayes(BBItemHead head) throws Exception {
		// アサート
		if (head == null || head.getTitle() == null) {
			return null;
		}
		
		// 形態素解析器を初期化
		Tokenizer tokenizer = Tokenizer.builder().build();
		
		// 掲示タイトルを形態素解析
		List<Token> tokens = tokenizer.tokenize(head.getTitle());
		
		// List<Token> から Map<BBWord, Integer> に変換, およびパラメータの取得
		Map<BBWord, Integer> words = new HashMap<BBWord, Integer>();
		for(Token token : tokens) {
			if (isNounOrVerb(token)) {
				BBWord word = new BBWord(token.getSurfaceForm()).unique();
				if (word != null) {
					if (!words.containsKey(word)) {
						words.put(word, Integer.valueOf(0));
					}
					words.put(word, words.get(word) + 1);
				}
			}
		}
		
		// ナイーブベイズ推定
		BBCategory maxCategory = null;
		double maxPcd = 0;
		// TODO
//		for all category in BBCategory of User user {
//			double Pcd = calcProbCGivenD(head, category, words);
//			if (maxPcd < Pcd) {
//				maxCategory = category;
//				maxPcd = Pcd;
//			}
//		}
		
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
	private double calcProbCGivenD(BBItemHead head, BBCategory category, Map<BBWord, Integer> words) throws Exception {
		double P = 1;
		double Pc = getProbC(category);
		
		for(BBWord word : words.keySet()) {
			BBNaiveBayesParam param = new BBNaiveBayesParam(head.getUser(), word, category).unique();
			if (param == null) {
				throw new Exception("Missing a param necessary for calculating Pcd");
			}
			
			Integer w = words.get(word);
			P = P * calcProbWGivenC(w.intValue(), param.getP1(), Pc);
		}
		P = P * Pc;
		
		return P;
	}
	
	/**
	 * 条件付き確率 P_{W_i|C} (w_i | c) を計算する
	 * @param w word の出現回数
	 * @param p1 パラメータ p1
	 * @param Pc P_{C} (c) 事前確率
	 * @return 条件付き確率 P_{W_i|C} (w_i | c)
	 */
	private double calcProbWGivenC(int w, double p1, double Pc) {
		return (1.0 / Math.sqrt(2*Math.PI)) * Math.exp(- (Math.pow((double)w - p1, 2)) / 2);
	}
	
	/**
	 * カテゴリ C の事前確率を求める
	 * @param category カテゴリ
	 * @return
	 */
	private double getProbC(BBCategory category) {
		// TODO
		return 0;
	}
	
	private boolean isNounOrVerb(Token t) {
		String[] arr = t.getAllFeaturesArray();
		return (arr[0].contains("名詞") || arr[0].equals("動詞"));
	}
}

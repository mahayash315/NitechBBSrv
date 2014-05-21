package models.service.BBItemAppendix;

import java.util.List;

import models.entity.BBCategory;
import models.entity.BBItemHead;
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
		return estimateCategoryUsingNaiveBayes(head);
	}

	
	
	
	
	private BBCategory estimateCategoryUsingNaiveBayes(BBItemHead head) {
		// アサート
		if (head == null || head.getTitle() == null) {
			return null;
		}
		
		// 形態素解析器を初期化
		Tokenizer tokenizer = Tokenizer.builder().build();
		
		// 掲示タイトルを形態素解析
		List<Token> tokens = tokenizer.tokenize(head.getTitle());
		
		// ナイーブベイズ推定
		
	}
	
	private double calcProbCGivenD(BBCategory category, List<BBWord> words) {
		
	}
	
	private double calcProbWGivenC(BBWord word, BBCategory category) {
		
	}
}

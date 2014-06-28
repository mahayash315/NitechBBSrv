package utils.bbanalyzer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import models.entity.BBItem;
import models.entity.BBWord;
import models.response.bbanalyzer.BBAnalyzerResult;

import org.atilika.kuromoji.Token;
import org.atilika.kuromoji.Tokenizer;

public class BBAnalyzerUtil {
	
	Tokenizer tokenizer;
	
	public static BBAnalyzerUtil use() {
		return new BBAnalyzerUtil();
	}
	
	public BBAnalyzerUtil() {
		init();
	}
	
	private void init() {
		tokenizer = Tokenizer.builder().build();
	}
	

	// --------------------------------------------------------------------------------------
	// tokenizer
	// --------------------------------------------------------------------------------------

	public BBAnalyzerResult analyzeText(String text) {
		BBAnalyzerResult result = new BBAnalyzerResult();
		List<String> surfaces = new ArrayList<String>();
		List<String> features = new ArrayList<String>();
		
		for(Token token : tokenizer.tokenize(text)) {
			surfaces.add(token.getSurfaceForm());
			features.add(token.getAllFeatures());
		}
		
		result.setSurfaces(surfaces);
		result.setFeatures(features);
		
		return result;
	}
	
	public Map<BBWord, Integer> countFeatures(BBItem item) {
		Map<BBWord, Integer> features = new HashMap<BBWord, Integer>();
		Set<String> texts = new HashSet<String>();
		if (item.getAuthor() != null) {
			texts.add(item.getAuthor());
		}
		if (item.getTitle() != null) {
			texts.add(item.getTitle());
		}
		if (item.getBody() != null) {
			texts.add(item.getBody());
		}
		
		for(String text : texts) {
			List<Token> tokens = tokenizer.tokenize(text);
			for(Token token : tokens) {
				if (isFeature(token)) {
					BBWord bbWord = new BBWord(token.getSurfaceForm()).uniqueOrStore();
					if (!features.containsKey(bbWord)) {
						features.put(bbWord, Integer.valueOf(0));
					}
					features.put(bbWord, Integer.valueOf(features.get(bbWord).intValue()+1));
				}
			}
		}
		
		return features;
	}
	
	public Set<BBWord> extractFeatures(BBItem item) {
		Set<BBWord> features = new HashSet<BBWord>();
		Set<String> texts = new HashSet<String>();
		if (item.getAuthor() != null) {
			texts.add(item.getAuthor());
		}
		if (item.getTitle() != null) {
			texts.add(item.getTitle());
		}
		if (item.getBody() != null) {
			texts.add(item.getBody());
		}
		
		for(String text : texts) {
			List<Token> tokens = tokenizer.tokenize(text);
			for(Token token : tokens) {
				if (isFeature(token)) {
					BBWord bbWord = new BBWord(token.getSurfaceForm()).uniqueOrStore();
					features.add(bbWord);
				}
			}
		}
		
		return features;
	}
	
	private boolean isFeature(Token token) {
		if (isNoun(token)) {
			return true;
		}
		return false;
	}
	
	private boolean isNoun(Token token) {
		return token.getAllFeaturesArray()[0].equals("名詞");
	}
	
}

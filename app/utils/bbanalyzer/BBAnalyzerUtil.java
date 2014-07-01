package utils.bbanalyzer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import models.entity.BBItem;
import models.entity.BBWord;
import models.response.bbanalyzer.BBAnalyzerResult;

import org.atilika.kuromoji.Token;
import org.atilika.kuromoji.Tokenizer;

public class BBAnalyzerUtil {
	
	static BBAnalyzerUtil instance;
	
	Tokenizer tokenizer;
	
	public static BBAnalyzerUtil use() {
		if (instance == null) {
			instance = new BBAnalyzerUtil();
		}
		return instance;
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
	
	
	

	// --------------------------------------------------------------------------------------
	// feature
	// --------------------------------------------------------------------------------------
	public static double featureMultiply(Map<Long,Double> f1, Map<Long,Double> f2) {
		double res = 0;
		for(Long key : f1.keySet()) {
			if (f2.containsKey(key)) {
				res = res + (f1.get(key) * f2.get(key));
			}
		}
		return res;
	}
	
	public static double featureSize(Map<Long,Double> feature) {
		double size = 0;
		for(Entry<Long, Double> entry : feature.entrySet()) {
			double d = entry.getValue();
			size = size + d*d;
		}
		size = Math.sqrt(size);
		return size;
	}
	
	public static double featureDifference(Map<Long,Double> f1, Map<Long,Double> f2) {
		double cosin = featureMultiply(f1, f2) / (featureSize(f1) * featureSize(f2));
		return (1.0 - cosin);
	}
	
	public static String printFeature(Map<Long,Double> feature) {
		StringBuilder sb = new StringBuilder();
		sb.append("(");
		for(Long key : feature.keySet()) {
			sb.append("["+key+"]");
			sb.append(feature.get(key));
			sb.append(" ");
		}
		sb.deleteCharAt(sb.length()-1);
		sb.append(")");
		return sb.toString();
	}
	
}

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
	
	
	// --------------------------------------------------------------------------------------
	// vector
	// --------------------------------------------------------------------------------------
	
	public static void vectorAdd(double[] dst, double[] v) throws IllegalArgumentException {
		if (dst.length != v.length) {
			throw new IllegalArgumentException("v1.length != v2.length");
		}
		for(int i = 0; i < dst.length; ++i) {
			dst[i] = dst[i] + v[i];
		}
	}
	
	public static double vectorMultiply(double[] v1, double[] v2) throws IllegalArgumentException {
		if (v1.length != v2.length) {
			throw new IllegalArgumentException("v1.length != v2.length");
		}
		double res = 0;
		for(int i = 0; i < v1.length; ++i) {
			res = res + (v1[i] * v2[i]);
		}
		return res;
	}
	
	public static void vectorDivide(double[] dst, double div) {
		for(int i = 0; i < dst.length; ++i) {
			dst[i] = dst[i] / div;
		}
	}
	
	public static void vectorMultiplyAndAdd(double[] dst, double[] v, double factor) throws IllegalArgumentException {
		if (dst.length != v.length) {
			throw new IllegalArgumentException("v1.length != v2.length");
		}
		for(int i = 0; i < dst.length; ++i) {
			dst[i] = dst[i] + factor*v[i];
		}
	}
	
	public static double vectorSize(double[] v) {
		double sum = 0;
		for(int i = 0; i < v.length; ++i) {
			sum = sum + (v[i] * v[i]);
		}
		return Math.sqrt(sum);
	}
	
	public static String printVector(double[] vector) {
		StringBuilder sb = new StringBuilder();
		sb.append("[ ");
		for(double d : vector) {
			sb.append(d);
			sb.append(" ");
		}
		sb.append("]");
		return sb.toString();
	}
}

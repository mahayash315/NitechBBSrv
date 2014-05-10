package utils.bbanalyzer;

import java.util.ArrayList;
import java.util.List;

import models.response.bbanalyzer.BBAnalyzerResult;

import org.atilika.kuromoji.Token;
import org.atilika.kuromoji.Tokenizer;

public class BBAnalyzerUtil {

	public static BBAnalyzerResult analyzeText(String text) {
		BBAnalyzerResult result = new BBAnalyzerResult();
		List<String> surfaces = new ArrayList<String>();
		List<String> features = new ArrayList<String>();
		
		Tokenizer tokenizer = Tokenizer.builder().build();
		
		for(Token token : tokenizer.tokenize(text)) {
			surfaces.add(token.getSurfaceForm());
			features.add(token.getAllFeatures());
		}
		
		result.setSurfaces(surfaces);
		result.setFeatures(features);
		
		return result;
	}
}

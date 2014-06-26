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

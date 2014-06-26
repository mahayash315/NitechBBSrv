package utils.bbanalyzer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonUtil {

	private static Gson gson;
	
	
	public GsonUtil() {
		
	}
	
	/**
	 * GsonUtil を使用する
	 * @return
	 */
	public static Gson use() {
		if (gson == null) {
			init();
		}
		return gson;
	}
	
	
	private static void init() {
		gson = new GsonBuilder().create();
		// add additional type adapters to gson here
	}
	
	
//	/**
//	 * JSON からオブジェクトに変換する
//	 * @param <T>
//	 * @param json JSON
//	 * @param classOfT 変換先オブジェクトのクラス
//	 * @return
//	 * @throws JsonSyntaxException 変換に失敗した際にスローされる
//	 */
//	public <T> T fromJson(String json, Class<T> classOfT) throws JsonSyntaxException {
//		return gson.fromJson(json, classOfT);
//	}
//	
//	/**
//	 * JSON からオブジェクトに変換する
//	 * @param <T>
//	 * @param json JSON
//	 * @param typeOfT 変換先オブジェクトのタイプ(java.lang.reflect.Type)
//	 * @return
//	 * @throws JsonSyntaxException 変換に失敗した際にスローされる
//	 */
//	public <T> T fromJson(String json, Type typeOfT) throws JsonSyntaxException {
//		return gson.fromJson(json, typeOfT);
//	}
//	
//	
//	/**
//	 * オブジェクトから JSON に変換する
//	 * @param src
//	 * @return
//	 */
//	public String toJson(Object src) {
//		return gson.toJson(src);
//	}
}

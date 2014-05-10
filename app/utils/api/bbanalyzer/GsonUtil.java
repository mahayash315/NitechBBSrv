package utils.api.bbanalyzer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

public class GsonUtil {

	private static Gson gson;
	
	
	public GsonUtil() {
		gson = new GsonBuilder().create();
	}
	
	/**
	 * GsonUtil を使用する
	 * @return
	 */
	public static GsonUtil use() {
		return new GsonUtil();
	}
	
	
	/**
	 * JSON からオブジェクトに変換する
	 * @param <T>
	 * @param json JSON
	 * @param classOfT 変換先オブジェクトのクラス
	 * @return
	 * @throws JsonSyntaxException 変換に失敗した際にスローされる
	 */
	public <T> T fromJson(String json, Class<T> classOfT) throws JsonSyntaxException {
		return gson.fromJson(json, classOfT);
	}
	
	
	/**
	 * オブジェクトから JSON に変換する
	 * @param src
	 * @return
	 */
	public String toJson(Object src) {
		return gson.toJson(src);
	}
}

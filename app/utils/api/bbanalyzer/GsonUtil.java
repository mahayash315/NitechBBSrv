package utils.api.bbanalyzer;

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
		gson = new GsonBuilder()
						.create();
		// add additional type adapters to gson here
	}
}

package utils.bbanalyzer;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import models.entity.bbanalyzer.BBWord;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

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
						.registerTypeAdapter(BBWordAdapter.type, new BBWordAdapter())
						.create();
		// add additional type adapters to gson here
	}
	
	
	private static class BBWordAdapter implements JsonSerializer<Map<BBWord,Double>>, JsonDeserializer<Map<BBWord,Double>> {
		private static Type type = new TypeToken<Map<BBWord,Double>>(){}.getType();
		
		@Override
		public Map<BBWord,Double> deserialize(JsonElement arg0, Type arg1,
				JsonDeserializationContext arg2) throws JsonParseException {
			JsonArray array = arg0.getAsJsonArray();
			if (array != null) {
				Map<BBWord, Double> result = new HashMap<BBWord, Double>();
				int size = array.size();
				for(int i = 0; i < size; ++i) {
					JsonArray entry = array.get(i).getAsJsonArray();
					if (entry != null) {
						long id = entry.get(0).getAsLong();
						double value = entry.get(1).getAsDouble();
						result.put(new BBWord(id).unique(), value);
					}
				}
				return result;
			}
			return null;
		}

		@Override
		public JsonElement serialize(Map<BBWord,Double> arg0, Type arg1,
				JsonSerializationContext arg2) {
			if (arg0 != null) {
				JsonArray result = new JsonArray();
				for(BBWord word : arg0.keySet()) {
					JsonArray entry = new JsonArray();
					double d = arg0.get(word);
					entry.add(new JsonPrimitive(word.getId()));
					entry.add(new JsonPrimitive(d));
					result.add(entry);
				}
				return result;
			}
			return null;
		}
		
	}
}

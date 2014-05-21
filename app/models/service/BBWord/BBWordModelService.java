package models.service.BBWord;

import java.lang.reflect.Type;
import java.util.List;

import models.entity.BBWord;
import models.service.Model.ModelService;
import utils.bbanalyzer.GsonUtil;

import com.google.gson.reflect.TypeToken;

public class BBWordModelService implements ModelService<Long, BBWord> {
	
	private static final Type GSON_TYPE_FEATURES = new TypeToken<List<String>>(){}.getType();

	public static BBWordModelService use() {
		return new BBWordModelService();
	}
	
	@Override
	public BBWord findById(Long id) {
		if (id != null) {
			BBWord o = BBWord.find.byId(id);
			deserializeJson(o);
			return o;
		}
		return null;
	}

	@Override
	public BBWord save(BBWord entry) {
		if (entry != null) {
			serializeJson(entry);
			entry.save();
			if (entry.getId() != null) {
				return entry;
			}
		}
		return null;
	}

	@Override
	public BBWord update(BBWord entry) {
		if (entry != null) {
			serializeJson(entry);
			entry.update();
			if (entry.getId() != null) {
				return entry;
			}
		}
		return null;
	}

	@Override
	public BBWord update(BBWord entry, Long id) {
		if (entry != null && id != null) {
			serializeJson(entry);
			entry.update(id);
			if (entry.getId().equals(id)) {
				return entry;
			}
		}
		return null;
	}

	@Override
	public void delete(BBWord entry) {
		if (entry != null) {
			entry.delete();
		}
	}
	
	
	/**
	 * Surface でエントリを探す
	 * @param entry
	 * @return
	 */
	public BBWord findBySurface(String surface) {
		if (surface != null) {
			BBWord o = BBWord.find.where().eq("surface", surface).findUnique();
			deserializeJson(o);
			return o;
		}
		return null;
	}
	
	
	
	private void deserializeJson(BBWord o) {
		String jsonFeatures = o.getJsonFeatures();
		if (jsonFeatures != null) {
			List<String> features = GsonUtil.use().fromJson(jsonFeatures, GSON_TYPE_FEATURES);
			o.setFeatures(features);
		}
	}
	
	private void serializeJson(BBWord o) {
		if (o != null) {
			String jsonFeatures = GsonUtil.use().toJson(o.getFeatures());
			o.setJsonFeatures(jsonFeatures);
		}
	}

}

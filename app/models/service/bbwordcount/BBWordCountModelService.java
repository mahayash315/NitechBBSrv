package models.service.bbwordcount;

import models.entity.BBItem;
import models.entity.BBWord;
import models.entity.BBWordCount;
import models.service.model.ModelService;

public class BBWordCountModelService implements ModelService<Long, BBWordCount> {

	public static BBWordCountModelService use() {
		return new BBWordCountModelService();
	}
	
	@Override
	public BBWordCount findById(Long id) {
		if (id != null) {
			BBWordCount o = BBWordCount.find.byId(id);
			return o;
		}
		return null;
	}

	@Override
	public BBWordCount save(BBWordCount entry) {
		if (entry != null) {
			entry.save();
			if (entry.getId() != null) {
				return entry;
			}
		}
		return null;
	}

	@Override
	public BBWordCount update(BBWordCount entry) {
		if (entry != null) {
			entry.update();
			if (entry.getId() != null) {
				return entry;
			}
		}
		return null;
	}

	@Override
	public BBWordCount update(BBWordCount entry, Long id) {
		if (entry != null && id != null) {
			entry.update(id);
			if (entry.getId().equals(id)) {
				return entry;
			}
		}
		return null;
	}

	@Override
	public void delete(BBWordCount entry) {
		if (entry != null) {
			entry.delete();
		}
	}
	
	
	public BBWordCount findByItemWord(BBItem item, BBWord word) {
		if (word != null) {
			BBWordCount o = BBWordCount.find.where().eq("item", item).eq("word", word).findUnique();
			return o;
		}
		return null;
	}
	
	
	/**
	 * Surface でエントリを探す
	 * @param entry
	 * @return
	 */
	public BBWordCount findByItemSurface(BBItem item, String surface) {
		if (surface != null) {
			BBWord word = BBWord.find.where().eq("surface", surface).findUnique();
			return findByItemWord(item, word);
		}
		return null;
	}
	
	
}

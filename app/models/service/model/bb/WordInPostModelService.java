package models.service.model.bb;

import com.avaje.ebean.Ebean;

import models.entity.bb.WordInPost;
import models.entity.bb.WordInPost.PK;
import models.service.model.ModelService;

public class WordInPostModelService implements ModelService<WordInPost.PK, WordInPost> {

	@Override
	public WordInPost findById(PK id) {
		if (id != null) {
			return WordInPost.find.byId(id);
		}
		return null;
	}

	@Override
	public WordInPost save(WordInPost entry) {
		if (entry != null) {
			Ebean.save(entry);
			if (entry.getId() != null) {
				return entry;
			}
		}
		return null;
	}

	@Override
	public void delete(WordInPost entry) {
		if (entry != null) {
			Ebean.delete(entry);
		}
	}

}

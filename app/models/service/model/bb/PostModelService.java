package models.service.model.bb;

import models.entity.bb.Post;
import models.service.model.ModelService;

import com.avaje.ebean.Ebean;

public class PostModelService implements ModelService<Long, Post> {
	
	@Override
	public Post findById(Long id) {
		if (id != null) {
			return Post.find.byId(id);
		}
		return null;
	}

	@Override
	public Post save(Post entry) {
		if (entry != null) {
			Ebean.save(entry);
			if (entry.getId() != null) {
				return entry;
			}
		}
		return null;
	}

	@Override
	public void delete(Post entry) {
		if (entry != null) {
			Ebean.delete(entry);
		}
	}

	
	
}

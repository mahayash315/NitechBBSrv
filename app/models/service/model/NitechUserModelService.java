package models.service.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.entity.NitechUser;
import models.entity.bb.Post;
import models.entity.bb.Word;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.SqlRow;

public class NitechUserModelService implements ModelService<Long, NitechUser> {
	
	@Override
	public NitechUser findById(Long id) {
		if (id != null) {
			return NitechUser.find.byId(id);
		}
		return null;
	}

	@Override
	public NitechUser save(NitechUser entry) {
		if (entry != null) {
			Ebean.save(entry);
			if (entry.getId() != null) {
				return entry;
			}
		}
		return null;
	}

	@Override
	public void delete(NitechUser entry) {
		if (entry != null) {
			Ebean.delete(entry);
		}
	}

	
	public NitechUser findByHashedId(String hashedId) {
		if (hashedId != null) {
			return NitechUser.find.where().eq("hashedId", hashedId).findUnique();
		}
		return null;
	}
	
	public List<Post> findPossessingPosts(NitechUser nitechUser) {
		if (nitechUser != null) {
			return Post.find.fetch("possessions.nitechUser").where().eq("possessions.nitechUser", nitechUser).findList();
		}
		return null;
	}
}

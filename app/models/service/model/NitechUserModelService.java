package models.service.model;

import models.entity.NitechUser;

import com.avaje.ebean.Ebean;

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
}

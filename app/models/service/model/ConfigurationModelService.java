package models.service.model;

import models.entity.Configuration;

import com.avaje.ebean.Ebean;

public class ConfigurationModelService implements ModelService<Long, Configuration> {

	@Override
	public Configuration findById(Long id) {
		if (id != null) {
			return Configuration.find.byId(id);
		}
		return null;
	}

	@Override
	public Configuration save(Configuration entry) {
		if (entry != null) {
			Ebean.save(entry);
			if (entry.getId() != null) {
				return entry;
			}
		}
		return null;
	}

	@Override
	public void delete(Configuration entry) {
		if (entry != null) {
			Ebean.delete(entry);
		}
	}
	
	public Configuration findByKey(String key) {
		if (key != null) {
			return Configuration.find.where().eq("key", key).findUnique();
		}
		return null;
	}

}

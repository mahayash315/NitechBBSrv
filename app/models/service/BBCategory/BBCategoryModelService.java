package models.service.BBCategory;

import models.entity.BBCategory;
import models.entity.User;
import models.service.Model.ModelService;

public class BBCategoryModelService implements ModelService<Long, BBCategory> {

	public static BBCategoryModelService use() {
		return new BBCategoryModelService();
	}
	
	@Override
	public BBCategory findById(Long id) {
		if (id != null) {
			BBCategory o = BBCategory.find.byId(id);
			return o;
		}
		return null;
	}

	@Override
	public BBCategory save(BBCategory entry) {
		if (entry != null) {
			entry.save();
			if (entry.getId() != null) {
				return entry;
			}
		}
		return null;
	}

	@Override
	public BBCategory update(BBCategory entry) {
		if (entry != null) {
			entry.update();
			if (entry.getId() != null) {
				return entry;
			}
		}
		return null;
	}

	@Override
	public BBCategory update(BBCategory entry, Long id) {
		if (entry != null && id != null) {
			entry.update(id);
			if (entry.getId().equals(id)) {
				return entry;
			}
		}
		return null;
	}

	@Override
	public void delete(BBCategory entry) {
		if (entry != null) {
			entry.delete();
		}
	}
	
	
	/**
	 * User と Name でエントリを探す
	 * @param user
	 * @param name
	 * @return
	 */
	public BBCategory findByUserName(User user, String name) {
		if (user != null && name != null) {
			return BBCategory.find
						.where()
							.eq("user", user)
							.eq("name", name)
						.findUnique();
		}
		return null;
	}

}

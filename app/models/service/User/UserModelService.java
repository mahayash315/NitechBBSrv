package models.service.User;

import models.entity.User;
import models.service.Model.ModelService;

public class UserModelService implements ModelService<Long, User> {

	public static UserModelService use() {
		return new UserModelService();
	}
	
	@Override
	public User findById(Long id) {
		if (id != null) {
			return User.find.byId(id);
		}
		return null;
	}

	@Override
	public User save(User entry) {
		if (entry != null) {
			entry.save();
			if (entry.getId() != null) {
				return entry;
			}
		}
		return null;
	}

	@Override
	public User update(User entry) {
		if (entry != null) {
			entry.update();
			return entry;
		}
		return null;
	}

	@Override
	public User update(User entry, Long id) {
		if (entry != null && id != null) {
			entry.update(id);
			if (entry.getId().equals(id)) {
				return entry;
			}
		}
		return null;
	}

	@Override
	public void delete(User entry) {
		entry.delete();
	}
	
	
	/**
	 * nitechId でエントリを取り出す
	 * @param entry
	 * @return
	 */
	public User findByNitechId(String nitechId) {
		if (nitechId != null) {
			return User.find
					.where()
						.eq("hashedNitechId", nitechId)
					.findUnique();
		}
		return null;
	}

}

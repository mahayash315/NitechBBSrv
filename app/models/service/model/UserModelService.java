package models.service.model;

import java.util.Set;

import models.entity.User;

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
	public void delete(User entry) {
		if (entry != null) {
			entry.delete();
		}
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
	
	public Set<User> findSet() {
		return User.find.findSet();
	}

}

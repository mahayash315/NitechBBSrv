package models.service.model.bb;

import java.util.List;

import models.entity.NitechUser;
import models.entity.bb.UserCluster;
import models.service.model.ModelService;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.ExpressionList;

public class UserClusterModelService implements ModelService<Long, UserCluster> {
	
	@Override
	public UserCluster findById(Long id) {
		if (id != null) {
			return UserCluster.find.byId(id);
		}
		return null;
	}

	@Override
	public UserCluster save(UserCluster entry) {
		if (entry != null) {
			Ebean.save(entry);
			if (entry.getId() != null) {
				return entry;
			}
		}
		return null;
	}

	@Override
	public void delete(UserCluster entry) {
		if (entry != null) {
			Ebean.delete(entry);
		}
	}

	
	public UserCluster findByNitechUser(NitechUser nitechUser) {
		if (nitechUser != null) {
			return UserCluster.find.where().eq("nitechUser", nitechUser).findUnique();
		}
		return null;
	}
	
	public List<UserCluster> findList(Integer depth) {
		ExpressionList<UserCluster> expr = UserCluster.find.where();
		return expr.findList();
	}
}

package models.service.bbanalyzer;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import models.entity.User;
import utils.bbanalyzer.BBAnalyzerUtil;

public class AtomUserCluster extends UserCluster {

	public User user;
	
	public AtomUserCluster(User user) throws SQLException {
		super();
		this.depth = 0;
		if (user != null) {
			this.id = user.getId();
			this.feature = user.getUserFeature();
			this.user = user;
		}
	}
	
	@Override
	public void addChild(UserCluster child, double distance) {
		// do nothing
	}
	
	@Override
	public Set<User> getAllUsers() {
		return new HashSet<User>(){{ add(user); }};
	}
	
	@Override
	public double getWeight() {
		return 1.0;
	}
	
//	@Override
//	public void updateVector() {
//		// do nothing
//	}
	
	@Override
	public void updateFeature() {
		// do nothing
	}
	
	/* hashCode, equals */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((user == null) ? 0 : user.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		AtomUserCluster other = (AtomUserCluster) obj;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		return true;
	}
	
	/* toString() */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("depth=");
		sb.append(depth);
		sb.append(", id=");
		sb.append(id);
		sb.append(", user=");
		sb.append(user);
		sb.append(", ");
		sb.append(BBAnalyzerUtil.printFeature(feature));
		return sb.toString();
	}
}

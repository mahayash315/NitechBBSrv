package models.service.bbanalyzer;

import java.util.HashSet;
import java.util.Set;

import models.entity.User;
import utils.bbanalyzer.MathUtil;

public class AtomUserCluster extends UserCluster {

	public User user;

	@Override
	public Set<User> getAllUsers() {
		return new HashSet<User>(){{ add(user); }};
	}
	
	@Override
	public double getWeight() {
		return 1.0;
	}
	
	@Override
	public void updateVector() {
		// do nothing
	}
	
	/* toString() */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("user=");
		sb.append(user);
		sb.append(", ");
		sb.append(MathUtil.printVector(vector));
		return sb.toString();
	}
}
